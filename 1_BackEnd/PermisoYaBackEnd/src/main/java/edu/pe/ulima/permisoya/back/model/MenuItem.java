package edu.pe.ulima.permisoya.back.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa un elemento de menu dentro de la aplicacion,
 * con soporte para jerarquia padre-hijo y asociacion a roles.
 *
 * Cada {@code MenuItem} puede contener multiples hijos y conocer su elemento
 * padre, permitiendo construir un arbol de navegacion.
 * Ademas, mantiene una lista de relaciones {@code RoleMenu} para saber
 * que roles tienen acceso a este item.
 *
 * @author Henry Wong <hwong@ulima.edu.pe>
 */
public class MenuItem {


    private Integer idMenuItem;
    private String nombre;
    private Integer orden;
    private MenuItem parent;
    private List<MenuItem> children = new ArrayList<>();
    private List<RoleMenu> roleMenus = new ArrayList<>();

    /** 
     * Constructor vacio. 
     */
    public MenuItem() {
    }

    /**
     * Construye un item de menu con valores iniciales.
     *
     * @param idMenuItem clave primaria del menu
     * @param nombre     etiqueta visible
     * @param orden      posicion relativa en la lista
     * @param parent     elemento padre (null si es un item raiz)
     */
    public MenuItem(Integer idMenuItem, String nombre, Integer orden, MenuItem parent) {
        this.idMenuItem = idMenuItem;
        this.nombre     = nombre;
        this.orden      = orden;
        this.parent     = parent;
    }

    /**
     * Devuelve el identificador unico del item de menu.
     *
     * @return id unico del item de menu
     */
    public Integer getIdMenuItem() {
        return idMenuItem;
    }

    /**
     * Asigna el identificador unico del item de menu.
     *
     * @param idMenuItem identificador unico a asignar
     */
    public void setIdMenuItem(Integer idMenuItem) {
        this.idMenuItem = idMenuItem;
    }

    /**
     * Devuelve la etiqueta mostrada en la interfaz.
     *
     * @return etiqueta visible del item
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece la etiqueta mostrada en la interfaz.
     *
     * @param nombre nueva etiqueta del item
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Devuelve el orden de presentacion dentro de su nivel.
     *
     * @return posicion relativa de este item
     */
    public Integer getOrden() {
        return orden;
    }

    /**
     * Define el orden de presentacion dentro de su nivel.
     *
     * @param orden nueva posicion relativa
     */
    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    /**
     * Devuelve el elemento padre de este item.
     *
     * @return elemento padre, o null si es raiz
     */
    public MenuItem getParent() {
        return parent;
    }

    /**
     * Asigna el elemento padre de este item.
     *
     * @param parent elemento padre a asignar
     */
    public void setParent(MenuItem parent) {
        this.parent = parent;
    }

    /**
     * Devuelve la lista de items hijos.
     *
     * @return lista de items hijos
     */
    public List<MenuItem> getChildren() {
        return children;
    }

    /**
     * Anade un item hijo, configurando automaticamente su referencia padre.
     *
     * @param child elemento hijo a agregar
     */
    public void addChild(MenuItem child) {
        child.setParent(this);
        this.children.add(child);
    }

    /**
     * Devuelve la lista de relaciones RoleMenu asociadas a este item.
     *
     * @return lista de asociaciones role-menu
     */
    public List<RoleMenu> getRoleMenus() {
        return roleMenus;
    }

    /**
     * Anade una relacion RoleMenu a este item.
     *
     * @param rm objeto que representa la asociacion con un rol
     */
    public void addRoleMenu(RoleMenu rm) {
        this.roleMenus.add(rm);
    }
}
