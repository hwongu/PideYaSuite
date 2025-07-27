package edu.pe.ulima.pideya.back.dao;

import edu.pe.ulima.pideya.back.model.ClienteNatural;
import edu.pe.ulima.pideya.back.model.Usuario;
import edu.pe.ulima.pideya.back.types.TipoUsuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad ClienteNatural. Maneja insercion, actualizacion,
 * eliminacion, obtencion y listado combinando tablas Cliente y ClienteNatural.
 * Extiende de Conexion para gestion de conexion y transacciones.
 *
 * @author Henry Wong <hwong@ulima.edu.pe>
 */
public class ClienteNaturalDAO extends Conexion<ClienteNatural> {

    // SQL para operaciones en tablas Cliente, ClienteNatural y Usuario
    private static final String INSERT_CLIENTE_SQL
            = "INSERT INTO PideYa.Cliente(fechaCreacion) VALUES(?)";
    private static final String INSERT_NATURAL_SQL
            = "INSERT INTO PideYa.ClienteNatural(idCliente, nombre, apellido, dni) VALUES(?, ?, ?, ?)";
    private static final String INSERT_USUARIO_SQL
            = "INSERT INTO PideYa.Usuario(idCliente, correo, clave, tipoUsuario) VALUES(?, ?, ?, ?)";
    private static final String UPDATE_NATURAL_SQL
            = "UPDATE PideYa.ClienteNatural SET nombre = ?, apellido = ?, dni = ? WHERE idCliente = ?";
    private static final String UPDATE_USUARIO_SQL
            = "UPDATE PideYa.Usuario SET correo = ?, clave = ?, tipoUsuario = ? WHERE idUsuario = ?";
    private static final String DELETE_USUARIO_SQL
            = "DELETE FROM PideYa.Usuario WHERE idCliente = ?";
    private static final String DELETE_NATURAL_SQL
            = "DELETE FROM PideYa.ClienteNatural WHERE idCliente = ?";
    private static final String DELETE_CLIENTE_SQL
            = "DELETE FROM PideYa.Cliente WHERE idCliente = ?";
    private static final String SELECT_ONE_SQL
            = "SELECT c.idCliente, c.fechaCreacion, cn.nombre, cn.apellido, cn.dni "
            + "FROM PideYa.Cliente c "
            + "JOIN PideYa.ClienteNatural cn ON c.idCliente = cn.idCliente "
            + "WHERE c.idCliente = ?";
    private static final String SELECT_ALL_SQL
            = "SELECT c.idCliente, c.fechaCreacion, cn.nombre, cn.apellido, cn.dni, "
            + "u.idUsuario, u.correo, u.clave, u.tipoUsuario "
            + "FROM PideYa.Cliente c "
            + "JOIN PideYa.ClienteNatural cn ON c.idCliente = cn.idCliente "
            + "LEFT JOIN PideYa.Usuario u ON u.idCliente = c.idCliente "
            + "WHERE u.tipoUsuario != 'ADMINISTRADOR' "
            + "ORDER BY cn.apellido";
    private static final String SELECT_BY_NOMBRE_SQL
            = "SELECT c.idCliente, c.fechaCreacion, cn.nombre, cn.apellido, cn.dni, "
            + "u.idUsuario, u.correo, u.clave, u.tipoUsuario "
            + "FROM PideYa.Cliente c "
            + "JOIN PideYa.ClienteNatural cn ON c.idCliente = cn.idCliente "
            + "LEFT JOIN PideYa.Usuario u ON u.idCliente = c.idCliente "
            + "WHERE cn.nombre LIKE ? AND u.tipoUsuario != 'ADMINISTRADOR' "
            + "ORDER BY cn.apellido, cn.nombre";

