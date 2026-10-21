package IIProyectoP3.logic;

import domain.Category;
import domain.Resource;
import IIProyectoP3.data.*;
import java.util.ArrayList;
import java.util.List;

public class ResourceService {

    private final ResourceDAO resourceDAO;
    private final CategoryDAO categoryDAO;

    public ResourceService(){
        this.resourceDAO = new ResourceDAO();
        this.categoryDAO = new CategoryDAO();
    }

   public List<Resource> findAllResources() throws Exception{
        return resourceDAO.findAll();
    }

    public List<Resource> findByDesc(String des) throws Exception {
        return resourceDAO.findByDescription(des);
    }

    public List<Resource> findByCategory(String catDesc) throws Exception {
        List<Category> categories = categoryDAO.findbyDescription(catDesc);
        List<Resource> resultado = new ArrayList<>();

        for (Category c :categories){
            resultado.addAll(resourceDAO.findByCategory(c.getId()));
        }
        return resultado;
    }
    public void delete(String id) throws Exception {
        resourceDAO.delete(id);
    }

    public void save(Resource r)throws Exception{
        if (r.getId() == null || r.getId().trim().isEmpty()) {
            throw new Exception("El código del recurso es obligatorio");
        }
        if (r.getDescription() == null || r.getDescription().trim().isEmpty()) {
            throw new Exception("La descripción del recurso es obligatoria");
        }
        if (r.getCategory() == null) {
            throw new Exception("Debe seleccionar una categoría");
        }

        resourceDAO.create(r);

    }

    public void update (Resource r) throws Exception{
        if (r.getId() == null || r.getId().trim().isEmpty()) {
            throw new Exception("El código del recurso es obligatorio");
        }
        if (r.getDescription() == null || r.getDescription().trim().isEmpty()) {
            throw new Exception("La descripción del recurso es obligatoria");
        }
        if (r.getCategory() == null) {
            throw new Exception("Debe seleccionar una categoría");
        }
        resourceDAO.update(r);
    }
}
