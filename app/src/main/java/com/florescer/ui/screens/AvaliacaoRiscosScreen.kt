package com.florescer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.florescer.ui.theme.*

@Composable
fun AvaliacaoRiscosScreen(navController: NavHostController) {
    val gradient = Brush.verticalGradient(listOf(GradienteTop, GradienteBottom))

    // Perguntas (curtas e claras)
    val perguntas = listOf(
        "Você tem dificuldades para dormir?",
        "Sente que está sobrecarregado(a) com tarefas?",
        "Tem se sentido isolado(a) das pessoas próximas?",
        "Sente que suas emoções estão descontroladas?"
    )

    // Opções padronizadas (0..4)
    val opcoes = listOf("Nunca", "Raramente", "Às vezes", "Frequentemente", "Sempre")

    // Estados
    var respostas by rememberSaveable { mutableStateOf(List(perguntas.size) { 0 }) }
    var mostrarResultado by rememberSaveable { mutableStateOf(false) }

    val soma = remember(respostas) { respostas.sum() }
    val (tituloRisco, textoRisco, corDestaque) = remember(soma) {
        when (soma) {
            in 0..4 -> Triple(
                "Baixo risco",
                "Você parece estar emocionalmente equilibrado(a). Continue cuidando de você! 💚",
                Green
            )

            in 5..8 -> Triple(
                "Risco moderado",
                "Alguns sinais apareceram. Observe sua rotina, desacelere quando possível e se acolha. 💛",
                Yellow
            )

            else -> Triple(
                "Alto risco",
                "Seu bem-estar emocional pode estar comprometido. Procure apoio e converse com alguém de confiança. ❤️‍🩹",
                RosaEscuro
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        /* Header — igual look & feel das outras telas */
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface.copy(
                    alpha = 0.92f
                )
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AssistChip(
                    onClick = { /* no-op */ },
                    label = {
                        Text(
                            "Autoavaliação breve",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        labelColor = RosaTexto,
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "Avaliação de Riscos Psicossociais",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = RosaTexto,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Responda com sinceridade. Isso ajuda você a perceber sinais e acolher o seu momento.",
                    fontSize = 14.sp,
                    color = Preto.copy(alpha = 0.75f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.height(18.dp))

        /* Perguntas */
        perguntas.forEachIndexed { index, pergunta ->
            QuestionCard(
                titulo = "Pergunta ${index + 1}",
                descricao = pergunta,
                opcoes = opcoes,
                selecionado = respostas[index],
                onSelecionar = { nota ->
                    respostas = respostas.toMutableList().also { it[index] = nota }
                    mostrarResultado = false
                }
            )
            Spacer(Modifier.height(10.dp))
        }

        Spacer(Modifier.height(8.dp))

        /* Botões de ação */
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = {
                    respostas = List(perguntas.size) { 0 }
                    mostrarResultado = false
                },
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = RosaBotao)
            ) {
                Text("Limpar", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }

            Button(
                onClick = { mostrarResultado = true },
                colors = ButtonDefaults.buttonColors(containerColor = RosaBotao),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    "Enviar",
                    color = Branco,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        /* Resultado */
        if (mostrarResultado) {
            Spacer(Modifier.height(16.dp))
            ResultCard(
                titulo = tituloRisco,
                texto = textoRisco,
                cor = corDestaque,
                soma = soma,
                max = (perguntas.size * 4)
            )
            Spacer(Modifier.height(12.dp))

            // CTA secundária (recursos de ajuda)
            Button(
                onClick = { navController.navigate("recursos") },
                colors = ButtonDefaults.buttonColors(containerColor = RosaEscuro),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Procurar ajuda",
                    color = Branco,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(Modifier.height(18.dp))

        // Voltar
        Button(
            onClick = { navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(containerColor = RosaEscuro),
            shape = RoundedCornerShape(30),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Voltar", color = Branco, fontSize = 18.sp)
        }
    }
}

/* ================== Componentes ================== */

@Composable
private fun QuestionCard(
    titulo: String,
    descricao: String,
    opcoes: List<String>,
    selecionado: Int,
    onSelecionar: (Int) -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Text(titulo, color = RosaTexto, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Spacer(Modifier.height(6.dp))
            Text(descricao, color = Preto.copy(alpha = 0.85f), fontSize = 14.sp)

            Spacer(Modifier.height(12.dp))

            // Grade de opções (mais confortável em telas pequenas)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OptionChip(
                        text = opcoes[0],
                        selected = selecionado == 0,
                        onClick = { onSelecionar(0) }
                    )
                    OptionChip(
                        text = opcoes[1],
                        selected = selecionado == 1,
                        onClick = { onSelecionar(1) }
                    )
                    OptionChip(
                        text = opcoes[2],
                        selected = selecionado == 2,
                        onClick = { onSelecionar(2) }
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OptionChip(
                        text = opcoes[3],
                        selected = selecionado == 3,
                        onClick = { onSelecionar(3) }
                    )
                    OptionChip(
                        text = opcoes[4],
                        selected = selecionado == 4,
                        onClick = { onSelecionar(4) }
                    )
                }
            }

            Spacer(Modifier.height(4.dp))

            // Dica leve
            Text(
                text = "Escolha a opção que mais parece com sua semana.",
                color = Preto.copy(alpha = 0.6f),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun OptionChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = text,
                fontSize = 13.sp,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
            )
        },
        shape = RoundedCornerShape(20.dp),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = RosaBotao.copy(alpha = 0.18f),
            selectedLabelColor = RosaTexto,
            containerColor = MaterialTheme.colorScheme.surface,
            labelColor = Preto.copy(alpha = 0.85f)
        )
    )
}

@Composable
private fun ResultCard(
    titulo: String,
    texto: String,
    cor: androidx.compose.ui.graphics.Color,
    soma: Int,
    max: Int
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = titulo,
                color = cor,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = texto,
                color = Preto.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                fontSize = 14.sp
            )

            Spacer(Modifier.height(12.dp))

            // Barrinha de progresso para visual
            LinearProgressIndicator(
                progress = { (soma.toFloat() / max.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = cor,
                trackColor = Preto.copy(alpha = 0.08f)
            )

            Spacer(Modifier.height(6.dp))
            Text(
                text = "Pontuação: $soma de $max",
                color = Preto.copy(alpha = 0.7f),
                fontSize = 12.sp
            )
        }
    }
}
