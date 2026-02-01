package test.domain;

import com.syos.domain.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category("Beverages", "CAT001");
    }


    @Test
    void testConstructor_StoresValues() {
        assertEquals("CAT001", category.getCode());
        assertEquals("Beverages", category.getName());
    }

    @Test
    void testConstructor_NullValues() {
        Category nullCat = new Category(null, null);
        assertNull(nullCat.getCode());
        assertNull(nullCat.getName());
    }

    @Test
    void testConstructor_EmptyValues() {
        Category emptyCat = new Category("", "");
        assertEquals("", emptyCat.getCode());
        assertEquals("", emptyCat.getName());
    }

    @Test
    void testConstructor_SpecialCharacters() {

        Category specialCat = new Category("Snacks & Drinks", "#@!");

        assertEquals("#@!", specialCat.getCode());
        assertEquals("Snacks & Drinks", specialCat.getName());
    }
}