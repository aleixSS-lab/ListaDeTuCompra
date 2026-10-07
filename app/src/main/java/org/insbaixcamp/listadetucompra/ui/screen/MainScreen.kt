package org.insbaixcamp.listadetucompra.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.tooling.ComposeToolingApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

    @Preview
    @Composable
    fun pantallaInicial() {
        pantallaTop()
        pantallaMedio()
        pantallaMenuNavegacion()
    }


    @Composable
fun pantallaTop(){
    Column {
        Text(text = "ListaDeTuCompra")

        Row {
            //TODO este boton al darle llamara al metodo añadir lista

            Button(onClick = {
                //Aqui llamamos al metodo añadir lista
            }){
                Text(text = " + ")
            }

            //TODO este boton al darle llamara al metodo eliminar lista
            Button(onClick = {
                //Aqui llamamos al metodo eliminar lista
            }){ Text(text = " - ")}
        }
    }
}

@Composable
fun pantallaMedio(){

    }
    @Composable
fun pantallaMenuNavegacion(){

    Row{
//        Boton para ir las categorias
       Button(
           modifier = Modifier.weight(1f),
           onClick = {
               //Aqui llamaremos a la pagina de Categorias
           }
       ) { Text(text = "Categorias") }

        Button(
            modifier = Modifier.weight(1f),
            onClick = {
//                Aqui se llama a la pagina Home
            }
        ) { Text( text = "Inicio") }

        Button(
            modifier = Modifier.weight(1f),
            onClick = {
                //Aqui se llama a la pagina de  etiquetas
            }
        ) {Text( text = "Etiquetas") }
    }
}
