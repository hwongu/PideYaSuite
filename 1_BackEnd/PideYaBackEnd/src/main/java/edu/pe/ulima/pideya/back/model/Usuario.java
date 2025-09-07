package edu.pe.ulima.pideya.back.model;

import edu.pe.ulima.pideya.back.types.TipoUsuario;

/**
 * Modelo para la entidad Usuario. Representa un usuario con correo, clave,
 * cliente y tipoUsuario.
 *
 * @author Henry Wong <hwong@ulima.edu.pe>
 */
public class Usuario {

    private Integer idUsuario;
    private String correo;
    private String clave;
    private Cliente cliente;
    private TipoUsuario tipoUsuario;

    /**
     * Constructor vacio.
     */
    public Usuario() {
    }

    /**
     * Constructor con idUsuario, correo, clave, cliente y tipoUsuario.
     *
     * @param idUsuario id del usuario
     * @param correo correo del usuario
     * @param clave clave del usuario
     * @param cliente cliente asociado
     * @param tipoUsuario tipo de usuario
     */
    public Usuario(Integer idUsuario, String correo, String clave, Cliente cliente, TipoUsuario tipoUsuario) {
        this.idUsuario = idUsuario;
        this.correo = correo;
        this.clave = clave;
        this.cliente = cliente;
        this.tipoUsuario = tipoUsuario;
    }

    /**
     * Obtiene el id del usuario.
     *
     * @return idUsuario
     */
    public Integer getIdUsuario() {
        return idUsuario;
    }

    /**
     * Asigna el id del usuario.
     *
     * @param idUsuario id a asignar
     */
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Obtiene el correo del usuario.
     *
     * @return correo
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Asigna el correo del usuario.
     *
     * @param correo correo a asignar
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Obtiene la clave del usuario.
     *
     * @return clave
     */
    public String getClave() {
        return clave;
    }

    /**
     * Asigna la clave del usuario.
     *
     * @param clave clave a asignar
     */
    public void setClave(String clave) {
        this.clave = clave;
    }

    /**
     * Obtiene el cliente asociado.
     *
     * @return cliente
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * Asigna el cliente asociado.
     *
     * @param cliente cliente a asignar
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * Obtiene el tipo de usuario.
     *
     * @return tipoUsuario
     */
    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    /**
     * Asigna el tipo de usuario.
     *
     * @param tipoUsuario tipo a asignar
     */
    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
}
