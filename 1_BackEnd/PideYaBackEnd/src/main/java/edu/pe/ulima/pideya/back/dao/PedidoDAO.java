package edu.pe.ulima.pideya.back.dao;

import edu.pe.ulima.pideya.back.model.Pedido;
import edu.pe.ulima.pideya.back.model.Cliente;
import edu.pe.ulima.pideya.back.model.ClienteEmpresa;
import edu.pe.ulima.pideya.back.model.ClienteNatural;
import edu.pe.ulima.pideya.back.model.DetallePedido;
import edu.pe.ulima.pideya.back.model.Producto;
import edu.pe.ulima.pideya.back.types.TipoUsuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO para la entidad Pedido. Implementa operaciones CRUD sobre la tabla
 * PideYa.Pedido y DetallePedido, gestionando transacciones y mapeo a objeto
 * Pedido.
 *
 * @author Henry Wong <hwong@ulima.edu.pe>
 */
public class PedidoDAO extends Conexion<Pedido> {

    // Sentencias SQL parametrizadas para Pedido y DetallePedido
    private static final String INSERT_PEDIDO_SQL
            = "INSERT INTO PideYa.Pedido(idCliente, fechaPedido, montoTotal) VALUES(?, ?, ?)";
    private static final String INSERT_DETALLE_SQL
            = "INSERT INTO PideYa.DetallePedido(idPedido, idProducto, precioUnitario, cantidad, subTotal) "
            + "VALUES(?, ?, ?, ?, ?)";
    private static final String UPDATE_STOCK_SQL
            = "UPDATE PideYa.Producto SET stock = stock - ? WHERE idProducto = ?";
    private static final String UPDATE_SQL
            = "UPDATE PideYa.Pedido SET fechaPedido = ?, montoTotal = ? WHERE idPedido = ?";
    private static final String SELECT_ONE_SQL
            = "SELECT idPedido, idCliente, fechaPedido, montoTotal FROM PideYa.Pedido WHERE idPedido = ?";
    private static final String SELECT_ALL_SQL
            = "SELECT idPedido, idCliente, fechaPedido, montoTotal FROM PideYa.Pedido";
    private static final String SELECT_DETALLES_PEDIDO_SQL
            = "SELECT idProducto, cantidad FROM PideYa.DetallePedido WHERE idPedido = ?";
    private static final String UPDATE_RESTORE_STOCK_SQL
            = "UPDATE PideYa.Producto SET stock = stock + ? WHERE idProducto = ?";
    private static final String DELETE_DETALLES_PEDIDO_SQL
            = "DELETE FROM PideYa.DetallePedido WHERE idPedido = ?";
    private static final String DELETE_PEDIDO_SQL
            = "DELETE FROM PideYa.Pedido WHERE idPedido = ?";
    private static final String SELECT_SIN_COMPRA_SQL
            = "SELECT "
            + "  p.idPedido, "
            + "  p.idCliente, "
            + "  p.fechaPedido, "
            + "  p.montoTotal, "
            + "  CASE "
            + "    WHEN cn.idCliente IS NOT NULL THEN 'CLIENTE_NATURAL' "
            + "    WHEN ce.idCliente IS NOT NULL THEN 'CLIENTE_EMPRESA' "
            + "    ELSE 'DESCONOCIDO' "
            + "  END AS tipoCliente, "
            + "  pr.idProducto, "
            + "  dp.precioUnitario, "
            + "  dp.cantidad, "
            + "  dp.subTotal "
            + "FROM PideYa.Pedido p "
            + "JOIN PideYa.DetallePedido dp ON p.idPedido = dp.idPedido "
            + "JOIN PideYa.Producto pr ON dp.idProducto = pr.idProducto "
            + "LEFT JOIN PideYa.ClienteNatural cn ON p.idCliente = cn.idCliente "
            + "LEFT JOIN PideYa.ClienteEmpresa ce ON p.idCliente = ce.idCliente "
            + "LEFT JOIN PideYa.Compra c ON p.idPedido = c.idPedido "
            + "WHERE c.idPedido IS NULL "
            + "ORDER BY p.idPedido, pr.idProducto";
    private static final String SELECT_SIN_COMPRA_SQL_POR_ID_CLIENTE
            = "SELECT "
            + "  p.idPedido, "
            + "  p.idCliente, "
            + "  p.fechaPedido, "
            + "  p.montoTotal, "
            + "  CASE "
            + "    WHEN cn.idCliente IS NOT NULL THEN 'CLIENTE_NATURAL' "
            + "    WHEN ce.idCliente IS NOT NULL THEN 'CLIENTE_EMPRESA' "
            + "    ELSE 'DESCONOCIDO' "
            + "  END AS tipoCliente, "
            + "  pr.idProducto, "
            + "  dp.precioUnitario, "
            + "  dp.cantidad, "
            + "  dp.subTotal "
            + "FROM PideYa.Pedido p "
            + "JOIN PideYa.DetallePedido dp ON p.idPedido = dp.idPedido "
            + "JOIN PideYa.Producto pr ON dp.idProducto = pr.idProducto "
            + "LEFT JOIN PideYa.ClienteNatural cn ON p.idCliente = cn.idCliente "
            + "LEFT JOIN PideYa.ClienteEmpresa ce ON p.idCliente = ce.idCliente "
            + "LEFT JOIN PideYa.Compra c ON p.idPedido = c.idPedido "
            + "WHERE c.idPedido IS NULL and  p.idCliente = ? "
            + "ORDER BY p.idPedido, pr.idProducto";

