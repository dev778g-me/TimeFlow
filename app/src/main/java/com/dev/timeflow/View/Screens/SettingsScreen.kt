package com.dev.timeflow.View.Screens

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.composables.icons.lucide.CircleCheck
import com.composables.icons.lucide.Code
import com.composables.icons.lucide.Contrast
import com.composables.icons.lucide.Github
import com.composables.icons.lucide.Info
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Palette
import com.composables.icons.lucide.Star
import com.composables.icons.lucide.Sun
import com.composables.icons.lucide.Twitter
import com.composables.icons.lucide.Type
import com.dev.timeflow.BuildConfig
import com.dev.timeflow.Data.Model.BodyFont
import com.dev.timeflow.Data.Model.ContrastLevel
import com.dev.timeflow.Data.Model.ThemePreferences
import com.dev.timeflow.Data.Model.ThemeType
import com.dev.timeflow.R
import com.dev.timeflow.Viewmodel.ThemeViewModel
import com.materialkolor.PaletteStyle

private const val GITHUB_REPO = "https://github.com/dev778g-me/TimeFlow"
private const val DEVELOPER_PROFILE = "https://github.com/dev778g-me/"
private const val DEVELOPER_TWITTER = "https://x.com/Dev778g"
private const val PLAY_STORE =
    "https://play.google.com/store/apps/details?id=com.dev.timeflow"

/** A null [argb] means "no seed": fall back to the app or wallpaper colors. */
private data class SeedSwatch(val label: String, val argb: Long?)

private val seedSwatches = listOf(
    SeedSwatch("Default", null),
    SeedSwatch("Red", 0xFFD7263D),
    SeedSwatch("Orange", 0xFFF4511E),
    SeedSwatch("Amber", 0xFFF9A825),
    SeedSwatch("Green", 0xFF43A047),
    SeedSwatch("Teal", 0xFF00897B),
    SeedSwatch("Cyan", 0xFF00ACC1),
    SeedSwatch("Blue", 0xFF1E88E5),
    SeedSwatch("Indigo", 0xFF3949AB),
    SeedSwatch("Purple", 0xFF8E24AA),
    SeedSwatch("Pink", 0xFFD81B60),
    SeedSwatch("Brown", 0xFF6D4C41),
    SeedSwatch("Slate", 0xFF546E7A),
)

private val dimmed = 0.38f

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalLayoutApi::class
)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val themeViewModel: ThemeViewModel = hiltViewModel()
    val preferences by themeViewModel.themePreferences.collectAsState()
    val context = LocalContext.current

    fun openUrl(url: String) {
        context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                },
                title = { Text(text = "Settings") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = innerPadding.calculateTopPadding()
                )
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            AppearanceSection(preferences, themeViewModel)
            ColorsSection(preferences, themeViewModel)
            FontSection(preferences, themeViewModel)
            AboutSection(::openUrl)
        }
    }
}

