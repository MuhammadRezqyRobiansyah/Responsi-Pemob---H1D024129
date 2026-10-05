package com.film.robiansyah.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.film.robiansyah.ui.theme.NeoBorder
import com.film.robiansyah.ui.theme.NeoLime
import com.film.robiansyah.ui.theme.NeoSurface
import com.film.robiansyah.ui.theme.NeoTextWhite

@Composable
fun CineSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = "Cari judul film atau serial TV...",
                color = Color.Gray
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = NeoLime // Aksen kuning-hijau elektrik khas Neo-Brutalism
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = {
                    onClear()
                    focusManager.clearFocus()
                }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Hapus Pencarian",
                        tint = Color.White
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(8.dp), // Sudut tegas khas Neo-Brutalism
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch()
                focusManager.clearFocus()
            }
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = NeoLime,
            unfocusedBorderColor = NeoBorder,
            focusedContainerColor = NeoSurface,
            unfocusedContainerColor = NeoSurface,
            focusedTextColor = NeoTextWhite,
            unfocusedTextColor = NeoTextWhite
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(2.dp, NeoBorder, RoundedCornerShape(8.dp)) // Border tegas 2.dp
    )
}
