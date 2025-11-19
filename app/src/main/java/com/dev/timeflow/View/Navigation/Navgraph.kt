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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.composables.icons.lucide.CalendarDays
import com.composables.icons.lucide.CalendarRange
import com.composables.icons.lucide.CalendarX2
import com.composables.icons.lucide.House
import com.composables.icons.lucide.Info
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Menu
import com.dev.timeflow.Data.Model.DropdownModel
import com.dev.timeflow.Data.Model.NavigationDrawerModel
import com.dev.timeflow.R

import com.dev.timeflow.View.Screens.CalenderScreen
import com.dev.timeflow.View.Screens.PrivacyPolicyScreen
import com.dev.timeflow.View.Screens.TodayScreen
import com.dev.timeflow.View.Screens.onBoarding.FeatureScreen
import com.dev.timeflow.View.Screens.onBoarding.NotificationScreen
import com.dev.timeflow.View.Screens.onBoarding.WelcomeScreen
import com.dev.timeflow.Viewmodel.TaskAndEventViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NavGraph(modifier: Modifier = Modifier, startDest : String) {
    val navController = rememberNavController()
    val currentRoute by navController.currentBackStackEntryAsState()
    var showDropDown by remember { mutableStateOf(false) }
    var selectedDrawerItem by rememberSaveable() {mutableStateOf(0) }
    val isCompleted = startDest == Routes.TimerScreen.route
    var userName by rememberSaveable() {mutableStateOf("") }
    val taskAndEventViewModel : TaskAndEventViewModel = hiltViewModel()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val selectedCalendarType by taskAndEventViewModel.readCalendarType().collectAsStateWithLifecycle(0)
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(drawerState.isClosed) {
        keyboardController?.hide()
    }
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

    val navigationDrawerItems = listOf<NavigationDrawerModel>(
        NavigationDrawerModel(
            name = "Home",
            selectedIcon = Lucide.House,
            unSelectedIcon = Lucide.House,
            onClick = {}
        ),
        NavigationDrawerModel(
            name = "Privacy Policy",
            selectedIcon = Lucide.Info,
            unSelectedIcon = Lucide.Info,
            onClick = {}
        ),
        NavigationDrawerModel(
            name = "About",
            selectedIcon = Lucide.Info,
            unSelectedIcon = Lucide.Info,
            onClick = {}
        )
    )



    ModalNavigationDrawer(
       // gesturesEnabled = false,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(

            ) {

                Column(
                //    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier.padding(
                        horizontal = 16.dp,
                        vertical = 16.dp
                    )
                ) {

                   ListItem(
                       modifier = modifier.fillMaxWidth(0.7f),
                       colors = ListItemDefaults.colors(
                           containerColor = Color.Transparent
                       ),
                       headlineContent = {
                           Row(
                               modifier = modifier.fillMaxWidth(),
                               verticalAlignment = Alignment.CenterVertically
                           ) {

                               Text(
                                   modifier = modifier.weight(1f),
                                   text = "Timeflow",
                                   style = MaterialTheme.typography.headlineSmall
                               )
                               Icon(
                                   modifier = modifier.size(ButtonDefaults.LargeIconSize + 12.dp),
                                   painter = painterResource(
                                       id = R.drawable.mono_logo,

                                       ),
                                   contentDescription = null
                              )
                          }
                       },
                       supportingContent = {
                           Text(
                               text = "Version 1.2",
                               fontFamily = FontFamily.Monospace
                           )
                       }

                   )
                    Spacer(modifier = modifier.height(40.dp))
                    TextField(
                        modifier = modifier.padding(
                            bottom = 8.dp
                        ),
                        value = userName,
                        onValueChange = {
                            userName = it
                        },
                        label = {
                            Text("user name")
                        }
                    )

                    navigationDrawerItems.forEachIndexed { index , item ->
                        val isSelected = selectedDrawerItem == index
                        NavigationDrawerItem(
                            modifier = modifier
                                .fillMaxWidth(0.7f)
                                .padding(
                                    vertical = 8.dp
                                ),
                            selected = isSelected,
                            label = {
                                Text(
                                    item.name
                                )
                            },
                            onClick = {
                                selectedDrawerItem = index
                              //  navController.navigate(Routes.PrivacyScreen.route)
                            },
                            icon = {
                                Icon(
                                    imageVector = item.selectedIcon, contentDescription = null
                                )
                            })
                    }
                }
            }

        },

        ) {
       Scaffold(
           topBar = {
              if (isCompleted){
                  TopAppBar(
                      navigationIcon = {

                          IconButton(
                              onClick = {
                                  scope.launch {
                                      drawerState.open()
                                  }
                              }
                          ) {
                              Icon(imageVector = Lucide.Menu, contentDescription = null)
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
                              targetState = currentRoute?.destination?.route== Routes.CalendarScreen.route,
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

                                          val isSelected = selectedCalendarType == index

                                          DropdownMenuItem(
                                              modifier = Modifier
                                                  .fillMaxWidth()
                                                  .padding(
                                                      horizontal = 4.dp, vertical = 2.dp
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
              }
           },
           floatingActionButtonPosition = FabPosition.Center,
           floatingActionButton = {

           },
           bottomBar = {
              if (isCompleted){
                  FloatingBottomNav(
                      navController = navController
                  )
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

                   composable(route = Routes.PrivacyScreen.route) {
                       PrivacyPolicyScreen()
                   }


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
        route = Routes.CalendarScreen
    ),
)