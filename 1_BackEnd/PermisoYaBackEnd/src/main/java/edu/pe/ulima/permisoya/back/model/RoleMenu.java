package edu.pe.ulima.permisoya.back.model;

import java.util.Objects;

/**
 * Entidad RoleMenu que representa la asignacion de un MenuItem a un Role.
 *
 * @author Henry Wong <hwong@ulima.edu.pe>
 */
public class RoleMenu {

    private Integer idRole;
    private Integer idMenuItem;

    /**
     * Constructor vacio.
     */
    public RoleMenu() {
    }

    /**
     * Construye un RoleMenu completo.
     *
     * @param idRole     clave primaria del rol
     * @param idMenuItem clave primaria del menu item
     */
    public RoleMenu(Integer idRole, Integer idMenuItem) {
        this.idRole = idRole;
        this.idMenuItem = idMenuItem;
    }

    /**
     * Obtiene el id del Role.
     *
     * @return idRole
     */
    public Integer getIdRole() {
        return idRole;
    }

    /**
     * Asigna el id del Role.
     *
     * @param idRole valor a asignar
     */
    public void setIdRole(Integer idRole) {
        this.idRole = idRole;
    }

    /**
     * Obtiene el id del MenuItem.
     *
     * @return idMenuItem
     */
    public Integer getIdMenuItem() {
        return idMenuItem;
    }

    /**
     * Asigna el id del MenuItem.
     *
     * @param idMenuItem valor a asignar
     */
    public void setIdMenuItem(Integer idMenuItem) {
        this.idMenuItem = idMenuItem;
    }

    @Override
    public String toString() {
        return "RoleMenu{roleId=" + idRole + ", menuItemId=" + idMenuItem + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoleMenu)) return false;
        RoleMenu that = (RoleMenu) o;
        return Objects.equals(idRole, that.idRole)
            && Objects.equals(idMenuItem, that.idMenuItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRole, idMenuItem);
    }
}