    private static final String SELECT_PEDIDOS_SIN_COMPRA_SQL
            = "SELECT "
            + "  p.idPedido, "
            + "  p.idCliente, "
            + "  p.fechaPedido, "
            + "  p.montoTotal, "
            + "  CASE "
            + "    WHEN cn.idCliente IS NOT NULL THEN 'CLIENTE_NATURAL' "
            + "    WHEN ce.idCliente IS NOT NULL THEN 'CLIENTE_EMPRESA' "
            + "    ELSE 'DESCONOCIDO' "
            + "  END AS tipoCliente "
            + "FROM PideYa.Pedido p "
            + "LEFT JOIN PideYa.ClienteNatural cn ON p.idCliente = cn.idCliente "
            + "LEFT JOIN PideYa.ClienteEmpresa ce ON p.idCliente = ce.idCliente "
            + "LEFT JOIN PideYa.Compra c ON p.idPedido = c.idPedido "
            + "WHERE c.idPedido IS NULL "
            + "ORDER BY p.idPedido";

    private static final String SELECT_PEDIDOS_SIN_COMPRA_SQL_POR_ID_CLIENTE
            = "SELECT "
            + "  p.idPedido, "
            + "  p.idCliente, "
            + "  p.fechaPedido, "
            + "  p.montoTotal, "
            + "  CASE "
            + "    WHEN cn.idCliente IS NOT NULL THEN 'CLIENTE_NATURAL' "
            + "    WHEN ce.idCliente IS NOT NULL THEN 'CLIENTE_EMPRESA' "
            + "    ELSE 'DESCONOCIDO' "
            + "  END AS tipoCliente "
            + "FROM PideYa.Pedido p "
            + "LEFT JOIN PideYa.ClienteNatural cn ON p.idCliente = cn.idCliente "
            + "LEFT JOIN PideYa.ClienteEmpresa ce ON p.idCliente = ce.idCliente "
            + "LEFT JOIN PideYa.Compra c ON p.idPedido = c.idPedido "
            + "WHERE c.idPedido IS NULL AND p.idCliente = ? "
            + "ORDER BY p.idPedido";

    /**
     * Inserta un nuevo pedido y sus detalles en la base de datos.
     *
     * 1. Prepara e inserta en Pedido y obtiene idPedido. 2. Inserta cada
     * DetallePedido con precioUnitario. 3. Actualiza stock de cada producto. 4.
     * Commit al final o rollback si hay error.
     *
     * @param p Pedido sin idPedido
     * @return Pedido con idPedido asignado
     * @throws SQLException en error SQL
     */
    @Override
    public Pedido insertar(Pedido p) throws SQLException {
        try {
            // Paso 1: insertar en Pedido
            try (PreparedStatement ps = getConnection()
                    .prepareStatement(INSERT_PEDIDO_SQL, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, p.getCliente().getIdCliente());
                ps.setTimestamp(2, Timestamp.valueOf(p.getFechaPedido()));
                ps.setDouble(3, p.getMontoTotal());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        p.setIdPedido(rs.getInt(1));
                    }
                }
            }

            // Paso 2 y 3: insertar detalles y ajustar stock
            for (DetallePedido dp : p.getDetallePedido()) {
                try (PreparedStatement psDet = getConnection()
                        .prepareStatement(INSERT_DETALLE_SQL)) {
                    psDet.setInt(1, p.getIdPedido());
                    psDet.setInt(2, dp.getProducto().getIdProducto());
                    psDet.setDouble(3, dp.getPrecioUnitario());
                    psDet.setDouble(4, dp.getCantidad());
                    psDet.setDouble(5, dp.getSubTotal());
                    psDet.executeUpdate();
                }
                try (PreparedStatement psStock = getConnection()
                        .prepareStatement(UPDATE_STOCK_SQL)) {
                    psStock.setDouble(1, dp.getCantidad());
                    psStock.setInt(2, dp.getProducto().getIdProducto());
                    psStock.executeUpdate();
                }
            }

