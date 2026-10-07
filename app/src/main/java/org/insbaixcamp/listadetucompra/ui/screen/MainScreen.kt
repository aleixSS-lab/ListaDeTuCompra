package org.insbaixcamp.listadetucompra.ui.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.HorizontalOrVertical
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.tooling.ComposeToolingApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
        Text(
            text = "ListaDeTuCompra",
            modifier = Modifier.padding(all = 5.dp),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Row (
            horizontalArrangement = Arrangement.SpaceAround,
//            horizontalArrangament =  Arrangament.SpaceBetween,
            ) {
            //TODO este boton al darle llamara al metodo añadir lista

            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(all = 5.dp)
                    .fillMaxWidth()
                    .border(1.dp , color = Color.Black, shape = CircleShape) ,
                enabled = true,
                onClick = {
                    //Aqui llamamos al metodo añadir lista
                }){
                Text(text = " + ")

            }

            //TODO este boton al darle llamara al metodo eliminar lista
            Button(modifier = Modifier
                .weight(1f)
                .padding(all = 5.dp)
                .fillMaxWidth()
                .border(1.dp , color = Color.Black, shape = CircleShape) ,
                enabled = true,
                onClick = {
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
