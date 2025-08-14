package com.dev.timeflow.View.Navigation

import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dev.timeflow.View.Screens.EventScreen
import com.dev.timeflow.View.Screens.HomeScreen
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.dev.timeflow.View.Screens.AddEventScreen
import com.dev.timeflow.View.Screens.NewTask
import com.dev.timeflow.View.Screens.TaskScreen
import com.dev.timeflow.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        androidx.compose.foundation.Image(
                            modifier = modifier.size(40.dp),
                            painter = painterResource(
                                id = R.drawable.timeflow_mono_logo
                            ),
                            colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(
                                MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            contentDescription = null
                        )
                        Text(
                            text = "TimeFlow",
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEachIndexed {
                    index , navItem ->
                    val isSelected = backStackEntry?.destination?.route == navItem.route.route
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            navController.navigate(navItem.route.route){
                                popUpTo(navController.graph.findStartDestination().id){
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) navItem.selectedIcon else navItem.unselectedIcon,
                                contentDescription = null,
                                tint =if (isSelected)  MaterialTheme.colorScheme.onPrimaryContainer else NavigationBarItemDefaults.colors().selectedIconColor
                            )
                        },
                        label = {
                            Text(text = navItem.title)
                        }
                    )
                }
            }
        }
    ) {
        NavHost(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
            navController = navController,
            startDestination = Routes.TimerScreen.route
        ) {
            composable(route = Routes.TimerScreen.route) {
                HomeScreen(navController = navController)
            }

            composable(route = Routes.EventScreen.route) {
                EventScreen(
                    onNavigate = {
                        navController.navigate(Routes.AddEventScreen.route)
                    }
                )
            }


            composable(route = Routes.AddEventScreen.route) {
                AddEventScreen()
            }
            composable(route = Routes.TaskScreen.route) {
                TaskScreen()
            }
            composable(route = Routes.NewTaskScreen.route) {
                NewTask()
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
    BottomNavAttribute(
        title = "Events",
        unselectedIcon = Icons.Outlined.Event,
        selectedIcon = Icons.Filled.Event,
        route = Routes.EventScreen
    )
)