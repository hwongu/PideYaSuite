package edu.pe.ulima.permisoya.back.model;

import java.util.Objects;

/**
 * Entidad Role que representa un rol de usuario en el sistema.
 *
 * @author Henry Wong <hwong@ulima.edu.pe>
 */
public class Role {

    private Integer idRole;
    private String nombre;

    /**
     * Constructor vacio.
     */
    public Role() {
    }

    /**
     * Construye un Role completo.
     *
     * @param idRole clave primaria del rol
     * @param nombre texto que identifica el rol
     */
    public Role(Integer idRole, String nombre) {
        this.idRole = idRole;
        this.nombre = nombre;
    }

    /**
     * Obtiene el id del rol.
     *
     * @return idRole
     */
    public Integer getIdRole() {
        return idRole;
    }

    /**
     * Asigna el id del rol.
     *
     * @param idRole valor a asignar
     */
    public void setIdRole(Integer idRole) {
        this.idRole = idRole;
    }

    /**
     * Obtiene el nombre del rol.
     *
     * @return nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna el nombre del rol.
     *
     * @param nombre valor a asignar
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Role)) {
            return false;
        }
        Role that = (Role) o;
        return Objects.equals(idRole, that.idRole);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRole);
    }
}
