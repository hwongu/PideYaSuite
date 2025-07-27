package edu.pe.ulima.permisoya.back.model;

/**
 * Representa la asignacion de un MenuItem a un Role.
 *
 * Cada {@code RoleMenu} vincula un rol con un elemento de menu,
 * indicando que dicho rol tiene acceso a ese item.
 *
 * @author Henry Wong <hwong@ulima.edu.pe>
 */
public class RoleMenu {

    private Role role;
    private MenuItem menuItem;

    /**
     * Constructor vacio.
     */
    public RoleMenu() {
    }

    /**
     * Construye una asociacion entre un rol y un item de menu.
     *
     * @param role     rol que recibe el permiso
     * @param menuItem item de menu al que se da acceso
     */
    public RoleMenu(Role role, MenuItem menuItem) {
        this.role     = role;
        this.menuItem = menuItem;
    }

    /**
     * Devuelve el rol asociado a esta relacion.
     *
     * @return objeto Role que representa el rol
     */
    public Role getRole() {
        return role;
    }

    /**
     * Asigna el rol asociado a esta relacion.
     *
     * @param role nuevo rol a asociar
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Devuelve el item de menu asociado a esta relacion.
     *
     * @return objeto MenuItem que representa el item de menu
     */
    public MenuItem getMenuItem() {
        return menuItem;
    }

    /**
     * Asigna el item de menu asociado a esta relacion.
     *
     * @param menuItem nuevo item de menu a asociar
     */
    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }
}
