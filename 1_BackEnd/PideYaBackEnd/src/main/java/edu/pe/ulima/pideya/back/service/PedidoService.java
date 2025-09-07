package edu.pe.ulima.pideya.back.service;

import edu.pe.ulima.pideya.back.dao.PedidoDAO;
import edu.pe.ulima.pideya.back.model.Pedido;
import java.util.List;
import java.util.Objects;

/**
 * Servicio para gestionar operaciones CRUD sobre {@link Pedido}.
 * <p>
 * Esta clase encapsula la logica de negocio relacionada con pedidos y delega
 * las operaciones de persistencia al {@link PedidoDAO}.
 * </p>
 *
 * @author Henry Wong <hwong@ulima.edu.pe>
 */
public class PedidoService {

    /**
     * DAO responsable de la persistencia de pedidos.
     */
    private final PedidoDAO pedidoDAO;

    /**
     * Construye el servicio usando el DAO por defecto.
     */
    public PedidoService() {
        this(new PedidoDAO());
    }

    /**
     * Construye el servicio con un DAO inyectado.
     *
     * @param pedidoDAO instancia de {@link PedidoDAO}, no puede ser null
     */
    private PedidoService(PedidoDAO pedidoDAO) {
        this.pedidoDAO = Objects.requireNonNull(pedidoDAO, "pedidoDAO no puede ser null");
    }

    /**
     * Realiza un nuevo pedido con sus detalles y actualiza stock.
     *
     * 1. Valida que el objeto pedido no sea null y contenga al menos un
     * detalle. 2. Delegar insercion de pedido, detalles y actualizacion de
     * stock al DAO.
     *
     * @param pedido Pedido con lista de DetallePedido, no puede ser null
     * @return el Pedido creado con su idPedido y detalles persistidos
     * @throws Exception si ocurre un error durante la operacion
     */
    public Pedido realizarPedido(Pedido pedido) throws Exception {
        Objects.requireNonNull(pedido, "pedido no puede ser null");
        if (pedido.getDetallePedido() == null || pedido.getDetallePedido().isEmpty()) {
            throw new IllegalArgumentException("El pedido debe contener al menos un detalle");
        }
        return pedidoDAO.insertar(pedido);
    }

    /**
     * Obtiene el listado de pedidos sin compra asociada con sus detalles
     * completos.
     *
     * @return lista de pedidos con detalle de productos y datos de cliente
     * @throws Exception si ocurre un error al listar
     */
    public List<Pedido> listarPedidosConDetalles() throws Exception {
        return pedidoDAO.listarPedidosConDetalles();
    }

    /**
     * Obtiene el listado de pedidos que no tienen compra asociada.
     * <p>
     * Delegates la consulta al {@link PedidoDAO#listarPedidosSinCompra()}.
     * </p>
     *
     * @return lista de {@link Pedido} sin compra relacionada; puede estar vacia
     * @throws Exception si ocurre un error al listar
     */
    public List<Pedido> listarPedidosSinCompra() throws Exception {
        return pedidoDAO.listarPedidosSinCompra();
    }
    
    public List<Pedido> listarPedidosSinCompraPorIdCliente(Integer idCliente) throws Exception {
        Objects.requireNonNull(idCliente, "idCliente no puede ser null");
        return pedidoDAO.listarPedidosSinCompraPorIdCliente(idCliente);
    }

    /**
     * Anula un pedido existente, restaurando el stock y eliminando el registro.
     *
     * 1. Valida que el pedido no sea null. 
     * 2. Valida que el idPedido no sea null. 
     * 3. Delegar anulacion (eliminar) al DAO.
     *
     * @param pedido Pedido con idPedido a anular, no puede ser null
     * @return el Pedido anulado o null si no existia
     * @throws IllegalArgumentException si el ID del pedido es null
     * @throws Exception si ocurre un error durante la anulacion
     */
    public Pedido anularPedido(Pedido pedido) throws Exception {
        Objects.requireNonNull(pedido, "pedido no puede ser null");
        if (pedido.getIdPedido() == null) {
            throw new IllegalArgumentException("El ID del pedido es obligatorio para anular");
        }
        return pedidoDAO.eliminar(pedido);
    }
    
    public List<Pedido> listarPedidosConDetallesPorIdCliente(Integer idCliente) throws Exception {
        Objects.requireNonNull(idCliente, "idCliente no puede ser null");
        return pedidoDAO.listarPedidosConDetallesPorIdCliente(idCliente);
    }

}
