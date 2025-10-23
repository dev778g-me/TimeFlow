package com.dev.timeflow.View.Navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dev.timeflow.View.Screens.HomeScreen
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.room.util.copy
import com.composables.icons.lucide.Calendar
import com.composables.icons.lucide.Contact
import com.composables.icons.lucide.Lucide
import com.dev.timeflow.View.Screens.AddEventScreen
import com.dev.timeflow.View.Screens.NewTask
import com.dev.timeflow.R
import com.dev.timeflow.View.Screens.TodayScreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val isTaskScreen = backStackEntry?.destination?.route == Routes.NewTaskScreen.route
    Scaffold(
        topBar = {
            TopAppBar(
//                navigationIcon = {
//                    Icon(
//                        modifier = modifier.padding(
//                            start = 12.dp,
//                            end = 2.dp
//                        ),
//                        imageVector = Lucide.Calendar,
//                        contentDescription = null
//                    )
//                },
                navigationIcon = {
                    Row(
                        modifier = modifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        androidx.compose.foundation.Image(
                            modifier = modifier.size(ButtonDefaults.ExtraLargeIconSize),
                            painter = painterResource(
                                id = R.drawable.timeflow_mono_logo
                            ),
                            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(
                                MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            contentDescription = null
                        )
//
                    }
                },
                title = {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(
                                color = MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = 0.8f
                                )
                            )){
                                append("Welcome back, ")
                            }

                            withStyle(style = SpanStyle(
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )){
                                append("Dev")
                            }
                        },
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Normal
                        )
                    )
                },
                actions = {
                    FilledIconButton(
                        onClick = {}
                    ) {
                        Icon(
                            modifier = modifier.size(IconButtonDefaults.smallIconSize),
                            imageVector = Lucide.Contact,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingBottomNav(

                navController =
                navController
            )
        },

        contentWindowInsets = WindowInsets(0.dp),

        ) {
        p->
        Box(
            contentAlignment = Alignment.Center
        ){
            NavHost(
                modifier = modifier
                    .fillMaxSize()
                    .padding(p),
                navController = navController,
                startDestination = Routes.TimerScreen.route
            ) {
                composable(route = Routes.TimerScreen.route) {
                    TodayScreen()
                }
                composable(route = Routes.AddEventScreen.route) {
                    AddEventScreen()
                }

                composable(route = Routes.NewTaskScreen.route) {
                    NewTask()
                }

            }

        }
    }


}



data class BottomNavAttribute(
    val title : String,
    val unselectedIcon : ImageVector,
    val selectedIcon : ImageVector,
    val route: Routes
)

val bottomNavItems = listOf(
    BottomNavAttribute(
        title = "Home",
        unselectedIcon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home,
        route = Routes.TimerScreen
    ),
    BottomNavAttribute(
        title = "Tasks",
        unselectedIcon = Icons.Outlined.TaskAlt,
        selectedIcon = Icons.Filled.TaskAlt,
        route = Routes.NewTaskScreen
    ),
)