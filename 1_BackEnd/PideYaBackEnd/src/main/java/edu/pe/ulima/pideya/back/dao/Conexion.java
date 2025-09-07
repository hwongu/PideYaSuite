package edu.pe.ulima.pideya.back.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import edu.pe.ulima.pideya.back.util.Utilitario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase abstracta que provee operaciones básicas de conexión y manejo de
 * transacciones para DAOs específicos. Utiliza try-with-resources para el
 * manejo automatizado de recursos y SLF4J para registro de eventos.
 *
 * @param <E> Tipo de entidad manejada por el DAO
 * @author Henry Wong <hwong@ulima.edu.pe>
 */
public abstract class Conexion<E> implements AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(Conexion.class);
    private Connection connection;

    /**
     * Inicializa la conexión y desactiva el auto-commit para el control manual
     * de transacciones.
     */
    protected Conexion() {
        try {
            this.connection = crearConexion();
            this.connection.setAutoCommit(false);
            this.connection.setTransactionIsolation(
                    Connection.TRANSACTION_READ_COMMITTED
            );
        } catch (ClassNotFoundException | SQLException ex) {
            logger.error("Error al inicializar la conexión", ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     * Crea una nueva conexión usando propiedades definidas en utilitario.
     *
     * @return nueva instancia de Connection
     * @throws ClassNotFoundException cuando el driver JDBC no se encuentra
     * @throws SQLException cuando falla la conexión
     */
    private Connection crearConexion() throws ClassNotFoundException, SQLException {
        Class.forName(Utilitario.obtenerPropiedad("conexion.jdbc"));
        String url = Utilitario.obtenerPropiedad("conexion.url");
        String user = Utilitario.obtenerPropiedad("conexion.usuario");
        String password = Utilitario.obtenerPropiedad("conexion.clave");
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Obtiene la conexión activa.
     *
     * @return Connection
     */
    protected Connection getConnection() {
        return this.connection;
    }

    // Métodos CRUD mediante abstracción
    public abstract E insertar(E e) throws SQLException;

    public abstract E actualizar(E e) throws SQLException;

    public abstract E eliminar(E e) throws SQLException;

    public abstract E obtener(E e) throws SQLException;

    public abstract List<E> listar() throws SQLException;

    /**
     * Realiza commit de la transacción actual.
     *
     * @throws SQLException si ocurre un error durante el commit
     */
    public void commit() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.commit();
        }
    }

    /**
     * Revierte la transacción actual y registra errores si ocurren.
     */
    public void rollback() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.rollback();
            }
        } catch (SQLException ex) {
            logger.error("Error durante rollback", ex);
        }
    }

    /**
     * Cierra automáticamente la conexión al finalizar el try-with-resources o
     * close manual.
     */
    @Override
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                logger.error("Error cerrando la conexión", ex);
            }
        }
    }

    /**
     * Cierra recursos de ResultSet y PreparedStatement pasados.
     *
     * @param rs ResultSet a cerrar
     * @param ps PreparedStatement a cerrar
     */
    protected void closeResources(ResultSet rs, PreparedStatement ps) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
                logger.warn("Error cerrando ResultSet", ex);
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException ex) {
                logger.warn("Error cerrando PreparedStatement", ex);
            }
        }
    }
}
