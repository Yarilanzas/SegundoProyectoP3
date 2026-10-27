package IIProyectoP3.logic;

import IIProyectoP3.data.CategoryDAO;
import domain.Category;

import java.util.List;

public class CategoryService {

    private CategoryDAO categoryDAO;

    public CategoryService() {
        this.categoryDAO =  new CategoryDAO();
    }

    public List<Category> findAll() throws Exception{
        return categoryDAO.findAll();
    }

    public Category findById(String id) throws Exception{
        try {
            return categoryDAO.read(id);
        } catch (Exception ex){
            return null;
        }
    }

    public Category findByDesc(String des)throws Exception{
        List<Category> categories = categoryDAO.findbyDescription(des);
        for (Category c :categories){
            if (c.getDescription().equalsIgnoreCase(des)){
                return c;
            }
        }
        return null;
    }

    public List<Category> findByDescription(String descripcion) throws Exception{
        return categoryDAO.findbyDescription(descripcion);
    }

    public void save(Category categoria) throws Exception {
        if (categoria.getDescription() == null || categoria.getDescription().trim().isEmpty()) {
            throw new Exception("La descripción de la categoría es obligatoria");
        }

        Category existe = null;
       if (categoria.getId() != null && !categoria.getId().trim().isEmpty()){
           existe = findById(categoria.getId());
       }

       if (existe != null){
           categoryDAO.update(categoria);
       } else{
           if (categoria.getId() == null || categoria.getId().trim().isEmpty()){
               categoria.setId(generarId(categoryDAO.findAll()));
           }
           categoryDAO.create(categoria);
       }
    }

    public void delete(String id) throws Exception {
       categoryDAO.delete(id);
    }

    private String generarId(List<Category> categorias){
        int max = 0;
        for(Category c : categorias){
            String num = c.getId().replace("CAT-", "");
            try {
                int valor = Integer.parseInt(num);
                if (valor > max) {
                    max = valor;
                }
            } catch (NumberFormatException ignored) {}
        }

        int nuevonum = max + 1;
        String numText = String.valueOf(nuevonum);
        while(numText.length() < 6){
            numText = "0" + numText;
        }
        return "CAT-" + numText;
    }

}
