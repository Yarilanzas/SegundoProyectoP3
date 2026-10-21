package IIProyectoP3.data;

import domain.Employee;
import domain.Reservation;
import domain.Resource;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
    Database db;
    UserDAO userDAO;
    ResourceDAO resourceDAO;

    public ReservationDAO(){
        db = Database.instance();
        userDAO = new UserDAO();
        resourceDAO = new ResourceDAO();
    }

    private Time parseTime(String timeStr) {
        if (timeStr == null || timeStr.trim().isEmpty()) return null;
        timeStr = timeStr.trim();
        if (timeStr.length() == 5) {
            timeStr += ":00";
        }
        return Time.valueOf(timeStr);
    }

    public void create(Reservation r) throws Exception {
       try {
           db.beginTransaction();

           String sqlReserva = "insert into reserva (id, actividad,funcionario_id, fecha, hora_inicio, hora_fin, estado) " +
                   "values (?,?,?,?,?,?,?)";
           PreparedStatement stm = db.prepareStatement(sqlReserva);
           stm.setString(1,r.getId());
           stm.setString(2,r.getActivity());
           stm.setString(3,r.getEmployee().getId());
           stm.setDate(4,Date.valueOf(r.getDate()));
           stm.setTime(5, parseTime(r.getStartTime()));
           stm.setTime(6, parseTime(r.getEndTime()));
           stm.setString(7,r.getStatus() != null ? r.getStatus() : "ACTIVA");

           int count = db.executeUpdate(stm);
           if (count == 0){
               throw new Exception("La reserva ya existe o no se pudo realizar");
           }

           if (r.getResources() != null && !r.getResources().isEmpty()){
               String sqlRecurso = "insert into reserva_recurso (reserva_id, recurso_id) values (?, ?)";
               for (Resource rec : r.getResources()) {
                   PreparedStatement stmRec = db.prepareStatement(sqlRecurso);
                   stmRec.setString(1, r.getId());
                   stmRec.setString(2, rec.getId());
                   db.executeUpdate(stmRec);
               }
           }
           db.commit();
       } catch(Exception ex){
           db.rollback();
           throw ex;
       }
    }

    public Reservation read(String id) throws Exception{
        String sql = "select r.id, r.actividad, r.fecha, r.hora_inicio, r.hora_fin, r.estado, " +
                "u.id, u.clave, u.rol, f.nombre, f.telefono " +
                "from reserva r " +
                "inner join funcionario f on r.funcionario_id = f.id " +
                "inner join usuario u on f.id = u.id " +
                "where r.id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1,id);
        ResultSet rs =db.executeQuery(stm);

        if (rs != null && rs.next()){
            Reservation reservation = from(rs,"r", "u","f");
            reservation.setResources(readResourcesForReserva(id));
            return reservation;
        } else {
            throw new Exception("La reserva no existe");
        }
    }

    public void update(Reservation r) throws Exception {
        try {
            db.beginTransaction();

            String sqlReserva = "update reserva set actividad = ?, funcionario_id = ?, fecha = ?, " +
                    "hora_inicio = ?, hora_fin = ?, estado = ? where id = ?";
            PreparedStatement stm = db.prepareStatement(sqlReserva);
            stm.setString(1, r.getActivity());
            stm.setString(2, r.getEmployee().getId());
            stm.setDate(3, Date.valueOf(r.getDate()));
            stm.setTime(4, parseTime(r.getStartTime()));
            stm.setTime(5, parseTime(r.getEndTime()));
            stm.setString(6, r.getStatus());
            stm.setString(7, r.getId());

            int count = db.executeUpdate(stm);
            if (count == 0) {
                throw new Exception("La reserva no existe");
            }

            // Elimina relaciones anteriores y las sustituye por la nueva lista
            String sqlDeleteRel = "delete from reserva_recurso where reserva_id = ?";
            PreparedStatement stmDel = db.prepareStatement(sqlDeleteRel);
            stmDel.setString(1, r.getId());
            db.executeUpdate(stmDel);

            if (r.getResources() != null && !r.getResources().isEmpty()) {
                String sqlInsertRel = "insert into reserva_recurso (reserva_id, recurso_id) values (?, ?)";
                for (Resource rec : r.getResources()) {
                    PreparedStatement stmRec = db.prepareStatement(sqlInsertRel);
                    stmRec.setString(1, r.getId());
                    stmRec.setString(2, rec.getId());
                    db.executeUpdate(stmRec);
                }
            }

            db.commit();
        } catch (Exception ex) {
            db.rollback();
            throw ex;
        }
    }

    public void delete(String id) throws Exception{
        try {
            db.beginTransaction();

            String sqlRel = "delete from reserva_recurso where reserva_id = ?";
            PreparedStatement stmRel = db.prepareStatement(sqlRel);
            stmRel.setString(1,id);
            db.executeUpdate(stmRel);

            String sql = "delete from reserva where id=?";
            PreparedStatement stm = db.prepareStatement(sql);
            stm.setString(1,id);

            int count = db.executeUpdate(stm);
            if (count == 0){
                throw new Exception("La reserva no existe");
            }
            db.commit();
        } catch (Exception e) {
            db.rollback();
            throw e;
        }
    }

    public List<Reservation> findbyEmployee (String employeeId) throws Exception{
        List<Reservation> resultado = new ArrayList<>();
        String sql = "select r.id, r.actividad, r.fecha, r.hora_inicio, r.hora_fin, r.estado, " +
                "u.id, u.clave, u.rol, f.nombre, f.telefono " +
                "from reserva r " +
                "inner join funcionario f on r.funcionario_id = f.id " +
                "inner join usuario u on f.id = u.id " +
                "where f.id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1,employeeId);
        ResultSet rs = db.executeQuery(stm);

        while (rs.next()){
            Reservation res = from(rs,"r","u","f");
            res.setResources(readResourcesForReserva(res.getId()));
            resultado.add(res);
        }
        return resultado;
    }

    public List<Resource> readResourcesForReserva(String reservaId) throws Exception{
        List<Resource> resources = new ArrayList<>();
        String sql = "select rec.id, rec.descripcion, c.id, c.descripcion " +
                "from reserva_recurso rr " +
                "inner join recurso rec on rr.recurso_id = rec.id " +
                "inner join categoria c on rec.categoria_id = c.id " +
                "where rr.reserva_id = ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1,reservaId);
        ResultSet rs = db.executeQuery(stm);

        while (rs.next()){
            resources.add(resourceDAO.from(rs,"rec","c"));
        }
        return resources;
    }

    public Reservation from(ResultSet rs, String aliasR, String aliasU, String aliasF) throws SQLException {
        Reservation r = new Reservation();
        r.setId(rs.getString(aliasR + ".id"));
        r.setActivity(rs.getString(aliasR + ".actividad"));

        if (rs.getDate(aliasR + ".fecha") != null) {
            r.setDate(rs.getDate(aliasR + ".fecha").toLocalDate());
        }

        r.setStartTime(rs.getString(aliasR + ".hora_inicio").substring(0,5));
        r.setEndTime(rs.getString(aliasR + ".hora_fin").substring(0,5));
        r.setStatus(rs.getString(aliasR + ".estado"));
        r.setEmployee((Employee) userDAO.from(rs, aliasU, aliasF));

        return r;
    }

    public List<Reservation> findAll() throws Exception{
        List<Reservation> resultado = new ArrayList<>();
        String sql = "select r.id, r.actividad, r.fecha, r.hora_inicio, r.hora_fin, r.estado, " +
                "u.id, u.clave, u.rol, f.nombre, f.telefono " +
                "from reserva r " +
                "inner join funcionario f on r.funcionario_id = f.id " +
                "inner join usuario u on f.id = u.id";
        PreparedStatement stm = db.prepareStatement(sql);
        ResultSet rs = db.executeQuery(stm);

        while (rs.next()){
            Reservation res = from(rs,"r","u","f");
            res.setResources(readResourcesForReserva(res.getId()));
            resultado.add(res);
        }
        return resultado;
    }
}
