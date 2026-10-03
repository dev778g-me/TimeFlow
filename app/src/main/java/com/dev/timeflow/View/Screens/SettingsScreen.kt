package com.dev.timeflow.View.Screens

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonColors
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.icons.lucide.ChevronRight
import com.composables.icons.lucide.CircleCheck
import com.composables.icons.lucide.Code
import com.composables.icons.lucide.Contrast
import com.composables.icons.lucide.Dock
import com.composables.icons.lucide.Github
import com.composables.icons.lucide.Info
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Palette
import com.composables.icons.lucide.Paintbrush
import com.composables.icons.lucide.Settings
import com.composables.icons.lucide.SlidersHorizontal
import com.composables.icons.lucide.Star
import com.composables.icons.lucide.Sun
import com.composables.icons.lucide.Twitter
import com.composables.icons.lucide.Type
import com.composables.icons.lucide.User
import com.dev.timeflow.BuildConfig
import com.dev.timeflow.Data.Model.BodyFont
import com.dev.timeflow.Data.Model.ColorSpecVersion
import com.dev.timeflow.Data.Model.ContrastLevel
import com.dev.timeflow.Data.Model.ThemePreferences
import com.dev.timeflow.Data.Model.ThemeType
import com.dev.timeflow.R
import com.dev.timeflow.Viewmodel.TaskAndEventViewModel
import com.dev.timeflow.Viewmodel.ThemeViewModel
import com.materialkolor.PaletteStyle

private const val PRIVACY_POLICY = "https://timeflow.framer.website/privacypolicy"
private const val GITHUB_REPO = "https://github.com/dev778g-me/TimeFlow"
private const val DEVELOPER_PROFILE = "https://github.com/dev778g-me/"
private const val DEVELOPER_TWITTER = "https://x.com/Dev778g"
private const val PLAY_STORE =
    "https://play.google.com/store/apps/details?id=com.dev.timeflow"

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val themeViewModel: ThemeViewModel = hiltViewModel()
    val preferences by themeViewModel.themePreferences.collectAsState()
    val taskAndEventViewModel: TaskAndEventViewModel = hiltViewModel()
    val name by taskAndEventViewModel.readName().collectAsStateWithLifecycle("")
    var showNameChange by rememberSaveable { mutableStateOf(false) }
    var userName by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    fun openUrl(url: String) {
        context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
    }

    if (showNameChange) {
        NameChangeSheet(
            savedName = name,
            userName = userName,
            onUserNameChange = { userName = it },
            onDismiss = {
                userName = ""
                showNameChange = false
            },
            onSave = { newName ->
                taskAndEventViewModel.saveName(name = newName)
                userName = ""
                showNameChange = false
            }
        )
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
                .padding(bottom = 24.dp)
        ) {
            GeneralSection(
                name = name,
                onChangeName = { showNameChange = true }
            )
            AppearanceSection(preferences, themeViewModel)
            AnimatedVisibility(
                visible = !preferences.isDynamicTheme,
                enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
                exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()
            ) {
                ColorsSection(preferences, themeViewModel)
            }
            FontSection(preferences, themeViewModel)
            AboutSection(::openUrl)
        }
    }
}

@Composable
private fun GeneralSection(
    name: String,
    onChangeName: () -> Unit,
) {
    SettingsSection(title = "General", icon = { Icon(Lucide.Settings, null) }) {
        SettingsGroup {
            ListItem(
                modifier = Modifier.clickable(onClick = onChangeName),
                leadingContent = {
                    Icon(
                        imageVector = Lucide.User,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(50.dp)
                            .padding(8.dp)
                    )
                },
                headlineContent = { Text("Change name") },
                supportingContent = {
                    Text(if (name.isEmpty()) "Not set yet" else name)
                },
                trailingContent = {
                    Icon(
                        imageVector = Lucide.ChevronRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                colors = transparentListItemColors()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NameChangeSheet(
    savedName: String,
    userName: String,
    onUserNameChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()

                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                value = userName,
                onValueChange = onUserNameChange,
                placeholder = { Text("What should we call you?") },
                label = { Text("Your name") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                enabled = userName.isNotEmpty(),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                onClick = { onSave(userName) }
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = if (savedName.isEmpty()) "Save" else "Update"
                )
            }
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
            ListItem(
                headlineContent = { Text("AMOLED Mode") },
                supportingContent = {
                    Text("Use pure black backgrounds for a darker, battery-friendly experience.")
                },

                trailingContent = {
                    Switch(
                        checked = preferences.isAmoled,
                        onCheckedChange = { themeViewModel.setAmoledMode(it) }
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
    SettingsSection(title = "Colors", icon = { Icon(Lucide.Palette, null) }) {
        SettingsGroup {
            SectionLabel("Seed color")
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
                        onClick = { themeViewModel.setSeedColor(swatch.argb) }
                    )
                }
            }

            ChipRow(
                title = "Palette style",
                icon = { Icon(Lucide.Paintbrush, contentDescription = null) },
                options = PaletteStyle.entries.map { it.name to it.name },
                selected = preferences.paletteStyle,
                onSelect = { themeViewModel.setPaletteStyle(it) }
            )

            ChipRow(
                title = "Color spec",
                icon = { Icon(Lucide.SlidersHorizontal, contentDescription = null) },
                options = ColorSpecVersion.entries.map { it.name to it.code.toString() },
                selected = preferences.colorSpecVersion.name,
                onSelect = { name ->
                    themeViewModel.setColorSpecVersion(
                        ColorSpecVersion.entries.first { it.name == name }
                    )
                }
            )

            ChipRow(
                title = "Contrast",
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FontSection(
    preferences: ThemePreferences,
    themeViewModel: ThemeViewModel,
) {
    SettingsSection(title = "Font", icon = { Icon(Lucide.Type, null) }) {
        SettingsGroup {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                BodyFont.entries.forEach { font ->
                    ToggleButton (
                        colors = ToggleButtonDefaults.toggleButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        checked = font == preferences.bodyFont,
                        onCheckedChange = { themeViewModel.setBodyFont(font) },
                        content = {
                            Text(
                                text = font.label,
                                fontFamily = font.fontFamily,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    )
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
            SectionDivider()
            ListItem(
                modifier = Modifier.clickable { onOpenUrl(PRIVACY_POLICY) },
                leadingContent = {
                    Icon(
                        imageVector = Lucide.Dock,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(50.dp)
                            .padding(8.dp)
                    )
                },
                headlineContent = { Text("Privacy Policy") },
                supportingContent = { Text("Read how Timeflow handles your data") },
                trailingContent = {
                    Icon(
                        imageVector = Lucide.ChevronRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
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
    Column(
        // Section spacing lives here rather than on the parent so it collapses
        // along with the Colors section when it animates out.
        modifier = Modifier.padding(top = 20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
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
private fun SectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp)
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ChipRow(
    title: String,
    icon: @Composable () -> Unit,
    options: List<Pair<String, String>>,
    selected: String,
    onSelect: (String) -> Unit,
) {
    Column(modifier = Modifier.padding(top = 4.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
        ) {
            icon()
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
            .clickable(onClick = onClick)
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
