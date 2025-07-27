package edu.pe.ulima.pideya.back.dao;

import edu.pe.ulima.pideya.back.model.Cliente;
import edu.pe.ulima.pideya.back.model.ClienteEmpresa;
import edu.pe.ulima.pideya.back.model.ClienteNatural;
import edu.pe.ulima.pideya.back.model.Compra;
import edu.pe.ulima.pideya.back.model.DetallePedido;
import edu.pe.ulima.pideya.back.model.Pedido;
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
 * DAO para la entidad Compra. Implementa operaciones CRUD sobre la tabla
 * PideYa.Compra, gestionando transacciones y mapeo a objeto Compra. Extiende de
 * Conexion para gestion de conexion.
 *
 * @author Henry Wong <hwong@ulima.edu.pe>
 */
public class CompraDAO extends Conexion<Compra> {

    // Sentencias SQL parametrizadas para Compra
    private static final String INSERT_SQL
            = "INSERT INTO PideYa.Compra(idPedido, fechaCompra) VALUES(?, ?)";
    private static final String UPDATE_SQL
            = "UPDATE PideYa.Compra SET idPedido = ?, fechaCompra = ? WHERE idCompra = ?";
    private static final String DELETE_SQL
            = "DELETE FROM PideYa.Compra WHERE idCompra = ?";
    private static final String SELECT_ONE_SQL
            = "SELECT idCompra, idPedido, fechaCompra FROM PideYa.Compra WHERE idCompra = ?";
    private static final String SELECT_ALL_SQL
            = "SELECT idCompra, idPedido, fechaCompra FROM PideYa.Compra";
    private static final String SELECT_COMPRAS_CON_DETALLE_FILTRO_SQL
            = "SELECT "
            + "  c.idCompra, "
            + "  c.idPedido, "
            + "  c.fechaCompra, "
            + "  p.idCliente, "
            + "  p.fechaPedido, "
            + "  p.montoTotal, "
            + "  CASE "
            + "    WHEN cn.idCliente IS NOT NULL THEN 'CLIENTE_NATURAL' "
            + "    WHEN ce.idCliente IS NOT NULL THEN 'CLIENTE_EMPRESA' "
            + "    ELSE 'DESCONOCIDO' "
            + "  END AS tipoCliente, "
            + "  COALESCE( CONCAT(cn.nombre, ' ', cn.apellido), ce.razonSocial ) AS nombreCliente, "
            + "  dp.idProducto, "
            + "  pr.nombre      AS nombreProducto, "
            + "  dp.precioUnitario, "
            + "  dp.cantidad, "
            + "  dp.subTotal    AS subtotal "
            + "FROM PideYa.Compra c "
            + "JOIN PideYa.Pedido p ON c.idPedido = p.idPedido "
            + "LEFT JOIN PideYa.ClienteNatural cn ON p.idCliente = cn.idCliente "
            + "LEFT JOIN PideYa.ClienteEmpresa ce ON p.idCliente = ce.idCliente "
            + "JOIN PideYa.DetallePedido dp ON p.idPedido = dp.idPedido "
            + "JOIN PideYa.Producto pr ON dp.idProducto = pr.idProducto "
            + "WHERE COALESCE( CONCAT(cn.nombre, ' ', cn.apellido), ce.razonSocial ) LIKE ? "
            + "ORDER BY c.fechaCompra";
    
    private static final String SELECT_COMPRAS_CON_DETALLE_FILTRO_IDCLIENTE
            = "SELECT "
            + "  c.idCompra, "
            + "  c.idPedido, "
            + "  c.fechaCompra, "
            + "  p.idCliente, "
            + "  p.fechaPedido, "
            + "  p.montoTotal, "
            + "  CASE "
            + "    WHEN cn.idCliente IS NOT NULL THEN 'CLIENTE_NATURAL' "
            + "    WHEN ce.idCliente IS NOT NULL THEN 'CLIENTE_EMPRESA' "
            + "    ELSE 'DESCONOCIDO' "
            + "  END AS tipoCliente, "
            + "  COALESCE( CONCAT(cn.nombre, ' ', cn.apellido), ce.razonSocial ) AS nombreCliente, "
            + "  dp.idProducto, "
            + "  pr.nombre      AS nombreProducto, "
            + "  dp.precioUnitario, "
            + "  dp.cantidad, "
            + "  dp.subTotal    AS subtotal "
            + "FROM PideYa.Compra c "
            + "JOIN PideYa.Pedido p ON c.idPedido = p.idPedido "
            + "LEFT JOIN PideYa.ClienteNatural cn ON p.idCliente = cn.idCliente "
            + "LEFT JOIN PideYa.ClienteEmpresa ce ON p.idCliente = ce.idCliente "
            + "JOIN PideYa.DetallePedido dp ON p.idPedido = dp.idPedido "
            + "JOIN PideYa.Producto pr ON dp.idProducto = pr.idProducto "
            + "WHERE p.idCliente = ? "
            + "ORDER BY c.fechaCompra";
    
