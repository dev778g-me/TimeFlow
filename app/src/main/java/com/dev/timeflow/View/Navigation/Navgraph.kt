package com.dev.timeflow.View.Navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateBounds
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.motionScheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.composables.icons.lucide.CalendarDays
import com.composables.icons.lucide.CalendarRange
import com.composables.icons.lucide.CalendarX2
import com.composables.icons.lucide.ListTodo
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Settings
import com.dev.timeflow.Data.Model.DropdownModel
import com.dev.timeflow.View.Screens.CalenderScreen
import com.dev.timeflow.View.Screens.SettingsScreen
import com.dev.timeflow.View.Screens.TodayScreen
import com.dev.timeflow.View.Screens.onBoarding.FeatureScreen
import com.dev.timeflow.View.Screens.onBoarding.NotificationScreen
import com.dev.timeflow.View.Screens.onBoarding.WelcomeScreen
import com.dev.timeflow.View.utils.JoinedBlockShape
import com.dev.timeflow.Viewmodel.TaskAndEventViewModel
import kotlinx.coroutines.launch

private val appBarEnter = fadeIn(animationSpec = tween(120)) + slideInVertically(
    animationSpec = spring(
        dampingRatio = Spring.DampingRatioLowBouncy,
        stiffness = Spring.StiffnessLow
    ),
)

