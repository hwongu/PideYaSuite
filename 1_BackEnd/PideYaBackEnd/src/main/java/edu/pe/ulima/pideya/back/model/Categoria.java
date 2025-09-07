package edu.pe.ulima.pideya.back.model;

import java.util.Objects;

/**
 * Modelo para la entidad Categoria. Representa una categoria con idCategoria y
 * nombre.
 *
 * @author Henry Wong <hwong@ulima.edu.pe>
 */
public class Categoria {

    private Integer idCategoria;
    private String nombre;

    /**
     * Constructor vacio.
     */
    public Categoria() {
    }

    /**
     * Constructor con idCategoria y nombre.
     *
     * @param idCategoria id de la categoria
     * @param nombre nombre de la categoria
     */
    public Categoria(Integer idCategoria, String nombre) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
    }

    /**
     * Obtiene el id de la categoria.
     *
     * @return idCategoria
     */
    public Integer getIdCategoria() {
        return idCategoria;
    }

    /**
     * Asigna el id de la categoria.
     *
     * @param idCategoria id a asignar
     */
    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    /**
     * Obtiene el nombre de la categoria.
     *
     * @return nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna el nombre de la categoria.
     *
     * @param nombre nombre a asignar
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Retorna el nombre en mayusculas.
     *
     * @return nombre en mayusculas
     */
    @Override
    public String toString() {
        return nombre.toUpperCase();
    }

    /**
     * Compara esta categoria con otro objeto por idCategoria.
     *
     * @param o objeto a comparar
     * @return true si son iguales, false en caso contrario
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Categoria)) {
            return false;
        }
        Categoria that = (Categoria) o;
        return Objects.equals(idCategoria, that.idCategoria);
    }

    /**
     * Retorna el hash code basado en idCategoria.
     *
     * @return hash code de la categoria
     */
    @Override
    public int hashCode() {
        return Objects.hash(idCategoria);
    }
}
