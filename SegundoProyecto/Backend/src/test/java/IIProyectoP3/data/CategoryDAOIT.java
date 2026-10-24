package IIProyectoP3.data;

import domain.Category;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryDAOIT {

    private final CategoryDAO dao = new CategoryDAO();

    @Test
    void crearLeerYBorrar() throws Exception {
        Category c = new Category();
        c.setId("CAT-TEST01");
        c.setDescription("Categoria de test");

        dao.create(c);

        Category leida = dao.read("CAT-TEST01");
        assertEquals("Categoria de test", leida.getDescription());

        dao.delete("CAT-TEST01");
        assertThrows(Exception.class, () -> dao.read("CAT-TEST01"));
    }

    @Test
    void borrarInexistenteDaError() {
        Exception ex = assertThrows(Exception.class, () -> dao.delete("CAT-NOEXISTE"));
        assertEquals("La categoria no existe", ex.getMessage());
    }
}