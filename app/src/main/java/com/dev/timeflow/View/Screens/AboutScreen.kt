package com.dev.timeflow.View.Screens

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.glance.LocalContext
import com.composables.icons.lucide.Code
import com.composables.icons.lucide.Github
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Star
import com.composables.icons.lucide.Twitter
import com.dev.timeflow.R
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun AboutScreen(modifier: Modifier = Modifier) {
    val context = androidx.compose.ui.platform.LocalContext.current
    androidx.compose.material3.Scaffold(
        topBar = {
            LargeTopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {

                        },

                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Text(
                        text = "About"
                    )
                }
            )
        }
    ) { innerPadding->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp, end = 16.dp,
                    top = innerPadding.calculateTopPadding()
                )
        ) {
            ListItem(
                modifier = modifier
                    .padding(
                        vertical = 2.dp
                    )
                    .clip(
                        RoundedCornerShape(
                            topEnd = 24.dp,
                            topStart = 24.dp,
                            bottomStart = 8.dp, bottomEnd = 8.dp
                        )
                    ),
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                leadingContent = {
                    androidx.compose.foundation.Image(
                        modifier = modifier.size(50.dp),
                        painter = painterResource(
                            id = R.drawable.timeflow_mono_logo
                        ),
                        contentDescription = null
                    )
                },
                headlineContent = {
                    Text(
                        text = "Timeflow"
                    )
                },
                supportingContent = {
                    Text(
                        text = "Version 1.0"
                    )
                },
                trailingContent = {
                    FilledTonalIconButton(
                        onClick = {
                            val url = "https://github.com/dev778g-me/TimeFlow"
                            val intent =
                                Intent(Intent.ACTION_VIEW, url.toUri())
                            context.startActivity(intent)
                        }
                    ) {
                        Icon(
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            imageVector = Lucide.Github,
                            contentDescription = null
                        )
                    }
                }
            )
            ListItem(
                modifier = modifier
                    .padding(
                        vertical = 2.dp
                    )
                    .clip(RoundedCornerShape(12.dp)),

                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                leadingContent = {
                    Icon(
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = modifier
                            .size(50.dp)
                            .padding(
                                8.dp
                            ),
                        imageVector = Lucide.Code,
                        contentDescription = null
                    )
                },
                headlineContent = {
                    Text(
                        text = "Debansh Sahu"
                    )
                },
                trailingContent = {
                   Row {
                       FilledTonalIconButton(
                           onClick = {
                               val url = "https://x.com/Dev778g"
                               val intent =
                                   Intent(Intent.ACTION_VIEW, url.toUri())
                               context.startActivity(intent)
                           }
                       ) {
                           Icon(
                               tint = MaterialTheme.colorScheme.onPrimaryContainer,
                               imageVector = Lucide.Twitter,
                               contentDescription = null
                           )
                       }
                       FilledTonalIconButton(
                           onClick = {
                               val url = "https://github.com/dev778g-me/"
                               val intent =
                                   Intent(Intent.ACTION_VIEW, url.toUri())
                               context.startActivity(intent)
                           }
                       ) {
                           Icon(
                               tint = MaterialTheme.colorScheme.onPrimaryContainer,
                               imageVector = Lucide.Github,
                               contentDescription = null
                           )
                       }



                   }
                },
                supportingContent = {
                    Column {
                        Text(
                            text = "Developer"
                        )

                    }
                }
            )

            ListItem(
                modifier = modifier
                    .padding(
                        vertical = 2.dp
                    )
                    .clip(
                        RoundedCornerShape(
                            topEnd = 8.dp,
                            topStart = 8.dp,
                            bottomStart = 24.dp, bottomEnd = 24.dp
                        )
                    ).clickable(
                        onClick = {
                            val url = "https://play.google.com/store/apps/details?id=com.dev.timeflow"
                            val intent =
                                Intent(Intent.ACTION_VIEW, url.toUri())
                            context.startActivity(intent)
                        }
                    ),
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                leadingContent = {
                    Icon(
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = modifier
                            .size(50.dp)
                            .padding(
                                8.dp
                            ),
                        imageVector = Lucide.Star,
                        contentDescription = null
                    )
                },
                headlineContent = {
                    Text(
                        text = "Rate on Google Play"
                    )

                }
                , supportingContent = {
                    Text(
                        text = "Liked the app? Write a review"
                    )
                }
            )
        }
    }
}

