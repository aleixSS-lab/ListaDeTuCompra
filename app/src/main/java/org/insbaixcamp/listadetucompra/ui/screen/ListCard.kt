package org.insbaixcamp.listadetucompra.ui.screen

import android.R.attr.fontWeight
import android.R.attr.maxLines
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import org.insbaixcamp.listadetucompra.ui.model.ShoppingList
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize


@Composable
fun ListCard (
    list: ShoppingList,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .aspectRatio(1f)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = list.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 3,
                overflow= TextOverflow.Ellipsis
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ListCardPreview() {
    ListCard(        list = ShoppingList(name = "Compra semanal"),
        onClick = {},
        modifier = Modifier.width(200.dp)
    )
}