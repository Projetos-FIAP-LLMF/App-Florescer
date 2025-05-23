@file:OptIn(ExperimentalMaterial3Api::class)

package com.florescer.ui.screens

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

    var selectedMood by remember { mutableStateOf<Int?>(null) }
    var desabafo by remember { mutableStateOf(TextFieldValue("")) }
    var heartRate by remember { mutableStateOf(TextFieldValue("")) }
    var diaRegistrado by remember { mutableStateOf(false) }
    var mostrarDialog by remember { mutableStateOf(false) }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ✅ LOGO DO FLORESCER
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
                    IconButton(onClick = { selectedMood = index }) {
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
                shape = RoundedCornerShape(16.dp)
            )

            OutlinedTextField(
                value = heartRate,
                onValueChange = { heartRate = it },
                label = { Text("Batimentos cardíacos (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
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
                symptoms.forEach { symptom ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Checkbox(
                            checked = selectedSymptoms.contains(symptom),
                            onCheckedChange = {
                                if (it) selectedSymptoms.add(symptom)
                                else selectedSymptoms.remove(symptom)
                            },
                            colors = CheckboxDefaults.colors(checkedColor = RosaBotao)
                        )
                        Text(text = symptom, color = RosaTexto)
                    }
                }
            }

            Button(
                onClick = {
                    if (selectedMood != null) {
                        val humor = HumorEntry(
                            mood = moodsText[selectedMood!!],
                            symptoms = selectedSymptoms.joinToString(", "),
                            heartRate = heartRate.text,
                            comment = desabafo.text,
                            timestamp = System.currentTimeMillis()
                        )

                        coroutineScope.launch {
                            try {
                                humorRepository.saveHumorLocal(humor)
                                humorRepository.sendHumorToBackend(humor)
                                mostrarDialog = true
                                diaRegistrado = true
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = RosaBotao),
                shape = RoundedCornerShape(30),
                enabled = selectedMood != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar Meu Dia", color = Branco, fontSize = 18.sp)
            }

            if (diaRegistrado) {
                Button(
                    onClick = { navController.navigate("analiseSintomas/${moodsText[selectedMood!!]}") },
                    colors = ButtonDefaults.buttonColors(containerColor = RosaTexto),
                    shape = RoundedCornerShape(30),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ver recomendações", color = Branco, fontSize = 18.sp)
                }
            }

            Button(
                onClick = { navController.navigate("recursos") },
                colors = ButtonDefaults.buttonColors(containerColor = RosaTexto),
                shape = RoundedCornerShape(30),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver Sugestões personalizadas", color = Branco, fontSize = 18.sp)
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
