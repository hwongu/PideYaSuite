package edu.pe.ulima.permisoya.back.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase de utilidades para acceder a propiedades de configuración de la
 * aplicación.
 *
 * @author Henry Wong <hwong@ulima.edu.pe>
 */
public final class Utilitario {

    private static final Logger logger = LoggerFactory.getLogger(Utilitario.class);
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("propiedades");

    // Constructor privado para evitar instanciación
    private Utilitario() {
        throw new UnsupportedOperationException("Clase de utilidades estática");
    }

    /**
     * Obtiene el valor asociado a la clave en el archivo
     * propiedades.properties.
     *
     * @param clave La clave del valor que deseamos obtener.
     * @return El valor de la clave, o {@code null} si la clave no existe.
     */
    public static String obtenerPropiedad(String clave) {
        try {
            return BUNDLE.getString(clave);
        } catch (MissingResourceException e) {
            logger.warn("Clave no encontrada en el bundle de propiedades: {}", clave);
            return null;
        }
    }

    /**
     * Obtiene el valor asociado a la clave en el archivo
     * propiedades.properties, devolviendo un valor por defecto si la clave no
     * existe.
     *
     * @param clave La clave del valor que deseamos obtener.
     * @param valorDefault Valor por defecto si la clave no existe.
     * @return El valor de la clave, o {@code valorDefault} si la clave no
     * existe.
     */
    public static String obtenerPropiedad(String clave, String valorDefault) {
        String valor = obtenerPropiedad(clave);
        return (valor != null) ? valor : valorDefault;
    }
}
