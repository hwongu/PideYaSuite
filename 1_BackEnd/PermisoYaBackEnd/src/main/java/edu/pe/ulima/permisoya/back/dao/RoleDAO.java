package edu.pe.ulima.permisoya.back.dao;

import edu.pe.ulima.permisoya.back.model.MenuItem;
import edu.pe.ulima.permisoya.back.model.Role;
import edu.pe.ulima.permisoya.back.model.RoleMenu;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO para la entidad Role. Provee acceso a roles y menus asociados.
 *
 * @author hwong
 */
public class RoleDAO extends Conexion<Role> {

    // Sentencia para obtener menu items de un role por su nombre
    private static final String SELECT_MENUS_BY_ROLE_SQL
            = "SELECT "
            + "  r.idRole, "
            + "  r.nombre AS roleNombre, "
            + "  mi.idMenuItem, "
            + "  mi.nombre AS menuNombre, "
            + "  mi.parentId, "
            + "  mi.orden "
            + "FROM PermisoYa.Role r "
            + "JOIN PermisoYa.Role_Menu rm ON r.idRole = rm.idRole "
            + "JOIN PermisoYa.MenuItem mi ON rm.idMenuItem = mi.idMenuItem "
            + "WHERE r.nombre = ? "
            + "ORDER BY mi.parentId, mi.orden";

    @Override
    public Role insertar(Role e) throws SQLException {
        throw new UnsupportedOperationException("Metodo no implementado");
    }

    @Override
    public Role actualizar(Role e) throws SQLException {
        throw new UnsupportedOperationException("Metodo no implementado");
    }

    @Override
    public Role eliminar(Role e) throws SQLException {
        throw new UnsupportedOperationException("Metodo no implementado");
    }

    @Override
    public Role obtener(Role e) throws SQLException {
        throw new UnsupportedOperationException("Metodo no implementado");
    }

    @Override
    public List<Role> listar() throws SQLException {
        throw new UnsupportedOperationException("Metodo no implementado");
    }

    /**
     * Obtiene un Role completo (id + nombre) y su lista de RoleMenu asociada.
     *
     * @param roleNombre nombre del rol a buscar
     * @return objeto Role con roleMenus poblado (vacío si no hay menús)
     * @throws SQLException en error SQL
     */
    public Role listarMenusPorRole(String roleNombre) throws SQLException {
        Role role = null;

        try (PreparedStatement ps = getConnection().prepareStatement(SELECT_MENUS_BY_ROLE_SQL)) {
            ps.setString(1, roleNombre);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // la primera vez, inicializamos el Role
                    if (role == null) {
                        role = new Role();
                        role.setIdRole(rs.getInt("idRole"));
                        role.setNombre(rs.getString("roleNombre"));
                    }

                    // Construimos el MenuItem
                    MenuItem m = new MenuItem();
                    m.setIdMenuItem(rs.getInt("idMenuItem"));
                    m.setNombre(rs.getString("menuNombre"));
                    m.setOrden(rs.getInt("orden"));
                    // parentId → parent MenuItem con sólo el ID
                    int parentId = rs.getInt("parentId");
                    if (!rs.wasNull()) {
                        MenuItem parent = new MenuItem();
                        parent.setIdMenuItem(parentId);
                        m.setParent(parent);
                    }

                    // Construimos la asociación RoleMenu
                    RoleMenu rm = new RoleMenu();
                    rm.setRole(role);
                    rm.setMenuItem(m);

                    // Enlazamos bidireccionalmente
                    role.addRoleMenu(rm);
                    m.addRoleMenu(rm);
                }
            }
        }

        // Si no encontramos el rol (ni una fila), devolvemos uno vacío con nombre
        if (role == null) {
            role = new Role();
            role.setNombre(roleNombre);
        }

        return role;
    }
}
