package org.zhiwei.compose.screen.graphics

import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.zhiwei.compose.R
import org.zhiwei.compose.ui.widget.Title_Text
import kotlin.math.roundToInt

@Composable
internal fun BeforeAfterScreen(modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Title_Text("BeforeAfter Layout")
        BeforeAfterSample()
    }
}

@Preview
@Composable
private fun BeforeAfterSample() {

    val infiniteTransition = rememberInfiniteTransition("before-after")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "before-after"
    )

    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        BeforeAfterLayoutWithBlendMode(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .size(240.dp),
            progress = { progress },
            beforeLayout = {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(R.drawable.avatar01),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
            },
            afterLayout = {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(R.drawable.avatar02),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        BeforeAfterLayoutWithBlendMode(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .fillMaxWidth(.8f)
                .aspectRatio(16 / 9f),
            progress = { progress },
            beforeLayout = {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(R.drawable.avatar02),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
            },
            afterLayout = {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(R.drawable.avatar01),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        BeforeAfterLayout(
            progress = progress,
            beforeLayout = {
                Box(
                    modifier = Modifier
                        .border(4.dp, Color.Yellow, RoundedCornerShape(16.dp))
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .fillMaxWidth(.9f)
                        .height(80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Progress: ${(progress * 100).roundToInt()}",
                        fontSize = 24.sp,
                        color = Color.Red
                    )
                }
            },
            afterLayout = {
                Box(
                    modifier = Modifier
                        .border(4.dp, Color.Yellow, RoundedCornerShape(16.dp))
                        .background(Color.Red, RoundedCornerShape(16.dp))
                        .fillMaxWidth(.9f)
                        .height(80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Progress: ${(progress * 100).roundToInt()}",
                        color = Color.White,
                        fontSize = 24.sp
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        val context = LocalContext.current

        BeforeAfterLayout(
            progress = progress,
            beforeLayout = {
                androidx.compose.material.Button(
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .fillMaxWidth(),
                    onClick = {
                        Toast.makeText(context, "M2 Button", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    androidx.compose.material.Text("M2 Button")
                }
            },
            afterLayout = {
                Button(
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .fillMaxWidth(),
                    onClick = {
                        Toast.makeText(context, "M3 Button", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text("M3 Button")
                }
            }
        )
    }
}

@Composable
private fun BeforeAfterLayout(
    modifier: Modifier = Modifier,
    progress: Float,
    beforeLayout: @Composable BoxScope.() -> Unit,
    afterLayout: @Composable BoxScope.() -> Unit,
) {

    val shapeBefore = remember(progress) {
        GenericShape { size: Size, layoutDirection: LayoutDirection ->
            addRect(
                rect = Rect(
                    topLeft = Offset(size.width * progress, 0f),
                    bottomRight = Offset(size.width, size.height)
                )
            )
        }
    }

    val shapeAfter = remember(progress) {
        GenericShape { size: Size, layoutDirection: LayoutDirection ->
            addRect(
                rect = Rect(
                    topLeft = Offset.Zero,
                    bottomRight = Offset(size.width * progress, size.height)
                )
            )
        }
    }

    Box(modifier) {
        Box(
            modifier = Modifier.clip(shapeBefore)
        ) {
            beforeLayout()
        }

        Box(
            modifier = Modifier.clip(shapeAfter)
        ) {
            afterLayout()
        }
    }
}

@Composable
private fun BeforeAfterLayoutWithBlendMode(
    modifier: Modifier = Modifier,
    progress: () -> Float,
    beforeLayout: @Composable BoxScope.() -> Unit,
    afterLayout: @Composable BoxScope.() -> Unit,
) {
    Box(modifier) {
        afterLayout()
        Box(
            modifier = Modifier
                .graphicsLayer {
                    compositingStrategy = CompositingStrategy.Offscreen
                }
                .drawWithContent {
                    drawContent()
                    drawRect(
                        color = Color.Transparent,
                        size = Size(size.width * progress(), size.height),
                        blendMode = BlendMode.Clear
                    )

                }
        ) {
            beforeLayout()
        }
    }
}

@Preview
@Composable
private fun PreviewBeforeAfter() {
    BeforeAfterScreen()
}