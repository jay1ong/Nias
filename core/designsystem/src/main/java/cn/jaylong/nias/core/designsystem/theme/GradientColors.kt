package cn.jaylong.nias.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/10
 */
@Immutable
data class GradientColors(
    val top: Color = Color.Unspecified,
    val bottom: Color = Color.Unspecified,
    val container: Color = Color.Unspecified,
)

val LocalGradientColors = staticCompositionLocalOf { GradientColors() }