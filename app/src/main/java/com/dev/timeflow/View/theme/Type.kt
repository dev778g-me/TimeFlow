package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.Font
import com.dev.timeflow.Data.Model.BodyFont
import com.dev.timeflow.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val bodyFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("DM Sans"),
        fontProvider = provider,
    )
)

val displayFontFamily = FontFamily(
    Font(
        googleFont = GoogleFont("DM Sans"),
        fontProvider = provider,
    )
)

// Default Material 3 typography values
val baseline = Typography()



@OptIn(ExperimentalTextApi::class)
val playWriteIrelandFontFamily = FontFamily(
    androidx.compose.ui.text.font.Font(
        R.font.playwrite_ireland,
        FontWeight.Light,
        variationSettings = FontVariation.Settings(FontVariation.weight(300))
    ),
    androidx.compose.ui.text.font.Font(
        R.font.playwrite_ireland,
        FontWeight.Normal,
        variationSettings = FontVariation.Settings(FontVariation.weight(400))
    ),
    androidx.compose.ui.text.font.Font(
        R.font.playwrite_ireland,
        FontWeight.Medium,
        variationSettings = FontVariation.Settings(FontVariation.weight(500))
    ),
    androidx.compose.ui.text.font.Font(
        R.font.playwrite_ireland,
        FontWeight.SemiBold,
        variationSettings = FontVariation.Settings(FontVariation.weight(600))
    ),
    androidx.compose.ui.text.font.Font(
        R.font.playwrite_ireland,
        FontWeight.Bold,
        variationSettings = FontVariation.Settings(FontVariation.weight(700))
    ),
    androidx.compose.ui.text.font.Font(
        R.font.playwrite_ireland,
        FontWeight.ExtraBold,
        variationSettings = FontVariation.Settings(FontVariation.weight(800))
    ),
)

//@OptIn(ExperimentalTextApi::class)
//val dmSans = FontFamily(
//    androidx.compose.ui.text.font.Font(
//        R.font.dm_sans_variable,
//        FontWeight.Light,
//        variationSettings = FontVariation.Settings(FontVariation.weight(300))
//    ),
//    androidx.compose.ui.text.font.Font(
//        R.font.dm_sans_variable,
//        FontWeight.Normal,
//        variationSettings = FontVariation.Settings(FontVariation.weight(400))
//    ),
//    androidx.compose.ui.text.font.Font(
//        R.font.dm_sans_variable,
//        FontWeight.Medium,
//        variationSettings = FontVariation.Settings(FontVariation.weight(500))
//    ),
//    androidx.compose.ui.text.font.Font(
//        R.font.dm_sans_variable,
//        FontWeight.SemiBold,
//        variationSettings = FontVariation.Settings(FontVariation.weight(600))
//    ),
//    androidx.compose.ui.text.font.Font(
//        R.font.,
//        FontWeight.Bold,
//        variationSettings = FontVariation.Settings(FontVariation.weight(700))
//    ),
//    androidx.compose.ui.text.font.Font(
//        R.font.dm_sans_variable,
//        FontWeight.ExtraBold,
//        variationSettings = FontVariation.Settings(FontVariation.weight(800))
//    ),
//)

@OptIn(ExperimentalTextApi::class)
val jetBrainsMono = FontFamily(
    androidx.compose.ui.text.font.Font(
        R.font.jetbrains_mono_variable,
        FontWeight.Light,
        variationSettings = FontVariation.Settings(FontVariation.weight(300))
    ),
    androidx.compose.ui.text.font.Font(
        R.font.jetbrains_mono_variable,
        FontWeight.Normal,
        variationSettings = FontVariation.Settings(FontVariation.weight(400))
    ),
    androidx.compose.ui.text.font.Font(
        R.font.jetbrains_mono_variable,
        FontWeight.Medium,
        variationSettings = FontVariation.Settings(FontVariation.weight(500))
    ),
    androidx.compose.ui.text.font.Font(
        R.font.jetbrains_mono_variable,
        FontWeight.SemiBold,
        variationSettings = FontVariation.Settings(FontVariation.weight(600))
    ),
    androidx.compose.ui.text.font.Font(
        R.font.jetbrains_mono_variable,
        FontWeight.Bold,
        variationSettings = FontVariation.Settings(FontVariation.weight(700))
    ),
    androidx.compose.ui.text.font.Font(
        R.font.jetbrains_mono_variable,
        FontWeight.ExtraBold,
        variationSettings = FontVariation.Settings(FontVariation.weight(800))
    ),
)

