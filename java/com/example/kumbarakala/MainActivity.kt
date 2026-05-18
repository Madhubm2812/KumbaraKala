package com.example.kumbarakala

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kumbarakala.ui.theme.KumbaraKalaTheme

data class ClayItem(val name: String, val color: Color, @DrawableRes val imageRes: Int)

class MainActivity : ComponentActivity() {

    val items = listOf(
        ClayItem("Clay Pot", Color(0xFF8D6E63), R.drawable.clay_pot),
        ClayItem("Clay Lamp", Color(0xFFA1887F), R.drawable.clay_lamp),
        ClayItem("Clay Water Bottle", Color(0xFF795548), R.drawable.clay_bottle),
        ClayItem("Clay Cooking Pot", Color(0xFF5D4037), R.drawable.clay_cooking_pot),
        ClayItem("Clay Plate", Color(0xFF6D4C41), R.drawable.clay_plate),
        ClayItem("Clay Jug", Color(0xFF4E342E), R.drawable.clay_jug)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KumbaraKalaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFFBE9E7) // Light earthy background
                ) {
                    ItemListScreen(items) { selectedItem ->
                        val intent = Intent(this, DetailActivity::class.java)
                        intent.putExtra("item_name", selectedItem.name)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}

@Composable
fun ItemListScreen(items: List<ClayItem>, onItemClick: (ClayItem) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {

        Text(
            text = "Kumbara-Kala",
            style = MaterialTheme.typography.headlineLarge.copy(
                color = Color(0xFF3E2723),
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Text(
            text = "Digital Story Cards for Traditional Art",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF5D4037)),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items) { item ->
                ClayItemCard(item) { onItemClick(item) }
            }
        }
    }
}

@Composable
fun ClayItemCard(item: ClayItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.8f)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(item.color),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = item.name,
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Fit
                )
            }
            
            Text(
                text = item.name,
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color(0xFF3E2723),
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}
