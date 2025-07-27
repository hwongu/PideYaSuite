package edu.pe.ulima.pideya.back.service;

import edu.pe.ulima.pideya.back.dao.ClienteNaturalDAO;
import edu.pe.ulima.pideya.back.model.ClienteNatural;
import java.util.List;
import java.util.Objects;

/**
 * Servicio para gestionar operaciones CRUD y consultas de
 * {@link edu.pe.ulima.pideya.back.model.ClienteNatural}.
 * <p>
 * Esta clase encapsula la logica de negocio relacionada con clientes naturales
 * y delega la persistencia al
 * {@link edu.pe.ulima.pideya.back.dao.ClienteNaturalDAO}.
 * </p>
 *
 * @author Henry Wong <hwong@ulima.edu.pe>
 */
public class ClienteNaturalService {

    /**
     * DAO responsable de la persistencia de clientes naturales.
     */
    private final ClienteNaturalDAO clienteNaturalDAO;

    /**
     * Construye el servicio usando el DAO por defecto.
     */
    public ClienteNaturalService() {
        this(new ClienteNaturalDAO());
    }

    /**
     * Construye el servicio con un DAO inyectado.
     *
     * @param clienteNaturalDAO instancia de {@link ClienteNaturalDAO}, no puede
     * ser null
     */
    private ClienteNaturalService(ClienteNaturalDAO clienteNaturalDAO) {
        this.clienteNaturalDAO = Objects.requireNonNull(clienteNaturalDAO,
                "clienteNaturalDAO no puede ser null");
    }

    /**
     * Agrega un nuevo ClienteNatural al sistema.
     *
     * @param cn entidad a insertar, no puede ser null
     * @return el ClienteNatural creado con su ID y Usuario asociado
     * @throws Exception si ocurre un error durante la persistencia
     */
    public ClienteNatural agregarClienteNatural(ClienteNatural cn) throws Exception {
        Objects.requireNonNull(cn, "clienteNatural no puede ser null");
        return clienteNaturalDAO.insertar(cn);
    }

    /**
     * Actualiza los datos de un ClienteNatural existente.
     *
     * @param cn entidad con ID y nuevos atributos, no puede ser null
     * @return el ClienteNatural actualizado o null si no existia
     * @throws IllegalArgumentException si el ID de ClienteNatural es null
     * @throws Exception si ocurre un error durante la actualizacion
     */
    public ClienteNatural editarInformacionClienteNatural(ClienteNatural cn) throws Exception {
        Objects.requireNonNull(cn, "clienteNatural no puede ser null");
        if (cn.getIdCliente() == null) {
            throw new IllegalArgumentException("El ID de ClienteNatural es obligatorio para actualizar");
        }
        return clienteNaturalDAO.actualizar(cn);
    }

    /**
     * Elimina un ClienteNatural existente y su Usuario asociado.
     *
     * @param cn entidad con ID a eliminar, no puede ser null
     * @return el ClienteNatural eliminado o null si no existia
     * @throws IllegalArgumentException si el ID de ClienteNatural es null
     * @throws Exception si ocurre un error durante la eliminacion
     */
    public ClienteNatural eliminarClienteNatural(ClienteNatural cn) throws Exception {
        Objects.requireNonNull(cn, "clienteNatural no puede ser null");
        if (cn.getIdCliente() == null) {
            throw new IllegalArgumentException("El ID de ClienteNatural es obligatorio para eliminar");
        }
        return clienteNaturalDAO.eliminar(cn);
    }

    /**
     * Obtiene el listado completo de clientes naturales.
     *
     * @return lista de ClienteNatural; puede estar vacia si no hay registros
     * @throws Exception si ocurre un error al listar
     */
    public List<ClienteNatural> listarClienteNaturales() throws Exception {
        return clienteNaturalDAO.listar();
    }

    /**
     * Filtra clientes naturales por nombre.
     *
     * @param prefijo cadena de inicio para filtrar nombres, no puede ser null
     * @return lista de ClienteNatural cuyo nombre comienza con el prefijo
     * @throws IllegalArgumentException si prefijo es null
     * @throws Exception si ocurre un error en la consulta
     */
    public List<ClienteNatural> filtrarPorNombre(String prefijo) throws Exception {
        if (prefijo == null) {
            throw new IllegalArgumentException("El prefijo no puede ser null");
        }
        return clienteNaturalDAO.listarPorNombre(prefijo);
    }
}
