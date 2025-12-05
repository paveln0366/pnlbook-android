package com.pnlbook.presentation.chart

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max
import kotlin.math.min

// ===== МОДЕЛИ =====

data class Candle(
    val time: Long,
    val open: Float,
    val high: Float,
    val low: Float,
    val close: Float
)

data class TradeMarker(
    val time: Long,     // timestamp сделки
    val price: Float,   // цена входа/выхода
    val isEntry: Boolean // true = вход, false = выход
)

enum class Timeframe {
    M1, M5, M15, M30, H1, H4, D1, W1, MN1
}

// Формат времени в зависимости от ТФ
private fun timeframePattern(tf: Timeframe): String = when (tf) {
    Timeframe.M1,
    Timeframe.M5,
    Timeframe.M15,
    Timeframe.M30 -> "HH:mm"

    Timeframe.H1,
    Timeframe.H4 -> "MM-dd HH:mm"

    Timeframe.D1,
    Timeframe.W1,
    Timeframe.MN1 -> "yyyy-MM-dd"
}

// Для примера: шаг в минутах по таймфрейму
private fun timeframeStepMinutes(tf: Timeframe): Int = when (tf) {
    Timeframe.M1 -> 1
    Timeframe.M5 -> 5
    Timeframe.M15 -> 15
    Timeframe.M30 -> 30
    Timeframe.H1 -> 60
    Timeframe.H4 -> 240
    Timeframe.D1 -> 60 * 24
    Timeframe.W1 -> 60 * 24 * 7
    Timeframe.MN1 -> 60 * 24 * 30
}

// ===== КНОПКИ ВЫБОРА ТАЙМФРЕЙМА =====