@OptIn(ExperimentalTextApi::class)
val spaceGrotesk =  FontFamily(
    androidx.compose.ui.text.font.Font(
        R.font.space_grotesk_variable,
        FontWeight.Light,
        variationSettings = FontVariation.Settings(FontVariation.weight(300))
    ),
    androidx.compose.ui.text.font.Font(
        R.font.space_grotesk_variable,
        FontWeight.Normal,
        variationSettings = FontVariation.Settings(FontVariation.weight(400))
    ),
    androidx.compose.ui.text.font.Font(
        R.font.space_grotesk_variable,
        FontWeight.Medium,
        variationSettings = FontVariation.Settings(FontVariation.weight(500))
    ),
    androidx.compose.ui.text.font.Font(
        R.font.space_grotesk_variable,
        FontWeight.SemiBold,
        variationSettings = FontVariation.Settings(FontVariation.weight(600))
    ),
    androidx.compose.ui.text.font.Font(
        R.font.space_grotesk_variable,
        FontWeight.Bold,
        variationSettings = FontVariation.Settings(FontVariation.weight(700))
    ),
    androidx.compose.ui.text.font.Font(
        R.font.space_grotesk_variable,
        FontWeight.ExtraBold,
        variationSettings = FontVariation.Settings(FontVariation.weight(800))
    ),
)



@OptIn(ExperimentalTextApi::class)
val inter = FontFamily(
    androidx.compose.ui.text.font.Font(
        R.font.inter_variable,
        FontWeight.Light,
        variationSettings = FontVariation.Settings(FontVariation.weight(300))
    ),
    androidx.compose.ui.text.font.Font(
        R.font.inter_variable,
        FontWeight.Normal,
        variationSettings = FontVariation.Settings(FontVariation.weight(400))
    ),
    androidx.compose.ui.text.font.Font(
        R.font.inter_variable,
        FontWeight.Medium,
        variationSettings = FontVariation.Settings(FontVariation.weight(500))
    ),
    androidx.compose.ui.text.font.Font(
        R.font.inter_variable,
        FontWeight.SemiBold,
        variationSettings = FontVariation.Settings(FontVariation.weight(600))
    ),
    androidx.compose.ui.text.font.Font(
        R.font.inter_variable,
        FontWeight.Bold,
        variationSettings = FontVariation.Settings(FontVariation.weight(700))
    ),
    androidx.compose.ui.text.font.Font(
        R.font.inter_variable,
        FontWeight.ExtraBold,
        variationSettings = FontVariation.Settings(FontVariation.weight(800))
    ),
)

@OptIn(ExperimentalTextApi::class)
val montserrat = FontFamily(
    androidx.compose.ui.text.font.Font(
        R.font.montserrat_variable,
        FontWeight.Light,
        variationSettings = FontVariation.Settings(FontVariation.weight(300))
    ),
    androidx.compose.ui.text.font.Font(
        R.font.montserrat_variable,
        FontWeight.Normal,
        variationSettings = FontVariation.Settings(FontVariation.weight(400))
    ),
    androidx.compose.ui.text.font.Font(
        R.font.montserrat_variable,
        FontWeight.Medium,
        variationSettings = FontVariation.Settings(FontVariation.weight(500))
    ),
    androidx.compose.ui.text.font.Font(
        R.font.montserrat_variable,
        FontWeight.SemiBold,
        variationSettings = FontVariation.Settings(FontVariation.weight(600))
    ),
    androidx.compose.ui.text.font.Font(
        R.font.montserrat_variable,
        FontWeight.Bold,
        variationSettings = FontVariation.Settings(FontVariation.weight(700))
    ),
    androidx.compose.ui.text.font.Font(
        R.font.montserrat_variable,
        FontWeight.ExtraBold,
        variationSettings = FontVariation.Settings(FontVariation.weight(800))
    ),
)


