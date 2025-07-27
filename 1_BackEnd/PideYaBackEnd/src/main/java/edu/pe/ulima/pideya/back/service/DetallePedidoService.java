package edu.pe.ulima.pideya.back.service;

import edu.pe.ulima.pideya.back.dao.DetallePedidoDAO;
import edu.pe.ulima.pideya.back.model.DetallePedido;
import java.util.List;
import java.util.Objects;

/**
 * Servicio para gestionar consultas de {@link DetallePedido}.
 * <p>
 * Esta clase delega la obtencion de los detalles de un pedido al
 * {@link DetallePedidoDAO}.
 * </p>
 *
 * @author Henry Wong <hwong@ulima.edu.pe>
 */
public class DetallePedidoService {

    /**
     * DAO responsable de la persistencia de DetallePedido.
     */
    private final DetallePedidoDAO detallePedidoDAO;

    /**
     * Construye el servicio usando el DAO por defecto.
     */
    public DetallePedidoService() {
        this(new DetallePedidoDAO());
    }

    /**
     * Construye el servicio con un DAO inyectado.
     *
     * @param detallePedidoDAO instancia de {@link DetallePedidoDAO}, no puede
     * ser null
     */
    private DetallePedidoService(DetallePedidoDAO detallePedidoDAO) {
        this.detallePedidoDAO = Objects.requireNonNull(
                detallePedidoDAO, "detallePedidoDAO no puede ser null"
        );
    }

    /**
     * Lista todos los detalles asociados a un pedido.
     *
     * 1. Valida que el idPedido no sea null. 2. Delegar consulta al DAO.
     *
     * @param idPedido identificador del pedido, no puede ser null
     * @return lista de {@link DetallePedido} para ese pedido
     * @throws Exception si ocurre un error durante la consulta
     */
    public List<DetallePedido> listarDetallesPorPedido(Integer idPedido) throws Exception {
        Objects.requireNonNull(idPedido, "idPedido no puede ser null");
        return detallePedidoDAO.listarPorPedido(idPedido);
    }
}
