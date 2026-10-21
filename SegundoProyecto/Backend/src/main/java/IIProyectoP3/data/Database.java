package IIProyectoP3.data;


import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class Database {
    private static Database theInstance;
    public static final String PROPERTIES_FILE_NAME = "/reservas/database.properties";
    Connection cnx;

    public static synchronized Database instance(){
        if (theInstance==null){
            theInstance=new Database();
        }
        return theInstance;
    }

    private Database(){
        //  this.cnx=this.getConnection();
    }

    public Connection getConnection() throws SQLException {
        if (cnx != null && !cnx.isClosed()){
            return cnx;
        }

        try {
            Properties prop = new Properties();
            InputStream is = getClass().getResourceAsStream(PROPERTIES_FILE_NAME);

            if (is == null) {
                throw new Exception("No se encontró el archivo: " + PROPERTIES_FILE_NAME);
            }
            prop.load(is);

            String driver = prop.getProperty("database_driver");
            String server = prop.getProperty("database_server");
            String port = prop.getProperty("database_port");
            String user = prop.getProperty("database_user");
            String password = prop.getProperty("database_password");
            String database = prop.getProperty("database_name");

            String url = "jdbc:mysql://" + server + ":" + port + "/" + database +
                    "?user=" + user + "&password=" + password + "&serverTimezone=UTC";

            Class.forName(driver);
            cnx = DriverManager.getConnection(url);
            return cnx;

        } catch (SQLException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new SQLException("Error de conexión a la Base de Datos: " + ex.getMessage());
        }
    }

    public PreparedStatement prepareStatement(String statement) throws SQLException {
        return getConnection().prepareStatement(statement);
    }

    public int executeUpdate(PreparedStatement statement) throws SQLException {
        return statement.executeUpdate();
    }

    public ResultSet executeQuery(PreparedStatement statement) throws SQLException{
        return statement.executeQuery();
    }

    public void beginTransaction() throws SQLException {
        getConnection().setAutoCommit(false);
    }

    public void commit() throws SQLException {
        getConnection().commit();
        getConnection().setAutoCommit(true);
    }

    public void rollback() {
        try {
            if (cnx != null && !cnx.getAutoCommit()) {
                cnx.rollback();
                cnx.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            System.err.println("Error al realizar rollback: " + ex.getMessage());
        }
    }

    public void close() throws Exception{
        if (cnx!=null && !cnx.isClosed()){
            cnx.close();
        }
    }
}

