
package com.example.clubsentiersnature.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clubsentiersnature.models.Randonnee

@Composable
fun EcranProfilMembre(
    randonnees: MutableList<Randonnee>,
    onAjouter: () -> Unit
) {
    val contexte = LocalContext.current

    var randonneeASupprimer by remember {
        mutableStateOf<Randonnee?>(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text = "Profil du membre",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                modifier = Modifier.size(75.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "AM",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(15.dp))

            Column {
                Text(
                    text = "Aminata",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(text = "Membre du Club Sentiers Nature")

                Text(text = "${randonnees.size} sorties enregistrées")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onAjouter,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("+ Ajouter")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = {
                Toast.makeText(
                    contexte,
                    "Fonctionnalité à venir.",
                    Toast.LENGTH_SHORT
                ).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Changer de profil")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Mes randonnées",
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (randonnees.isEmpty()) {
            Text("Aucune randonnée enregistrée.")
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(randonnees.asReversed()) { randonnee ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(15.dp)
                        ) {
                            Text(
                                text = randonnee.nom,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(text = "Lieu : ${randonnee.lieu}")
                            Text(text = "Date : ${randonnee.date}")
                            Text(text = "Description : ${randonnee.description}")
                            Text(
                                text = "Appréciation : ${"★".repeat(randonnee.note)}"
                            )

                            TextButton(
                                onClick = {
                                    randonneeASupprimer = randonnee
                                }
                            ) {
                                Text("Supprimer cette sortie")
                            }
                        }
                    }
                }
            }
        }
    }

    if (randonneeASupprimer != null) {
        AlertDialog(
            onDismissRequest = {
                randonneeASupprimer = null
            },
            title = {
                Text("Supprimer cette randonnée ?")
            },
            text = {
                Text(
                    "Voulez-vous vraiment supprimer ${randonneeASupprimer?.nom} ?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        randonneeASupprimer?.let {
                            randonnees.remove(it)
                        }
                        randonneeASupprimer = null
                    }
                ) {
                    Text("Confirmer")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        randonneeASupprimer = null
                    }
                ) {
                    Text("Annuler")
                }
            }
        )
    }
}
