package com.dev.timeflow.View.Screens.onBoarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Timer
import com.composables.icons.lucide.TimerReset
import com.dev.timeflow.R
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun WelcomeScreen(modifier: Modifier = Modifier,onNavigate :() -> Unit) {

    var animateText by remember { mutableStateOf(false) }
    var animateDescText by remember { mutableStateOf(false) }
    var animate by remember { mutableStateOf(false) }
    var showImage by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300)
        animateText = true
        delay(300)
        animateDescText = true
        delay(100)
        showImage = true
        delay(300)
        animate = true
    }

    val f1 by animateFloatAsState(
        targetValue = if (animate) 78f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        )
    )
    val f2 by animateFloatAsState(
        targetValue = if (animate) 45f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        )
    )
    val f3 by animateFloatAsState(
        targetValue = if (animate) 65f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessLow
        )
    )


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Timeflow", fontWeight = FontWeight.Bold)
                }
            )
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
                .padding(it)
                .padding(
                    horizontal = 8.dp
                )
        ) {
            AnimatedContent(
                targetState = animateText,
                transitionSpec = {
                    slideInVertically(
                        initialOffsetY = { it / 2 },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ) togetherWith slideOutVertically()
                }
            ) {
                if (it) {
                    Text(
                        modifier = modifier.padding(
                            top = 16.dp
                        ),
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontStyle = FontStyle.Italic,
                                    fontFamily = FontFamily.Serif,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            ) {
                                append("track ")
                            }
                            append("your ")
                            withStyle(
                                style = SpanStyle(
                                    fontStyle = FontStyle.Italic,
                                    fontFamily = FontFamily.Serif,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            ) {
                                append("time ")
                            }
                            append("now")
                        },
                        style = MaterialTheme.typography.headlineMedium
                    )
                }else {
                    Text(
                        modifier = modifier
                            .alpha(0f)
                            .padding(
                                top = 16.dp
                            ),
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontStyle = FontStyle.Italic,
                                    fontFamily = FontFamily.Serif,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("track ")
                            }
                            append("your ")
                            withStyle(
                                style = SpanStyle(
                                    fontStyle = FontStyle.Italic,
                                    fontFamily = FontFamily.Serif,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("time ")
                            }
                            append("now")
                        },
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }

           AnimatedContent(
               targetState = animateDescText,
              transitionSpec = {
                  slideInVertically(
                      initialOffsetY = { it / 2 },
                      animationSpec = spring(
                          dampingRatio = Spring.DampingRatioMediumBouncy,
                          stiffness = Spring.StiffnessLow
                      )
                  ) togetherWith slideOutVertically()
              }
           ) {
               if (it){
                   Text(
                       textAlign = TextAlign.Center,
                       text = "See how your day, week, month, and year progress in real time."
                   )
               }else{
                   Text(
                    modifier = modifier.alpha(0f),
                       textAlign = TextAlign.Center,
                       text = "See how your day, week, month, and year progress in real time."
                   )
               }
           }
            Spacer(
                modifier = modifier.weight(1f)
            )

            AsyncImage(
                modifier = modifier.padding(
                    horizontal = 34.dp
                ),
                model = R.drawable.time,
                contentDescription = null
            )
            Spacer(
                modifier = modifier.weight(1f)
            )
            DummyProgressBar(
                text = "day progress",
                float = f1
            )
            DummyProgressBar(
                text = "month progress",
                float = f2
            )
            DummyProgressBar(
                text = "year progress",
                float = f3
            )



            Spacer(
                modifier = modifier.weight(1f)
            )
           


            AnimatedContent(
                targetState = animate
            ) {
                if (it){
                    Button(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp
                            ),
                        shape = RoundedCornerShape(16.dp),
                        onClick = {
                            onNavigate.invoke()
                        }
                    ) {
//
                        Text(
                            modifier = modifier.padding(
                                vertical = 8.dp
                            ),
                            text = buildAnnotatedString {
                                append("Ready? let's flow")
                                withStyle(
                                    style = SpanStyle(
                                        fontFamily = FontFamily.Serif,
                                        fontStyle = FontStyle.Italic
                                    )
                                ) {
                                    // append("flow")
                                }
                            },
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                } else {
                    Button(
                        modifier = modifier
                            .fillMaxWidth()
                            .alpha(0f)
                            .padding(
                                horizontal = 16.dp
                            ),
                        shape = RoundedCornerShape(16.dp),
                        onClick = {

                        }
                    ) {
//
                        Text(
                            modifier = modifier.padding(
                                vertical = 8.dp
                            ),
                            text = buildAnnotatedString {
                                append("Ready? let's flow")
                                withStyle(
                                    style = SpanStyle(
                                        fontFamily = FontFamily.Serif,
                                        fontStyle = FontStyle.Italic
                                    )
                                ){
                                    // append("flow")
                                }
                            },
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
}
            }
        }
    }
}


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DummyProgressBar(modifier: Modifier = Modifier, text: String, float: Float) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 4.dp
            )
    ) {
        Text(text = text, modifier = modifier.padding(
            bottom = 4.dp
        ))
        LinearProgressIndicator(
            progress = {
                float /100
            },
            //waveSpeed = 0.5.dp,

            modifier = modifier.fillMaxWidth()
        )
    }
}