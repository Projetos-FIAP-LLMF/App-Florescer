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
import androidx.compose.runtime.saveable.rememberSaveable
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
    val gradient = Brush.verticalGradient(listOf(GradienteTop, GradienteBottom))

    val moodsImages = listOf(
        R.drawable.emoji_feliz,
        R.drawable.emoji_neutro,
        R.drawable.emoji_triste,
        R.drawable.emoji_bravo,
        R.drawable.emoji_vomito,
        R.drawable.emoji_amor
    )
    val moodsText = listOf("feliz", "neutro", "triste", "bravo", "enjoado", "amoroso")

    // ===== Controle de envio único por dia =====
    var checkingToday by rememberSaveable { mutableStateOf(true) }
    var alreadyRegisteredToday by rememberSaveable { mutableStateOf(false) }
    var todaysMoodForUi by rememberSaveable { mutableStateOf<String?>(null) }
    var submittedThisSession by rememberSaveable { mutableStateOf(false) } // marca envio local
    val hasSubmittedToday = alreadyRegisteredToday || submittedThisSession

    // Checa se já existe registro hoje (backend/local)
    LaunchedEffect(Unit) {
        try {
            val today = LocalDate.now(ZoneId.systemDefault()).toString()
            val evolucao = humorRepository.getHistoricoEvolucao()
            evolucao.firstOrNull { it.data == today }?.let { entry ->
                alreadyRegisteredToday = true
                todaysMoodForUi = mapApiMoodToUi(entry.humor)
            }
        } catch (_: Exception) {
        } finally {
            checkingToday = false
        }
    }

    // ===== Estados de UI do formulário =====
    var selectedMood by remember { mutableStateOf<Int?>(null) }
    var desabafo by remember { mutableStateOf(TextFieldValue("")) }
    var heartRate by remember { mutableStateOf(TextFieldValue("")) }
    var diaRegistrado by remember { mutableStateOf(false) }
    var mostrarDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

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

            hasSubmittedToday -> {
                // ===== Estado acolhedor quando já registrou hoje =====
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
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
                                        "Check-in do dia",
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
                                text = "Você já registrou hoje 💖",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = RosaTexto,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = "Obrigada por se cuidar! Quer ver recomendações para o seu momento?",
                                fontSize = 14.sp,
                                color = Preto.copy(alpha = 0.75f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(Modifier.height(18.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = true),
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 20.dp, vertical = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.titulo2),
                                contentDescription = "Florescer",
                                modifier = Modifier
                                    .height(90.dp)
                                    .fillMaxWidth(),
                                contentScale = ContentScale.Fit
                            )

                            Spacer(Modifier.height(10.dp))

                            Text(
                                "Humor registrado hoje:",
                                fontSize = 14.sp,
                                color = Preto.copy(alpha = 0.7f)
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = (todaysMoodForUi
                                    ?: "neutro").replaceFirstChar { it.uppercase() },
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = RosaTexto
                            )

                            Spacer(Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    val moodParam = todaysMoodForUi ?: "neutro"
                                    navController.navigate("analiseSintomas/$moodParam")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = RosaBotao),
                                shape = RoundedCornerShape(28.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    "Ver recomendações",
                                    color = Branco,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(Modifier.height(10.dp))

                            OutlinedButton(
                                onClick = { navController.navigate("avaliacao") },
                                shape = RoundedCornerShape(28.dp),
                                modifier = Modifier.fillMaxWidth(),
                                border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = RosaBotao)
                            ) {
                                Text(
                                    "Realizar avaliação",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(6.dp))

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

            else -> {
                // ===== Fluxo de registro =====
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
                            .fillMaxWidth(),
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
                        horizontalArrangement = Arrangement.SpaceEvenly,
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
                        val symptoms = listOf(
                            "Cansaço",
                            "Fadiga",
                            "Dor de cabeça",
                            "Dores musculares",
                            "Insônia"
                        )
                        symptoms.forEach { symptom ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Mantendo sua lógica local de estado por item
                                val selectedSymptoms = remember { mutableStateListOf<String>() }
                                Checkbox(
                                    checked = selectedSymptoms.contains(symptom),
                                    onCheckedChange = {
                                        if (it) selectedSymptoms.add(symptom) else selectedSymptoms.remove(
                                            symptom
                                        )
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

                    // ===== Botão de registrar com orquestração anti-duplo envio =====
                    Button(
                        onClick = {
                            // Guarda extra: não deixa clicar se já enviou hoje ou se está carregando
                            if (hasSubmittedToday || isLoading) return@Button

                            selectedMood?.let { moodIndex ->
                                isLoading = true
                                errorMessage = null

                                val humor = HumorEntry(
                                    mood = moodsText[moodIndex],
                                    symptoms = "", // mantido
                                    heartRate = heartRate.text,
                                    comment = desabafo.text,
                                    timestamp = System.currentTimeMillis()
                                )

                                coroutineScope.launch {
                                    try {
                                        // Checagem imediata antes de salvar/enviar
                                        val today = LocalDate.now(ZoneId.systemDefault()).toString()
                                        val jaTemHoje = runCatching {
                                            humorRepository.getHistoricoEvolucao()
                                                .any { it.data == today }
                                        }.getOrDefault(false)

                                        if (jaTemHoje) {
                                            // Já há registro hoje -> só marca flags e mostra UI de "já registrou"
                                            alreadyRegisteredToday = true
                                            submittedThisSession = true
                                            mostrarDialog = false
                                            errorMessage = null
                                        } else {
                                            // Envia de verdade
                                            humorRepository.saveHumorLocal(humor)
                                            runCatching { humorRepository.sendHumorToBackend(humor) }

                                            // Marca como enviado para bloquear novo submit
                                            submittedThisSession = true
                                            alreadyRegisteredToday = true
                                            mostrarDialog = true
                                            diaRegistrado = true
                                        }
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
                        enabled = selectedMood != null && !isLoading && !hasSubmittedToday,
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
                            text = if (isLoading) "Registrando..." else "Registrar meu dia",
                            color = Branco,
                            fontSize = 18.sp
                        )
                    }

                    if (diaRegistrado) {
                        Button(
                            onClick = {
                                selectedMood?.let { idx ->
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
        }
    }

    if (mostrarDialog) {
        AlertDialog(
            onDismissRequest = { mostrarDialog = false },
            confirmButton = {
                TextButton(onClick = { mostrarDialog = false }) { Text("Ok", color = RosaTexto) }
            },
            title = { Text("Parabéns!", fontWeight = FontWeight.Bold, color = RosaTexto) },
            text = { Text("Você registrou seu dia. Continue se cuidando!") }
        )
    }
}

/* ==== helpers ==== */

private fun mapApiMoodToUi(apiMood: String): String = when (apiMood.uppercase()) {
    "FELIZ" -> "feliz"
    "NEUTRO" -> "neutro"
    "TRISTE" -> "triste"
    "BRAVO" -> "bravo"
    "ENJOADO" -> "enjoado"
    "AMOROSO" -> "amoroso"
    else -> apiMood.lowercase()
}
