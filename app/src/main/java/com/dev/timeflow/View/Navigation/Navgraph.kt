package com.dev.timeflow.View.Navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material3.AppBarRow
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.FloatingToolbarExitDirection.Companion.Bottom
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.composables.icons.lucide.Calendar
import com.composables.icons.lucide.Contact
import com.composables.icons.lucide.House
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import com.dev.timeflow.R
import com.dev.timeflow.View.Screens.AddEventScreen
import com.dev.timeflow.View.Screens.CalenderScreen
import com.dev.timeflow.View.Screens.TodayScreen
import com.dev.timeflow.Viewmodel.TaskAndEventViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val isTaskScreen = backStackEntry?.destination?.route == Routes.NewTaskScreen.route
    val taskAndEventViewModel : TaskAndEventViewModel = hiltViewModel()
    val scrollStateValue by taskAndEventViewModel.scrollStateValue.collectAsState(0)
    val bottomNavItems = listOf(
        BottomNavAttribute(
            title = "Today",
            unselectedIcon = Lucide.House,
            selectedIcon = Lucide.House,
            route = Routes.TimerScreen
        ),
        BottomNavAttribute(
            title = "Calender",
            unselectedIcon = Lucide.Calendar,
            selectedIcon = Lucide.Calendar,
            route = Routes.NewTaskScreen
        ),
    )
    var selectedIndex by remember { mutableStateOf(0) }
    Scaffold(
        modifier = modifier.nestedScroll(
            FloatingToolbarDefaults.exitAlwaysScrollBehavior(exitDirection =Bottom )
        ),
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

        },
        bottomBar = {
            FloatingBottomNav(
            navController = navController
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
                startDestination = Routes.TimerScreen.route,
                enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
                popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
                popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
            ) {
                composable(route = Routes.TimerScreen.route) {
                    TodayScreen()
                }
                composable(route = Routes.AddEventScreen.route) {
                    AddEventScreen()
                }

                composable(route = Routes.NewTaskScreen.route) {
                    CalenderScreen()
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