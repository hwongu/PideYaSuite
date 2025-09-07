package edu.pe.ulima.pideya.back.service;

import edu.pe.ulima.pideya.back.dao.UsuarioDAO;
import edu.pe.ulima.pideya.back.model.Usuario;
import java.util.Objects;

/**
 * Servicio para gestionar operaciones CRUD sobre {@link Usuario}.
 * <p>
 * Esta clase encapsula la logica de negocio relacionada con usuarios y delega
 * las operaciones de persistencia al {@link UsuarioDAO}.
 * </p>
 *
 * @author Henry Wong <hwong@ulima.edu.pe>
 */
public class UsuarioService {

    /**
     * DAO responsable de la persistencia de usuarios.
     */
    private final UsuarioDAO usuarioDAO;

    /**
     * Construye el servicio usando el DAO por defecto.
     */
    public UsuarioService() {
        this(new UsuarioDAO());
    }

    /**
     * Construye el servicio con un DAO inyectado.
     *
     * @param usuarioDAO instancia de {@link UsuarioDAO}, no puede ser null
     */
    private UsuarioService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = Objects.requireNonNull(usuarioDAO, "usuarioDAO no puede ser null");
    }

    /**
     * Autentica un usuario dado su correo y clave.
     *
     * 1. Valida que correo y clave no sean null. 2. Delegar consulta al DAO.
     *
     * @param correo correo del usuario, no puede ser null
     * @param clave clave del usuario, no puede ser null
     * @return el Usuario completo si credenciales validas, o null si no existe
     * @throws IllegalArgumentException si correo o clave es null
     * @throws Exception si ocurre un error durante la consulta
     */
    public Usuario autenticarUsuario(String correo, String clave) throws Exception {
        Objects.requireNonNull(correo, "correo no puede ser null");
        Objects.requireNonNull(clave, "clave no puede ser null");
        return usuarioDAO.autenticar(correo, clave);
    }

}
