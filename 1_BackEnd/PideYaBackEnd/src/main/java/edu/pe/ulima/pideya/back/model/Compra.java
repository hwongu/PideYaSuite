package edu.pe.ulima.pideya.back.model;

import java.time.LocalDateTime;

/**
 * Modelo para la entidad Compra. Representa una compra realizada a partir de un
 * Pedido.
 *
 * @author Henry Wong <hwong@ulima.edu.pe>
 */
public class Compra {

    private Integer idCompra;
    private Pedido pedido;
    private LocalDateTime fechaCompra;

    /**
     * Constructor vacio.
     */
    public Compra() {
    }

    /**
     * Constructor con idCompra, pedido y fechaCompra.
     *
     * @param idCompra id de la compra
     * @param pedido pedido asociado a la compra
     * @param fechaCompra fecha en que se realizo la compra
     */
    public Compra(Integer idCompra, Pedido pedido, LocalDateTime fechaCompra) {
        this.idCompra = idCompra;
        this.pedido = pedido;
        this.fechaCompra = fechaCompra;
    }

    /**
     * Obtiene el id de la compra.
     *
     * @return idCompra
     */
    public Integer getIdCompra() {
        return idCompra;
    }

    /**
     * Asigna el id de la compra.
     *
     * @param idCompra id a asignar
     */
    public void setIdCompra(Integer idCompra) {
        this.idCompra = idCompra;
    }

    /**
     * Obtiene el pedido asociado.
     *
     * @return pedido
     */
    public Pedido getPedido() {
        return pedido;
    }

    /**
     * Asigna el pedido asociado.
     *
     * @param pedido pedido a asignar
     */
    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    /**
     * Obtiene la fecha de la compra.
     *
     * @return fechaCompra
     */
    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    /**
     * Asigna la fecha de la compra.
     *
     * @param fechaCompra fecha a asignar
     */
    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }
}
