package com.dev.timeflow.View.Navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.composables.icons.lucide.Calendar
import com.composables.icons.lucide.House
import com.composables.icons.lucide.Lucide



@Composable
fun FloatingBottomNav(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }



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


  Box(
     modifier = Modifier
          .fillMaxWidth(),
      contentAlignment = androidx.compose.ui.Alignment.Center
  ) {
      Box(
          modifier = modifier,
//
//       modifier.fillMaxWidth().background(
//           color = Color.T
//       ),
          contentAlignment = androidx.compose.ui.Alignment.Center
      ) {
          Box(
              modifier = modifier
                  .padding(
                      bottom = 24.dp
                  )
                  .background(
                      color = MaterialTheme.colorScheme.surfaceContainer,
                      shape = RoundedCornerShape(34.dp)
                  )
          ) {
              Row(
                  modifier = modifier.padding(8.dp),
                  horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                  bottomNavItems.forEachIndexed { index, attribute ->
                      val isSelected = selectedIndex == index

                      val bgColor by animateColorAsState(
                          targetValue = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                      )
                      val textColor by animateColorAsState(
                          targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                      )
                      Row(
                          modifier = modifier
                              .clip(RoundedCornerShape(24.dp))
                              .background(
                                  color =bgColor
                              )
                              .clickable(
                                  onClick = {
                                      selectedIndex = index
                                      navController.navigate(attribute.route.route){
                                          popUpTo(navController.graph.findStartDestination().id){
                                              saveState = true
                                          }
                                          launchSingleTop = true
                                          restoreState = true
                                      }
                                  })
                              .padding(10.dp),
                          verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                          Icon(
                              modifier = modifier.size(ButtonDefaults.IconSize),
                              tint = textColor,
                              imageVector = if (selectedIndex == index) attribute.selectedIcon else attribute.unselectedIcon,
                              contentDescription = null
                          )
                          Spacer(
                              modifier = modifier.width(2.dp)
                          )
                          androidx.compose.material3.Text(
                              style = MaterialTheme.typography.titleSmall.copy(
                                  fontWeight = FontWeight.SemiBold,
                                  color = textColor
                              ),
                              text = attribute.title
                          )
                      }
                  }
              }
          }
      }
  }
}