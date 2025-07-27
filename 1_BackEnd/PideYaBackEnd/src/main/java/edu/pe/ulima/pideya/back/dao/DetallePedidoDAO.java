package edu.pe.ulima.pideya.back.dao;

import edu.pe.ulima.pideya.back.model.DetallePedido;
import edu.pe.ulima.pideya.back.model.Pedido;
import edu.pe.ulima.pideya.back.model.Producto;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad DetallePedido. Implementa operaciones CRUD sobre la tabla
 * PideYa.DetallePedido, gestionando transacciones y mapeo a objeto DetallePedido.
 * Extiende de Conexion para gestion de conexion y transacciones.
 *
 * @author Henry Wong <hwong@ulima.edu.pe>
 */
public class DetallePedidoDAO extends Conexion<DetallePedido> {

    private static final String INSERT_SQL =
        "INSERT INTO PideYa.DetallePedido(idPedido, idProducto, precioUnitario, cantidad, subTotal) VALUES(?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL =
        "UPDATE PideYa.DetallePedido SET precioUnitario = ?, cantidad = ?, subTotal = ? " +
        "WHERE idPedido = ? AND idProducto = ?";
    private static final String DELETE_SQL =
        "DELETE FROM PideYa.DetallePedido WHERE idPedido = ? AND idProducto = ?";
    private static final String SELECT_ONE_SQL =
        "SELECT idPedido, idProducto, precioUnitario, cantidad, subTotal " +
        "FROM PideYa.DetallePedido WHERE idPedido = ? AND idProducto = ?";
    private static final String SELECT_ALL_SQL =
        "SELECT idPedido, idProducto, precioUnitario, cantidad, subTotal FROM PideYa.DetallePedido";
    private static final String SELECT_BY_PEDIDO_SQL =
        "SELECT idPedido, idProducto, precioUnitario, cantidad, subTotal " +
        "FROM PideYa.DetallePedido WHERE idPedido = ?";

    /**
     * Inserta un nuevo detalle de pedido en la base de datos.
     *
     * @param dp DetallePedido a insertar
     * @return DetallePedido con valores insertados
     * @throws SQLException en error SQL
     */
    @Override
    public DetallePedido insertar(DetallePedido dp) throws SQLException {
        try (PreparedStatement ps = getConnection().prepareStatement(
                INSERT_SQL)) {
            ps.setInt(1, dp.getPedido().getIdPedido());
            ps.setInt(2, dp.getProducto().getIdProducto());
            ps.setDouble(3, dp.getPrecioUnitario());
            ps.setDouble(4, dp.getCantidad());
            ps.setDouble(5, dp.getSubTotal());
            ps.executeUpdate();
            commit();
            return dp;
        } catch (SQLException ex) {
            rollback();
            throw ex;
        }
    }

    /**
     * Actualiza un detalle de pedido existente.
     *
     * @param dp DetallePedido con valores a actualizar
     * @return DetallePedido actualizado o null si no existia
     * @throws SQLException en error SQL
     */
    @Override
    public DetallePedido actualizar(DetallePedido dp) throws SQLException {
        try (PreparedStatement ps = getConnection().prepareStatement(
                UPDATE_SQL)) {
            ps.setDouble(1, dp.getPrecioUnitario());
            ps.setDouble(2, dp.getCantidad());
            ps.setDouble(3, dp.getSubTotal());
            ps.setInt(4, dp.getPedido().getIdPedido());
            ps.setInt(5, dp.getProducto().getIdProducto());
            int rows = ps.executeUpdate();
            commit();
            return rows > 0 ? dp : null;
        } catch (SQLException ex) {
            rollback();
            throw ex;
        }
    }

    /**
     * Elimina un detalle de pedido.
     *
     * @param dp DetallePedido con idPedido y idProducto a eliminar
     * @return DetallePedido eliminado o null si no existia
     * @throws SQLException en error SQL
     */
    @Override
    public DetallePedido eliminar(DetallePedido dp) throws SQLException {
        try (PreparedStatement ps = getConnection().prepareStatement(
                DELETE_SQL)) {
            ps.setInt(1, dp.getPedido().getIdPedido());
            ps.setInt(2, dp.getProducto().getIdProducto());
            int rows = ps.executeUpdate();
            commit();
            return rows > 0 ? dp : null;
        } catch (SQLException ex) {
            rollback();
            throw ex;
        }
    }

    /**
     * Obtiene un detalle de pedido segun sus claves.
     *
     * @param dp DetallePedido con idPedido y idProducto a buscar
     * @return DetallePedido encontrado o null si no existe
     * @throws SQLException en error SQL
     */
    @Override
    public DetallePedido obtener(DetallePedido dp) throws SQLException {
        try (PreparedStatement ps = getConnection().prepareStatement(
                SELECT_ONE_SQL)) {
            ps.setInt(1, dp.getPedido().getIdPedido());
            ps.setInt(2, dp.getProducto().getIdProducto());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Pedido ped = new Pedido();
                    ped.setIdPedido(rs.getInt("idPedido"));
                    Producto prod = new Producto();
                    prod.setIdProducto(rs.getInt("idProducto"));
                    return new DetallePedido(
                        ped,
                        prod,
                        rs.getDouble("precioUnitario"),
                        rs.getDouble("cantidad"),
                        rs.getDouble("subTotal")
                    );
                }
            }
        }
        return null;
    }

    /**
     * Lista todos los detalles de pedido.
     *
     * @return lista de DetallePedido
     * @throws SQLException en error SQL
     */
    @Override
    public List<DetallePedido> listar() throws SQLException {
        List<DetallePedido> lista = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(
                SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Pedido ped = new Pedido();
                ped.setIdPedido(rs.getInt("idPedido"));
                Producto prod = new Producto();
                prod.setIdProducto(rs.getInt("idProducto"));
                lista.add(new DetallePedido(
                    ped,
                    prod,
                    rs.getDouble("precioUnitario"),
                    rs.getDouble("cantidad"),
                    rs.getDouble("subTotal")
                ));
            }
        }
        return lista;
    }

    /**
     * Lista los detalles de un pedido especifico.
     *
     * @param idPedido id del pedido a filtrar
     * @return lista de DetallePedido
     * @throws SQLException en error SQL
     */
    public List<DetallePedido> listarPorPedido(Integer idPedido) throws SQLException {
        List<DetallePedido> lista = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(
                SELECT_BY_PEDIDO_SQL)) {
            ps.setInt(1, idPedido);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pedido ped = new Pedido();
                    ped.setIdPedido(idPedido);
                    Producto prod = new Producto();
                    prod.setIdProducto(rs.getInt("idProducto"));
                    prod = new ProductoDAO().obtener(prod);
                    lista.add(new DetallePedido(
                        ped,
                        prod,
                        rs.getDouble("precioUnitario"),
                        rs.getDouble("cantidad"),
                        rs.getDouble("subTotal")
                    ));
                }
            }
        }
        return lista;
    }
}
