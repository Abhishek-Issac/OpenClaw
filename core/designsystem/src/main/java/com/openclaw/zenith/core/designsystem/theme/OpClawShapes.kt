package com.openclaw.zenith.core.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * OxygenOS leans on large corner radii for cards and pills. The shape tokens
 * below default to soft, generous curves (20-28 dp on cards) and a fully
 * rounded pill for chips and buttons.
 */
val OpClawShapes: Shapes =
    Shapes(
        extraSmall = RoundedCornerShape(8.dp),
        small = RoundedCornerShape(12.dp),
        medium = RoundedCornerShape(20.dp),
        large = RoundedCornerShape(28.dp),
        extraLarge = RoundedCornerShape(36.dp),
    )

val PillShape: RoundedCornerShape = RoundedCornerShape(percent = 50)