private val appBarExit = fadeOut(animationSpec = tween(100)) + scaleOut(
    animationSpec = tween(100),
    targetScale = 0.7f,
    transformOrigin = TransformOrigin(
        pivotFractionX = 0.5f,
        pivotFractionY = 1f
    )
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NavGraph(modifier: Modifier = Modifier, startDest : String) {
    val navController = rememberNavController()
    val currentRoute by navController.currentBackStackEntryAsState()
    val destination = currentRoute?.destination?.route
    var showDropDown by rememberSaveable { mutableStateOf(false) }

    val showTopBarAndNavigationBar =
        destination == Routes.TimerScreen.route || destination == Routes.CalendarScreen.route

    val taskAndEventViewModel : TaskAndEventViewModel = hiltViewModel()
    val selectedCalendarType by taskAndEventViewModel.readCalendarType()
        .collectAsStateWithLifecycle(0)
    val scope = rememberCoroutineScope()
    val name by taskAndEventViewModel.readName().collectAsStateWithLifecycle("")

    val selectedIndex = when (destination) {
        Routes.TimerScreen.route -> 0
        Routes.CalendarScreen.route -> 1
        else -> -1
    }


    val boundAnimationSpec: FiniteAnimationSpec<androidx.compose.ui.geometry.Rect> =
        motionScheme.slowEffectsSpec ()
    val dropdownItem = listOf<DropdownModel>(
        DropdownModel(
            title = "Week",
            icon = Lucide.CalendarDays,
            onClick = {
                scope.launch {
                    taskAndEventViewModel.saveSelectedCalenderType(
                        type = 0
                    )
                }
                showDropDown = false
            }
        ),
        DropdownModel(
            title = "Month",
            icon = Lucide.CalendarX2,
            onClick = {
                scope.launch {
                    taskAndEventViewModel.saveSelectedCalenderType(
                        type = 1
                    )
                }
                showDropDown = false
            }
        )
    )






       Scaffold(
           topBar = {
              AnimatedVisibility(
                  visible = showTopBarAndNavigationBar,
                  enter = appBarEnter,
                  exit = appBarExit
              ) {
                  TopAppBar(
                      title = {
                          Text(
                              maxLines = 1,
                              text = buildAnnotatedString {
                                  withStyle(
                                      style = SpanStyle(
                                          color = MaterialTheme.colorScheme.onSurface.copy(
                                              alpha = 0.8f
                                          )
                                      )
                                  ) {
                                      append("Welcome back ")
                                  }

                                  withStyle(
                                      style = SpanStyle(
                                          fontWeight = FontWeight.ExtraBold,
                                          color = MaterialTheme.colorScheme.onSurfaceVariant
                                      )
                                  ) {
                                      append(name)
                                  }
                              },
                              style = MaterialTheme.typography.titleMedium.copy(
                                  fontWeight = FontWeight.Normal
                              )
                          )
                      },
                      actions = {
                          AnimatedContent(
                              targetState = destination == Routes.CalendarScreen.route,
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
                                      shape = RoundedCornerShape(16.dp),
                                      expanded = showDropDown,
                                      onDismissRequest = { showDropDown = false }
                                  ) {

                                          Column(
                                              modifier = Modifier.padding(horizontal = 4.dp)
                                          ) {
                                              dropdownItem.forEachIndexed { index, model ->

                                                  val isSelected = selectedCalendarType == index

                                                  DropdownMenuItem(
                                                      modifier = Modifier
                                                          .fillMaxWidth()
                                                          .padding(
                                                              horizontal = 4.dp, vertical = 2.dp
                                                          )
                                                          .clip(
                                                              JoinedBlockShape(
                                                                  largeRadius = 12.dp,
                                                                  isFirst = dropdownItem.indexOf(
                                                                      model
                                                                  ) == 0,
                                                                  isLast = index == dropdownItem.lastIndex
                                                              )
                                                          )

                                                          .background(
                                                              if (isSelected) MaterialTheme.colorScheme.secondaryContainer
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
                                                                  tint = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer
                                                                  else MaterialTheme.colorScheme.onSurface
                                                              )

                                                              Spacer(Modifier.width(8.dp))

                                                              Text(
                                                                  text = model.title,
                                                                  color = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer
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


                               }else{
                                   IconButton(
                                       onClick = {
                                           navController.navigate(Routes.SettingsScreen.route)
                                       }
                                   ) {
                                       Icon(
                                           imageVector = Lucide.Settings,
                                           contentDescription = "Settings"
                                       )
                                   }
                               }
                          }
                      }
                  )
              }
           },
           floatingActionButtonPosition = FabPosition.Center,
           floatingActionButton = {

           },
           bottomBar = {
               AnimatedVisibility(
                   visible = showTopBarAndNavigationBar,
                   enter = appBarEnter,
                   exit = appBarExit
               ) {
                   LookaheadScope {
                       Box(
                           modifier = Modifier
                               .fillMaxWidth()
                               .padding(bottom = 42.dp),
                           contentAlignment = Alignment.BottomCenter
                       ) {
                           HorizontalFloatingToolbar(
                               expanded = true,
                               //   scrollBehavior = toolbarScrollBehavior,
                               colors = FloatingToolbarDefaults.vibrantFloatingToolbarColors(
                                   toolbarContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                   toolbarContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                               ),
                               modifier = Modifier
                                   .zIndex(1f)
                               ,
                           ) {
                                bottomNavItems.forEachIndexed { index, item ->
                                    val selected = selectedIndex == index

                                   TooltipBox(
                                       positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                                           TooltipAnchorPosition.Above
                                       ),
                                       tooltip = {
                                           PlainTooltip {
                                               Text(item.title)
                                           }
                                       },
                                       state = rememberTooltipState()
                                   ) {
                                       ToggleButton(
                                           checked = selected,
                                           onCheckedChange = {
                                                navController.navigate(item.route.route) {
                                                    popUpTo(navController.graph.findStartDestination().id) {
                                                       saveState = true
                                                   }
                                                   launchSingleTop = true
                                                   restoreState = true
                                               }
                                           },
                                           colors = ToggleButtonDefaults.toggleButtonColors(
                                               containerColor = MaterialTheme.colorScheme.primaryContainer,
                                               contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                               checkedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                                               checkedContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                                           ),
                                           shapes = ToggleButtonDefaults.shapes(
                                               CircleShape,
                                               CircleShape,
                                               CircleShape
                                           ),
                                           modifier = Modifier
                                               .height(56.dp)
                                               .animateBounds(
                                                   lookaheadScope = this@LookaheadScope,
                                                   boundsTransform = BoundsTransform { i, o ->
                                                       boundAnimationSpec
                                                   }
                                               )
                                       ) {
                                           Row(
                                               verticalAlignment = Alignment.CenterVertically
                                           ) {
                                               Crossfade(
                                                   targetState = selected,
                                                   label = "navigationIcon"
                                               ) { isSelected ->
                                                   Icon(
                                                       imageVector =
                                                           if (isSelected) {
                                                               item.selectedIcon
                                                           } else {
                                                               item.unselectedIcon
                                                           },

                                                       contentDescription = ""
                                                   )
                                               }

                                               AnimatedVisibility(
                                                   visible = selected,
                                                   enter = expandHorizontally(
                                                       animationSpec = motionScheme.defaultSpatialSpec()
                                                   ) + fadeIn(),
                                                   exit = shrinkHorizontally(
                                                       animationSpec = motionScheme.defaultSpatialSpec()
                                                   ) + fadeOut()
                                               ) {
                                                   Text(
                                                       text = item.title,
                                                       fontSize = 16.sp,
                                                       lineHeight = 24.sp,
                                                       fontWeight = FontWeight.ExtraBold,
                                                       maxLines = 1,
                                                       softWrap = false,
                                                       overflow = TextOverflow.Clip,
                                                       modifier = Modifier.padding(
                                                           start = ButtonDefaults.IconSpacing
                                                       )
                                                   )
                                               }
                                           }
                                       }
                                   }
                               }
                           }
                       }}
               }
           },


           contentWindowInsets = WindowInsets(0.dp),

           ) { p ->
           Box(
               contentAlignment = Alignment.Center
           ) {
               NavHost(
                   modifier = modifier
                       .fillMaxSize()
                       .padding(p),
                   navController = navController,
                   startDestination = startDest,
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
                           selectedTab = selectedCalendarType
                       )
                   }

                   composable(route = Routes.WelcomeScreen.route) {
                       WelcomeScreen(
                           onNavigate = {
                               navController.navigate(Routes.ShowFeaturesScreen.route)
                           }
                       )
                   }

                   composable(route = Routes.ShowFeaturesScreen.route) {
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

                    composable(
                        route = Routes.SettingsScreen.route
                    ){
                        SettingsScreen(
                            onBack = { navController.popBackStack() }
                        )
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
        title = "Today",
        unselectedIcon = Lucide.ListTodo,
        selectedIcon = Lucide.ListTodo,
        route = Routes.TimerScreen
    ),
    BottomNavAttribute(
        title = "Calendar",
        unselectedIcon = Lucide.CalendarDays,
        selectedIcon = Lucide.CalendarDays,
        route = Routes.CalendarScreen
    ),
)