            // Paso 4: confirmar transaccion
            commit();
            return p;

        } catch (SQLException ex) {
            // rollback ante cualquier fallo
            rollback();
            throw ex;
        }
    }

    /**
     * Actualiza un pedido existente.
     *
     * 1. Prepara UPDATE con fechaPedido y montoTotal. 2. Ejecuta y commit o
     * rollback.
     *
     * @param p Pedido con idPedido y campos modificados
     * @return Pedido actualizado o null si no existe
     * @throws SQLException en error SQL
     */
    @Override
    public Pedido actualizar(Pedido p) throws SQLException {
        try (PreparedStatement ps = getConnection().prepareStatement(UPDATE_SQL)) {
            ps.setTimestamp(1, Timestamp.valueOf(p.getFechaPedido()));
            ps.setDouble(2, p.getMontoTotal());
            ps.setInt(3, p.getIdPedido());
            int rows = ps.executeUpdate();
            commit();
            return rows > 0 ? p : null;
        } catch (SQLException ex) {
            rollback();
            throw ex;
        }
    }

    /**
     * Elimina un pedido y restaura el stock de sus detalles.
     *
     * 1. Recupera los DetallePedido del pedido. 2. Para cada detalle,
     * incrementa el stock del producto. 3. Elimina los registros de
     * DetallePedido. 4. Elimina el registro de Pedido. 5. Commit si todo fue
     * exitoso, o rollback en caso de error.
     *
     * @param p Pedido con idPedido a eliminar
     * @return Pedido eliminado o null si no existia
     * @throws SQLException en error SQL
     */
    @Override
    public Pedido eliminar(Pedido p) throws SQLException {
        try {
            // Paso 1: recuperar detalles del pedido
            try (PreparedStatement psDet = getConnection()
                    .prepareStatement(SELECT_DETALLES_PEDIDO_SQL)) {
                psDet.setInt(1, p.getIdPedido());
                try (ResultSet rs = psDet.executeQuery()) {
                    while (rs.next()) {
                        int idProd = rs.getInt("idProducto");
                        double cantidad = rs.getDouble("cantidad");
                        // Paso 2: restaurar stock
                        try (PreparedStatement psStock = getConnection()
                                .prepareStatement(UPDATE_RESTORE_STOCK_SQL)) {
                            psStock.setDouble(1, cantidad);
                            psStock.setInt(2, idProd);
                            psStock.executeUpdate();
                        }
                    }
                }
            }

            // Paso 3: eliminar detalles del pedido
            try (PreparedStatement psDelDet = getConnection()
                    .prepareStatement(DELETE_DETALLES_PEDIDO_SQL)) {
                psDelDet.setInt(1, p.getIdPedido());
                psDelDet.executeUpdate();
            }

            // Paso 4: eliminar el pedido
            try (PreparedStatement psDelPed = getConnection()
                    .prepareStatement(DELETE_PEDIDO_SQL)) {
                psDelPed.setInt(1, p.getIdPedido());
                int rows = psDelPed.executeUpdate();
                if (rows > 0) {
                    // Paso 5: confirmar transaccion
                    commit();
                    return p;
                } else {
                    rollback();
                    return null;
                }
            }

        } catch (SQLException ex) {
            // rollback ante cualquier fallo
            rollback();
            throw ex;
        }
    }

    /**
     * Obtiene un pedido por su ID.
     *
     * Realiza SELECT_ONE_SQL y mapea resultados a objeto Pedido.
     *
     * @param p Pedido con idPedido a buscar
     * @return Pedido completo o null si no existe
     * @throws SQLException en error SQL
     */
    @Override
    public Pedido obtener(Pedido p) throws SQLException {
        try (PreparedStatement ps = getConnection().prepareStatement(SELECT_ONE_SQL)) {
            ps.setInt(1, p.getIdPedido());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cliente c = new Cliente() {
                    };
                    c.setIdCliente(rs.getInt("idCliente"));
                    return new Pedido(
                            rs.getInt("idPedido"),
                            rs.getTimestamp("fechaPedido").toLocalDateTime(),
                            rs.getDouble("montoTotal"),
                            c
                    );
                }
            }
        }
        return null;
    }

    /**
     * Lista todos los pedidos.
     *
     * Ejecuta SELECT_ALL_SQL y mapea cada fila a Pedido.
     *
     * @return lista de pedidos
     * @throws SQLException en error SQL
     */
    @Override
    public List<Pedido> listar() throws SQLException {
        List<Pedido> lista = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(SELECT_ALL_SQL); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Cliente c = new Cliente() {
                };
                c.setIdCliente(rs.getInt("idCliente"));
                Pedido p = new Pedido(
                        rs.getInt("idPedido"),
                        rs.getTimestamp("fechaPedido").toLocalDateTime(),
                        rs.getDouble("montoTotal"),
                        c
                );
                lista.add(p);
            }
        }
        return lista;
    }

    /**
     * Lista todos los pedidos que no tienen compra asociada, con sus detalles,
     * incluyendo Producto y Cliente (natural o empresa).
     *
     * 1. Ejecuta la consulta con LEFT JOIN a Compra y filtra WHERE c.idPedido
     * IS NULL. 2. Para cada fila, crea o recupera el objeto Pedido y agrega un
     * DetallePedido. 3. Utiliza los DAOs de ClienteNatural, ClienteEmpresa y
     * Producto para obtener objetos completos.
     *
     * @return lista de Pedido con DetallePedido cargados
     * @throws SQLException en error SQL
     */
    public List<Pedido> listarPedidosConDetalles() throws SQLException {
        Map<Integer, Pedido> cache = new HashMap<>();
        List<Pedido> resultados = new ArrayList<>();

        try (PreparedStatement ps = getConnection().prepareStatement(SELECT_SIN_COMPRA_SQL); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int pedidoId = rs.getInt("idPedido");
                Pedido pedido = cache.get(pedidoId);
                if (pedido == null) {
                    // Construir Cliente segun tipoCliente
                    TipoUsuario tipoUsuario = TipoUsuario.valueOf(rs.getString("tipoCliente"));
                    Cliente clienteObj = null;
                    if (tipoUsuario == TipoUsuario.CLIENTE_NATURAL) {
                        ClienteNatural cn = new ClienteNatural();
                        cn.setIdCliente(rs.getInt("idCliente"));
                        clienteObj = new ClienteNaturalDAO().obtener(cn);
                    } else if (tipoUsuario == TipoUsuario.CLIENTE_EMPRESA) {
                        ClienteEmpresa ce = new ClienteEmpresa();
                        ce.setIdCliente(rs.getInt("idCliente"));
                        clienteObj = new ClienteEmpresaDAO().obtener(ce);
                    }
                    // Crear nuevo Pedido
                    pedido = new Pedido(
                            pedidoId,
                            rs.getTimestamp("fechaPedido").toLocalDateTime(),
                            rs.getDouble("montoTotal"),
                            clienteObj
                    );
                    pedido.setDetallePedido(new ArrayList<>());
                    cache.put(pedidoId, pedido);
                    resultados.add(pedido);
                }

                // Obtener Producto completo
                Producto prodStub = new Producto();
                prodStub.setIdProducto(rs.getInt("idProducto"));
                Producto producto = new ProductoDAO().obtener(prodStub);

                // Crear DetallePedido y anadirlo
                DetallePedido dp = new DetallePedido(
                        pedido,
                        producto,
                        rs.getDouble("precioUnitario"),
                        rs.getDouble("cantidad"),
                        rs.getDouble("subTotal")
                );
                pedido.getDetallePedido().add(dp);
            }
        }

        return resultados;
    }

    public List<Pedido> listarPedidosConDetallesPorIdCliente(Integer idCliente) throws SQLException {
        Map<Integer, Pedido> cache = new HashMap<>();
        List<Pedido> resultados = new ArrayList<>();

        try (PreparedStatement ps = getConnection().prepareStatement(SELECT_SIN_COMPRA_SQL_POR_ID_CLIENTE)) {
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int pedidoId = rs.getInt("idPedido");
                Pedido pedido = cache.get(pedidoId);
                if (pedido == null) {
                    // Construir Cliente segun tipoCliente
                    TipoUsuario tipoUsuario = TipoUsuario.valueOf(rs.getString("tipoCliente"));
                    Cliente clienteObj = null;
                    if (tipoUsuario == TipoUsuario.CLIENTE_NATURAL) {
                        ClienteNatural cn = new ClienteNatural();
                        cn.setIdCliente(rs.getInt("idCliente"));
                        clienteObj = new ClienteNaturalDAO().obtener(cn);
                    } else if (tipoUsuario == TipoUsuario.CLIENTE_EMPRESA) {
                        ClienteEmpresa ce = new ClienteEmpresa();
                        ce.setIdCliente(rs.getInt("idCliente"));
                        clienteObj = new ClienteEmpresaDAO().obtener(ce);
                    }
                    // Crear nuevo Pedido
                    pedido = new Pedido(
                            pedidoId,
                            rs.getTimestamp("fechaPedido").toLocalDateTime(),
                            rs.getDouble("montoTotal"),
                            clienteObj
                    );
                    pedido.setDetallePedido(new ArrayList<>());
                    cache.put(pedidoId, pedido);
                    resultados.add(pedido);
                }

                // Obtener Producto completo
                Producto prodStub = new Producto();
                prodStub.setIdProducto(rs.getInt("idProducto"));
                Producto producto = new ProductoDAO().obtener(prodStub);

                // Crear DetallePedido y anadirlo
                DetallePedido dp = new DetallePedido(
                        pedido,
                        producto,
                        rs.getDouble("precioUnitario"),
                        rs.getDouble("cantidad"),
                        rs.getDouble("subTotal")
                );
                pedido.getDetallePedido().add(dp);
            }
        }

        return resultados;
    }

    /**
     * Lista pedidos sin compra relacionada, sin incluir detalles de producto.
     *
     * 1. Ejecuta consulta sobre Pedido, ClienteNatural y ClienteEmpresa. 2.
     * Para cada fila, crea el objeto ClienteNatural o ClienteEmpresa segun
     * tipo. 3. Construye y agrega el objeto Pedido a la lista.
     *
     * @return lista de Pedido con atributo cliente cargado
     * @throws SQLException en error SQL
     */
    public List<Pedido> listarPedidosSinCompra() throws SQLException {
        List<Pedido> lista = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(SELECT_PEDIDOS_SIN_COMPRA_SQL); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int idPedido = rs.getInt("idPedido");
                int idCliente = rs.getInt("idCliente");
                LocalDateTime fecha = rs.getTimestamp("fechaPedido").toLocalDateTime();
                double montoTotal = rs.getDouble("montoTotal");

                // determinar y cargar el objeto Cliente
                TipoUsuario tipoUsuario = TipoUsuario.valueOf(rs.getString("tipoCliente"));
                Cliente clienteObj = null;
                if (tipoUsuario == TipoUsuario.CLIENTE_NATURAL) {
                    ClienteNatural cn = new ClienteNatural();
                    cn.setIdCliente(idCliente);
                    clienteObj = new ClienteNaturalDAO().obtener(cn);
                } else if (tipoUsuario == TipoUsuario.CLIENTE_EMPRESA) {
                    ClienteEmpresa ce = new ClienteEmpresa();
                    ce.setIdCliente(idCliente);
                    clienteObj = new ClienteEmpresaDAO().obtener(ce);
                }
                // construir y agregar Pedido
                Pedido pedido = new Pedido(idPedido, fecha, montoTotal, clienteObj);
                lista.add(pedido);
            }
        }
        return lista;
    }

    public List<Pedido> listarPedidosSinCompraPorIdCliente(Integer idCliente) throws SQLException {
        List<Pedido> lista = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(SELECT_PEDIDOS_SIN_COMPRA_SQL_POR_ID_CLIENTE)) {
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int idPedido = rs.getInt("idPedido");
                idCliente = rs.getInt("idCliente");
                LocalDateTime fecha = rs.getTimestamp("fechaPedido").toLocalDateTime();
                double montoTotal = rs.getDouble("montoTotal");

                // determinar y cargar el objeto Cliente
                TipoUsuario tipoUsuario = TipoUsuario.valueOf(rs.getString("tipoCliente"));
                Cliente clienteObj = null;
                if (tipoUsuario == TipoUsuario.CLIENTE_NATURAL) {
                    ClienteNatural cn = new ClienteNatural();
                    cn.setIdCliente(idCliente);
                    clienteObj = new ClienteNaturalDAO().obtener(cn);
                } else if (tipoUsuario == TipoUsuario.CLIENTE_EMPRESA) {
                    ClienteEmpresa ce = new ClienteEmpresa();
                    ce.setIdCliente(idCliente);
                    clienteObj = new ClienteEmpresaDAO().obtener(ce);
                }
                // construir y agregar Pedido
                Pedido pedido = new Pedido(idPedido, fecha, montoTotal, clienteObj);
                lista.add(pedido);
            }
        }
        return lista;
    }

}
