package edu.pe.ulima.pideya.back.model;

import java.util.Objects;

/**
 * Modelo para la entidad Producto. Representa un producto con nombre, precio,
 * stock y categoria.
 *
 * @author Henry Wong <hwong@ulima.edu.pe>
 */
public class Producto {

    private Integer idProducto;
    private String nombre;
    private Double precio;
    private Double stock;
    private Categoria categoria;

    /**
     * Constructor vacio.
     */
    public Producto() {
    }

    /**
     * Constructor con todos los campos.
     *
     * @param idProducto id del producto
     * @param nombre nombre del producto
     * @param precio precio unitario
     * @param stock cantidad disponible en stock
     * @param categoria categoria del producto
     */
    public Producto(Integer idProducto, String nombre, Double precio, Double stock, Categoria categoria) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
    }

    /**
     * Obtiene el id del producto.
     *
     * @return idProducto
     */
    public Integer getIdProducto() {
        return idProducto;
    }

    /**
     * Asigna el id del producto.
     *
     * @param idProducto id a asignar
     */
    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    /**
     * Obtiene el nombre del producto.
     *
     * @return nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna el nombre del producto.
     *
     * @param nombre nombre a asignar
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el precio unitario.
     *
     * @return precio
     */
    public Double getPrecio() {
        return precio;
    }

    /**
     * Asigna el precio unitario.
     *
     * @param precio precio a asignar
     */
    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    /**
     * Obtiene la cantidad disponible en stock.
     *
     * @return stock
     */
    public Double getStock() {
        return stock;
    }

    /**
     * Asigna la cantidad disponible en stock.
     *
     * @param stock stock a asignar
     */
    public void setStock(Double stock) {
        this.stock = stock;
    }

    /**
     * Obtiene la categoria del producto.
     *
     * @return categoria
     */
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * Asigna la categoria del producto.
     *
     * @param categoria categoria a asignar
     */
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    /**
     * Devuelve el nombre en mayusculas.
     *
     * @return nombre en mayusculas
     */
    @Override
    public String toString() {
        return nombre.toUpperCase();
    }

    /**
     * Compara productos por idProducto.
     *
     * @param o objeto a comparar
     * @return true si tienen mismo idProducto
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Producto)) {
            return false;
        }
        Producto that = (Producto) o;
        return Objects.equals(idProducto, that.idProducto);
    }

    /**
     * Retorna hash code basado en idProducto.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(idProducto);
    }
}
