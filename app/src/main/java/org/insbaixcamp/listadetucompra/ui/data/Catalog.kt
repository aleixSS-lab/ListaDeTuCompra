package org.insbaixcamp.listadetucompra.ui.data

object Catalog {
    private val products = mapOf(
        // Fruta
        "manzana" to "Fruta", "plátano" to "Fruta", "naranja" to "Fruta",
        "pera" to "Fruta", "fresas" to "Fruta", "uvas" to "Fruta", "limón" to "Fruta", "aguacate" to "Fruta", "piña" to "Fruta", "sandía" to "Fruta", "melón" to "Fruta",
        // Verdura
        "tomate" to "Verdura", "lechuga" to "Verdura", "zanahoria" to "Verdura",
        "cebolla" to "Verdura", "patata" to "Verdura", "pimiento" to "Verdura", "ajo" to "Verdura", "espinacas" to "Verdura", "brócoli" to "Verdura",
        // Lácteos
        "leche" to "Lácteos", "yogur" to "Lácteos", "queso" to "Lácteos",
        "mantequilla" to "Lácteos", "huevos" to "Lácteos", "nata" to "Lácteos",
        // Carne y pescado
        "pollo" to "Carne y pescado", "ternera" to "Carne y pescado",
        "salmón" to "Carne y pescado", "merluza" to "Carne y pescado",
        "jamón" to "Carne y pescado", "atún" to "Carne y pescado", "cerdo" to "Carne y pescado",
        // Panadería
        "pan" to "Panadería", "galletas" to "Panadería", "croissants" to "Panadería", "magdalenas" to "Panadería",
        // Bebidas
        "agua" to "Bebidas", "zumo" to "Bebidas", "refresco" to "Bebidas",
        "cerveza" to "Bebidas", "vino" to "Bebidas", "café" to "Bebidas", "té" to "Bebidas",
        // Despensa
        "arroz" to "Despensa", "pasta" to "Despensa", "aceite" to "Despensa",
        "sal" to "Despensa", "azúcar" to "Despensa", "avellanas" to "Despensa", "harina" to "Despensa",
        // Limpieza
        "detergente" to "Limpieza", "lejía" to "Limpieza", "papel higiénico" to "Limpieza", "jabón" to "Limpieza", "suavizante" to "Limpieza"
    )

    fun categoryFor(name: String): String? =
        products[name.trim().lowercase()]

    fun emojiFor(name: String): String {
        return when (categoryFor(name)) {
            "Fruta" -> "🍎"
            "Verdura" -> "🥦"
            "Lácteos" -> "🥛"
            "Carne y pescado" -> "🥩"
            "Panadería" -> "🍞"
            "Bebidas" -> "🥤"
            "Despensa" -> "🥫"
            "Limpieza" -> "🧻"
            else -> "🛒"
        }
    }
}
