package org.insbaixcamp.listadetucompra.ui.data

/** Catálogo local para sugerir automáticamente una categoría al escribir productos comunes. */
object Catalog {
    val defaultCategories = listOf(
        "Fruta",
        "Verdura",
        "Lácteos",
        "Carne y pescado",
        "Panadería",
        "Bebidas",
        "Despensa",
        "Limpieza",
        "Otros"
    )

    private val products = mapOf(
        // Fruta (castellano y algunos nombres habituales en catalán)
        "manzana" to "Fruta", "manzanas" to "Fruta", "poma" to "Fruta",
        "plátano" to "Fruta", "platano" to "Fruta", "plátanos" to "Fruta", "plàtan" to "Fruta",
        "naranja" to "Fruta", "naranjas" to "Fruta", "pera" to "Fruta", "peras" to "Fruta",
        "fresa" to "Fruta", "fresas" to "Fruta", "uva" to "Fruta", "uvas" to "Fruta",
        "limón" to "Fruta", "limon" to "Fruta", "aguacate" to "Fruta", "piña" to "Fruta",
        "sandía" to "Fruta", "melón" to "Fruta",
        // Verdura
        "tomate" to "Verdura", "tomates" to "Verdura", "tomàquet" to "Verdura",
        "lechuga" to "Verdura", "zanahoria" to "Verdura", "pastanaga" to "Verdura",
        "cebolla" to "Verdura", "patata" to "Verdura", "pimiento" to "Verdura", "ajo" to "Verdura",
        "espinacas" to "Verdura", "brócoli" to "Verdura", "brocoli" to "Verdura",
        // Lácteos
        "leche" to "Lácteos", "llet" to "Lácteos", "yogur" to "Lácteos", "yogures" to "Lácteos",
        "queso" to "Lácteos", "mantequilla" to "Lácteos", "huevos" to "Lácteos", "nata" to "Lácteos",
        // Carne y pescado
        "pollo" to "Carne y pescado", "ternera" to "Carne y pescado", "salmón" to "Carne y pescado",
        "salmon" to "Carne y pescado", "merluza" to "Carne y pescado", "jamón" to "Carne y pescado",
        "atun" to "Carne y pescado", "atún" to "Carne y pescado", "cerdo" to "Carne y pescado",
        // Panadería
        "pan" to "Panadería", "pa" to "Panadería", "galletas" to "Panadería", "croissants" to "Panadería",
        "magdalenas" to "Panadería", "baguette" to "Panadería",
        // Bebidas
        "agua" to "Bebidas", "aigua" to "Bebidas", "zumo" to "Bebidas", "refresco" to "Bebidas",
        "cerveza" to "Bebidas", "vino" to "Bebidas", "café" to "Bebidas", "cafe" to "Bebidas", "té" to "Bebidas",
        // Despensa
        "arroz" to "Despensa", "arròs" to "Despensa", "pasta" to "Despensa", "aceite" to "Despensa",
        "sal" to "Despensa", "azúcar" to "Despensa", "azucar" to "Despensa", "avellanas" to "Despensa",
        "harina" to "Despensa", "lentejas" to "Despensa", "garbanzos" to "Despensa",
        // Limpieza
        "detergente" to "Limpieza", "lejía" to "Limpieza", "lejia" to "Limpieza",
        "papel higiénico" to "Limpieza", "papel higienico" to "Limpieza", "jabón" to "Limpieza",
        "jabon" to "Limpieza", "suavizante" to "Limpieza"
    )

    fun categoryFor(name: String): String? {
        val normalized = name.trim().lowercase()
        if (normalized.isBlank()) return null
        products[normalized]?.let { return it }
        // Permite entradas como "leche semidesnatada" o "tomate cherry".
        return products.entries.firstOrNull { (product, _) ->
            normalized.startsWith("$product ") || normalized.startsWith("${product}s ")
        }?.value
    }

    fun emojiFor(name: String): String = when (categoryFor(name)) {
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
