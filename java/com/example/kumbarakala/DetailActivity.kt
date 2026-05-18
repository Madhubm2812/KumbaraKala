package com.example.kumbarakala

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kumbarakala.ui.theme.KumbaraKalaTheme
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val itemName = intent.getStringExtra("item_name") ?: "Clay Item"

        setContent {
            KumbaraKalaTheme {
                DetailScreen(itemName, onBack = { finish() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(itemName: String, onBack: () -> Unit) {
    val context = LocalContext.current
    var artisanName by remember { mutableStateOf("Master Rameshwar") }
    var artisanPhone by remember { mutableStateOf("+91 98765 43210") }
    var artisanBio by remember { mutableStateOf("I am a 5th generation potter from the village of Kumbara. My family has been preserving the art of clay for over 150 years, using traditional techniques passed down from my grandfather.") }
    var generatedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val story = remember { generateText(itemName) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(itemName) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFBE9E7),
                    titleContentColor = Color(0xFF3E2723)
                )
            )
        },
        containerColor = Color(0xFFFBE9E7)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Artisan Input Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Artisan Information",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF3E2723)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = artisanName,
                        onValueChange = { artisanName = it },
                        label = { Text("Your Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = artisanPhone,
                        onValueChange = { artisanPhone = it },
                        label = { Text("Phone Number") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = artisanBio,
                        onValueChange = { artisanBio = it },
                        label = { Text("About Your Heritage (Bio)") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (artisanName.isBlank() || artisanPhone.isBlank()) {
                        Toast.makeText(context, "Please enter artisan details", Toast.LENGTH_SHORT).show()
                    } else {
                        generatedBitmap = createStoryCard(itemName, story, artisanName, artisanPhone, artisanBio)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8D6E63))
            ) {
                Text("Generate Story Card")
            }

            Spacer(modifier = Modifier.height(24.dp))

            generatedBitmap?.let { bitmap ->
                Text(
                    text = "Preview",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFF3E2723),
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Preview",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .background(Color.LightGray)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { shareImage(context, bitmap) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Icon(Icons.Default.Share, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Share on WhatsApp")
                }
            }
        }
    }
}

fun createStoryCard(
    item: String,
    story: String,
    artisanName: String,
    artisanPhone: String,
    artisanBio: String
): Bitmap {
    val width = 1080
    val height = 1350
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)

    // Background
    val paint = Paint().apply {
        color = android.graphics.Color.rgb(251, 233, 231)
    }
    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

    // Border
    val borderPaint = Paint().apply {
        color = android.graphics.Color.rgb(62, 39, 35)
        style = Paint.Style.STROKE
        strokeWidth = 20f
    }
    canvas.drawRect(40f, 40f, width - 40f, height - 40f, borderPaint)

    // Draw item name
    val titlePaint = Paint().apply {
        color = android.graphics.Color.rgb(62, 39, 35)
        textSize = 90f
        isFakeBoldText = true
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
    }
    canvas.drawText(item, width / 2f, 250f, titlePaint)

    // Draw "Story"
    val storyPaint = TextPaint().apply {
        color = android.graphics.Color.rgb(93, 64, 55)
        textSize = 55f
        isAntiAlias = true
        typeface = Typeface.create(Typeface.SERIF, Typeface.NORMAL)
    }
    val staticLayout = StaticLayout.Builder.obtain(story, 0, story.length, storyPaint, width - 200)
        .setAlignment(Layout.Alignment.ALIGN_CENTER)
        .build()
    
    canvas.save()
    canvas.translate(100f, 450f)
    staticLayout.draw(canvas)
    canvas.restore()

    // Draw Bio
    if (artisanBio.isNotBlank()) {
        val bioPaint = TextPaint().apply {
            color = android.graphics.Color.rgb(121, 85, 72)
            textSize = 35f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
        }
        val bioLayout = StaticLayout.Builder.obtain(artisanBio, 0, artisanBio.length, bioPaint, width - 300)
            .setAlignment(Layout.Alignment.ALIGN_CENTER)
            .build()
        
        canvas.save()
        canvas.translate(150f, 850f)
        bioLayout.draw(canvas)
        canvas.restore()
    }

    // Draw Artisan Info at bottom
    val infoPaint = Paint().apply {
        color = android.graphics.Color.rgb(62, 39, 35)
        textSize = 45f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }
    canvas.drawText("Crafted by: $artisanName", width / 2f, height - 250f, infoPaint)
    canvas.drawText("Call for orders: $artisanPhone", width / 2f, height - 180f, infoPaint)

    // Footer
    val footerPaint = Paint().apply {
        color = android.graphics.Color.rgb(121, 85, 72)
        textSize = 30f
        textAlign = Paint.Align.CENTER
    }
    canvas.drawText("Powered by Kumbara-Kala", width / 2f, height - 80f, footerPaint)

    return bitmap
}

fun shareImage(context: Context, bitmap: Bitmap) {
    try {
        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs()
        val stream = FileOutputStream("$cachePath/story_card.png")
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.close()

        val imageFile = File(cachePath, "story_card.png")
        val contentUri: Uri = FileProvider.getUriForFile(
            context,
            "com.example.kumbarakala.fileprovider",
            imageFile
        )

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            setDataAndType(contentUri, context.contentResolver.getType(contentUri))
            putExtra(Intent.EXTRA_STREAM, contentUri)
            type = "image/png"
            setPackage("com.whatsapp") // Try to open WhatsApp directly
        }
        
        // If WhatsApp is not installed, fallback to general share
        try {
            context.startActivity(shareIntent)
        } catch (e: Exception) {
            shareIntent.setPackage(null)
            context.startActivity(Intent.createChooser(shareIntent, "Share Card"))
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error sharing image", Toast.LENGTH_SHORT).show()
    }
}

fun generateText(item: String): String {
    return when (item) {
        "Clay Pot" -> "Our ancestors lived healthy lives using clay pots. They naturally cool water and balance the pH level, providing minerals that are essential for our body. Switch to clay, switch to health."
        "Clay Lamp" -> "Bring the warmth of tradition to your home. These hand-crafted clay lamps are eco-friendly and symbolize the eternal light of our rich heritage. Light up your life naturally."
        "Clay Water Bottle" -> "The natural way to stay hydrated. Clay bottles keep water cool without electricity and add an earthy flavor. Better for you, better for the planet."
        "Clay Cooking Pot" -> "Rediscover the taste of slow-cooked food. Clay pots retain 100% of nutrients and use less oil. The alkaline nature of clay neutralizes acidity in food."
        "Clay Plate" -> "Eat as nature intended. Clay plates are chemical-free and sustainable. Experience the authentic touch of earth with every meal."
        "Clay Jug" -> "A beautiful blend of utility and art. This clay jug keeps your beverages naturally fresh and cool. A testament to the skill of our local artisans."
        else -> "Handmade with love by our local artisans. Each piece is unique and carries the legacy of generations. Support traditional crafts for a sustainable future."
    }
}
