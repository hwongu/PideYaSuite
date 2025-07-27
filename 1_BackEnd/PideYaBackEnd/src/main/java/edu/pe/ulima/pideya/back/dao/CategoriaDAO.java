package edu.pe.ulima.pideya.back.dao;

import edu.pe.ulima.pideya.back.model.Categoria;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Categoria. Provee operaciones CRUD sobre la tabla
 * PideYa.Categoria. Hereda de Conexion para manejo de conexion y transacciones.
 *
 * @author Henry Wong <hwong@ulima.edu.pe>
 */
public class CategoriaDAO extends Conexion<Categoria> {

    // Sentencias SQL parametrizadas
    private static final String INSERT_SQL
            = "INSERT INTO PideYa.Categoria(nombre) VALUES(?)";
    private static final String UPDATE_SQL
            = "UPDATE PideYa.Categoria SET nombre = ? WHERE idCategoria = ?";
    private static final String DELETE_SQL
            = "DELETE FROM PideYa.Categoria WHERE idCategoria = ?";
    private static final String SELECT_ONE_SQL
            = "SELECT idCategoria, nombre FROM PideYa.Categoria WHERE idCategoria = ?";
    private static final String SELECT_ALL_SQL
            = "SELECT idCategoria, nombre FROM PideYa.Categoria ORDER BY nombre";
    private static final String SELECT_BY_PREFIX_SQL
            = "SELECT idCategoria, nombre"
            + " FROM PideYa.Categoria"
            + " WHERE nombre LIKE ?"
            + " ORDER BY nombre";

    /**
     * Inserta una nueva categoria en la base de datos.
     *
     * 1. Prepara sentencia INSERT con parametros. 2. Ejecuta y obtiene la clave
     * generada. 3. Commit o rollback.
     *
     * @param c Categoria sin idCategoria
     * @return Categoria con idCategoria asignado
     * @throws SQLException en error SQL
     */
    @Override
    public Categoria insertar(Categoria c) throws SQLException {
        try (PreparedStatement ps = getConnection()
                .prepareStatement(INSERT_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getNombre().toUpperCase().trim());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    c.setIdCategoria(rs.getInt(1));
                }
            }
            commit();
            return c;
        } catch (SQLException ex) {
            rollback();
            throw ex;
        }
    }

    /**
     * Actualiza el nombre de una categoria existente.
     *
     * 1. Prepara sentencia UPDATE con nuevo nombre e idCategoria. 2. Ejecuta y
     * commit o rollback.
     *
     * @param c Categoria con idCategoria y nuevo nombre
     * @return Categoria actualizado o null si no existe
     * @throws SQLException en error SQL
     */
    @Override
    public Categoria actualizar(Categoria c) throws SQLException {
        try (PreparedStatement ps = getConnection().prepareStatement(UPDATE_SQL)) {
            ps.setString(1, c.getNombre().toUpperCase().trim());
            ps.setInt(2, c.getIdCategoria());
            int rows = ps.executeUpdate();
            commit();
            return (rows > 0) ? c : null;
        } catch (SQLException ex) {
            rollback();
            throw ex;
        }
    }

    /**
     * Elimina una categoria por su ID.
     *
     * 1. Prepara sentencia DELETE. 2. Ejecuta y commit o rollback.
     *
     * @param c Categoria con idCategoria a eliminar
     * @return Categoria eliminado o null si no existia
     * @throws SQLException en error SQL
     */
    @Override
    public Categoria eliminar(Categoria c) throws SQLException {
        try (PreparedStatement ps = getConnection().prepareStatement(DELETE_SQL)) {
            ps.setInt(1, c.getIdCategoria());
            int rows = ps.executeUpdate();
            commit();
            return (rows > 0) ? c : null;
        } catch (SQLException ex) {
            rollback();
            throw ex;
        }
    }

    /**
     * Obtiene una categoria por su ID.
     *
     * Realiza SELECT_ONE_SQL y mapea resultado a Categoria.
     *
     * @param c Categoria con idCategoria a buscar
     * @return Categoria con datos o null si no existe
     * @throws SQLException en error SQL
     */
    @Override
    public Categoria obtener(Categoria c) throws SQLException {
        try (PreparedStatement ps = getConnection().prepareStatement(SELECT_ONE_SQL)) {
            ps.setInt(1, c.getIdCategoria());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Categoria(
                            rs.getInt("idCategoria"),
                            rs.getString("nombre").toUpperCase().trim()
                    );
                }
            }
            return null;
        }
    }

    /**
     * Lista todas las categorias.
     *
     * Ejecuta SELECT_ALL_SQL y mapea cada fila a Categoria.
     *
     * @return lista de Categoria
     * @throws SQLException en error SQL
     */
    @Override
    public List<Categoria> listar() throws SQLException {
        List<Categoria> lista = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(SELECT_ALL_SQL); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Categoria(
                        rs.getInt("idCategoria"),
                        rs.getString("nombre").toUpperCase().trim()
                ));
            }
        }
        return lista;
    }

    /**
     * Lista categorias cuyo nombre comienza con el prefijo dado.
     *
     * 1. Prepara SELECT_BY_PREFIX_SQL con prefijo. 2. Ejecuta y mapea
     * resultados.
     *
     * @param nombrePrefijo prefijo para filtro LIKE (no null)
     * @return lista de Categoria
     * @throws SQLException en error SQL
     * @throws IllegalArgumentException si nombrePrefijo es null
     */
    public List<Categoria> listarPorNombre(String nombrePrefijo) throws SQLException {
        if (nombrePrefijo == null) {
            throw new IllegalArgumentException("El nombrePrefijo no puede ser null");
        }
        List<Categoria> resultados = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(SELECT_BY_PREFIX_SQL)) {
            ps.setString(1, nombrePrefijo + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultados.add(new Categoria(
                            rs.getInt("idCategoria"),
                            rs.getString("nombre").toUpperCase().trim()
                    ));
                }
            }
        }
        return resultados;
    }
}
