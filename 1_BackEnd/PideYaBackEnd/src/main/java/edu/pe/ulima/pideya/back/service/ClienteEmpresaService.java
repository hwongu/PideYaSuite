package edu.pe.ulima.pideya.back.service;

import edu.pe.ulima.pideya.back.dao.ClienteEmpresaDAO;
import edu.pe.ulima.pideya.back.model.ClienteEmpresa;
import java.util.List;
import java.util.Objects;

/**
 * Servicio para gestionar operaciones CRUD sobre
 * {@link edu.pe.ulima.pideya.back.model.ClienteEmpresa}.
 * <p>
 * Esta clase encapsula la logica de negocio relacionada con clientes empresa y
 * delega las operaciones de persistencia al {@link ClienteEmpresaDAO}.
 * </p>
 *
 * @author Henry Wong <hwong@ulima.edu.pe>
 */
public class ClienteEmpresaService {

    /**
     * DAO responsable de la persistencia de clientes empresa.
     */
    private final ClienteEmpresaDAO clienteEmpresaDAO;

    /**
     * Construye el servicio usando el DAO por defecto.
     */
    public ClienteEmpresaService() {
        this(new ClienteEmpresaDAO());
    }

    /**
     * Construye el servicio con un DAO inyectado.
     *
     * @param clienteEmpresaDAO instancia de {@link ClienteEmpresaDAO}, no puede
     * ser {@code null}
     */
    public ClienteEmpresaService(ClienteEmpresaDAO clienteEmpresaDAO) {
        this.clienteEmpresaDAO = Objects.requireNonNull(clienteEmpresaDAO,
                "clienteEmpresaDAO no puede ser null");
    }

    /**
     * Inserta un nuevo cliente empresa en el sistema.
     *
     * @param clienteEmpresa entidad a insertar, no puede ser {@code null}
     * @return la entidad creada con su ID generado
     * @throws Exception si ocurre un error durante la persistencia
     */
    public ClienteEmpresa agregarClienteEmpresa(ClienteEmpresa clienteEmpresa) throws Exception {
        Objects.requireNonNull(clienteEmpresa, "clienteEmpresa no puede ser null");
        return clienteEmpresaDAO.insertar(clienteEmpresa);
    }

    /**
     * Actualiza los datos de un cliente empresa existente.
     *
     * @param clienteEmpresa entidad con ID y nuevos atributos, no puede ser
     * {@code null}
     * @return la entidad actualizada
     * @throws IllegalArgumentException si el ID es {@code null}
     * @throws Exception si ocurre un error durante la actualizacion
     */
    public ClienteEmpresa editarInformacionClienteEmpresa(ClienteEmpresa clienteEmpresa) throws Exception {
        Objects.requireNonNull(clienteEmpresa, "clienteEmpresa no puede ser null");
        if (clienteEmpresa.getIdCliente() == null) {
            throw new IllegalArgumentException("El ID del cliente empresa es obligatorio para actualizar");
        }
        return clienteEmpresaDAO.actualizar(clienteEmpresa);
    }

    /**
     * Elimina un cliente empresa existente.
     *
     * @param clienteEmpresa entidad con ID a eliminar, no puede ser
     * {@code null}
     * @return la entidad eliminada o {@code null} si no existia
     * @throws IllegalArgumentException si el ID es {@code null}
     * @throws Exception si ocurre un error durante la eliminacion
     */
    public ClienteEmpresa eliminarClienteEmpresa(ClienteEmpresa clienteEmpresa) throws Exception {
        Objects.requireNonNull(clienteEmpresa, "clienteEmpresa no puede ser null");
        if (clienteEmpresa.getIdCliente() == null) {
            throw new IllegalArgumentException("El ID del cliente empresa es obligatorio para eliminar");
        }
        return clienteEmpresaDAO.eliminar(clienteEmpresa);
    }

    /**
     * Obtiene el listado completo de clientes empresa disponibles.
     *
     * @return lista de clientes empresa; puede estar vacia si no hay registros
     * @throws Exception si ocurre un error al listar
     */
    public List<ClienteEmpresa> listarClienteEmpresas() throws Exception {
        return clienteEmpresaDAO.listar();
    }

    /**
     * Obtiene el listado de clientes empresa filtrado por razon social.
     *
     * @param razonSocial razon social de cliente empresa a filtrar
     * @return lista de clientes empresa; puede estar vacia si no hay registros
     * @throws Exception si ocurre un error al listar
     */
    public List<ClienteEmpresa> filtrarClienteEmpresaPorRazonSocial(String razonSocial) throws Exception {
        return clienteEmpresaDAO.listarPorRazonSocial(razonSocial);
    }
}
