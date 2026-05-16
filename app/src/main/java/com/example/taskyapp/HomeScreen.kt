package com.example.taskyapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onNavigateToSolicitarTarea: () -> Unit,
    onNavigateToRealizarTarea: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Tasky Home", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onNavigateToSolicitarTarea, modifier = Modifier.fillMaxWidth()) {
            Text("Solicitar Tarea")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onNavigateToRealizarTarea, modifier = Modifier.fillMaxWidth()) {
            Text("Realizar Tarea")
        }
    }
}
