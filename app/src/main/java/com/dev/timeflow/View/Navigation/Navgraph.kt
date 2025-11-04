package com.dev.timeflow.View.Navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.FloatingToolbarExitDirection.Companion.Bottom
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.composables.icons.lucide.CalendarDays
import com.composables.icons.lucide.CalendarRange
import com.composables.icons.lucide.CalendarX2
import com.composables.icons.lucide.Lucide
import com.dev.timeflow.Data.Model.DropdownModel
import com.dev.timeflow.R

import com.dev.timeflow.View.Screens.CalenderScreen
import com.dev.timeflow.View.Screens.TodayScreen
import com.dev.timeflow.View.Screens.onBoarding.FeatureScreen
import com.dev.timeflow.View.Screens.onBoarding.NotificationScreen
import com.dev.timeflow.View.Screens.onBoarding.WelcomeScreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    var showDropDown by remember { mutableStateOf(false) }
    var selectedDropDown by remember { mutableIntStateOf(0) }

    val dropdownItem = listOf<DropdownModel>(
        DropdownModel(
            title = "Week",
            icon = Lucide.CalendarDays,
            onClick = {
                selectedDropDown = 0
                showDropDown = false
            }
        ),
        DropdownModel(
            title = "Month",
            icon = Lucide.CalendarX2,
            onClick = {
                selectedDropDown = 1
                showDropDown = false
            }
        )
    )


   if (currentRoute?.startsWith("m_") == true){
       Scaffold(
           modifier = modifier.nestedScroll(
               FloatingToolbarDefaults.exitAlwaysScrollBehavior(exitDirection =Bottom )
           ),
           topBar = {
               TopAppBar(
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
                       AnimatedContent(
                           targetState = navController.currentDestination?.route == Routes.CalendarScreen.route,
                           transitionSpec = {
                               scaleIn(
                                   animationSpec = spring(
                                       dampingRatio = Spring.DampingRatioMediumBouncy,
                                       stiffness = Spring.StiffnessLow
                                   )
                               ) togetherWith scaleOut(
                                   animationSpec = spring(
                                       dampingRatio = Spring.DampingRatioMediumBouncy,
                                       stiffness = Spring.StiffnessLow
                                   )
                               )
                           }
                       ) {
                           if (it){
                               IconButton(
                                   onClick = {
                                       showDropDown = !showDropDown
                                   }
                               ) {
                                   Icon(
                                       imageVector = Lucide.CalendarRange,
                                       contentDescription = null
                                   )
                               }

                               DropdownMenu(
                                   expanded = showDropDown,
                                   onDismissRequest = { showDropDown = false }
                               ) {
                                   dropdownItem.forEachIndexed { index, model ->

                                       val isSelected = selectedDropDown == index

                                       DropdownMenuItem(
                                           modifier = Modifier
                                               .fillMaxWidth()
                                               .padding(
                                                   horizontal = 4.dp,
                                                   vertical = 2.dp
                                               )
                                               .clip(RoundedCornerShape(8.dp))

                                               .background(
                                                   if (isSelected) MaterialTheme.colorScheme.primaryContainer
                                                   else MaterialTheme.colorScheme.surface
                                               ),
                                           text = {
                                               Row(
                                                   verticalAlignment = Alignment.CenterVertically,
                                                   modifier = Modifier.fillMaxWidth()
                                               ) {
                                                   Icon(
                                                       imageVector = model.icon,
                                                       contentDescription = null,
                                                       tint = if (isSelected) MaterialTheme.colorScheme.primary
                                                       else MaterialTheme.colorScheme.onSurface
                                                   )

                                                   Spacer(Modifier.width(8.dp))

                                                   Text(
                                                       text = model.title,
                                                       color = if (isSelected) MaterialTheme.colorScheme.primary
                                                       else MaterialTheme.colorScheme.onSurface
                                                   )
                                               }
                                           },
                                           onClick = {
                                               model.onClick()
                                               showDropDown = false
                                           }
                                       )
                                   }
                               }


                           }
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

                   composable(route = Routes.CalendarScreen.route) {
                       CalenderScreen(
                           selectedTab = selectedDropDown
                       )
                   }



               }

           }
       }
   } else {
       NavHost(
           navController = navController,
           startDestination = Routes.WelcomeScreen.route
       ){
           composable(route = Routes.TimerScreen.route) {
               TodayScreen()
           }

           composable(route = Routes.CalendarScreen.route) {
               CalenderScreen(
                   selectedTab = selectedDropDown
               )
           }
           composable(route = Routes.WelcomeScreen.route){
               WelcomeScreen(
                   onNavigate = {
                       navController.navigate(Routes.ShowFeaturesScreen.route)
                   }
               )
           }

           composable(route = Routes.ShowFeaturesScreen.route){
               FeatureScreen(
                   onNavigate = {
                       navController.navigate(Routes.NotificationScreen.route)
                   }
               )
           }

           composable(route = Routes.NotificationScreen.route) {
               NotificationScreen(
                   onNavigate = {
                       navController.navigate(Routes.TimerScreen.route)
                   }
               )
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
        route = Routes.CalendarScreen
    ),
)