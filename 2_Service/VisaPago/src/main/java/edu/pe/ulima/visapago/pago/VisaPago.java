package edu.pe.ulima.visapago.pago;

/**
 * Clase utilitaria que simula el servicio de pago de Visa.
 * Proporciona un metodo para verificar si el servicio esta operativo.
 *
 * @author Henry Wong <hwong@ulima.edu.pe>
 */
public final class VisaPago {

    /**
     * Simula el estado del servicio de pago de Visa de manera aleatoria.
     * <p>
     * Tiene un 80% de probabilidad de devolver true (servicio operativo)
     * y un 20% de probabilidad de devolver false (servicio no operativo).
     * </p>
     *
     * @return true si el servicio esta operativo, false en caso contrario
     */
    public final static boolean servicioOperativo() {
        return Math.random() < 0.8;
    }
}
