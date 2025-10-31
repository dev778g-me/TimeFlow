package com.dev.timeflow.View.Navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.composables.icons.lucide.Calendar
import com.composables.icons.lucide.CalendarDays
import com.composables.icons.lucide.House
import com.composables.icons.lucide.HousePlug
import com.composables.icons.lucide.HousePlus
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MapPinHouse


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
            selectedIcon = Lucide.CalendarDays,
            route = Routes.NewTaskScreen
        ),
    )

    NavigationBar(){
        bottomNavItems.forEachIndexed { index, bottomNav ->
            val isSelected = selectedIndex == index
            val scale by animateFloatAsState(
                targetValue = if (isSelected) 1f else 0.9f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioHighBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
            NavigationBarItem(
                modifier =  modifier.graphicsLayer{
                    scaleY = scale
                    scaleX = scale
                },
                selected = isSelected,
                onClick = {
                    selectedIndex = index
                    navController.navigate(bottomNav.route.route){
                        popUpTo(navController.graph.findStartDestination().id){
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = {
                    Text(
                        text = bottomNav.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    )
                },
                icon = {
                    Icon(
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        imageVector = if (isSelected) bottomNav.selectedIcon else bottomNav.unselectedIcon,
                        contentDescription = null
                    )
                }
            )
        }
    }

}