    private static final String SELECT_COMPRAS_CON_DETALLE_FILTRO_IDCOMPRA
            = "SELECT "
            + "  c.idCompra, "
            + "  c.idPedido, "
            + "  c.fechaCompra, "
            + "  p.idCliente, "
            + "  p.fechaPedido, "
            + "  p.montoTotal, "
            + "  CASE "
            + "    WHEN cn.idCliente IS NOT NULL THEN 'CLIENTE_NATURAL' "
            + "    WHEN ce.idCliente IS NOT NULL THEN 'CLIENTE_EMPRESA' "
            + "    ELSE 'DESCONOCIDO' "
            + "  END AS tipoCliente, "
            + "  COALESCE( CONCAT(cn.nombre, ' ', cn.apellido), ce.razonSocial ) AS nombreCliente, "
            + "  dp.idProducto, "
            + "  pr.nombre      AS nombreProducto, "
            + "  dp.precioUnitario, "
            + "  dp.cantidad, "
            + "  dp.subTotal    AS subtotal "
            + "FROM PideYa.Compra c "
            + "JOIN PideYa.Pedido p ON c.idPedido = p.idPedido "
            + "LEFT JOIN PideYa.ClienteNatural cn ON p.idCliente = cn.idCliente "
            + "LEFT JOIN PideYa.ClienteEmpresa ce ON p.idCliente = ce.idCliente "
            + "JOIN PideYa.DetallePedido dp ON p.idPedido = dp.idPedido "
            + "JOIN PideYa.Producto pr ON dp.idProducto = pr.idProducto "
            + "WHERE c.idCompra = ? "
            + "ORDER BY c.fechaCompra";
    

    /**
     * Inserta una nueva compra en la base de datos.
     *
     * 1. Prepara INSERT con idPedido y fechaCompra. 2. Ejecuta y obtiene clave.
     * 3. Commit o rollback.
     *
     * @param c Compra a insertar
     * @return Compra con idCompra asignado
     * @throws SQLException en error SQL
     */
    @Override
    public Compra insertar(Compra c) throws SQLException {
        try (PreparedStatement ps = getConnection()
                .prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            // Setear idPedido y fechaCompra
            ps.setInt(1, c.getPedido().getIdPedido());
            LocalDateTime fecha = c.getFechaCompra() != null
                    ? c.getFechaCompra() : LocalDateTime.now();
            ps.setTimestamp(2, Timestamp.valueOf(fecha));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    c.setIdCompra(rs.getInt(1));
                    c.setFechaCompra(fecha);
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
     * Actualiza una compra existente.
     *
     * 1. Prepara UPDATE con nuevos valores y idCompra. 2. Ejecuta. 3. Commit o
     * rollback.
     *
     * @param c Compra con valores a actualizar
     * @return Compra actualizada o null si no existia
     * @throws SQLException en error SQL
     */
    @Override
    public Compra actualizar(Compra c) throws SQLException {
        try (PreparedStatement ps = getConnection().prepareStatement(UPDATE_SQL)) {
            ps.setInt(1, c.getPedido().getIdPedido());
            ps.setTimestamp(2, Timestamp.valueOf(c.getFechaCompra()));
            ps.setInt(3, c.getIdCompra());
            int rows = ps.executeUpdate();
            commit();
            return rows > 0 ? c : null;
        } catch (SQLException ex) {
            rollback();
            throw ex;
        }
    }

    /**
     * Elimina una compra por su ID.
     *
     * @param c Compra con idCompra a eliminar
     * @return Compra eliminada o null si no existia
     * @throws SQLException en error SQL
     */
    @Override
    public Compra eliminar(Compra c) throws SQLException {
        try (PreparedStatement ps = getConnection().prepareStatement(DELETE_SQL)) {
            ps.setInt(1, c.getIdCompra());
            int rows = ps.executeUpdate();
            commit();
            return rows > 0 ? c : null;
        } catch (SQLException ex) {
            rollback();
            throw ex;
        }
    }

    /**
     * Obtiene una compra por su ID.
     *
     * @param c Compra con idCompra a buscar
     * @return Compra encontrada o null si no existe
     * @throws SQLException en error SQL
     */
    @Override
    public Compra obtener(Compra c) throws SQLException {
        try (PreparedStatement ps = getConnection().prepareStatement(SELECT_ONE_SQL)) {
            ps.setInt(1, c.getIdCompra());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Mapear Pedido asociado
                    Pedido ped = new Pedido();
                    ped.setIdPedido(rs.getInt("idPedido"));
                    // Construir Compra
                    return new Compra(
                            rs.getInt("idCompra"),
                            ped,
                            rs.getTimestamp("fechaCompra").toLocalDateTime()
                    );
                }
            }
        }
        return null;
    }