@OptIn(ExperimentalTextApi::class)
val instumentSerif = FontFamily(
    androidx.compose.ui.text.font.Font(
        R.font.instrument_serif_regular,
        FontWeight.Light,
        variationSettings = FontVariation.Settings(FontVariation.weight(300))
    ),
    androidx.compose.ui.text.font.Font(
        R.font.instrument_serif_regular,
        FontWeight.Normal,
        variationSettings = FontVariation.Settings(FontVariation.weight(400))
    ),
    androidx.compose.ui.text.font.Font(
        R.font.instrument_serif_regular,
        FontWeight.Medium,
        variationSettings = FontVariation.Settings(FontVariation.weight(500))
    ),
    androidx.compose.ui.text.font.Font(
        R.font.instrument_serif_regular,
        FontWeight.SemiBold,
        variationSettings = FontVariation.Settings(FontVariation.weight(600))
    ),
    androidx.compose.ui.text.font.Font(
        R.font.instrument_serif_regular,
        FontWeight.Bold,
        variationSettings = FontVariation.Settings(FontVariation.weight(700))
    ),
    androidx.compose.ui.text.font.Font(
        R.font.instrument_serif_regular,
        FontWeight.ExtraBold,
        variationSettings = FontVariation.Settings(FontVariation.weight(800))
    ),
)

@OptIn(ExperimentalTextApi::class)
val googleSansFlexRounded  = FontFamily(
    androidx.compose.ui.text.font.Font(
        R.font.google_sans_flex_var,
        FontWeight.Light,
        variationSettings = FontVariation.Settings(FontVariation.weight(300),            FontVariation.Setting("ROND", 100f) ,)
    ),
    androidx.compose.ui.text.font.Font(
        R.font.google_sans_flex_var,
        FontWeight.Normal,
        variationSettings = FontVariation.Settings(FontVariation.weight(400),            FontVariation.Setting("ROND", 100f) )
    ),
    androidx.compose.ui.text.font.Font(
        R.font.google_sans_flex_var,
        FontWeight.Medium,
        variationSettings = FontVariation.Settings(FontVariation.weight(500),            FontVariation.Setting("ROND", 100f) )
    ),
    androidx.compose.ui.text.font.Font(
        R.font.google_sans_flex_var,
        FontWeight.SemiBold,
        variationSettings = FontVariation.Settings(FontVariation.weight(600),            FontVariation.Setting("ROND", 100f) )
    ),
    androidx.compose.ui.text.font.Font(
        R.font.google_sans_flex_var,
        FontWeight.Bold,
        variationSettings = FontVariation.Settings(FontVariation.weight(700),            FontVariation.Setting("ROND", 100f) )
    ),
    androidx.compose.ui.text.font.Font(
        R.font.google_sans_flex_var,
        FontWeight.ExtraBold,
        variationSettings = FontVariation.Settings(FontVariation.weight(800),            FontVariation.Setting("ROND", 100f) )
    ),
)


val defaultFontFamily = FontFamily.Default

fun  provideAppTypography(
    bodyFont: BodyFont,
) : Typography{
    return Typography(
        displayLarge = baseline.displayLarge.copy(fontFamily = bodyFont.fontFamily,),
        displayMedium = baseline.displayMedium.copy(fontFamily = bodyFont.fontFamily),
        displaySmall = baseline.displaySmall.copy(fontFamily = bodyFont.fontFamily),
        headlineLarge = baseline.headlineLarge.copy(fontFamily = bodyFont.fontFamily),
        headlineMedium = baseline.headlineMedium.copy(fontFamily = bodyFont.fontFamily),
        headlineSmall = baseline.headlineSmall.copy(fontFamily = bodyFont.fontFamily),
        titleLarge = baseline.titleLarge.copy(fontFamily = bodyFont.fontFamily),
        titleMedium = baseline.titleMedium.copy(fontFamily = bodyFont.fontFamily),
        titleSmall = baseline.titleSmall.copy(fontFamily = bodyFont.fontFamily),
        bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFont.fontFamily),
        bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFont.fontFamily),
        bodySmall = baseline.bodySmall.copy(fontFamily = bodyFont.fontFamily),
        labelLarge = baseline.labelLarge.copy(fontFamily = bodyFont.fontFamily),
        labelMedium = baseline.labelMedium.copy(fontFamily = bodyFont.fontFamily),
        labelSmall = baseline.labelSmall.copy(fontFamily = bodyFont.fontFamily),
    )
}
val AppTypography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily),
    displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily),
    displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily),
    titleLarge = baseline.titleLarge.copy(fontFamily = displayFontFamily),
    titleMedium = baseline.titleMedium.copy(fontFamily = displayFontFamily),
    titleSmall = baseline.titleSmall.copy(fontFamily = displayFontFamily),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily),
    bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily),
    labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily),
    labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily),
    labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily),
)

