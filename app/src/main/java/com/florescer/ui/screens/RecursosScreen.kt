package com.florescer.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.florescer.R
import com.florescer.data.HumorRepository
import com.florescer.ui.theme.*

@Composable
fun RecursosScreen(
    navController: NavHostController,
    humorRepository: HumorRepository
) {
    val context = LocalContext.current
    val gradient = Brush.verticalGradient(listOf(GradienteTop, GradienteBottom))

    fun abrirLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // HEADER
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.92f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AssistChip(
                    onClick = { },
                    label = {
                        Text(
                            "Pontos de apoio",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        labelColor = RosaTexto,
                        containerColor = Color.White
                    )
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "Recursos de Apoio 💛",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = RosaTexto,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Canais e serviços confiáveis para quando você precisar de acolhimento.",
                    fontSize = 14.sp,
                    color = Preto.copy(alpha = 0.75f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.titulo3),
            contentDescription = "Florescer",
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Fit
        )

        Spacer(Modifier.height(20.dp))

        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            ResourceCard(
                title = "CVV - Centro de Valorização da Vida",
                description = "Fale com alguém que pode te ouvir. Canal anônimo e gratuito.",
                onClick = { abrirLink("https://www.cvv.org.br/") }
            )

            ResourceCard(
                title = "Serviços públicos de saúde mental",
                description = "Encontre apoio profissional próximo de você.",
                onClick = { abrirLink("https://www.gov.br/saude/pt-br/composicao/saps/saude-mental") }
            )

            ResourceCard(
                title = "Mapa da Saúde Mental",
                description = "Encontre psicólogos e serviços de saúde mental em sua região.",
                onClick = { abrirLink("https://www.mapasaudemental.com.br/") }
            )

            ResourceCard(
                title = "Orientações de autocuidado",
                description = "Dicas de cuidado: durma bem, alimente-se com equilíbrio e crie momentos de pausa.",
                containerColor = RosaBotao.copy(alpha = 0.9f),
                textColor = Branco,
                onClick = { navController.navigate("orientacoes") }
            )
        }

        Spacer(Modifier.height(20.dp))

        // AVISO IMPORTANTE
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF4F4)),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text("⚠️ Alerta importante", fontWeight = FontWeight.Bold, color = RosaTexto)
                Text(
                    "Se estiver em emergência emocional, procure ajuda imediata: ligue 188 ou vá ao pronto atendimento.",
                    fontSize = 14.sp,
                    color = Preto.copy(alpha = 0.85f)
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // BOTÕES DE NAVEGAÇÃO
        Button(
            onClick = { navController.navigate("evolucao") },
            colors = ButtonDefaults.buttonColors(containerColor = RosaTexto),
            shape = RoundedCornerShape(30),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver Seus Dados", color = Branco, fontSize = 18.sp)
        }

        Spacer(Modifier.height(10.dp))

        Button(
            onClick = { navController.navigate("trilhas/neutro") },
            colors = ButtonDefaults.buttonColors(containerColor = RosaEscuro),
            shape = RoundedCornerShape(30),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Voltar", color = Branco, fontSize = 18.sp)
        }
    }
}

/* ---- COMPONENTE CARD REUTILIZÁVEL ---- */
@Composable
fun ResourceCard(
    title: String,
    description: String,
    containerColor: Color = Color.White.copy(alpha = 0.9f),
    textColor: Color = RosaTexto,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = textColor)
            Spacer(Modifier.height(6.dp))
            Text(description, fontSize = 14.sp, color = if (textColor == Branco) Branco else Preto)
        }
    }
}
