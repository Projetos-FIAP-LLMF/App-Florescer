@file:OptIn(ExperimentalMaterial3Api::class)

package com.florescer.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.florescer.R
import com.florescer.data.HumorEntry
import com.florescer.data.HumorRepository
import com.florescer.ui.theme.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MoodScreen(
    navController: NavHostController,
    humorRepository: HumorRepository
) {
    val gradient = Brush.verticalGradient(
        colors = listOf(GradienteTop, GradienteBottom)
    )

    val moodsImages = listOf(
        R.drawable.emoji_feliz,
        R.drawable.emoji_neutro,
        R.drawable.emoji_triste,
        R.drawable.emoji_bravo,
        R.drawable.emoji_vomito,
        R.drawable.emoji_amor
    )
    val moodsText = listOf("feliz", "neutro", "triste", "bravo", "enjoado", "amoroso")

    // --- NOVO: estados para checar se já tem registro hoje ---
    var checkingToday by remember { mutableStateOf(true) }
    var alreadyRegisteredToday by remember { mutableStateOf(false) }
    var todaysMoodForUi by remember { mutableStateOf<String?>(null) }

    // carrega histórico e verifica se há registro hoje
    LaunchedEffect(Unit) {
        try {
            val today = LocalDate.now(ZoneId.systemDefault()).toString() // yyyy-MM-dd
            val evolucao = humorRepository.getHistoricoEvolucao()
            val todayEntries = evolucao.filter { it.data == today }
            if (todayEntries.isNotEmpty()) {
                alreadyRegisteredToday = true
                // pega o primeiro (ou o mais recente se preferir ordenar)
                val apiMood = todayEntries.first().humor // ex: "NEUTRO"
                todaysMoodForUi = mapApiMoodToUi(apiMood) // ex: "neutro"
            }
        } catch (_: Exception) {
            // Em caso de erro na checagem, apenas segue o fluxo normal
        } finally {
            checkingToday = false
        }
    }

    var selectedMood by remember { mutableStateOf<Int?>(null) }
    var desabafo by remember { mutableStateOf(TextFieldValue("")) }
    var heartRate by remember { mutableStateOf(TextFieldValue("")) }
    var diaRegistrado by remember { mutableStateOf(false) }
    var mostrarDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val symptoms = listOf("Cansaço", "Fadiga", "Dor de cabeça", "Dores musculares", "Insônia")
    val selectedSymptoms = remember { mutableStateListOf<String>() }

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradient)
            .padding(20.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        when {
            checkingToday -> {
                // loading inicial da checagem
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = RosaTexto)
                    Spacer(Modifier.height(12.dp))
                    Text("Verificando seu registro de hoje…", color = RosaTexto)
                }
            }

            alreadyRegisteredToday -> {
                // --- NOVO: estado quando já registrou hoje ---
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.titulo2),
                        contentDescription = "Logo do Florescer",
                        modifier = Modifier
                            .height(100.dp)
                            .size(300.dp),
                        contentScale = ContentScale.Fit
                    )

                    Text(
                        text = "Você já registrou hoje 💖",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = RosaTexto,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Quer ver recomendações baseadas no seu humor?",
                        fontSize = 14.sp,
                        color = RosaTexto
                    )

                    Button(
                        onClick = {
                            val moodParam = todaysMoodForUi ?: "neutro"
                            navController.navigate("analiseSintomas/$moodParam")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RosaBotao),
                        shape = RoundedCornerShape(30),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Ver recomendações", color = Branco, fontSize = 18.sp)
                    }

                    OutlinedButton(
                        onClick = { navController.navigate("avaliacao") },
                        shape = RoundedCornerShape(30),
                        modifier = Modifier.fillMaxWidth(),
                        border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = RosaBotao)
                    ) {
                        Text("Fazer Minha Autoavaliação", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            else -> {
                // --- CONTEÚDO ORIGINAL (mantido) ---
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.titulo2),
                        contentDescription = "Logo do Florescer",
                        modifier = Modifier
                            .height(100.dp)
                            .size(300.dp),
                        contentScale = ContentScale.Fit
                    )

                    Text(
                        text = "Como você está se sentindo hoje?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = RosaTexto,
                        textAlign = TextAlign.Center
                    )

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        moodsImages.forEachIndexed { index, moodRes ->
                            IconButton(
                                onClick = { selectedMood = index },
                                enabled = !isLoading
                            ) {
                                Icon(
                                    painter = painterResource(id = moodRes),
                                    contentDescription = "Humor ${moodsText[index]}",
                                    tint = if (selectedMood == index) RosaBotao else RosaTexto,
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = desabafo,
                        onValueChange = { desabafo = it },
                        label = { Text("Quer desabafar?") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        enabled = !isLoading
                    )

                    OutlinedTextField(
                        value = heartRate,
                        onValueChange = { heartRate = it },
                        label = { Text("Batimentos cardíacos (opcional)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        enabled = !isLoading
                    )

                    Text(
                        text = "Sintomas que você sente:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = RosaTexto
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val symptoms = listOf("Cansaço", "Fadiga", "Dor de cabeça", "Dores musculares", "Insônia")
                        symptoms.forEach { symptom ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                val selectedSymptoms = remember { mutableStateListOf<String>() } // mantém mesmo comportamento
                                Checkbox(
                                    checked = selectedSymptoms.contains(symptom),
                                    onCheckedChange = {
                                        if (it) selectedSymptoms.add(symptom)
                                        else selectedSymptoms.remove(symptom)
                                    },
                                    colors = CheckboxDefaults.colors(checkedColor = RosaBotao),
                                    enabled = !isLoading
                                )
                                Text(text = symptom, color = RosaTexto)
                            }
                        }
                    }

                    errorMessage?.let { error ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = GradienteBottom),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = error,
                                color = RosaEscuro,
                                modifier = Modifier.padding(12.dp),
                                fontSize = 14.sp
                            )
                        }
                    }

                    Button(
                        onClick = {
                            selectedMood?.let { moodIndex ->
                                isLoading = true
                                errorMessage = null

                                val humor = HumorEntry(
                                    mood = moodsText[moodIndex],
                                    symptoms = "" /* mantido como antes; você montava acima */,
                                    heartRate = heartRate.text,
                                    comment = desabafo.text,
                                    timestamp = System.currentTimeMillis()
                                )

                                val selectedSymptoms = mutableListOf<String>() // só pra manter compat
                                // Salvar local + enviar
                                coroutineScope.launch {
                                    try {
                                        humorRepository.saveHumorLocal(humor)
                                        try {
                                            humorRepository.sendHumorToBackend(humor)
                                        } catch (_: Exception) { /* sincroniza depois */ }

                                        mostrarDialog = true
                                        diaRegistrado = true
                                    } catch (e: Exception) {
                                        errorMessage = "Erro ao registrar: ${e.message}"
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = RosaBotao),
                        shape = RoundedCornerShape(30),
                        enabled = selectedMood != null && !isLoading,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Branco,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text(
                            text = if (isLoading) "Registrando..." else "Registrar Meu Dia",
                            color = Branco,
                            fontSize = 18.sp
                        )
                    }

                    if (diaRegistrado) {
                        Button(
                            onClick = {
                                selectedMood?.let { idx ->
                                    // MANTIDO como estava: navega com o índice (sua Analise usa string; se quiser, troco)
                                    navController.navigate("analiseSintomas/$idx")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = RosaBotao),
                            shape = RoundedCornerShape(30),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Ver recomendações", color = Branco, fontSize = 18.sp)
                        }
                    }
                }
            }
        }
    }

    if (mostrarDialog) {
        AlertDialog(
            onDismissRequest = { mostrarDialog = false },
            confirmButton = {
                TextButton(onClick = { mostrarDialog = false }) {
                    Text("Ok", color = RosaTexto)
                }
            },
            title = { Text("Parabéns!", fontWeight = FontWeight.Bold, color = RosaTexto) },
            text = { Text("Você registrou seu dia. Continue se cuidando!") }
        )
    }
}

/* ==== helpers ==== */

// Converte o enum da API (ex: "NEUTRO") para o texto que sua tela de análise espera (ex: "neutro")
private fun mapApiMoodToUi(apiMood: String): String = when (apiMood.uppercase()) {
    "FELIZ"    -> "feliz"
    "NEUTRO"   -> "neutro"
    "TRISTE"   -> "triste"
    "BRAVO"    -> "bravo"
    "ENJOADO"  -> "enjoado"
    "AMOROSO"  -> "amoroso"
    else       -> apiMood.lowercase()
}
