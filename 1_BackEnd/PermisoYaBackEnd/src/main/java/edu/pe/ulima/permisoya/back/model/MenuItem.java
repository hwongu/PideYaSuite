package edu.pe.ulima.permisoya.back.model;

import java.util.Objects;

/**
 * Entidad MenuItem que representa un elemento de menu, con posible relacion padre-hijo.
 *
  * @author Henry Wong <hwong@ulima.edu.pe>
 */
public class MenuItem {

    private Integer idMenuItem;
    private String nombre;
    private Integer parentId;
    private Integer orden;

    /**
     * Constructor vacio.
     */
    public MenuItem() {
    }

    /**
     * Construye un MenuItem completo.
     *
     * @param idMenuItem clave primaria del menu
     * @param nombre     texto a mostrar en el menu
     * @param parentId   id del menu padre, null si es raiz
     * @param orden      orden de presentacion
     */
    public MenuItem(Integer idMenuItem, String nombre, Integer parentId, Integer orden) {
        this.idMenuItem = idMenuItem;
        this.nombre = nombre;
        this.parentId = parentId;
        this.orden = orden;
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

    /**
     * Obtiene el nombre del MenuItem.
     *
     * @return nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna el nombre del MenuItem.
     *
     * @param nombre valor a asignar
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el id del MenuItem padre.
     *
     * @return parentId o null si no tiene padre
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * Asigna el id del MenuItem padre.
     *
     * @param parentId valor a asignar
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * Obtiene la propiedad orden.
     *
     * @return orden
     */
    public Integer getOrden() {
        return orden;
    }

    /**
     * Asigna la propiedad orden.
     *
     * @param orden valor a asignar
     */
    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuItem)) return false;
        MenuItem that = (MenuItem) o;
        return Objects.equals(idMenuItem, that.idMenuItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMenuItem);
    }
}
