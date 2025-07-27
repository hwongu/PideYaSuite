package edu.pe.ulima.pideya.back.model;

import java.time.LocalDateTime;

/**
 * Modelo para la entidad ClienteNatural. Representa un cliente natural con
 * nombre, apellido y DNI.
 *
 * @author Henry Wong <hwong@ulima.edu.pe>
 */
public class ClienteNatural extends Cliente {

    private String nombre;
    private String apellido;
    private Integer DNI;

    /**
     * Constructor vacio.
     */
    public ClienteNatural() {
    }

    /**
     * Constructor con idCliente, fecha creacion, usuario, nombre, apellido y
     * DNI.
     *
     * @param idCliente id del cliente
     * @param fechaCreacion fecha de creacion del cliente
     * @param usuario usuario asociado al cliente
     * @param nombre nombre del cliente natural
     * @param apellido apellido del cliente natural
     * @param DNI dni del cliente natural
     */
    public ClienteNatural(Integer idCliente, LocalDateTime fechaCreacion, Usuario usuario,
            String nombre, String apellido, Integer DNI) {
        super(idCliente, fechaCreacion, usuario);
        this.nombre = nombre;
        this.apellido = apellido;
        this.DNI = DNI;
    }

    /**
     * Obtiene el nombre.
     *
     * @return nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Asigna el nombre.
     *
     * @param nombre nombre a asignar
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el apellido.
     *
     * @return apellido
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Asigna el apellido.
     *
     * @param apellido apellido a asignar
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * Obtiene el DNI.
     *
     * @return DNI
     */
    public Integer getDNI() {
        return DNI;
    }

    /**
     * Asigna el DNI.
     *
     * @param DNI dni a asignar
     */
    public void setDNI(Integer DNI) {
        this.DNI = DNI;
    }
    
    @Override
    public String toString() {
        return "CN : " + apellido.toUpperCase() + ", " + nombre.toUpperCase();
    }

}
