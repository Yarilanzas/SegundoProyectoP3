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
        Resource existe = null;
        try{
            existe = resourceDAO.read(r.getId());
        } catch (Exception ignored) {}

        if (existe != null){
            throw new Exception("Ya existe un recurso con ese ID");
        }else {
            resourceDAO.create(r);
        }
    }

    public void update (Resource r) throws Exception{
        resourceDAO.update(r);
    }
}
