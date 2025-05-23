package com.florescer.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.florescer.ui.theme.*

@Composable
fun RecursosScreen(navController: NavHostController) {
    val context = LocalContext.current
    val gradient = Brush.verticalGradient(colors = listOf(GradienteTop, GradienteBottom))

    fun abrirLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Recursos de Ajuda Psicológica",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = RosaTexto
            )

            Text(
                text = "Se estiver precisando de apoio, aqui estão caminhos que podem te ajudar a cuidar da sua saúde emocional.",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = RosaTexto,
                textAlign = TextAlign.Center
            )

            Button(
                onClick = { abrirLink("https://www.cvv.org.br/") },
                colors = ButtonDefaults.buttonColors(containerColor = RosaBotao),
                shape = RoundedCornerShape(30),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("CVV - Centro de Valorização da Vida", fontSize = 16.sp, color = Branco)
            }

            Button(
                onClick = { abrirLink("https://www.gov.br/saude/pt-br/composicao/saps/saude-mental") },
                colors = ButtonDefaults.buttonColors(containerColor = RosaBotao),
                shape = RoundedCornerShape(30),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Serviços públicos de saúde mental", fontSize = 16.sp, color = Branco)
            }

            Button(
                onClick = { abrirLink("https://www.mapasaudemental.com.br/") },
                colors = ButtonDefaults.buttonColors(containerColor = RosaBotao),
                shape = RoundedCornerShape(30),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Mapa da Saúde Mental", fontSize = 16.sp, color = Branco)
            }

            Button(
                onClick = { navController.navigate("trilhas/{mood}") },
                colors = ButtonDefaults.buttonColors(containerColor = RosaEscuro),
                shape = RoundedCornerShape(30),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Voltar", color = Branco, fontSize = 18.sp)
            }
        }
    }
}

