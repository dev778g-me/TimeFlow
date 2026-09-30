package com.dev.timeflow.Data.Model

import androidx.compose.ui.text.font.FontFamily
import com.example.ui.theme.defaultFontFamily
import com.example.ui.theme.googleSansFlexRounded
import com.example.ui.theme.instumentSerif
import com.example.ui.theme.inter
import com.example.ui.theme.jetBrainsMono
import com.example.ui.theme.montserrat
import com.example.ui.theme.spaceGrotesk

enum class BodyFont(
    val label: String,
    val fontFamily: FontFamily
) {
    Default("System default", defaultFontFamily),
    GoogleSansRounded("Google Sans Rounded", googleSansFlexRounded),
    Inter("Inter", inter),
    InstrumentSerif("Instrument Serif", instumentSerif),
    JetbrainsMono("JetBrains Mono", jetBrainsMono),
    Montserrat("Montserrat", montserrat),
    SpaceGrotesk("Space Grotesk", spaceGrotesk),
}