@Composable
fun TimeframeSelector(
    selected: Timeframe,
    onSelect: (Timeframe) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        Timeframe.M1,
        Timeframe.M5,
        Timeframe.M15,
        Timeframe.M30,
        Timeframe.H1,
        Timeframe.H4,
        Timeframe.D1
    )

    Row(
        modifier = modifier.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items.forEach { tf ->
            val isSelected = tf == selected
            OutlinedButton(
                onClick = { onSelect(tf) },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isSelected) Color.DarkGray else Color.Transparent,
                    contentColor = Color.White
                ),
                border = if (isSelected) ButtonDefaults.outlinedButtonBorder else null,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = tf.name,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

// ===== ГЛАВНЫЙ ЭКРАН С КНОПКАМИ И ГРАФИКОМ =====

@Composable
fun BtcChartScreen(
    paddingValues: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    var timeframe by remember { mutableStateOf(Timeframe.M1) }

    // В реальном приложении сюда вместо sample… подставишь свои данные
    val candles = remember(timeframe) { sampleBtcCandles(timeframe) }
    val markers = remember(candles) { sampleBtcMarkers(candles) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(Color.Black)
    ) {
        TimeframeSelector(
            selected = timeframe,
            onSelect = { timeframe = it }
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            CandlesChart(
                candles = candles,
                markers = markers,
                timeframe = timeframe,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

// ===== ОСНОВНОЙ ГРАФИК =====

@Composable
fun CandlesChart(
    candles: List<Candle>,
    markers: List<TradeMarker>,
    timeframe: Timeframe,
    modifier: Modifier = Modifier
) {
    var scaleX by remember { mutableStateOf(1f) }
    var scaleY by remember { mutableStateOf(1f) }          // вертикальный зум
    var offsetX by remember { mutableStateOf(0f) }

    var canvasSize by remember { mutableStateOf(IntSize.Zero) }

    // Базовый диапазон цены: учитываем И свечи, И маркеры, чтобы линии не пропадали по Y
    val candleMin = candles.minOfOrNull { it.low }
    val candleMax = candles.maxOfOrNull { it.high }
    val markerMin = markers.minOfOrNull { it.price }
    val markerMax = markers.maxOfOrNull { it.price }

    val minPrice = listOfNotNull(candleMin, markerMin).minOrNull() ?: 0f
    val maxPrice = listOfNotNull(candleMax, markerMax).maxOrNull() ?: 1f

    val pricePadding = (maxPrice - minPrice).let { if (it == 0f) 1f else it * 0.05f }
    val baseMin = minPrice - pricePadding
    val baseMax = maxPrice + pricePadding

    // Отступы под оси
    val rightPaddingPx = 80f
    val bottomPaddingPx = 40f

    // Формат времени по таймфрейму
    val timeFormatter = remember(timeframe) {
        SimpleDateFormat(timeframePattern(timeframe), Locale.getDefault())
    }

    // ===== АВТО-ЦЕНТРОВКА ПО МАРКЕРАМ (ПО ОСИ X) =====
    LaunchedEffect(candles, markers, canvasSize, scaleX) {
        if (candles.isEmpty() || markers.isEmpty() || canvasSize.width == 0) return@LaunchedEffect

        val candleWidth = 16f * scaleX
        val spaceBetween = 4f * scaleX
        val totalCandleWidth = candleWidth + spaceBetween
        val chartWidth = canvasSize.width - rightPaddingPx

        val markerIndices = markers.mapNotNull { marker ->
            val idx = candles.indexOfLast { it.time <= marker.time }
            if (idx >= 0) idx else null
        }
        if (markerIndices.isEmpty()) return@LaunchedEffect

        val minIdx = markerIndices.min()
        val maxIdx = markerIndices.max()
        val midIdx = (minIdx + maxIdx) / 2f

        val desiredCenterX = chartWidth / 2f
        val targetOffsetX =
            desiredCenterX - midIdx * totalCandleWidth - candleWidth / 2f

        offsetX = targetOffsetX
    }

    Box(
        modifier = modifier
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    // Горизонтальный зум
                    scaleX = (scaleX * zoom).coerceIn(0.5f, 10f)
                    // Вертикальный зум
                    scaleY = (scaleY * zoom).coerceIn(0.5f, 10f)
                    // Скролл по X
                    offsetX += pan.x
                }
            }
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged { canvasSize = it }
        ) {
            if (candles.isEmpty()) return@Canvas

            val chartWidth = size.width - rightPaddingPx
            val chartHeight = size.height - bottomPaddingPx

            val candleWidth = 16f * scaleX
            val spaceBetween = 4f * scaleX
            val totalCandleWidth = candleWidth + spaceBetween

            // Текущий ценовой диапазон с учётом вертикального зума
            val baseRange = baseMax - baseMin
            val visibleRange = baseRange / scaleY
            val centerPrice = (baseMax + baseMin) / 2f
            val chartMin = centerPrice - visibleRange / 2f
            val chartMax = centerPrice + visibleRange / 2f

            fun priceToY(price: Float): Float {
                val ratio = (price - chartMin) / (chartMax - chartMin)
                return chartHeight - ratio * chartHeight
            }

            // ===== СНАЧАЛА ГРИД + ЦЕНА СПРАВА (БЕЗ CLIP) =====
            val gridSteps = 5
            val priceStep = (chartMax - chartMin) / gridSteps

            drawIntoCanvas { canvas ->
                val nativeCanvas = canvas.nativeCanvas
                val textPaint = Paint().apply {
                    color = android.graphics.Color.WHITE
                    textSize = 26f
                    isAntiAlias = true
                }

                for (i in 0..gridSteps) {
                    val price = chartMin + priceStep * i
                    val y = priceToY(price)

                    // Горизонтальная линия (до правого края графика)
                    drawLine(
                        color = Color.DarkGray,
                        start = Offset(0f, y),
                        end = Offset(chartWidth, y),
                        strokeWidth = 1f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                    )

                    // Подпись цены СПРАВА, уже за пределами chartWidth
                    val label = String.format(Locale.getDefault(), "%.2f", price)
                    nativeCanvas.drawText(
                        label,
                        chartWidth + 4f,
                        y + 8f,
                        textPaint
                    )
                }
            }

            // ===== ВСЁ, ЧТО ОТНОСИТСЯ К ГРАФИКУ, КЛИПАЕМ =====
            clipRect(left = 0f, top = 0f, right = chartWidth, bottom = chartHeight) {

                // --- Свечи ---
                candles.forEachIndexed { index, candle ->
                    val xCenter = offsetX + index * totalCandleWidth + candleWidth / 2f

                    if (xCenter + candleWidth < 0 || xCenter - candleWidth > chartWidth) {
                        return@forEachIndexed
                    }

                    val openY = priceToY(candle.open)
                    val closeY = priceToY(candle.close)
                    val highY = priceToY(candle.high)
                    val lowY = priceToY(candle.low)

                    val isBull = candle.close >= candle.open
                    val bodyTop = min(openY, closeY)
                    val bodyBottom = max(openY, closeY)

                    val bodyColor = if (isBull) Color.Green else Color.Red

                    // Тень
                    drawLine(
                        color = bodyColor,
                        start = Offset(xCenter, highY),
                        end = Offset(xCenter, lowY),
                        strokeWidth = 2f
                    )

                    // Тело
                    drawRect(
                        color = bodyColor,
                        topLeft = Offset(xCenter - candleWidth / 2f, bodyTop),
                        size = Size(candleWidth, (bodyBottom - bodyTop).coerceAtLeast(2f))
                    )
                }

                // --- Маркеры сделок (линии + треугольники) ---
                markers.forEach { marker ->
                    val index = candles.indexOfLast { it.time <= marker.time }
                        .takeIf { it >= 0 } ?: return@forEach

                    val xCenter = offsetX + index * totalCandleWidth + candleWidth / 2f

                    val y = priceToY(marker.price)

                    val lineY = y
                    val lineStart = xCenter
                    val lineEnd = chartWidth

                    val markerColor = if (marker.isEntry) Color.Cyan else Color.Yellow

                    // Горизонтальная линия вправо — внутри clipRect она не залезет на ось цен
                    drawLine(
                        color = markerColor,
                        start = Offset(lineStart, lineY),
                        end = Offset(lineEnd, lineY),
                        strokeWidth = 2f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )

                    // Стрелка (треугольник)
                    val arrowSize = 10f
                    val path = Path().apply {
                        if (marker.isEntry) {
                            // стрелка вверх
                            moveTo(xCenter, y - arrowSize)
                            lineTo(xCenter - arrowSize, y + arrowSize)
                            lineTo(xCenter + arrowSize, y + arrowSize)
                        } else {
                            // стрелка вниз
                            moveTo(xCenter, y + arrowSize)
                            lineTo(xCenter - arrowSize, y - arrowSize)
                            lineTo(xCenter + arrowSize, y - arrowSize)
                        }
                        close()
                    }
                    drawPath(path, color = markerColor)
                }
            }

            // ===== ОСЬ ВРЕМЕНИ СНИЗУ (может быть вне clipRect) =====
            if (candles.isNotEmpty()) {
                drawIntoCanvas { canvas ->
                    val nativeCanvas = canvas.nativeCanvas
                    val textPaint = Paint().apply {
                        color = android.graphics.Color.LTGRAY
                        textSize = 24f
                        isAntiAlias = true
                    }

                    val labelCount = 4
                    for (i in 0..labelCount) {
                        val t = i.toFloat() / labelCount
                        val indexFloat = t * (candles.lastIndex)
                        val index = indexFloat.toInt().coerceIn(0, candles.lastIndex)
                        val candle = candles[index]

                        val xCenter = offsetX + index * totalCandleWidth + candleWidth / 2f
                        if (xCenter < 0f || xCenter > chartWidth) continue

                        val label = timeFormatter.format(Date(candle.time))

                        nativeCanvas.drawText(
                            label,
                            xCenter - textPaint.measureText(label) / 2f,
                            chartHeight + bottomPaddingPx - 10f,
                            textPaint
                        )
                    }
                }
            }
        }
    }
}

// ===== ПРИМЕР "BTC" ДАННЫХ =====

private fun sampleBtcCandles(timeframe: Timeframe): List<Candle> {
    val baseTime = 1_700_000_000_000L
    val basePrice = 60_000f
    val stepMinutes = timeframeStepMinutes(timeframe)

    return List(120) { i ->
        val t = baseTime + i * stepMinutes * 60_000L
        val drift = (i - 60) * 15f
        val noise = listOf(-150f, -80f, -20f, 20f, 80f, 150f).random()
        val open = basePrice + drift + noise
        val close = open + listOf(-120f, -60f, -20f, 20f, 60f, 120f).random()
        val high = max(open, close) + (20..120).random()
        val low = min(open, close) - (20..120).random()
        Candle(
            time = t,
            open = open,
            high = high,
            low = low,
            close = close
        )
    }
}

private fun sampleBtcMarkers(candles: List<Candle>): List<TradeMarker> {
    if (candles.size < 40) return emptyList()
    val entryCandle = candles[40]
    val exitCandle = candles[80]

    return listOf(
        TradeMarker(
            time = entryCandle.time,
            price = entryCandle.low + (entryCandle.high - entryCandle.low) * 0.3f,
            isEntry = true
        ),
        TradeMarker(
            time = exitCandle.time,
            price = exitCandle.low + (exitCandle.high - exitCandle.low) * 0.7f,
            isEntry = false
        )
    )
}

// ===== PREVIEW =====

@Preview(showBackground = true)
@Composable
fun BtcChartScreenPreview() {
    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        BtcChartScreen()
    }
}


