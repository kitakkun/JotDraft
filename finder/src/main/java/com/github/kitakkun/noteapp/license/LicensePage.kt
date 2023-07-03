package com.github.kitakkun.noteapp.license

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.github.kitakkun.noteapp.finder.R
import com.mikepenz.aboutlibraries.ui.compose.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.LibraryDefaults

@Composable
fun LicensePage(
    navController: NavController,
) {
    LibrariesContainer(
        header = {
            item { LicenseTopAppBar(onClickNavUp = navController::navigateUp) }
        },
        modifier = Modifier.fillMaxSize(),
        colors = LibraryDefaults.libraryColors(
            backgroundColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            badgeBackgroundColor = MaterialTheme.colorScheme.primary,
            badgeContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LicenseTopAppBar(
    onClickNavUp: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(text = stringResource(R.string.oss_licenses))
        },
        navigationIcon = {
            IconButton(onClick = onClickNavUp) {
                Icon(
                    imageVector = Icons.Default.NavigateBefore,
                    contentDescription = null
                )
            }
        }
    )
}
