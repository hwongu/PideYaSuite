package edu.pe.ulima.permisoya.back.service;

import edu.pe.ulima.permisoya.back.dao.RoleDAO;
import edu.pe.ulima.permisoya.back.model.MenuItem;
import java.util.List;
import java.util.Objects;

/**
 * Servicio para gestionar la obtencion de MenuItem asociados a un Role.
 * <p>
 * Esta clase delega la consulta de menus al {@link RoleDAO}.
 * </p>
 *
 * @author Henry Wong <hwong@ulima.edu.pe>
 */
public class RoleService {

    /** DAO responsable de la consulta de roles y menus. */
    private final RoleDAO roleDAO;

    /**
     * Construye el servicio usando el DAO por defecto.
     */
    public RoleService() {
        this(new RoleDAO());
    }

    /**
     * Construye el servicio con un DAO inyectado.
     *
     * @param roleDAO instancia de {@link RoleDAO}, no puede ser null
     */
    private RoleService(RoleDAO roleDAO) {
        this.roleDAO = Objects.requireNonNull(roleDAO, "roleDAO no puede ser null");
    }

    /**
     * Lista los MenuItem asociados al role con el nombre especificado.
     *
     * @param roleNombre nombre del role a filtrar, no puede ser null
     * @return lista de MenuItem asignados a ese role; puede estar vacia
     * @throws Exception si ocurre un error durante la consulta
     */
    public List<MenuItem> listarMenusPorRole(String roleNombre) throws Exception {
        Objects.requireNonNull(roleNombre, "roleNombre no puede ser null");
        return roleDAO.listarMenusPorRole(roleNombre);
    }
}
