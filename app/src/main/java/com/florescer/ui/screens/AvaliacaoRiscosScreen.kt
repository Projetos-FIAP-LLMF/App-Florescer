package com.florescer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.florescer.ui.theme.*

@Composable
fun AvaliacaoRiscosScreen(navController: NavHostController) {
    val gradient = Brush.verticalGradient(colors = listOf(GradienteTop, GradienteBottom))

    val perguntas = listOf(
        "Você tem dificuldades para dormir?",
        "Sente que está sobrecarregado com tarefas?",
        "Tem se sentido isolado das pessoas próximas?",
        "Sente que suas emoções estão descontroladas?"
    )

    val respostas = remember { mutableStateListOf<Int>() }
    if (respostas.size != perguntas.size) {
        respostas.clear()
        respostas.addAll(List(perguntas.size) { 0 })
    }

    var mostrarResultado by remember { mutableStateOf(false) }

    val resultadoTexto = when (respostas.sum()) {
        in 0..4 -> "Baixo risco: Você parece estar emocionalmente equilibrado."
        in 5..8 -> "Risco moderado: Preste atenção ao seu bem-estar."
        else -> "Alto risco: Seu bem-estar emocional pode estar comprometido. Busque apoio."
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Avaliação de Riscos Psicossociais",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = RosaTexto,
                textAlign = TextAlign.Center
            )

            perguntas.forEachIndexed { index, pergunta ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = pergunta,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = RosaTexto,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        listOf("Nunca", "Raramente", "Às vezes", "Freq.", "Sempre").forEachIndexed { nota, texto ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                RadioButton(
                                    selected = respostas[index] == nota,
                                    onClick = {
                                        respostas[index] = nota
                                        mostrarResultado = false
                                    },  colors = RadioButtonDefaults.colors(
                                        selectedColor = RosaEscuro
                                    )
                                )
                                Text(text = texto, fontSize = 13.sp, color = RosaTexto)
                            }
                        }
                    }
                }
            }

            Button(
                onClick = { mostrarResultado = true },
                colors = ButtonDefaults.buttonColors(containerColor = RosaBotao),
                shape = RoundedCornerShape(30),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver minha avaliação", color = Branco, fontSize = 18.sp)
            }

            if (mostrarResultado) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Branco, shape = RoundedCornerShape(20.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = resultadoTexto,
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        color = RosaTexto,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Button(
                onClick = { navController.navigate("recursos") },
                colors = ButtonDefaults.buttonColors(containerColor = RosaBotao),
                shape = RoundedCornerShape(30),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Procurar Ajuda", color = Branco, fontSize = 18.sp)
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



