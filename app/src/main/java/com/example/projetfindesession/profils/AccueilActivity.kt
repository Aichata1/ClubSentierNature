package com.example.projetfindesession.profils

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetfindesession.profils.AccueilActivity
import com.example.projetfindesession.ui.theme.ProjetFinDeSessionTheme

// ==============================================================================
// 1. CLASSE PRINCIPALE DE L'ÉCRAN D'ACCUEIL (AccueilActivity)
// 'ComponentActivity' indique à Android que cette classe est une vraie page de l'application.
// ==============================================================================
class AccueilActivity : ComponentActivity() {

    // La méthode 'onCreate' est le point d'entrée : elle s'exécute automatiquement à l'ouverture de la page.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 'enableEdgeToEdge()' permet à l'écran de s'afficher sur toute la surface du téléphone (sans barres noires).
        enableEdgeToEdge()

        // 'setContent' est le pont entre l'activité Android et l'interface réalisée en Jetpack Compose.
        setContent {
            // Application du thème global de l'application (couleurs et styles)
            ProjetFinDeSessionTheme() {
                
                // 'Scaffold' fournit la structure d'écran de base et calcule les marges de sécurité système (innerPadding).
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    
                    // On appelle la fonction Composable 'AccueilScreen' pour afficher l'interface.
                    AccueilScreen(
                        // Application de la marge de sécurité autour de l'écran pour ne pas chevaucher la barre du téléphone
                        modifier = Modifier.padding(innerPadding),
                        
                        // Action exécutée quand l'utilisateur clique sur le bouton "CREER UN PROFIL"
                        onClicCreerProfil = {
                            // Un 'Intent' est un message qui demande à Android d'ouvrir une autre activité (CreerProfilActivity)
                            val intention = Intent(this, CreerProfilActivity::class.java)
                            
                            // 'startActivity' déclenche le changement de page
                            startActivity(intention)
                        },
                        
                        // Action exécutée quand l'utilisateur clique sur "AJOUTER UNE SORTIE"
                        onClicAjouterSortie = {
                            // 'Toast' affiche une petite bulle de texte temporaire au bas de l'écran
                            Toast.makeText(this, "Ajouter une sortie", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

// ==============================================================================
// 2. FONCTION COMPOSABLE 'AccueilScreen'
// L'annotation '@Composable' indique à Kotlin que cette fonction dessine des éléments visuels à l'écran.
// ==============================================================================
@Composable
fun AccueilScreen(
    modifier: Modifier = Modifier,            // Modificateurs de mise en page transmis par le parent
    onClicCreerProfil: () -> Unit = {},         // Fonction callback appelée lors du clic sur Créer un profil
    onClicAjouterSortie: () -> Unit = {}        // Fonction callback appelée lors du clic sur Ajouter une sortie
) {
    // 'Surface' crée un conteneur de fond d'écran. Ici, la couleur de fond est blanche (Color.White).
    Surface(
        modifier = modifier.fillMaxSize(),    // 'fillMaxSize()' ordonne d'occuper 100% de l'écran
        color = Color.White                   // Couleur de fond blanche
    ) {
        // 'Column' empile tous ses enfants verticalement (les uns sous les autres).
        Column(
            modifier = Modifier
                .fillMaxSize()                                      // Remplir tout l'espace disponible
                .padding(horizontal = 24.dp, vertical = 32.dp),     // Marge intérieure de 24dp sur les côtés et 32dp en haut/bas
            horizontalAlignment = Alignment.CenterHorizontally,    // Aligne tous les éléments au centre horizontalement
            verticalArrangement = Arrangement.SpaceBetween        // Espace équitablement le haut, le milieu et le bas
        ) {

            // ------------------------------------------------------------------
            // BLOC A : LE TITRE "BIENVENUE" EN HAUT DE L'ÉCRAN
            // ------------------------------------------------------------------
            Text(
                text = "BIENVENUE",               // Le texte à afficher
                fontSize = 28.sp,                 // Taille de police de 28sp (sp s'adapte à la taille de police du téléphone)
                fontWeight = FontWeight.Bold,      // Rendre le texte en gras
                color = Color.Black,               // Couleur du texte en noir
                textAlign = TextAlign.Center,      // Centrer le texte horizontalement
                modifier = Modifier.fillMaxWidth() // Le composant prend toute la largeur pour bien centrer le texte
            )

            // ------------------------------------------------------------------
            // BLOC B : LE CONTENU CENTRAL (NOM DU CLUB ET SOUS-TITRE)
            // ------------------------------------------------------------------
            Column(
                horizontalAlignment = Alignment.CenterHorizontally, // Centrer le texte dans cette sous-colonne
                modifier = Modifier.fillMaxWidth()                  // Prendre toute la largeur
            ) {
                // Ligne 1 du club
                Text(
                    text = "Club",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32),     // Code couleur vert (Hexadécimal 0xFF2E7D32)
                    textAlign = TextAlign.Center
                )
                // Ligne 2 du club
                Text(
                    text = "Sentiers",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32),     // Couleur verte
                    textAlign = TextAlign.Center
                )
                // Ligne 3 du club
                Text(
                    text = "Nature",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32),     // Couleur verte
                    textAlign = TextAlign.Center
                )

                // 'Spacer' crée un espace vide invisible de 16dp de hauteur pour séparer le titre du sous-titre
                Spacer(modifier = Modifier.height(16.dp))

                // Sous-titre descriptif en deux lignes
                Text(
                    text = "Les arbres, les saisons\net les plantes",
                    fontSize = 16.sp,
                    color = Color.DarkGray,        // Couleur gris foncé
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp             // Hauteur d'interligne entre les 2 lignes de texte
                )
            }

            // ------------------------------------------------------------------
            // BLOC C : LES DEUX BOUTONS BLEUS EN BAS DE L'ÉCRAN
            // ------------------------------------------------------------------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp), // Ajoute automatiquement un espace de 14dp entre les 2 boutons
                horizontalAlignment = Alignment.CenterHorizontally  // Centre les boutons horizontalement
            ) {
                // 'Brush.verticalGradient' crée un dégradé de couleur bleu clair vers bleu foncé
                val couleurbouton = Brush.verticalGradient(
                    colors = listOf(Color(0xFF81D4FA), Color(0xFF0288D1))
                )

                // --- BOUTON 1 : CREER UN PROFIL ---
                Button(
                    onClick = onClicCreerProfil,    // Action exécutée au clic
                    modifier = Modifier
                        .fillMaxWidth()             // Bouton large occupant toute la largeur
                        .height(52.dp)              // Hauteur standard de 52dp pour faciliter le clic avec le doigt
                        .background(couleurbouton, shape = RoundedCornerShape(8.dp)), // Fond dégradé avec coins arrondis de 8dp
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent), // Rend le conteneur par défaut transparent pour laisser voir le dégradé
                    shape = RoundedCornerShape(8.dp) // Forme arrondie des coins
                ) {
                    Text(
                        text = "CREER UN PROFIL",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,        // Texte du bouton en blanc
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center
                    )
                }

                // --- BOUTON 2 : AJOUTER UNE SORTIE ---
                Button(
                    onClick = onClicAjouterSortie,  // Action exécutée au clic
                    modifier = Modifier
                        .fillMaxWidth()             // Bouton large
                        .height(52.dp)              // Hauteur de 52dp
                        .background(couleurbouton, shape = RoundedCornerShape(8.dp)), // Fond dégradé
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "AJOUTER UNE SORTIE",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,        // Texte blanc
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// ==============================================================================
// 3. APERÇU VISUEL DANS ANDROID STUDIO (@Preview)
// Permet aux développeurs de voir le résultat graphique directement dans l'IDE sans lancer d'émulateur.
// ==============================================================================
@Preview(showBackground = true)
@Composable
fun AccueilScreenPreview() {
    ProjetFinDeSessionTheme()   {
        AccueilScreen()
    }
}
