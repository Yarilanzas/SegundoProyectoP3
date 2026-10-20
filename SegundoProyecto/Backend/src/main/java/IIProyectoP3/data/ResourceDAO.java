package IIProyectoP3.data;

import domain.Resource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResourceDAO {
    Database db;
    CategoryDAO categoryDao;

    public ResourceDAO() {
        db = Database.instance();
        categoryDao = new CategoryDAO();
    }

    public void create(Resource r) throws Exception {
        String sql = "insert into recurso (id, descripcion, categoria_id) values (?,?,?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, r.getId());
        stm.setString(2, r.getDescription());
        stm.setString(3, r.getCategory().getId());

        int count = db.executeUpdate(stm);
        if (count == 0){
            throw new Exception("El recurso ya existe o no se pudo insertar");
        }
    }

    public Resource read (String id) throws Exception{
        String sql = "select r.descripcion, r.descripcion, c.id, c.descripcion " +
                "from recurso r " +
                "inner join categoria c on r.categoria_id = c.id " +
                "where r.id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs = db.executeQuery(stm);

        if (rs != null && rs.next()){
            return from(rs,"r","c");
        } else {
            throw new Exception("El recurso no existe");
        }
    }

    public void update(Resource r) throws Exception{
        String sql = "update recurso set descripcion = ?, categoria_id = ? where id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, r.getDescription());
        stm.setString(2, r.getCategory().getId());
        stm.setString(3, r.getId());

        int count = db.executeUpdate(stm);
        if (count == 0){
            throw new Exception("El recurso no existe");
        }
    }

    public void delete(String id) throws Exception{
        String sql = "delete from recurso where id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1,id);

        try{
            int count = db.executeUpdate(stm);
            if (count == 0){
                throw new Exception("El recurso no existe");
            }
        } catch (SQLException ex){
            if (ex.getErrorCode() == 1451) {
                throw new Exception("No se puede eliminar el recurso porque tiene reservas asociadas");
            }
            throw ex;
        }
    }

    public List<Resource> findByDescription(String descripcion){
        List<Resource> resultado = new ArrayList<>();
        try {
            String sql = "select r.id, r.descripcion, c.id, c.descripcion " +
                    "from recurso r " +
                    "inner join categoria c on r.categoria_id = c.id " +
                    "where r.descripcion like ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1,"%" + descripcion + "%");
            ResultSet rs = db.executeQuery(stm);

            while (rs != null && rs.next()){
                resultado.add(from(rs,"r","c"));
            }
        } catch (SQLException ex) {
            System.err.println("Error al buscar recursos: " + ex.getMessage());
        }
        return resultado;
    }

    public List<Resource> findByCategory(String categoriaId){
        List<Resource> resultado = new ArrayList<>();
        try {
            String sql = "select r.id, r.descripcion, c.id, c.descripcion " +
                    "from recurso r " +
                    "inner join categoria c on r.categoria_id = c.id " +
                    "where c.id = ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1,categoriaId);
            ResultSet rs = db.executeQuery(stm);

            while (rs != null && rs.next()){
                resultado.add(from(rs,"r","c"));
            }
        } catch (SQLException ex) {
            System.err.println("Error al buscar por categoria: " + ex.getMessage());
        }
        return resultado;
    }

    public List<Resource> findAll(){
        List<Resource> recursos = new ArrayList<>();
        try{
            String sql = "select r.id, r.descripcion, c.id, c.descripcion " +
                    "from recurso r " +
                    "inner join categoria c on r.categoria_id = c.id";
            PreparedStatement stm = db.prepareStatement(sql);
            ResultSet rs = db.executeQuery(stm);

            while (rs != null && rs.next()){
                recursos.add(from(rs,"r","c"));
            }

        } catch (SQLException ex){
            System.err.println("Error al obtener todos los recursos: " + ex.getMessage());
        }
        return recursos;
    }

    public Resource from(ResultSet rs, String aliasR, String aliasC) {
        try {
            Resource r = new Resource();
            r.setId(rs.getString(aliasR + ".id"));
            r.setDescription(rs.getString(aliasR + ".descripcion"));
            r.setCategory(categoryDao.from(rs, aliasC));
            return r;
        } catch (SQLException ex) {
            return null;
        }
    }
}
