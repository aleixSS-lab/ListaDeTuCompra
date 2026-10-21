package org.insbaixcamp.listadetucompra.ui.screen

import androidx.compose.foundation.layout.Column 
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.insbaixcamp.listadetucompra.ui.model.ShoppingList


@Composable
fun MainScreen(
    lists : List<ShoppingList>,
    onAddList: () -> Unit, //Función para añadir una nueva lista || Unit = void
    onListClick: (String) -> Unit //Función para manejar el clic en una lista
){

}

@Composable
fun TopScreen(){
    Column() {
        Text(text = "ListaDeTuCompra")


    }
}