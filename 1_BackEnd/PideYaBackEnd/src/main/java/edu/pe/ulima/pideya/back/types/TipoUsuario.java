package edu.pe.ulima.pideya.back.types;

/**
 * Enumeración de los tipos de usuario disponibles en el sistema. Cada tipo
 * incluye una descripción que puede usarse en la interfaz.
 *
 * @author Henry Wong <hwong@ulima.edu.pe>
 */
public enum TipoUsuario {

    /**
     * Usuario con todos los privilegios administrativos.
     */
    ADMINISTRADOR("Usuario con privilegios de Administrador"),
    /**
     * Usuario persona natural con permisos de cliente.
     */
    CLIENTE_NATURAL("Usuario con privilegios de Cliente Natural"),
    /**
     * Usuario empresa con permisos de cliente corporativo.
     */
    CLIENTE_EMPRESA("Usuario con privilegios de Cliente Empresa");

    /**
     * Descripción legible del tipo de usuario.
     */
    private final String descripcion;

    /**
     * Constructor que asigna la descripción a cada tipo.
     *
     * @param descripcion texto descriptivo del tipo de usuario
     */
    TipoUsuario(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene la descripción legible del tipo de usuario.
     *
     * @return descripción asociada al enum
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Representación en String de este enum, mostrando su descripción.
     *
     * @return cadena con el nombre del enum y su descripción
     */
    @Override
    public String toString() {
        return "TipoUsuario{descripcion=" + descripcion + '}';
    }
}