    /**
     * Inserta en Cliente, ClienteNatural y Usuario.
     *
     * @param cn ClienteNatural con datos a insertar
     * @return ClienteNatural con idCliente y usuario asignados
     * @throws SQLException en caso de error SQL
     */
    @Override
    public ClienteNatural insertar(ClienteNatural cn) throws SQLException {
        // Paso 1: insertar en Cliente
        try (PreparedStatement psCl = getConnection().prepareStatement(
                INSERT_CLIENTE_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            LocalDateTime now = cn.getFechaCreacion() != null
                    ? cn.getFechaCreacion()
                    : LocalDateTime.now();
            psCl.setTimestamp(1, Timestamp.valueOf(now));
            psCl.executeUpdate();
            try (ResultSet rs = psCl.getGeneratedKeys()) {
                if (rs.next()) {
                    cn.setIdCliente(rs.getInt(1));
                    cn.setFechaCreacion(now);
                }
            }
        }

        // Paso 2: insertar en ClienteNatural
        try (PreparedStatement psNat = getConnection().prepareStatement(INSERT_NATURAL_SQL)) {
            psNat.setInt(1, cn.getIdCliente());
            psNat.setString(2, cn.getNombre());
            psNat.setString(3, cn.getApellido());
            psNat.setInt(4, cn.getDNI());
            psNat.executeUpdate();
        }

        // Paso 3: insertar en Usuario
        try (PreparedStatement psUsu = getConnection().prepareStatement(
                INSERT_USUARIO_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            psUsu.setInt(1, cn.getIdCliente());
            psUsu.setString(2, cn.getUsuario().getCorreo());
            psUsu.setString(3, cn.getUsuario().getClave());
            psUsu.setString(4, TipoUsuario.CLIENTE_NATURAL.name());
            psUsu.executeUpdate();
            try (ResultSet rs = psUsu.getGeneratedKeys()) {
                if (rs.next()) {
                    cn.getUsuario().setIdUsuario(rs.getInt(1));
                }
            }
        }

        commit();
        return cn;
    }

    /**
     * Actualiza datos de ClienteNatural y Usuario.
     *
     * @param cn ClienteNatural con idCliente y valores nuevos
     * @return ClienteNatural actualizado o null si no existe
     * @throws SQLException en caso de error SQL
     */
    @Override
    public ClienteNatural actualizar(ClienteNatural cn) throws SQLException {
        boolean ok;
        // actualizar ClienteNatural
        try (PreparedStatement psNat = getConnection().prepareStatement(UPDATE_NATURAL_SQL)) {
            psNat.setString(1, cn.getNombre());
            psNat.setString(2, cn.getApellido());
            psNat.setInt(3, cn.getDNI());
            psNat.setInt(4, cn.getIdCliente());
            ok = psNat.executeUpdate() > 0;
        }
        // actualizar Usuario
        try (PreparedStatement psUsu = getConnection().prepareStatement(UPDATE_USUARIO_SQL)) {
            psUsu.setString(1, cn.getUsuario().getCorreo());
            psUsu.setString(2, cn.getUsuario().getClave());
            psUsu.setString(3, TipoUsuario.CLIENTE_NATURAL.name());
            psUsu.setInt(4, cn.getUsuario().getIdUsuario());
            ok &= psUsu.executeUpdate() > 0;
        }

        commit();
        return ok ? cn : null;
    }

    /**
     * Elimina registros de Usuario, ClienteNatural y Cliente.
     *
     * @param cn ClienteNatural con idCliente a eliminar
     * @return ClienteNatural eliminado o null si no existia
     * @throws SQLException en caso de error SQL
     */
    @Override
    public ClienteNatural eliminar(ClienteNatural cn) throws SQLException {
        boolean okUsu;
        try (PreparedStatement psUsu = getConnection().prepareStatement(DELETE_USUARIO_SQL)) {
            psUsu.setInt(1, cn.getIdCliente());
            okUsu = psUsu.executeUpdate() > 0;
        }
        boolean okNat;
        try (PreparedStatement psNat = getConnection().prepareStatement(DELETE_NATURAL_SQL)) {
            psNat.setInt(1, cn.getIdCliente());
            okNat = psNat.executeUpdate() > 0;
        }
        boolean okCl;
        try (PreparedStatement psCl = getConnection().prepareStatement(DELETE_CLIENTE_SQL)) {
            psCl.setInt(1, cn.getIdCliente());
            okCl = psCl.executeUpdate() > 0;
        }

        commit();
        return (okUsu && okNat && okCl) ? cn : null;
    }

    /**
     * Obtiene un ClienteNatural por su ID.
     *
     * @param cn ClienteNatural con idCliente buscado
     * @return ClienteNatural completo o null si no existe
     * @throws SQLException en caso de error SQL
     */
    @Override
    public ClienteNatural obtener(ClienteNatural cn) throws SQLException {
        try (PreparedStatement ps = getConnection().prepareStatement(SELECT_ONE_SQL)) {
            ps.setInt(1, cn.getIdCliente());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ClienteNatural(
                            rs.getInt("idCliente"),
                            rs.getTimestamp("fechaCreacion").toLocalDateTime(),
                            null,
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getInt("dni")
                    );
                }
            }
        }
        return null;
    }

    /**
     * Lista todos los ClienteNatural.
     *
     * @return lista de ClienteNatural
     * @throws SQLException en caso de error SQL
     */
    @Override
    public List<ClienteNatural> listar() throws SQLException {
        List<ClienteNatural> lista = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(SELECT_ALL_SQL); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ClienteNatural cn = new ClienteNatural(
                        rs.getInt("idCliente"),
                        rs.getTimestamp("fechaCreacion").toLocalDateTime(),
                        null,
                        rs.getString("nombre").toUpperCase(),
                        rs.getString("apellido").toUpperCase(),
                        rs.getInt("dni")
                );
                // asignar usuario si existe
                int idUsr = rs.getInt("idUsuario");
                if (!rs.wasNull()) {
                    Usuario u = new Usuario(
                            idUsr,
                            rs.getString("correo"),
                            rs.getString("clave"),
                            cn,
                            TipoUsuario.valueOf(rs.getString("tipoUsuario"))
                    );
                    cn.setUsuario(u);
                }
                lista.add(cn);
            }
        }
        return lista;
    }

    /**
     * Lista ClienteNatural cuyo nombre comienza con el prefijo.
     *
     * @param nombrePrefijo prefijo para filtro LIKE
     * @return lista de ClienteNatural con Usuario asociado
     * @throws SQLException en caso de error SQL
     */
    public List<ClienteNatural> listarPorNombre(String nombrePrefijo) throws SQLException {
        if (nombrePrefijo == null) {
            throw new IllegalArgumentException("El nombrePrefijo no puede ser null");
        }
        List<ClienteNatural> lista = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(SELECT_BY_NOMBRE_SQL)) {
            ps.setString(1, nombrePrefijo + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ClienteNatural cn = new ClienteNatural(
                            rs.getInt("idCliente"),
                            rs.getTimestamp("fechaCreacion").toLocalDateTime(),
                            null,
                            rs.getString("nombre").toUpperCase(),
                            rs.getString("apellido").toUpperCase(),
                            rs.getInt("dni")
                    );
                    int idUsr = rs.getInt("idUsuario");
                    if (!rs.wasNull()) {
                        Usuario u = new Usuario(
                                idUsr,
                                rs.getString("correo"),
                                rs.getString("clave"),
                                cn,
                                TipoUsuario.valueOf(rs.getString("tipoUsuario"))
                        );
                        cn.setUsuario(u);
                    }
                    lista.add(cn);
                }
            }
        }
        return lista;
    }
}
