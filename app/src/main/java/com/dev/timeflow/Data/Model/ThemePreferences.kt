package com.dev.timeflow.Data.Model


enum class ColorSpecVersion(val code: Int) {
    Spec2021(2021),
    Spec2025(2025),
    ;

    companion object {
        val Default = Spec2021

        fun fromCode(code: Int): ColorSpecVersion =
            entries.firstOrNull { it.code == code } ?: Default
    }
}

const val DEFAULT_PALETTE_STYLE = "TonalSpot"

enum class ThemeType {
    System,
    Light,
    Dark,
}

enum class ContrastLevel {
    Standard,
    Medium,
    High,
}

data class ThemePreferences(
    val themeType: ThemeType = ThemeType.System,
    val isDynamicTheme: Boolean = true,
    val isAmoled : Boolean = false,
    val seedColor: Long? = null,
    val paletteStyle: String = DEFAULT_PALETTE_STYLE,
    val colorSpecVersion: ColorSpecVersion = ColorSpecVersion.Default,
    val contrastLevel: ContrastLevel = ContrastLevel.Standard,
    val bodyFont: BodyFont = BodyFont.SpaceGrotesk,
)
