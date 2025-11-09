package com.dev.timeflow.View.Screens.onBoarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.unit.dp
import androidx.glance.text.Text
import coil3.compose.AsyncImage
import com.dev.timeflow.R
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeatureScreen(modifier: Modifier = Modifier,onNavigate :() -> Unit) {
    var animateText by remember { mutableStateOf(false) }
    var animateDescText by remember { mutableStateOf(false) }
    var animateButton by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(100)
        animateText = true
        delay(200)
        animateDescText = true
        delay(100)
        animateButton = true

    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    androidx.compose.material3.Text(text = "Timeflow", fontWeight = FontWeight.Bold)
                }
            )
        }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
                .padding(
                    horizontal = 8.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
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
              if (it){
                  androidx.compose.material3.Text(
                      modifier = modifier.padding(
                          top = 16.dp
                      ),
                      text = buildAnnotatedString {
                      append("Plan With ")
                      withStyle(
                          style = SpanStyle(
                              color = MaterialTheme.colorScheme.primary,
                              fontFamily = FontFamily.Serif,
                              fontStyle = FontStyle.Italic
                          )
                      ) {
                          append("Purpose")
                      }
                  }, style = MaterialTheme.typography.headlineMedium)
              }else{
                  androidx.compose.material3.Text(
                      modifier = modifier.alpha(0f),
                      text = buildAnnotatedString {
                      append("Plan With ")
                      withStyle(
                          style = SpanStyle(
                              color = MaterialTheme.colorScheme.primary,
                              fontFamily = FontFamily.Serif,
                              fontStyle = FontStyle.Italic
                          )
                      ) {
                          append("Purpose")
                      }
                  }, style = MaterialTheme.typography.headlineMedium)
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
                    androidx.compose.material3.Text(
                        modifier = modifier.padding(
                            horizontal = 8.dp
                        ),
                        textAlign = TextAlign.Center,
                        text = "Add tasks or events and stay focused on what truly matters."
                    )
                }else{
                    androidx.compose.material3.Text(
                        modifier = modifier
                            .alpha(0f)
                            .padding(
                                horizontal = 8.dp
                            ),
                        textAlign = TextAlign.Center,
                        text = "Add tasks or events and stay focused on what truly matters."
                    )
                }
            }

            Spacer(
                modifier = modifier.weight(1f)
            )

            AsyncImage(
                modifier = modifier.padding(
                    horizontal = 16.dp
                ),
                model = R.drawable.task_bro,
                contentDescription = null
            )

            Spacer(
                modifier = modifier.weight(1f)
            )
            AnimatedContent(
                targetState = animateButton,
                transitionSpec = {
                    slideInVertically(
                        initialOffsetY = { it / 2 },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ) togetherWith slideOutVertically()
                }
            ) {
                if (it) {
                    Button(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 12.dp
                            ),
                        shape = RoundedCornerShape(16.dp),
                        onClick = {
                            onNavigate.invoke()
                        }
                    ) {
//
                        androidx.compose.material3.Text(
                            modifier = modifier.padding(
                                vertical = 8.dp
                            ),
                            text = buildAnnotatedString {
                                append("Start Your Flow")

                            },
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }else {
                    Button(
                        modifier = modifier
                            .fillMaxWidth()
                            .alpha(0f)
                            .padding(
                                horizontal = 12.dp
                            ),
                        shape = RoundedCornerShape(16.dp),
                        onClick = {

                        }
                    ) {
//
                        androidx.compose.material3.Text(
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
                }
            }
        }
    }
}