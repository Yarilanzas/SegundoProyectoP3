package IIProyectoP3.data;


import domain.Administrator;
import domain.Employee;
import domain.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;


public class UserDAO {
    Database db;

    public UserDAO() {
        db = Database.instance();
    }

    public User login(String id, String pass) throws Exception {
        String sql = "select u.id, u.clave, u.rol, f.nombre, f.telefono " +
                "from usuario u " +
                "left join funcionario f on u.id = f.id " +
                "where u.id=? and u.clave=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        stm.setString(2, pass);
        ResultSet rs = db.executeQuery(stm);
        if (rs != null && rs.next()) {
            return from(rs, "u", "f");
        }
        return null;
    }

    public void changePassword(String id, String newPass) throws Exception{
        String sql = "update usuario set clave=? where id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, newPass);
        stm.setString(2, id);
        int count = db.executeUpdate(stm);
        if (count == 0){
            throw new Exception("El usuario no existe");
        }
    }

    public List<Employee> findAllEmployees() throws Exception {
        List<Employee> resultado = new ArrayList<>();
        String sql = "select u.id, u.clave, u.rol, f.nombre, f.telefono " +
                "from usuario u " +
                "inner join funcionario f on u.id = f.id";

        try (PreparedStatement stm = db.prepareStatement(sql);
             ResultSet rs = db.executeQuery(stm)) {

            while (rs != null && rs.next()) {
                Employee emp = (Employee) from(rs, "u", "f");
                if (emp != null) {
                    resultado.add(emp);
                }
            }
        }
        return resultado;
    }

    public void create(User u) throws Exception{
        String rol = (u instanceof  Administrator) ? "ADMIN" : "FUNCIONARIO";

        try{
            db.beginTransaction();
            String sqlUser = "insert into usuario (id, clave, rol) values (?,?,?)";
            PreparedStatement stmU = db.prepareStatement(sqlUser);
            stmU.setString(1, u.getId());
            stmU.setString(2, u.getPassword());
            stmU.setString(3, rol);
            int count = db.executeUpdate(stmU);
            if (count == 0){
                throw new Exception("El usuario ya existe");
            }

            if (u instanceof Employee){
                Employee emp = (Employee) u;
                String sqlFun = "insert into funcionario (id, nombre, telefono) values (?,?,?)";
                PreparedStatement stmF = db.prepareStatement(sqlFun);
                stmF.setString(1, emp.getId());
                stmF.setString(2, emp.getName());
                stmF.setString(3, emp.getPhone());
                int countF = db.executeUpdate(stmF);
                if (countF == 0){
                    throw new Exception("Error al intentar insertar los datos");
                }
            }
            db.commit();
        }catch (Exception ex){
            db.rollback();
            throw ex;
        }

    }

    public User read(String id) throws Exception{
        String sql = "select u.id, u.clave, u.rol, f.nombre, f.telefono " +
                "from usuario u " +
                "left join funcionario f on u.id = f.id " +
                "where u.id=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, id);
        ResultSet rs = db.executeQuery(stm);
        if (rs != null && rs.next()){
            return from(rs, "u", "f");
        } else {
            throw new Exception("El usuario no existe");
        }
    }

    public void update(User u) throws Exception {
        try {
            db.beginTransaction();

            String sqlUsuario = "update usuario set clave=? where id=?";
            PreparedStatement stmU = db.prepareStatement(sqlUsuario);
            stmU.setString(1, u.getPassword());
            stmU.setString(2, u.getId());
            int count = db.executeUpdate(stmU);
            if (count == 0){
                throw new Exception("El usuario no existe");
            }

            if (u instanceof Employee){
                Employee emp = (Employee) u;
                String sqlFun = "update funcionario set nombre=?, telefono=? where id=?";
                PreparedStatement stmF = db.prepareStatement(sqlFun);
                stmF.setString(1, emp.getName());
                stmF.setString(2, emp.getPhone());
                stmF.setString(3, emp.getId());
                int countF = db.executeUpdate(stmF);
                if (countF == 0){
                    throw new Exception("Error al actualizar los datos");
                }
            }
            db.commit();
        } catch (Exception ex){
            db.rollback();
            throw ex;
        }
    }

    public void delete(String id) throws Exception{
        try {
            db.beginTransaction();

            String sqlFuncionario = "delete from funcionario where id=?";
            PreparedStatement stmF = db.prepareStatement(sqlFuncionario);
            stmF.setString(1, id);
            db.executeUpdate(stmF);

            String sqlUsuario = "delete from usuario where id=?";
            PreparedStatement stmU = db.prepareStatement(sqlUsuario);
            stmU.setString(1, id);

            int count = db.executeUpdate(stmU);
            if (count == 0) {
                throw new Exception("Usuario no existe");
            }

            db.commit();
        } catch (SQLIntegrityConstraintViolationException ex){
            db.rollback();
            throw new Exception("No se puede eliminar, el funcionario tiene reservas registradas");
        }catch (Exception ex){
            db.rollback();
            throw ex;
        }
    }

    public List<Employee> findEmployeebyName(String nombre) throws Exception{
        List<Employee> resultado = new ArrayList<>();
            String sql = "select u.id, u.clave, u.rol, f.nombre, f.telefono " +
                    "from usuario u " +
                    "inner join funcionario f on u.id = f.id " +
                    "where f.nombre like ?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1,"%" + nombre + "%");
            ResultSet rs = db.executeQuery(stm);
            while (rs != null && rs.next()){
                resultado.add((Employee) from(rs,"u","f"));
            }
        return resultado;
    }

    public User from(ResultSet rs, String aliasU, String aliasF) throws SQLException {
        String rol = rs.getString(aliasU + ".rol");

        if ("ADMIN".equalsIgnoreCase(rol)) {
            Administrator admin = new Administrator();
            admin.setId(rs.getString(aliasU + ".id"));
            admin.setPassword(rs.getString(aliasU + ".clave"));
            return admin;
        } else {
            Employee emp = new Employee();
            emp.setId(rs.getString(aliasU + ".id"));
            emp.setPassword(rs.getString(aliasU + ".clave"));
            emp.setName(rs.getString(aliasF + ".nombre"));
            emp.setPhone(rs.getString(aliasF + ".telefono"));
            return emp;
        }
    }
}
