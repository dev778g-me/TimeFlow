package com.dev.timeflow.View.Widget

import android.graphics.drawable.Icon
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.HapticFeedbackConstantsCompat
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.Lucide

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NewCheckBox(
    modifier: Modifier = Modifier,
    isSelected : Boolean,
    onCheckChange : (Boolean) -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f
    )
    val view = LocalView.current
    val color =
        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val borderColor =
        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
    Box(
        modifier = modifier
            .padding(
                8.dp
            )
            .clip(RoundedCornerShape(6.dp))
            .clickable(
                onClick = {
                    view.performHapticFeedback(
                        HapticFeedbackConstantsCompat.CONFIRM
                    )
                    onCheckChange.invoke(!isSelected)
                }
            )
            .background(
                color = color
            )
            .border(
                shape = RoundedCornerShape(6.dp),
                width = 1.dp,
                color = borderColor
            )
    ) {
        androidx.compose.material3.Icon(
            modifier = modifier
                .graphicsLayer {
                    scaleY = scale
                    scaleX = scale
                }
                .size(ButtonDefaults.SmallIconSize + 0.dp)
                .padding(
                    2.dp
                ),
            tint = MaterialTheme.colorScheme.onPrimary,
            imageVector = Lucide.Check,
            contentDescription = null
        )
    }
}