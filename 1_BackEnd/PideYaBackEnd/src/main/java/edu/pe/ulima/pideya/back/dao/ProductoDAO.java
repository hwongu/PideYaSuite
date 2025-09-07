package edu.pe.ulima.pideya.back.dao;

import edu.pe.ulima.pideya.back.model.Producto;
import edu.pe.ulima.pideya.back.model.Categoria;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Producto. Provee operaciones CRUD sobre la tabla
 * PideYa.Producto. Hereda de Conexion para manejo de conexion y transacciones.
 *
 * @author Henry Wong <hwong@ulima.edu.pe>
 */
public class ProductoDAO extends Conexion<Producto> {

    // Consultas SQL parametrizadas
    private static final String INSERT_SQL =
        "INSERT INTO PideYa.Producto(nombre, idCategoria, precio, stock) VALUES(?, ?, ?, ?)";
    private static final String UPDATE_SQL =
        "UPDATE PideYa.Producto SET nombre = ?, idCategoria = ?, precio = ?, stock = ? WHERE idProducto = ?";
    private static final String DELETE_SQL =
        "DELETE FROM PideYa.Producto WHERE idProducto = ?";
    private static final String SELECT_ONE_SQL =
        "SELECT idProducto, nombre, idCategoria, precio, stock FROM PideYa.Producto WHERE idProducto = ?";
    private static final String SELECT_ALL_SQL =
        "SELECT idProducto, nombre, idCategoria, precio, stock FROM PideYa.Producto";
    private static final String SELECT_BY_NOMBRE_SQL =
        "SELECT idProducto, nombre, idCategoria, precio, stock FROM PideYa.Producto WHERE nombre LIKE ?";
    private static final String SELECT_BY_CATEGORIA_SQL =
        "SELECT idProducto, nombre, idCategoria, precio, stock FROM PideYa.Producto WHERE idCategoria = ?";