    /**
     * Lista todas las compras.
     *
     * @return lista de Compra
     * @throws SQLException en error SQL
     */
    @Override
    public List<Compra> listar() throws SQLException {
        List<Compra> lista = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(SELECT_ALL_SQL); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Pedido ped = new Pedido();
                ped.setIdPedido(rs.getInt("idPedido"));
                lista.add(new Compra(
                        rs.getInt("idCompra"),
                        ped,
                        rs.getTimestamp("fechaCompra").toLocalDateTime()
                ));
            }
        }
        return lista;
    }

    /**
     * Lista las compras que coinciden con el filtro de nombre de cliente,
     * incluyendo sus pedidos, cliente (natural o empresa), detalles y producto.
     *
     * 1. Prepara y ejecuta la consulta con LIKE sobre nombreCliente. 2. Para
     * cada fila, crea o reutiliza el objeto Compra y Pedido. 3. Construye
     * ClienteNatural o ClienteEmpresa según tipoCliente. 4. Crea DetallePedido
     * con Producto y agrega al Pedido.
     *
     * @param nombreClienteFiltro texto para buscar en el nombre del cliente
     * @return lista de Compra con datos completos
     * @throws SQLException en error SQL
     */
    public List<Compra> listarComprasPorCliente(String nombreClienteFiltro) throws SQLException {
        Map<Integer, Compra> cache = new HashMap<>();
        List<Compra> resultados = new ArrayList<>();

        try (PreparedStatement ps = getConnection()
                .prepareStatement(SELECT_COMPRAS_CON_DETALLE_FILTRO_SQL)) {
            ps.setString(1, "%" + nombreClienteFiltro + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idCompra = rs.getInt("idCompra");
                    Compra compra = cache.get(idCompra);
                    if (compra == null) {
                        // construir Compra y Pedido
                        Pedido pedido = new Pedido();
                        pedido.setIdPedido(rs.getInt("idPedido"));
                        pedido.setFechaPedido(rs.getTimestamp("fechaPedido").toLocalDateTime());
                        pedido.setMontoTotal(rs.getDouble("montoTotal"));

                        // determinar tipo y cargar cliente
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
                        pedido.setCliente(clienteObj);

                        // crear Compra
                        compra = new Compra();
                        compra.setIdCompra(idCompra);
                        compra.setPedido(pedido);
                        compra.setFechaCompra(rs.getTimestamp("fechaCompra").toLocalDateTime());

                        pedido.setDetallePedido(new ArrayList<>());
                        cache.put(idCompra, compra);
                        resultados.add(compra);
                    }

                    // crear DetallePedido y Producto
                    Producto prod = new Producto();
                    prod.setIdProducto(rs.getInt("idProducto"));
                    prod.setNombre(rs.getString("nombreProducto"));

                    DetallePedido dp = new DetallePedido();
                    dp.setPedido(compra.getPedido());
                    dp.setProducto(prod);
                    dp.setPrecioUnitario(rs.getDouble("precioUnitario"));
                    dp.setCantidad(rs.getDouble("cantidad"));
                    dp.setSubTotal(rs.getDouble("subtotal"));

                    compra.getPedido().getDetallePedido().add(dp);
                }
            }
        }

        return resultados;
    }
    
    /**
     * Lista la compra que coincide con el idCompra
     * incluyendo sus pedidos, cliente (natural o empresa), detalles y producto.
     *
     * 1. Prepara y ejecuta la consulta con LIKE sobre nombreCliente. 2. Para
     * cada fila, crea o reutiliza el objeto Compra y Pedido. 3. Construye
     * ClienteNatural o ClienteEmpresa según tipoCliente. 4. Crea DetallePedido
     * con Producto y agrega al Pedido.
     *
     * @param idCompra id de la compra
     * @return lista de Compra con datos completos
     * @throws SQLException en error SQL
     */
    public List<Compra> listarComprasPorIdCompra(Integer idCompra) throws SQLException {
        Map<Integer, Compra> cache = new HashMap<>();
        List<Compra> resultados = new ArrayList<>();

        try (PreparedStatement ps = getConnection()
                .prepareStatement(SELECT_COMPRAS_CON_DETALLE_FILTRO_IDCOMPRA)) {
            ps.setInt(1, idCompra);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    idCompra = rs.getInt("idCompra");
                    Compra compra = cache.get(idCompra);
                    if (compra == null) {
                        // construir Compra y Pedido
                        Pedido pedido = new Pedido();
                        pedido.setIdPedido(rs.getInt("idPedido"));
                        pedido.setFechaPedido(rs.getTimestamp("fechaPedido").toLocalDateTime());
                        pedido.setMontoTotal(rs.getDouble("montoTotal"));

                        // determinar tipo y cargar cliente
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
                        pedido.setCliente(clienteObj);

                        // crear Compra
                        compra = new Compra();
                        compra.setIdCompra(idCompra);
                        compra.setPedido(pedido);
                        compra.setFechaCompra(rs.getTimestamp("fechaCompra").toLocalDateTime());

                        pedido.setDetallePedido(new ArrayList<>());
                        cache.put(idCompra, compra);
                        resultados.add(compra);
                    }

                    // crear DetallePedido y Producto
                    Producto prod = new Producto();
                    prod.setIdProducto(rs.getInt("idProducto"));
                    prod.setNombre(rs.getString("nombreProducto"));

                    DetallePedido dp = new DetallePedido();
                    dp.setPedido(compra.getPedido());
                    dp.setProducto(prod);
                    dp.setPrecioUnitario(rs.getDouble("precioUnitario"));
                    dp.setCantidad(rs.getDouble("cantidad"));
                    dp.setSubTotal(rs.getDouble("subtotal"));

                    compra.getPedido().getDetallePedido().add(dp);
                }
            }
        }

        return resultados;
    }
    
    //SELECT_COMPRAS_CON_DETALLE_FILTRO_IDCLIENTE
    public List<Compra> listarComprasPorCliente(Integer idCliente) throws SQLException {
        Map<Integer, Compra> cache = new HashMap<>();
        List<Compra> resultados = new ArrayList<>();

        try (PreparedStatement ps = getConnection()
                .prepareStatement(SELECT_COMPRAS_CON_DETALLE_FILTRO_IDCLIENTE)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idCompra = rs.getInt("idCompra");
                    Compra compra = cache.get(idCompra);
                    if (compra == null) {
                        // construir Compra y Pedido
                        Pedido pedido = new Pedido();
                        pedido.setIdPedido(rs.getInt("idPedido"));
                        pedido.setFechaPedido(rs.getTimestamp("fechaPedido").toLocalDateTime());
                        pedido.setMontoTotal(rs.getDouble("montoTotal"));

                        // determinar tipo y cargar cliente
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
                        pedido.setCliente(clienteObj);

                        // crear Compra
                        compra = new Compra();
                        compra.setIdCompra(idCompra);
                        compra.setPedido(pedido);
                        compra.setFechaCompra(rs.getTimestamp("fechaCompra").toLocalDateTime());

                        pedido.setDetallePedido(new ArrayList<>());
                        cache.put(idCompra, compra);
                        resultados.add(compra);
                    }

                    // crear DetallePedido y Producto
                    Producto prod = new Producto();
                    prod.setIdProducto(rs.getInt("idProducto"));
                    prod.setNombre(rs.getString("nombreProducto"));

                    DetallePedido dp = new DetallePedido();
                    dp.setPedido(compra.getPedido());
                    dp.setProducto(prod);
                    dp.setPrecioUnitario(rs.getDouble("precioUnitario"));
                    dp.setCantidad(rs.getDouble("cantidad"));
                    dp.setSubTotal(rs.getDouble("subtotal"));

                    compra.getPedido().getDetallePedido().add(dp);
                }
            }
        }

        return resultados;
    }
}
