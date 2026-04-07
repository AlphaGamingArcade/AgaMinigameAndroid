package com.alphagamingarcade.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
public fun SectionHeader(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = modifier,
    )
}

//fun SectionHeader(title: String, onClickSeeAll: () -> Unit = {}){
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 12.dp, vertical = 12.dp),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically
//    ){
//        Text(
//            text = title,
//            style = MaterialTheme.typography.headlineSmall,
//            color = Color.White
//        )
//        OutlinedButton(
//            onClick = onClickSeeAll,
//            colors = ButtonDefaults.outlinedButtonColors(
//                contentColor = MaterialTheme.colorScheme.onSecondary
//            ),
//            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSecondary),
//        ) { Text("See all") }
//    }
//}