    /**
     * Inserta un nuevo producto en la base de datos.
     *
     * 1. Prepara sentencia INSERT con parametros.
     * 2. Ejecuta y obtiene la clave generada.
     * 3. Commit o rollback.
     *
     * @param p Objeto Producto sin idProducto
     * @return Producto con idProducto asignado
     * @throws SQLException en caso de error SQL
     */
    @Override
    public Producto insertar(Producto p) throws SQLException {
        try (PreparedStatement ps = getConnection()
                .prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getNombre().toUpperCase().trim());
            ps.setInt(2, p.getCategoria().getIdCategoria());
            ps.setDouble(3, p.getPrecio());
            ps.setDouble(4, p.getStock());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setIdProducto(rs.getInt(1));
                }
            }
            commit();
            return p;
        } catch (SQLException ex) {
            rollback();
            throw ex;
        }
    }

    /**
     * Actualiza los datos de un producto existente.
     *
     * 1. Prepara sentencia UPDATE con nuevos valores.
     * 2. Ejecuta y commit o rollback.
     *
     * @param p Producto con idProducto y campos actualizados
     * @return mismo Producto si se actualizo, o null si no encontro el ID
     * @throws SQLException en caso de error SQL
     */
    @Override
    public Producto actualizar(Producto p) throws SQLException {
        try (PreparedStatement ps = getConnection().prepareStatement(UPDATE_SQL)) {
            ps.setString(1, p.getNombre().toUpperCase().trim());
            ps.setInt(2, p.getCategoria().getIdCategoria());
            ps.setDouble(3, p.getPrecio());
            ps.setDouble(4, p.getStock());
            ps.setInt(5, p.getIdProducto());
            int rows = ps.executeUpdate();
            commit();
            return (rows > 0) ? p : null;
        } catch (SQLException ex) {
            rollback();
            throw ex;
        }
    }

    /**
     * Elimina un producto por su ID.
     *
     * 1. Prepara sentencia DELETE.
     * 2. Ejecuta y commit o rollback.
     *
     * @param p Producto con idProducto establecido
     * @return mismo Producto si se elimino, o null si no existia
     * @throws SQLException en caso de error SQL
     */
    @Override
    public Producto eliminar(Producto p) throws SQLException {
        try (PreparedStatement ps = getConnection().prepareStatement(DELETE_SQL)) {
            ps.setInt(1, p.getIdProducto());
            int rows = ps.executeUpdate();
            commit();
            return (rows > 0) ? p : null;
        } catch (SQLException ex) {
            rollback();
            throw ex;
        }
    }

    /**
     * Obtiene un producto por su ID junto con su categoria.
     *
     * Realiza SELECT_ONE_SQL y utiliza CategoriaDAO para cargar la entidad.
     *
     * @param p Producto con idProducto para buscar
     * @return Producto completo o null si no existe
     * @throws SQLException en caso de error SQL
     */
    @Override
    public Producto obtener(Producto p) throws SQLException {
        try (PreparedStatement ps = getConnection().prepareStatement(SELECT_ONE_SQL)) {
            ps.setInt(1, p.getIdProducto());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Categoria catProto = new Categoria(rs.getInt("idCategoria"), null);
                    Categoria cat = new CategoriaDAO().obtener(catProto);
                    return new Producto(
                        rs.getInt("idProducto"),
                        rs.getString("nombre").toUpperCase().trim(),
                        rs.getDouble("precio"),
                        rs.getDouble("stock"),
                        cat
                    );
                }
            }
        }
        return null;
    }

    /**
     * Lista todos los productos.
     *
     * Ejecuta SELECT_ALL_SQL y mapea cada fila a Producto con su Categoria.
     *
     * @return lista de Productos
     * @throws SQLException en caso de error SQL
     */
    @Override
    public List<Producto> listar() throws SQLException {
        List<Producto> lista = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Categoria catProto = new Categoria(rs.getInt("idCategoria"), null);
                Categoria cat = new CategoriaDAO().obtener(catProto);
                lista.add(new Producto(
                    rs.getInt("idProducto"),
                    rs.getString("nombre").toUpperCase().trim(),
                    rs.getDouble("precio"),
                    rs.getDouble("stock"),
                    cat
                ));
            }
        }
        return lista;
    }

    /**
     * Lista productos cuyo nombre comienza con el prefijo dado.
     *
     * @param nombrePrefijo cadena de inicio para filtrar nombres (no puede ser null)
     * @return lista de productos cuyo nombre arranca con el prefijo
     * @throws SQLException en caso de error SQL
     * @throws IllegalArgumentException si nombrePrefijo es null
     */
    public List<Producto> listarPorNombre(String nombrePrefijo) throws SQLException {
        if (nombrePrefijo == null) {
            throw new IllegalArgumentException("El nombrePrefijo no puede ser null");
        }
        List<Producto> resultados = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(SELECT_BY_NOMBRE_SQL)) {
            ps.setString(1, nombrePrefijo + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Categoria catProto = new Categoria(rs.getInt("idCategoria"), null);
                    Categoria cat = new CategoriaDAO().obtener(catProto);
                    resultados.add(new Producto(
                        rs.getInt("idProducto"),
                        rs.getString("nombre").toUpperCase().trim(),
                        rs.getDouble("precio"),
                        rs.getDouble("stock"),
                        cat
                    ));
                }
            }
        }
        return resultados;
    }

    /**
     * Lista productos que pertenecen a la categoria indicada.
     *
     * @param idCategoria ID de la categoria para filtrar (no puede ser null)
     * @return lista de productos en la categoria
     * @throws SQLException en caso de error SQL
     * @throws IllegalArgumentException si idCategoria es null
     */
    public List<Producto> listarPorCategoria(Integer idCategoria) throws SQLException {
        if (idCategoria == null) {
            throw new IllegalArgumentException("El idCategoria no puede ser null");
        }
        List<Producto> resultados = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(SELECT_BY_CATEGORIA_SQL)) {
            ps.setInt(1, idCategoria);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Categoria catProto = new Categoria(idCategoria, null);
                    Categoria cat = new CategoriaDAO().obtener(catProto);
                    resultados.add(new Producto(
                        rs.getInt("idProducto"),
                        rs.getString("nombre").toUpperCase().trim(),
                        rs.getDouble("precio"),
                        rs.getDouble("stock"),
                        cat
                    ));
                }
            }
        }
        return resultados;
    }
}
