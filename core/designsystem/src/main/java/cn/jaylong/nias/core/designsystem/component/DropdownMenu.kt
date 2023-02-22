package cn.jaylong.nias.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cn.jaylong.nias.core.designsystem.icon.NiaIcons

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/18
 */
@Composable
fun <T> NiaDropdownMenuButton(
    items: List<T>,
    onItemClick: (item: T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    dismissOnItemClick: Boolean = true,
    text: @Composable () -> Unit,
    itemText: @Composable (item: T) -> Unit,
    itemLeadingIcon: @Composable (item: T) -> Unit,
    itemTrailingIcon: @Composable (item: T) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        OutlinedButton(
            onClick = { expanded = true },
            enabled = enabled,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.onBackground,
            ),
            border = BorderStroke(
                width = NiaDropdownMenuDefaults.DropdownMenuButtonBorderWidth,
                color = if (enabled) {
                    MaterialTheme.colorScheme.outline
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(
                        alpha = NiaDropdownMenuDefaults.DisableDropdownMenuButtonBorderAlpha,
                    )
                },
            ),
        ) {
            NiaDropdownMenuButtonContent(
                text = text,
                trailingIcon = {
                    Icon(
                        imageVector = if (expanded) {
                            NiaIcons.ArrowDropUp
                        } else {
                            NiaIcons.ArrowDropDown
                        },
                        contentDescription = null,
                    )
                }
            )
        }
        NiaDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            items = items,
            onItemClick = onItemClick,
            dismissOnItemClick = dismissOnItemClick,
            itemText = itemText,
            itemLeadingIcon = itemLeadingIcon,
            itemTrailingIcon = itemTrailingIcon,
        )
    }
}

@Composable
private fun NiaDropdownMenuButtonContent(
    text: @Composable () -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    Box(
        Modifier
            .padding(
                end = if (trailingIcon != null) {
                    ButtonDefaults.IconSpacing
                } else {
                    0.dp
                },
            )
    ) {
        ProvideTextStyle(value = MaterialTheme.typography.labelSmall) {
            text()
        }
    }
    if (trailingIcon != null) {
        Box(modifier = Modifier.sizeIn(maxHeight = ButtonDefaults.IconSize)) {
            trailingIcon()
        }
    }
}

@Composable
fun <T> NiaDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    items: List<T>,
    onItemClick: (item: T) -> Unit,
    dismissOnItemClick: Boolean = true,
    itemText: @Composable (item: T) -> Unit,
    itemLeadingIcon: @Composable (item: T) -> Unit,
    itemTrailingIcon: @Composable (item: T) -> Unit,
) {
    DropdownMenu(expanded = expanded, onDismissRequest = onDismissRequest) {
        items.forEach { item ->
            DropdownMenuItem(
                text = { itemText(item) },
                onClick = {
                    onItemClick(item)
                    if (dismissOnItemClick) onDismissRequest()
                },
                leadingIcon = if (itemLeadingIcon != null) {
                    { itemLeadingIcon(item) }
                } else {
                    null
                },
                trailingIcon = if (itemTrailingIcon != null) {
                    { itemTrailingIcon(item) }
                } else {
                    null
                },
            )
        }
    }

}

object NiaDropdownMenuDefaults {
    const val DisableDropdownMenuButtonBorderAlpha = 0.12f

    val DropdownMenuButtonBorderWidth = 1.dp

    val DropdownMenuButtonContentPadding =
        PaddingValues(
            start = 24.dp,
            top = 8.dp,
            end = 16.dp,
            bottom = 8.dp,
        )
}