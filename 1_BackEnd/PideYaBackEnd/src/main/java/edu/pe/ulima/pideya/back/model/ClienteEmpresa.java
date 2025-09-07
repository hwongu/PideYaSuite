package edu.pe.ulima.pideya.back.model;

import java.time.LocalDateTime;

/**
 * Modelo para la entidad ClienteEmpresa. Representa un cliente empresa con
 * razonSocial y ruc.
 *
 * @author Henry Wong <hwong@ulima.edu.pe>
 */
public class ClienteEmpresa extends Cliente {

    private String razonSocial;
    private String ruc;

    /**
     * Constructor vacio.
     */
    public ClienteEmpresa() {
    }

    /**
     * Constructor con idCliente, fechaCreacion, usuario, razonSocial y ruc.
     *
     * @param idCliente id del cliente
     * @param fechaCreacion fecha de creacion del cliente
     * @param usuario usuario asociado al cliente
     * @param razonSocial razon social de la empresa
     * @param ruc ruc de la empresa
     */
    public ClienteEmpresa(Integer idCliente, LocalDateTime fechaCreacion, Usuario usuario, String razonSocial, String ruc) {
        super(idCliente, fechaCreacion, usuario);
        this.razonSocial = razonSocial;
        this.ruc = ruc;
    }

    /**
     * Constructor con razonSocial y ruc.
     *
     * @param razonSocial razon social de la empresa
     * @param ruc ruc de la empresa
     */
    public ClienteEmpresa(String razonSocial, String ruc) {
        this.razonSocial = razonSocial;
        this.ruc = ruc;
    }

    /**
     * Obtiene la razon social.
     *
     * @return razonSocial
     */
    public String getRazonSocial() {
        return razonSocial;
    }

    /**
     * Asigna la razon social.
     *
     * @param razonSocial razonSocial a asignar
     */
    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    /**
     * Obtiene el ruc.
     *
     * @return ruc
     */
    public String getRuc() {
        return ruc;
    }

    /**
     * Asigna el ruc.
     *
     * @param ruc ruc a asignar
     */
    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    @Override
    public String toString() {
        return "CE : " + razonSocial.toUpperCase() + " - " + ruc;
    }

}
