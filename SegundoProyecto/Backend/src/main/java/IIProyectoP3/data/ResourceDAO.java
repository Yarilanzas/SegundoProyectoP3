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
        try {
            db.executeUpdate(stm);
        } catch (SQLException ex) {
            if (ex.getErrorCode() == 1062) {
                throw new Exception("Ya existe un recurso con ese código");
            }
            if (ex.getErrorCode() == 1452) {
                throw new Exception("La categoría seleccionada no existe");
            }
            throw ex;
        }
    }

    public Resource read (String id) throws Exception{
        String sql = "select r.descripcion, c.id, c.descripcion " +
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

        try {
            int count = db.executeUpdate(stm);
            if (count == 0) {
                throw new Exception("El recurso no existe");
            }
        } catch (SQLException e) {
           if (e.getErrorCode() == 1452){
               throw new Exception("La categoria seleccionada no existe");
           }
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

    public List<Resource> findByDescription(String descripcion) throws Exception{
        List<Resource> resultado = new ArrayList<>();
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
        return resultado;
    }

    public List<Resource> findByCategory(String categoriaId) throws Exception{
        List<Resource> resultado = new ArrayList<>();
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
        return resultado;
    }

    public List<Resource> findAll() throws Exception{
        List<Resource> recursos = new ArrayList<>();
        String sql = "select r.id, r.descripcion, c.id, c.descripcion " +
                "from recurso r " +
                "inner join categoria c on r.categoria_id = c.id";

        PreparedStatement stm = db.prepareStatement(sql);
        ResultSet rs = db.executeQuery(stm);

        while (rs != null && rs.next()){
            recursos.add(from(rs,"r","c"));
        }
        return recursos;
    }

    public Resource from(ResultSet rs, String aliasR, String aliasC) throws SQLException {
            Resource r = new Resource();
            r.setId(rs.getString(aliasR + ".id"));
            r.setDescription(rs.getString(aliasR + ".descripcion"));
            r.setCategory(categoryDao.from(rs, aliasC));
            return r;
    }
}
