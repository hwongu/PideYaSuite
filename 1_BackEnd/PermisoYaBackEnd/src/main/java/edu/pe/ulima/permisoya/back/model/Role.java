package edu.pe.ulima.permisoya.back.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa un rol de usuario en el sistema.
 *
 * Cada {@code Role} mantiene su identificador, nombre y la lista de
 * relaciones {@code RoleMenu} asociadas, que indican los accesos a menus.
 *
 * @author Henry Wong <hwong@ulima.edu.pe>
 */
public class Role {

    private Integer idRole;
    private String nombre;
    private List<RoleMenu> roleMenus;

    /**
     * Constructor vacio.
     */
    public Role() {
        roleMenus = new ArrayList<>();
    }

    /**
     * Construye un {@code Role} con valores iniciales.
     *
     * @param idRole identificador unico del rol
     * @param nombre nombre del rol
     */
    public Role(Integer idRole, String nombre) {
        this.idRole = idRole;
        this.nombre = nombre;
        roleMenus = new ArrayList<>();
    }

    /**
     * Devuelve el identificador unico del rol.
     *
     * @return id unico del rol
     */
    public Integer getIdRole() {
        return idRole;
    }

    /**
     * Asigna el identificador unico del rol.
     *
     * @param idRole identificador unico a asignar
     */
    public void setIdRole(Integer idRole) {
        this.idRole = idRole;
    }

    /**
     * Devuelve el nombre del rol.
     *
     * @return nombre descriptivo del rol
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del rol.
     *
     * @param nombre nuevo nombre descriptivo del rol
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve la lista de relaciones {@code RoleMenu} asociadas a este rol.
     *
     * @return lista de asociaciones role-menu
     */
    public List<RoleMenu> getRoleMenus() {
        return roleMenus;
    }

    /**
     * Anade una relacion {@code RoleMenu} a este rol.
     *
     * @param rm objeto que representa la asociacion a un MenuItem
     */
    public void addRoleMenu(RoleMenu rm) {
        this.roleMenus.add(rm);
    }
}
