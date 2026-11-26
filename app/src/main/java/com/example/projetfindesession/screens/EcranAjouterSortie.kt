
package com.example.clubsentiersnature.screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.clubsentiersnature.models.Randonnee


@Composable
fun EcranAjouterSortie(
    onAnnuler: () -> Unit,
    onAjouter: (Randonnee) -> Unit
) {
    var nom by remember { mutableStateOf("") }
    var lieu by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var photo by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var messageErreur by remember { mutableStateOf("") }
    var note by remember { mutableIntStateOf(5) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "AJOUTER UNE SORTIE",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(25.dp))

        Text("NOM :", fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start))

        OutlinedTextField(
            value = nom,
            onValueChange = { nom = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nom de la randonnée") }
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text("LIEU :", fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start))

        OutlinedTextField(
            value = lieu,
            onValueChange = { lieu = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text("Lieu de la randonnée") }
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text("DATE :", fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start))

        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text("Ex. : 24 novembre 2026") }
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text("PHOTO (FACULTATIVE) :",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start))

        OutlinedTextField(
            value = photo,
            onValueChange = { photo = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text("Référence de la photo") }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text("DESCRIPTION :", fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            placeholder = { Text("Décrivez votre randonnée...") }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text("APPRÉCIATION :", fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start))

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            (1..5).forEach { valeur ->
                if (note == valeur) {
                    Button(
                        onClick = { note = valeur },
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        Text("$valeur ★")
                    }
                } else {
                    OutlinedButton(
                        onClick = { note = valeur },
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        Text("$valeur ★")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        if (messageErreur.isNotEmpty()) {
            Text(
                text = messageErreur,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    if (
                        nom.isBlank() ||
                        lieu.isBlank() ||
                        date.isBlank() ||
                        description.isBlank()
                    ) {
                        messageErreur =
                            "Veuillez remplir tous les champs obligatoires."
                    } else {
                        val nouvelleRandonnee = Randonnee(
                            nom = nom,
                            lieu = lieu,
                            date = date,
                            note = note,
                            description = description,
                            photo = photo
                        )

                        onAjouter(nouvelleRandonnee)
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("AJOUTER")
            }

            OutlinedButton(
                onClick = onAnnuler,
                modifier = Modifier.weight(1f)
            ) {
                Text("ANNULER")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
