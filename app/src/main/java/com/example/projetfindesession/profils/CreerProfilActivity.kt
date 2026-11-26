package com.example.projetfindesession.profils

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetfindesession.profils.CreerProfilActivity
import com.example.projetfindesession.ui.theme.ProjetFinDeSessionTheme

// ==============================================================================
// 1. CLASSE DE LA PAGE "CREER UN PROFIL" (CreerProfilActivity)
// Cette activité gère l'affichage de la page de création d'un nouveau profil.
// ==============================================================================
class CreerProfilActivity : ComponentActivity() {

    // Méthode automatiquement appelée lors du lancement de la page
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Activation de l'affichage plein écran moderne (edge-to-edge)
        enableEdgeToEdge()

        // Attribution du contenu de l'écran avec Jetpack Compose
        setContent {
            ProjetFinDeSessionTheme(){
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    
                    // Appel du composable principal avec les callbacks de fin
                    CreerProfilScreen(
                        modifier = Modifier.padding(innerPadding),
                        
                        // Action exécutée quand l'utilisateur valide la création du profil avec un nom valide
                        onProfilCree = { nom, prenom, _, _, _ ->
                            // Affichage d'un Toast de confirmation
                            Toast.makeText(this, "Profil créé avec succès : $nom $prenom", Toast.LENGTH_SHORT).show()
                            
                            // Ouvrir ProfilsActivity après la création
                            val intent = Intent(this, ProfilsActivity::class.java)
                            startActivity(intent)
                            finish()
                        },
                        
                        // Action exécutée quand l'utilisateur clique sur le bouton "ANNULER"
                        onClicAnnuler = {
                            // 'finish()' ferme cette page sans enregistrer
                            finish()
                        }
                    )
                }
            }
        }
    }
}