@Composable
private fun AppearanceSection(
    preferences: ThemePreferences,
    themeViewModel: ThemeViewModel,
) {
    SettingsSection(title = "Appearance", icon = { Icon(Lucide.Sun, null) }) {
        SettingsGroup {
            Text(
                text = "Theme",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp)
            )
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                ThemeType.entries.forEachIndexed { index, type ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = ThemeType.entries.size
                        ),
                        selected = preferences.themeType == type,
                        onClick = { themeViewModel.setThemeType(type) },
                        label = { Text(type.name) }
                    )
                }
            }
            ListItem(
                headlineContent = { Text("Dynamic colors") },
                supportingContent = {
                    Text("Take colors from your wallpaper. Turn off to customize below.")
                },
                trailingContent = {
                    Switch(
                        checked = preferences.isDynamicTheme,
                        onCheckedChange = { themeViewModel.setDynamicTheme(it) }
                    )
                },
                colors = transparentListItemColors()
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ColorsSection(
    preferences: ThemePreferences,
    themeViewModel: ThemeViewModel,
) {
    val colorsEnabled = !preferences.isDynamicTheme

    SettingsSection(title = "Colors", icon = { Icon(Lucide.Palette, null) }) {
        SettingsGroup {
            if (!colorsEnabled) {
                Text(
                    text = "Dynamic colors are on, so the options below are ignored. " +
                        "Turn them off to pick your own.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            SectionLabel(
                text = "Seed color",
                enabled = colorsEnabled
            )
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                seedSwatches.forEach { swatch ->
                    Swatch(
                        swatch = swatch,
                        selected = swatch.argb == preferences.seedColor,
                        enabled = colorsEnabled,
                        onClick = { themeViewModel.setSeedColor(swatch.argb) }
                    )
                }
            }

            ChipRow(
                title = "Palette style",
                enabled = colorsEnabled,
                options = PaletteStyle.entries.map { it.name to it.name },
                selected = preferences.paletteStyle,
                onSelect = { themeViewModel.setPaletteStyle(it) }
            )

            ChipRow(
                title = "Contrast",
                enabled = colorsEnabled,
                icon = { Icon(Lucide.Contrast, contentDescription = null) },
                options = ContrastLevel.entries.map { it.name to it.name },
                selected = preferences.contrastLevel.name,
                onSelect = { name ->
                    themeViewModel.setContrastLevel(
                        ContrastLevel.entries.first { it.name == name }
                    )
                }
            )
        }
    }
}

@Composable
private fun FontSection(
    preferences: ThemePreferences,
    themeViewModel: ThemeViewModel,
) {
    SettingsSection(title = "Font", icon = { Icon(Lucide.Type, null) }) {
        SettingsGroup {
            BodyFont.entries.forEachIndexed { index, font ->
                val selected = font == preferences.bodyFont
                ListItem(
                    modifier = Modifier.clickable { themeViewModel.setBodyFont(font) },
                    headlineContent = { Text(font.label) },
                    supportingContent = {
                        Text(
                            text = "Ag",
                            fontFamily = font.fontFamily,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    trailingContent = {
                        if (selected) {
                            Icon(
                                imageVector = Lucide.CircleCheck,
                                contentDescription = "Selected",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    colors = transparentListItemColors()
                )
                if (index != BodyFont.entries.lastIndex) {
                    SectionDivider()
                }
            }
        }
    }
}

@Composable
private fun AboutSection(onOpenUrl: (String) -> Unit) {
    SettingsSection(title = "About", icon = { Icon(Lucide.Info, null) }) {
        SettingsGroup {
            ListItem(
                leadingContent = {
                    Image(
                        modifier = Modifier.size(50.dp),
                        painter = painterResource(id = R.drawable.timeflow_mono_logo),
                        contentDescription = null
                    )
                },
                headlineContent = { Text("Timeflow") },
                supportingContent = { Text("Version ${BuildConfig.VERSION_NAME}") },
                trailingContent = {
                    FilledTonalIconButton(onClick = { onOpenUrl(GITHUB_REPO) }) {
                        Icon(
                            imageVector = Lucide.Github,
                            contentDescription = "Source code",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
                colors = transparentListItemColors()
            )
            SectionDivider()
            ListItem(
                leadingContent = {
                    Icon(
                        imageVector = Lucide.Code,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(50.dp)
                            .padding(8.dp)
                    )
                },
                headlineContent = { Text("Debansh Sahu") },
                supportingContent = { Text("Developer") },
                trailingContent = {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilledTonalIconButton(onClick = { onOpenUrl(DEVELOPER_TWITTER) }) {
                            Icon(
                                imageVector = Lucide.Twitter,
                                contentDescription = "Twitter",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        FilledTonalIconButton(onClick = { onOpenUrl(DEVELOPER_PROFILE) }) {
                            Icon(
                                imageVector = Lucide.Github,
                                contentDescription = "GitHub",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                },
                colors = transparentListItemColors()
            )
            SectionDivider()
            ListItem(
                modifier = Modifier.clickable { onOpenUrl(PLAY_STORE) },
                leadingContent = {
                    Icon(
                        imageVector = Lucide.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(50.dp)
                            .padding(8.dp)
                    )
                },
                headlineContent = { Text("Rate on Google Play") },
                supportingContent = { Text("Liked the app? Write a review") },
                colors = transparentListItemColors()
            )
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    icon: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(start = 4.dp)
        ) {
            icon()
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
        content()
    }
}

@Composable
private fun SettingsGroup(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer),
        content = content
    )
}

@Composable
private fun SectionLabel(text: String, enabled: Boolean) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .padding(start = 16.dp, top = 16.dp)
            .alpha(if (enabled) 1f else dimmed)
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ChipRow(
    title: String,
    enabled: Boolean,
    options: List<Pair<String, String>>,
    selected: String,
    onSelect: (String) -> Unit,
    icon: (@Composable () -> Unit)? = null,
) {
    Column(modifier = Modifier.padding(top = 4.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(start = 16.dp, bottom = 4.dp)
                .alpha(if (enabled) 1f else dimmed)
        ) {
            icon?.invoke()
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { (value, label) ->
                FilterChip(
                    selected = value == selected,
                    enabled = enabled,
                    onClick = { onSelect(value) },
                    label = { Text(label) }
                )
            }
        }
    }
}

@Composable
private fun Swatch(
    swatch: SeedSwatch,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val argb = swatch.argb
    val fill = if (argb == null) {
        MaterialTheme.colorScheme.surfaceBright
    } else {
        Color(argb.toInt())
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .alpha(if (enabled) 1f else dimmed)
            .clickable(enabled = enabled, onClick = onClick)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    Color.Transparent
                },
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(fill),
            contentAlignment = Alignment.Center
        ) {
            when {
                argb == null -> Text(
                    text = "Aa",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                selected -> Icon(
                    imageVector = Lucide.CircleCheck,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
        Text(
            text = swatch.label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun SectionDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp),
        color = MaterialTheme.colorScheme.outlineVariant
    )
}

@Composable
private fun transparentListItemColors() = ListItemDefaults.colors(
    containerColor = Color.Transparent
)
