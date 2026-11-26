package com.example.projetfindesession.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.clubsentiersnature.screens.EcranAjouterSortie
import com.example.projetfindesession.ui.theme.ProjetFinDeSessionTheme

class AjouterSortieActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ProjetFinDeSessionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    EcranAjouterSortie(
                        onAnnuler = {
                            finish() // Revenir en arrière
                        },
                        onAjouter = { nouvelleRandonnee ->
                            // TODO: Enregistrer la randonnée (Base de données ou ViewModel)
                            finish() // Revenir à l'écran précédent après l'ajout
                        }
                    )
                }
            }
        }
    }
}