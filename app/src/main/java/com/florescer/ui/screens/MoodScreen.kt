@file:OptIn(ExperimentalMaterial3Api::class)

package com.florescer.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
    
    // Lista dos emojis
    val moodsImages = listOf(
        R.drawable.emoji_feliz,
        R.drawable.emoji_neutro,
        R.drawable.emoji_triste,
        R.drawable.emoji_bravo,
        R.drawable.emoji_vomito,
        R.drawable.emoji_amor
    )

    var selectedMood by remember { mutableStateOf<Int?>(null) }
    var comment by remember { mutableStateOf(TextFieldValue("")) }
    var desabafo by remember { mutableStateOf(TextFieldValue("")) }
    var heartRate by remember { mutableStateOf(TextFieldValue("")) }

    var diaRegistrado by remember { mutableStateOf(false) }
    var mostrarDialog by remember { mutableStateOf(false) }

    val symptoms = listOf("Cansaço", "Fadiga", "Dor de cabeça", "Dores musculares", "Insônia")
    // Lista de sintomas físicos
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
                horizontalArrangement = Arrangement.spacedBy(7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                moodsImages.forEach { imageRes ->
                    val isSelected = selectedMood == imageRes

                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = "Mood",
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .border(
                                width = if (isSelected) 2.dp else 0.dp,
                                color = if (isSelected) Color(0xFFFFC1E3) else Color.Transparent,
                                shape = CircleShape
                            )
                            .padding(4.dp)
                            .clickable { selectedMood = imageRes },
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // Seção sintomas físicos
            Text(
                text = "Sintomas físicos:",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = RosaTexto,
                modifier = Modifier.fillMaxWidth()
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                symptoms.forEach { symptom ->
                    val isChecked = selectedSymptoms.contains(symptom)

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (isChecked) selectedSymptoms.remove(symptom)
                                else selectedSymptoms.add(symptom)
                            }
                            .padding(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .border(
                                    width = 2.dp,
                                    color = RosaBotao,
                                    shape = RoundedCornerShape(4.dp)
                                )
                        ) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = {
                                    if (it) selectedSymptoms.add(symptom)
                                    else selectedSymptoms.remove(symptom)
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color.Transparent,
                                    uncheckedColor = Color.Transparent,
                                    checkmarkColor = RosaBotao
                                ),
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(0.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = symptom,
                            fontSize = 16.sp,
                            color = RosaTexto
                        )
                    }
                }
            }

            // Botão Registrar Humor

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
