package com.dev.timeflow.View.Navigation

import android.content.Intent
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
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
import com.composables.icons.lucide.Dock
import com.composables.icons.lucide.EllipsisVertical
import com.composables.icons.lucide.Info
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Star
import com.composables.icons.lucide.User
import com.dev.timeflow.Data.Model.DropdownModel
import com.dev.timeflow.View.Screens.AboutScreen
import com.dev.timeflow.View.Screens.CalenderScreen
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
    var showNameChange by rememberSaveable {mutableStateOf(false) }
    var showDropDown by rememberSaveable { mutableStateOf(false) }
    var showTimerScreenDropDown by rememberSaveable { mutableStateOf(false) }

    val isCompleted = startDest == Routes.TimerScreen.route && currentRoute?.destination?.route != Routes.AboutScreen.route

    var userName by rememberSaveable {mutableStateOf("") }
    val taskAndEventViewModel : TaskAndEventViewModel = hiltViewModel()
    val selectedCalendarType by taskAndEventViewModel.readCalendarType().collectAsStateWithLifecycle(0)
    val scope = rememberCoroutineScope()
    val name by taskAndEventViewModel.readName().collectAsStateWithLifecycle("")
    val context = LocalContext.current
if (showNameChange){
    ModalBottomSheet(
        onDismissRequest = {
            userName = ""
            showNameChange = false
        }
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp
                )
        ) {
            OutlinedTextField(
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = modifier.fillMaxWidth(),
                value = userName,
                onValueChange = {
                    userName = it
                },
                placeholder = { Text("What should we call you?") },
                label = { Text("Your name") }

            )
            Spacer(
                modifier = modifier.height(16.dp)
            )
            Button(
                enabled = userName.isNotEmpty(),
                shape = RoundedCornerShape(12.dp),
                modifier = modifier.fillMaxWidth(),
                onClick = {
                    showNameChange = false
                    if (userName.isNotEmpty()){
                        taskAndEventViewModel.saveName(
                            name = userName
                        )
                        userName = ""
                    }

                }
            ) {

                Text(
                    modifier = modifier.padding(
                        vertical = 8.dp
                    ),
                    text = "Save")
            }
        }
    }
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






       Scaffold(
           topBar = {
              if (isCompleted) {
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


                              }else{
                                  IconButton(
                                      onClick = {
                                          showTimerScreenDropDown = true
                                      }
                                  ) {
                                      Icon(
                                          imageVector = Lucide.EllipsisVertical,
                                          contentDescription = null
                                      )
                                  }

                                  DropdownMenu(
                                      expanded = showTimerScreenDropDown,
                                      onDismissRequest = {
                                          showTimerScreenDropDown = false
                                      }
                                  ) {
                                      DropdownMenuItem(
                                          leadingIcon = {
                                              Icon(
                                                  imageVector = Lucide.User,
                                                  contentDescription = null
                                              )
                                          },
                                          onClick = {
                                              showTimerScreenDropDown = false
                                              showNameChange = true
                                          },
                                          text = {
                                              Text("Change name")
                                          }
                                      )
                                      DropdownMenuItem(
                                          leadingIcon = {
                                              Icon(
                                                  imageVector = Lucide.Dock,
                                                  contentDescription = null
                                              )
                                          },
                                          onClick = {
                                              showTimerScreenDropDown = false
                                              val url = "https://timeflow.framer.website/privacypolicy"
                                              val intent =
                                                  Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url))
                                              context.startActivity(intent)
                                          },
                                          text = {
                                              Text("Privacy Policy")
                                          }
                                      )
                                      DropdownMenuItem(
                                          leadingIcon = {
                                              Icon(
                                                  imageVector = Lucide.Star,
                                                  contentDescription = null
                                              )
                                          },
                                          onClick = {
                                              showTimerScreenDropDown = false

                                          },
                                          text = {
                                              Text("Rate Timeflow")
                                          }
                                      )
                                      DropdownMenuItem(
                                          leadingIcon = {
                                              Icon(
                                                  imageVector = Lucide.Info,
                                                  contentDescription = null
                                              )
                                          },
                                          onClick = {
                                              navController.navigate(Routes.AboutScreen.route)
                                              showTimerScreenDropDown = false

                                          },
                                          text = {
                                              Text("About")
                                          }
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
               if (isCompleted) {
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

                   composable(
                       route = Routes.AboutScreen.route
                   ){
                       AboutScreen()
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