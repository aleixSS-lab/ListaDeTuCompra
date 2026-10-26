package org.insbaixcamp.listadetucompra.ui.data

object Catalog {
    private val products = mapOf(
        // Fruta
        "manzana" to "Fruta", "plátano" to "Fruta", "naranja" to "Fruta",
        "pera" to "Fruta", "fresas" to "Fruta", "uvas" to "Fruta", "limón" to "Fruta",
        // Verdura
        "tomate" to "Verdura", "lechuga" to "Verdura", "zanahoria" to "Verdura",
        "cebolla" to "Verdura", "patata" to "Verdura", "pimiento" to "Verdura",
        // Lácteos
        "leche" to "Lácteos", "yogur" to "Lácteos", "queso" to "Lácteos",
        "mantequilla" to "Lácteos", "huevos" to "Lácteos",
        // Carne y pescado
        "pollo" to "Carne y pescado", "ternera" to "Carne y pescado",
        "salmón" to "Carne y pescado", "merluza" to "Carne y pescado",
        "jamón" to "Carne y pescado",
        // Panadería
        "pan" to "Panadería", "galletas" to "Panadería", "croissants" to "Panadería",
        // Bebidas
        "agua" to "Bebidas", "zumo" to "Bebidas", "refresco" to "Bebidas",
        "cerveza" to "Bebidas", "vino" to "Bebidas",
        // Despensa
        "arroz" to "Despensa", "pasta" to "Despensa", "aceite" to "Despensa",
        "sal" to "Despensa", "azúcar" to "Despensa", "avellanas" to "Despensa",
        // Limpieza
        "detergente" to "Limpieza", "lejía" to "Limpieza", "papel higiénico" to "Limpieza"
    )
    fun categoryFor(name: String): String? =
        products[name.trim().lowercase()]
}