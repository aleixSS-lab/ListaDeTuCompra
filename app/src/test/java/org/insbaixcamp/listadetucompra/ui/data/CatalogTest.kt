package org.insbaixcamp.listadetucompra.ui.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CatalogTest {
    @Test
    fun recognizesCommonProducts() {
        assertEquals("Lácteos", Catalog.categoryFor("leche"))
        assertEquals("Lácteos", Catalog.categoryFor("Leche semidesnatada"))
        assertEquals("Fruta", Catalog.categoryFor("manzana"))
        assertEquals("Verdura", Catalog.categoryFor("tomate"))
        assertEquals("Lácteos", Catalog.categoryFor("llet"))
    }

    @Test
    fun unknownProductsDoNotGetAnAutomaticCategory() {
        assertNull(Catalog.categoryFor("producto desconocido"))
    }

    @Test
    fun defaultCategoryListIsNotEmptyAndHasUniqueNames() {
        assertEquals(true, Catalog.defaultCategories.isNotEmpty())
        assertEquals(
            Catalog.defaultCategories.size,
            Catalog.defaultCategories.distinctBy { it.lowercase() }.size
        )
    }
}
