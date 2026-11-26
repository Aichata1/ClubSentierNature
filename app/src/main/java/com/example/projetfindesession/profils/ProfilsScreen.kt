
package com.example.projetfindesession.ui.profils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import com.example.projetfindesession.screens.AjouterSortieActivity

data class Profil(
    val id: Int,
    val nom: String,
    val nombreSorties: Int
)

@Composable
fun ProfilsScreen(
    onAccueilClick: () -> Unit = {},
    onProfilSelected: (Profil) -> Unit = {},
    modifier: Modifier
) {
    val profils = remember {
        mutableStateListOf(
            Profil(1, "Aicha", 2),
            Profil(2, "Karim", 4),
            Profil(3, "Léa", 1)
        )
    }

    var prochainId by remember { mutableIntStateOf(4) }
    var profilSelectionne by remember { mutableStateOf<Profil?>(null) }
    var profilASupprimer by remember { mutableStateOf<Profil?>(null) }
    var profilAConfirmer by remember { mutableStateOf<Profil?>(null) }

    var afficherAjout by remember { mutableStateOf(false) }
    var nouveauNom by remember { mutableStateOf("") }

    val vert = Color(0xFF2E7D32)
    val fond = Color(0xFFF4F7F2)

    Scaffold(
        containerColor = fond,
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = onAccueilClick,
                    icon = { Text("🏠") },
                    label = { Text("Accueil") }
                )

                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Text("👤") },
                    label = { Text("Profils") }
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
        ) {

            Text(
                text = "Club Sentiers Nature",
                fontSize = 14.sp,
                color = vert
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Choisir un profil",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Sélectionnez le membre qui souhaite accéder à ses randonnées.",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    nouveauNom = ""
                    afficherAjout = true
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = vert
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("+ Nouveau profil")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Membres (${profils.size})",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = profils,
                    key = { it.id }
                ) { profil ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(3.dp)
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .background(
                                        color = Color(0xFFE0F0DF),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = profil.nom.first().uppercase(),
                                    fontSize = 23.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = vert
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = profil.nom,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = "${profil.nombreSorties} randonnée(s)",
                                    fontSize = 13.sp,
                                    color = Color.Gray
                                )
                            }

                            TextButton(
                                onClick = {
                                    profilASupprimer = profil
                                }
                            ) {
                                Text(
                                    "🗑",
                                    fontSize = 20.sp,
                                    color = Color.Red
                                )
                            }
                        }

                        HorizontalDivider(
                            color = Color(0xFFEEEEEE)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            val context = LocalContext.current
                            OutlinedButton(
                                onClick = {
                                    val intent = Intent(context, AjouterSortieActivity::class.java)
                                    context.startActivity(intent)
                                },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Sélectionner")
                            }
                        }
                    }
                }
            }
        }
    }

    // FENÊTRE : AJOUTER UN PROFIL

    if (afficherAjout) {
        AlertDialog(
            onDismissRequest = {
                afficherAjout = false
            },
            title = {
                Text("Nouveau profil")
            },
            text = {
                OutlinedTextField(
                    value = nouveauNom,
                    onValueChange = { nouveauNom = it },
                    label = { Text("Nom du membre") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val nom = nouveauNom.trim()

                        if (nom.isNotEmpty()) {
                            profils.add(
                                Profil(
                                    id = prochainId,
                                    nom = nom,
                                    nombreSorties = 0
                                )
                            )

                            prochainId++
                            afficherAjout = false
                        }
                    }
                ) {
                    Text("Créer", color = vert)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        afficherAjout = false
                    }
                ) {
                    Text("Annuler")
                }
            }
        )
    }

    // FENÊTRE : CONFIRMER LA SUPPRESSION

    profilASupprimer?.let { profil ->

        AlertDialog(
            onDismissRequest = {
                profilASupprimer = null
            },
            title = {
                Text("Confirmer la suppression")
            },
            text = {
                Text(
                    "Voulez-vous vraiment supprimer le profil de ${profil.nom} ? Cette action est irréversible."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        profils.remove(profil)

                        if (profilSelectionne?.id == profil.id) {
                            profilSelectionne = null
                        }

                        profilASupprimer = null
                    }
                ) {
                    Text("Supprimer", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        profilASupprimer = null
                    }
                ) {
                    Text("Annuler")
                }
            }
        )
    }

    // FENÊTRE : CONFIRMER LE CHOIX DU PROFIL

    profilAConfirmer?.let { profil ->

        AlertDialog(
            onDismissRequest = {
                profilAConfirmer = null
            },
            title = {
                Text("Confirmer votre choix")
            },
            text = {
                Text(
                    "Voulez-vous continuer avec le profil de ${profil.nom} ?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        profilSelectionne = profil
                        profilAConfirmer = null
                        onProfilSelected(profil)
                    }
                ) {
                    Text("Confirmer", color = vert)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        profilAConfirmer = null
                    }
                ) {



                    Text("Annuler")
                }
            }
        )
    }
}
