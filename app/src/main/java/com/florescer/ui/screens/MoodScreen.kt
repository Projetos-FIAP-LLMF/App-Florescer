@file:OptIn(ExperimentalMaterial3Api::class)

package com.florescer.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.florescer.R
import com.florescer.ui.theme.*

@Composable
fun MoodScreen(navController: NavHostController) {
    val gradient = Brush.verticalGradient(
        colors = listOf(GradienteTop, GradienteBottom)
    )

    var selectedMood by remember { mutableStateOf<String?>(null) }
    var comment by remember { mutableStateOf(TextFieldValue("")) }
    var desabafo by remember { mutableStateOf(TextFieldValue("")) }
    var heartRate by remember { mutableStateOf(TextFieldValue("")) }

    var diaRegistrado by remember { mutableStateOf(false) }
    var mostrarDialog by remember { mutableStateOf(false) }

    val moods = listOf("😄", "😢", "😡", "😰", "😐", "🥰")
    val symptoms = listOf("Cansaço", "Fadiga", "Dor de cabeça", "Dores musculares", "Insônia")
    val selectedSymptoms = remember { mutableStateListOf<String>() }

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
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = RosaTexto
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                moods.forEach { mood ->
                    Text(
                        text = mood,
                        fontSize = 32.sp,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(
                                if (selectedMood == mood) Color(0xFFFFC1E3) else Color.Transparent
                            )
                            .padding(8.dp)
                            .clickable { selectedMood = mood }
                    )
                }
            }

            Text(
                text = "Sintomas físicos:",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = RosaTexto,
                modifier = Modifier.fillMaxWidth()
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                symptoms.forEach { symptom ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (selectedSymptoms.contains(symptom)) {
                                    selectedSymptoms.remove(symptom)
                                } else {
                                    selectedSymptoms.add(symptom)
                                }
                            }
                            .padding(3.dp)
                    ) {
                        Checkbox(
                            checked = selectedSymptoms.contains(symptom),
                            onCheckedChange = {
                                if (it) selectedSymptoms.add(symptom)
                                else selectedSymptoms.remove(symptom)
                            },
                            colors = CheckboxDefaults.colors(checkedColor = RosaBotao)
                        )
                        Text(text = symptom, fontSize = 16.sp, color = RosaTexto)
                    }
                }
            }

            OutlinedTextField(
                value = heartRate,
                onValueChange = { heartRate = it },
                label = { Text("Frequência Cardíaca (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RosaTexto,
                    unfocusedBorderColor = Color.LightGray,
                )
            )

            OutlinedTextField(
                value = desabafo,
                onValueChange = { desabafo = it },
                label = { Text("Quer desabafar?") },
                placeholder = { Text("Escreva aqui...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = RosaTexto,
                    unfocusedBorderColor = Color.LightGray
                )
            )

            Button(
                onClick = {
                    println("Humor: $selectedMood")
                    println("Comentário: ${comment.text}")
                    println("Sintomas: $selectedSymptoms")
                    println("Frequência Cardíaca: ${heartRate.text}")
                    println("Desabafo: ${desabafo.text}")

                    mostrarDialog = true
                    diaRegistrado = true
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
                    onClick = { navController.navigate("analiseSintomas/${selectedMood}") },
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
