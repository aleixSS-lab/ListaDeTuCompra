package org.insbaixcamp.listadetucompra.ui.theme

enum class AppLanguage(val code: String, val displayName: String) {
    ES("es", "Español"),
    EN("en", "English"),
    CA("ca", "Català")
}

object Strings {
    fun appTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ES -> "Lista De Tu Compra"
        AppLanguage.EN -> "Your Shopping List"
        AppLanguage.CA -> "Llista De La Teva Compra"
    }

    fun shareListButton(lang: AppLanguage) = when (lang) {
        AppLanguage.ES -> "Compartir"
        AppLanguage.EN -> "Share"
        AppLanguage.CA -> "Compartir"
    }

    fun renameListButton(lang: AppLanguage) = when (lang) {
        AppLanguage.ES -> "Cambiar nombre"
        AppLanguage.EN -> "Rename list"
        AppLanguage.CA -> "Canviar nom"
    }

    fun myListsTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ES -> "Mis listas de compra"
        AppLanguage.EN -> "My shopping lists"
        AppLanguage.CA -> "Les meves llistes de compra"
    }

    fun myListsSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ES -> "Tus listas de supermercado guardadas"
        AppLanguage.EN -> "Your saved supermarket lists"
        AppLanguage.CA -> "Les teves llistes de supermercat desades"
    }

    fun emptyListsTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ES -> "Aún no tienes listas de compra"
        AppLanguage.EN -> "You don't have shopping lists yet"
        AppLanguage.CA -> "Encara no tens llistes de compra"
    }

    fun emptyListsSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ES -> "Haz clic en el botón '+' azul abajo para crear tu primera lista."
        AppLanguage.EN -> "Click the blue '+' button below to create your first list."
        AppLanguage.CA -> "Fes clic al botó '+' blau de sota per crear la teva primera llista."
    }

    fun createListTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ES -> "Nueva Lista de Compra"
        AppLanguage.EN -> "New Shopping List"
        AppLanguage.CA -> "Nova Llista de Compra"
    }

    fun createListSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ES -> "Asigna un nombre a tu lista para organizar tus compras fácilmente."
        AppLanguage.EN -> "Name your list to easily organize your shopping."
        AppLanguage.CA -> "Assigna un nom a la teva llista per organitzar les compres fàcilment."
    }

    fun listNamePlaceholder(lang: AppLanguage) = when (lang) {
        AppLanguage.ES -> "Ej. Compra semanal, Mercadona..."
        AppLanguage.EN -> "E.g. Weekly shopping, Walmart..."
        AppLanguage.CA -> "Ex. Compra setmanal, Bonpreu..."
    }

    fun quickSuggestionsLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.ES -> "Sugerencias rápidas:"
        AppLanguage.EN -> "Quick suggestions:"
        AppLanguage.CA -> "Suggereiments ràpids:"
    }

    fun createButton(lang: AppLanguage) = when (lang) {
        AppLanguage.ES -> "Crear Lista"
        AppLanguage.EN -> "Create List"
        AppLanguage.CA -> "Crear Llista"
    }

    fun cancelButton(lang: AppLanguage) = when (lang) {
        AppLanguage.ES -> "Cancelar"
        AppLanguage.EN -> "Cancel"
        AppLanguage.CA -> "Cancelar"
    }

    fun deleteListTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ES -> "¿Eliminar lista?"
        AppLanguage.EN -> "Delete list?"
        AppLanguage.CA -> "Eliminar llista?"
    }

    fun deleteListMessage(lang: AppLanguage, name: String) = when (lang) {
        AppLanguage.ES -> "¿Estás seguro de que deseas borrar la lista '$name'? Se eliminarán todos sus productos guardados."
        AppLanguage.EN -> "Are you sure you want to delete the list '$name'? All saved products will be removed."
        AppLanguage.CA -> "Estàs segur que vols esborrar la llista '$name'? S'eliminaran tots els productes desats."
    }

    fun deleteButton(lang: AppLanguage) = when (lang) {
        AppLanguage.ES -> "Eliminar Lista"
        AppLanguage.EN -> "Delete List"
        AppLanguage.CA -> "Eliminar Llista"
    }

    fun tagsTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ES -> "Etiquetas"
        AppLanguage.EN -> "Tags"
        AppLanguage.CA -> "Etiquetes"
    }

    fun tagsSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ES -> "Organiza tus etiquetas para las listas de la compra"
        AppLanguage.EN -> "Organize your tags for shopping lists"
        AppLanguage.CA -> "Organitza les teves etiquetes per a les llistes de compra"
    }

    fun addTagButton(lang: AppLanguage) = when (lang) {
        AppLanguage.ES -> "Añadir etiqueta"
        AppLanguage.EN -> "Add tag"
        AppLanguage.CA -> "Afegir etiqueta"
    }

    fun newTagTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ES -> "Nueva etiqueta"
        AppLanguage.EN -> "New tag"
        AppLanguage.CA -> "Nova etiqueta"
    }

    fun newTagLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.ES -> "Nombre de la etiqueta"
        AppLanguage.EN -> "Tag name"
        AppLanguage.CA -> "Nom de l'etiqueta"
    }

    fun tagExistsError(lang: AppLanguage) = when (lang) {
        AppLanguage.ES -> "Esta etiqueta ya existe"
        AppLanguage.EN -> "This tag already exists"
        AppLanguage.CA -> "Aquesta etiqueta ja existeix"
    }

    fun deleteTagTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ES -> "¿Eliminar etiqueta?"
        AppLanguage.EN -> "Delete tag?"
        AppLanguage.CA -> "Eliminar etiqueta?"
    }

    fun deleteTagMessage(lang: AppLanguage, name: String) = when (lang) {
        AppLanguage.ES -> "¿Estás seguro de que deseas borrar la etiqueta '$name'?"
        AppLanguage.EN -> "Are you sure you want to delete the tag '$name'?"
        AppLanguage.CA -> "Estàs segur que vols esborrar l'etiqueta '$name'?"
    }

    fun addProductPlaceholder(lang: AppLanguage) = when (lang) {
        AppLanguage.ES -> "Escribe un producto y pulsa Enter..."
        AppLanguage.EN -> "Type a product and press Enter..."
        AppLanguage.CA -> "Escriu un producte i prem Entrar..."
    }

    fun boughtProductsCount(lang: AppLanguage, completed: Int, total: Int) = when (lang) {
        AppLanguage.ES -> "🛍️ Comprados: $completed de $total"
        AppLanguage.EN -> "🛍️ Purchased: $completed of $total"
        AppLanguage.CA -> "🛍️ Comprats: $completed de $total"
    }

    fun emptyListTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ES -> "Tu lista está vacía"
        AppLanguage.EN -> "Your list is empty"
        AppLanguage.CA -> "La teva llista està buida"
    }

    fun emptyListSubtitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ES -> "Escribe el producto abajo y pulsa Enter en el teclado para añadirlo al instante."
        AppLanguage.EN -> "Type the product below and press Enter on keyboard to add it instantly."
        AppLanguage.CA -> "Escriu el producte a sota i prem Entrar al teclat per afegir-lo a l'instant."
    }

    fun navHome(lang: AppLanguage) = when (lang) {
        AppLanguage.ES -> "Mis Listas"
        AppLanguage.EN -> "My Lists"
        AppLanguage.CA -> "Les meves llistes"
    }

    fun navTags(lang: AppLanguage) = when (lang) {
        AppLanguage.ES -> "Etiquetas"
        AppLanguage.EN -> "Tags"
        AppLanguage.CA -> "Etiquetes"
    }

    fun navSettings(lang: AppLanguage) = when (lang) {
        AppLanguage.ES -> "Configuración"
        AppLanguage.EN -> "Settings"
        AppLanguage.CA -> "Configuració"
    }

    fun navSignOut(lang: AppLanguage) = when (lang) {
        AppLanguage.ES -> "Cerrar sesión"
        AppLanguage.EN -> "Sign out"
        AppLanguage.CA -> "Tancar sessió"
    }

    fun settingsTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ES -> "Ajustes y Configuración"
        AppLanguage.EN -> "Settings & Preferences"
        AppLanguage.CA -> "Ajustos i Configuració"
    }

    fun darkModeOption(lang: AppLanguage) = when (lang) {
        AppLanguage.ES -> "Modo Oscuro"
        AppLanguage.EN -> "Dark Mode"
        AppLanguage.CA -> "Mode Fosc"
    }

    fun languageOption(lang: AppLanguage) = when (lang) {
        AppLanguage.ES -> "Idioma de la aplicación"
        AppLanguage.EN -> "Application Language"
        AppLanguage.CA -> "Idioma de l'aplicació"
    }

    fun saveButton(lang: AppLanguage) = when (lang) {
        AppLanguage.ES -> "Guardar"
        AppLanguage.EN -> "Save"
        AppLanguage.CA -> "Desar"
    }

    fun viewProductsHint(lang: AppLanguage) = when (lang) {
        AppLanguage.ES -> "🛒 Toca para ver o mantén para borrar"
        AppLanguage.EN -> "🛒 Tap to view or hold to delete"
        AppLanguage.CA -> "🛒 Toca per veure o mantén per esborrar"
    }

    fun greeting(lang: AppLanguage, name: String) = when (lang) {
        AppLanguage.ES -> "¡Hola, $name! 👋"
        AppLanguage.EN -> "Hello, $name! 👋"
        AppLanguage.CA -> "Hola, $name! 👋"
    }

    fun activeListsCount(lang: AppLanguage, count: Int) = when (lang) {
        AppLanguage.ES -> "📋 $count listas activas"
        AppLanguage.EN -> "📋 $count active lists"
        AppLanguage.CA -> "📋 $count llistes actives"
    }
}