// ==============================================================================
// 2. FONCTION COMPOSABLE 'CreerProfilScreen'
// Définit toute l'interface visuelle du formulaire de création de profil.
// ==============================================================================
@Composable
fun CreerProfilScreen(
    modifier: Modifier = Modifier,
    // Callback transmettant les informations saisies (Nom, Prénom, Photo, Adresse, Téléphone) au composant parent
    onProfilCree: (nom: String, prenom: String, photoUri: Uri?, adresse: String, telephone: String) -> Unit = { _, _, _, _, _ -> },
    onClicAnnuler: () -> Unit = {}
) {
    // --------------------------------------------------------------------------
    // A. VARIABLES D'ÉTAT EN MÉMOIRE (State Management)
    // 'var ... by remember { mutableStateOf(...) }' permet à Compose de garder en mémoire
    // la valeur actuelle et de mettre à jour l'écran automatiquement à chaque saisie.
    // --------------------------------------------------------------------------
    var nom by remember { mutableStateOf("") }              // Variable enregistrant le Nom saisi
    var prenom by remember { mutableStateOf("") }           // Variable enregistrant le Prénom saisi
    var photoUri by remember { mutableStateOf<Uri?>(null) } // Variable enregistrant le lien (URI) de la photo choisie
    var adresse by remember { mutableStateOf("") }          // Variable enregistrant l'Adresse saisie
    var telephone by remember { mutableStateOf("") }        // Variable enregistrant le Numéro de téléphone
    var montreErreur by remember { mutableStateOf(false) }   // Variable booléenne (true/false) pour afficher une alerte rouge si le Nom obligatoire est vide

    // Récupération du contexte Android actuel (nécessaire pour accéder à la galerie d'images)
    val context = LocalContext.current
    
    // Création d'un état de défilement pour permettre de faire glisser le formulaire de haut en bas
    val defilementFormulaire = rememberScrollState()

    // --------------------------------------------------------------------------
    // B. SÉLECTEUR DE PHOTO SYSTÈME (ActivityResultLauncher)
    // 'rememberLauncherForActivityResult' ouvre l'application Galerie native du téléphone.
    // --------------------------------------------------------------------------
    val ouvrirGalerie = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent() // 'GetContent' demande la sélection d'un fichier
    ) { uriSelectionne: Uri? ->
        // Si l'utilisateur a choisi une photo (uriSelectionne n'est pas null), on la sauvegarde dans 'photoUri'
        if (uriSelectionne != null) {
            photoUri = uriSelectionne
        }
    }

    // --------------------------------------------------------------------------
    // C. CONVERSION DU FICHIER IMAGE EN BITMAP DESSINABLE
    // Transforme l'adresse URI du fichier choisi en objet 'Bitmap' prêt à être affiché à l'écran.
    // --------------------------------------------------------------------------
    val imageBitmap: Bitmap? = remember(photoUri) {
        photoUri?.let { uri ->
            try {
                // Sur les versions d'Android récentes (Android 9+ / API 28+), on utilise 'ImageDecoder'
                if (Build.VERSION.SDK_INT >= 28) {
                    val source = ImageDecoder.createSource(context.contentResolver, uri)
                    ImageDecoder.decodeBitmap(source)
                } else {
                    // Sur les anciennes versions d'Android, on utilise MediaStore
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                }
            } catch (e: Exception) {
                null // En cas d'erreur de lecture, retourne null
            }
        }
    }

    // --------------------------------------------------------------------------
    // D. STRUCTURE GRAPHIQUE DU FORMULAIRE (Mise en page plein écran)
    // --------------------------------------------------------------------------
    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.White // Fond d'écran blanc pur
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()                                      // Remplir tout l'écran
                .padding(horizontal = 24.dp, vertical = 24.dp),     // Marge intérieure autour de l'écran
            horizontalAlignment = Alignment.CenterHorizontally     // Centrer les éléments horizontalement
        ) {

            // --- TITRE DE LA PAGE ---
            Text(
                text = "CREER UN PROFIL",
                fontSize = 26.sp,                 // Taille de texte 26sp
                fontWeight = FontWeight.Bold,      // Texte en gras
                color = Color.Black,               // Couleur noire
                textAlign = TextAlign.Center,      // Centré
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // --- FORMULAIRE DÉFILANT (Column avec verticalScroll) ---
            Column(
                modifier = Modifier
                    .weight(1f)                                     // Prend tout l'espace vertical restant au milieu
                    .fillMaxWidth()
                    .verticalScroll(defilementFormulaire),          // Rendre la liste de champs défilable avec le doigt
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)   // Espace de 14dp entre chaque champ de saisie
            ) {

                // --------------------------------------------------------------
                // 1. EMPLACEMENT CIRCULAIRE DE SÉLECTION DE LA PHOTO DE PROFIL
                // --------------------------------------------------------------
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "PHOTO:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                    )
                    
                    // 'Box' circulaire cliquable qui ouvre la galerie d'images au toucher
                    Box(
                        modifier = Modifier
                            .size(90.dp)                                    // Taille fixe de 90dp x 90dp
                            .clip(CircleShape)                              // Découpe le conteneur en cercle parfait
                            .background(Color(0xFFE0E0E0))                  // Fond gris clair
                            .border(1.5.dp, Color.Gray, CircleShape)        // Bordure grise de 1.5dp
                            .clickable { ouvrirGalerie.launch("image/*") },  // Déclenche l'ouverture de la galerie pour choisir une image (*=tous types d'images)
                        contentAlignment = Alignment.Center
                    ) {
                        // Si une photo a été sélectionnée par l'utilisateur (imageBitmap non null)
                        if (imageBitmap != null) {
                            Image(
                                bitmap = imageBitmap.asImageBitmap(),       // Convertit et affiche la photo Bitmap
                                contentDescription = "Photo du profil",
                                contentScale = ContentScale.Crop,            // Découpe et remplit le cercle sans déformer la photo
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            // Sinon, affiche l'icône d'appareil photo par défaut avec le texte "Choisir photo"
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = "📷", fontSize = 26.sp)
                                Text(
                                    text = "Choisir photo",
                                    fontSize = 11.sp,
                                    color = Color.DarkGray,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // --------------------------------------------------------------
                // 2. CHAMP NOM (Obligatoire avec astérisque *)
                // --------------------------------------------------------------
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically // Aligne le texte du label et la case de saisie sur la même ligne horizontale
                ) {
                    // Label "NOM: *"
                    Text(
                        text = "NOM: *",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier.width(90.dp) // Largeur fixe de 90dp pour aligner tous les labels proprement
                    )
                    // Case de saisie de texte
                    OutlinedTextField(
                        value = nom,                       // Valeur affichée dans la case
                        onValueChange = { texteEntre ->    // Fonction appelée à chaque lettre tapée
                            nom = texteEntre
                            if (texteEntre.isNotBlank()) montreErreur = false // Efface l'erreur dès que l'utilisateur commence à taper
                        },
                        isError = montreErreur,             // Affiche le cadre en rouge si 'montreErreur' est vrai
                        singleLine = true,                 // Empêche les retours à la ligne
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words), // Majuscule automatique au début de chaque mot
                        modifier = Modifier.fillMaxWidth() // Remplir la largeur restante
                    )
                }

                // --------------------------------------------------------------
                // 3. CHAMP PRENOM (Optionnel)
                // --------------------------------------------------------------
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "PRENOM:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier.width(90.dp)
                    )
                    OutlinedTextField(
                        value = prenom,
                        onValueChange = { prenom = it },   // 'it' désigne la nouvelle valeur saisie
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // --------------------------------------------------------------
                // 4. CHAMP ADRESSE (Optionnel)
                // --------------------------------------------------------------
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ADRESSE:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier.width(90.dp)
                    )
                    OutlinedTextField(
                        value = adresse,
                        onValueChange = { adresse = it },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences), // Majuscule en début de phrase
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // --------------------------------------------------------------
                // 5. CHAMP TELEPHONE (Optionnel)
                // --------------------------------------------------------------
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TEL:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier.width(90.dp)
                    )
                    OutlinedTextField(
                        value = telephone,
                        onValueChange = { telephone = it },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), // Ouvre un clavier avec les chiffres téléphoniques
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // --------------------------------------------------------------
                // 6. MESSAGE D'INFORMATION DE L'ASTÉRISQUE (*)
                // --------------------------------------------------------------
                Text(
                    text = "Les champs avec * ne doivent pas rester vide",
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic,        // Texte en italique
                    color = if (montreErreur) Color.Red else Color.Gray, // Devient ROUGE si l'utilisateur a essayé d'envoyer un nom vide
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ------------------------------------------------------------------
            // E. BOUTONS BLEUS EN BAS DE PAGE (CREER UN PROFIL / ANNULER)
            // ------------------------------------------------------------------
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp) // Espace de 10dp entre les deux boutons
            ) {
                // Couleur en dégradé bleu pour le fond des boutons
                val couleurbouton = Brush.verticalGradient(
                    colors = listOf(Color(0xFF81D4FA), Color(0xFF0288D1))
                )

                // --- BOUTON 1 : CREER UN PROFIL ---
                Button(
                    onClick = {
                        // VÉRIFICATION DE VALIDATION : Le champ Nom ne doit pas être vide
                        if (nom.isBlank()) {
                            montreErreur = true // Active le mode d'erreur rouge
                        } else {
                            // Transmet toutes les données saisies à la fonction de confirmation
                            onProfilCree(nom, prenom, photoUri, adresse, telephone)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()             // Bouton prenant toute la largeur
                        .height(50.dp)              // Hauteur confortable de 50dp
                        .background(couleurbouton, shape = RoundedCornerShape(8.dp)), // Application du dégradé bleu
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "CREER UN PROFIL",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center
                    )
                }

                // --- BOUTON 2 : ANNULER ---
                Button(
                    onClick = onClicAnnuler,        // Déclenche l'annulation et la fermeture de la page
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(couleurbouton, shape = RoundedCornerShape(8.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "ANNULER",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
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
// ==============================================================================
@Preview(showBackground = true)
@Composable
fun CreerProfilScreenPreview() {
    ProjetFinDeSessionTheme() {
        CreerProfilScreen()
    }
}
