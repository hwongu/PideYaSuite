package edu.pe.ulima.pideya.back.model;

import java.time.LocalDateTime;

/**
 * Modelo abstracto para la entidad Cliente. Representa los atributos comunes de
 * ClienteNatural y ClienteEmpresa.
 *
 * @author Henry Wong <hwong@ulima.edu.pe>
 */
public abstract class Cliente {

    private Integer idCliente;
    private LocalDateTime fechaCreacion;
    private Usuario usuario;

    /**
     * Constructor vacio.
     */
    public Cliente() {
    }

    /**
     * Constructor con idCliente, fechaCreacion y usuario.
     *
     * @param idCliente id del cliente
     * @param fechaCreacion fecha de creacion del cliente
     * @param usuario usuario asociado al cliente
     */
    public Cliente(Integer idCliente, LocalDateTime fechaCreacion, Usuario usuario) {
        this.idCliente = idCliente;
        this.fechaCreacion = fechaCreacion;
        this.usuario = usuario;
    }

    /**
     * Obtiene el id del cliente.
     *
     * @return idCliente
     */
    public Integer getIdCliente() {
        return idCliente;
    }

    /**
     * Asigna el id del cliente.
     *
     * @param idCliente id a asignar
     */
    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    /**
     * Obtiene la fecha de creacion.
     *
     * @return fechaCreacion
     */
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Asigna la fecha de creacion.
     *
     * @param fechaCreacion fecha a asignar
     */
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * Obtiene el usuario asociado.
     *
     * @return usuario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Asigna el usuario asociado.
     *
     * @param usuario usuario a asignar
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
