package edu.pe.ulima.permisoya.back.service;

import edu.pe.ulima.permisoya.back.dao.RoleDAO;
import edu.pe.ulima.permisoya.back.model.Role;
import java.util.Objects;

/**
 * Servicio para gestionar la obtencion de menuItems asociados a un role.
 *
 * Esta clase delega la consulta de menus al {@link RoleDAO}.
 *
 * @author Henry Wong <hwong@ulima.edu.pe>
 */
public class RoleService {

    /** DAO responsable de la consulta de roles y menus. */
    private final RoleDAO roleDAO;

    /**
     * Constructor por defecto que inicializa el servicio con el DAO
     * predeterminado.
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
     * Obtiene un {@code Role} completo para el nombre especificado,
     * con su lista de {@code RoleMenu} que representa los accesos.
     *
     * @param roleNombre nombre del role a filtrar, no puede ser null
     * @return objeto {@code Role} con sus roleMenus; puede no tener menus
     * @throws Exception si ocurre un error durante la consulta
     */
    public Role listarMenusPorRole(String roleNombre) throws Exception {
        Objects.requireNonNull(roleNombre, "roleNombre no puede ser null");
        return roleDAO.listarMenusPorRole(roleNombre);
    }
}
