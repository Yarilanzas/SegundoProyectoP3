package IIProyectoP3.data;

import domain.Category;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    Database db;

    public CategoryDAO() {
        db = Database.instance();
    }

    public void create(Category c) throws Exception {
        String sql = "insert into categoria (id, descripcion) values (?,?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, c.getId());
        stm.setString(2, c.getDescription());

        int count = db.executeUpdate(stm);
        if (count == 0){
            throw new Exception("La categoria ya existe o no se pudo insertar");
        }
    }

    public Category read (String id) throws Exception{
        String sql = "select c.id, c.descripcion " + "from categoria c " +
                "where c.id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs = db.executeQuery(stm);

        if (rs != null && rs.next()){
            return from(rs,"c");
        } else {
            throw new Exception("La categoria no existe");
        }
    }

    public void update(Category c) throws Exception{
        String sql = "update categoria set descripcion = ? where id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, c.getDescription());
        stm.setString(2, c.getId());

        int count = db.executeUpdate(stm);
        if (count == 0){
            throw new Exception("La categoria no existe");
        }
    }

    public void delete(String id) throws Exception{
        String sql = "delete from categoria where id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1,id);

        try{
            int count = db.executeUpdate(stm);
            if (count == 0){
                throw new Exception("La categoria no existe");
            }
        } catch (SQLException ex){
            if (ex.getErrorCode() == 1451) {
                throw new Exception("No se puede eliminar la categoría porque tiene recursos asociados");
            }
            throw ex;
        }
    }

    public List<Category> findbyDescription(String descripcion){
        List<Category> resultado = new ArrayList<>();
        try {
            String sql = "select c.id, c.descripcion " +
                    "from categoria c " +
                    "where c.descripcion like ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1,"%" + descripcion + "%");
            ResultSet rs = db.executeQuery(stm);

            while (rs != null && rs.next()){
                resultado.add(from(rs,"c"));
            }
        } catch (SQLException ex) {
            System.err.println("Error al buscar categorias: " + ex.getMessage());
        }
        return resultado;
    }

    public List<Category> findAll(){
        return findbyDescription("");
    }

    public Category from(ResultSet rs, String alias) {
        try {
            Category c = new Category();
            c.setId(rs.getString(alias + ".id"));
            c.setDescription(rs.getString(alias + ".descripcion"));
            return c;
        } catch (SQLException ex) {
            return null;
        }
    }
}
