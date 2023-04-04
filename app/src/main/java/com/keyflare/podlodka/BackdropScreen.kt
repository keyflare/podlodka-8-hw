@file:OptIn(ExperimentalMaterialApi::class)

package com.keyflare.podlodka

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keyflare.podlodka.ui.theme.PodlodkaTheme

val CARD_WIDTH_DP = 200.dp
val CARD_HEIGHT_DP = 150.dp
const val ALPHA_MULTIPLIER = 3f
const val SCALE_WRAPPED = 0.3f

@Composable
fun BackdropScreen() {
    val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed)
    val progress = remember {
        derivedStateOf {
            if (scaffoldState.progress.to == BackdropValue.Concealed) {
                1 - scaffoldState.progress.fraction
            } else {
                scaffoldState.progress.fraction
            }
        }
    }

    BackdropScaffold(
        appBar = {},
        backLayerContent = { BackLayerContent(progress) },
        frontLayerContent = {},
        scaffoldState = scaffoldState,
    )
}

@Composable
private fun BackLayerContent(progress: State<Float>) {
    BoxWithConstraints(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        val alphaState = remember {
            derivedStateOf { (progress.value - (1 - 1 / ALPHA_MULTIPLIER)) * ALPHA_MULTIPLIER }
        }
        val translationDpState = remember {
            derivedStateOf { (maxHeight / 2 - CARD_HEIGHT_DP) * progress.value }
        }
        val scaleState = remember {
            derivedStateOf { SCALE_WRAPPED + (1 - SCALE_WRAPPED) * progress.value }
        }

        Box(
            modifier = Modifier
                .graphicsLayer {
                    transformOrigin = TransformOrigin(0.5f, 0f)
                    translationY = translationDpState.value.toPx()
                    scaleX = scaleState.value
                    scaleY = scaleState.value
                }
                .width(CARD_WIDTH_DP)
                .height(CARD_HEIGHT_DP)
                .background(
                    color = Color.Yellow,
                    shape = RoundedCornerShape(16.dp)
                )
        )
        Box(
            modifier = Modifier
                .graphicsLayer {
                    translationY = maxHeight.toPx() / 2
                    alpha = alphaState.value
                }
                .width(CARD_WIDTH_DP)
                .height(CARD_HEIGHT_DP)
                .background(
                    color = Color.Red,
                    shape = RoundedCornerShape(16.dp)
                )
        )
    }
}

@Preview
@Composable
private fun BackdropScreenPreview() {
    PodlodkaTheme {
        BackdropScreen()
    }
}
