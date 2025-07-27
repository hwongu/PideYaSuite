package edu.pe.ulima.pideya.back.dao;

import edu.pe.ulima.pideya.back.model.ClienteEmpresa;
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
 * DAO para la entidad ClienteEmpresa. Maneja insercion, actualizacion,
 * eliminacion, obtencion y listado combinando tablas Cliente y ClienteEmpresa.
 * Extiende de Conexion para gestion de conexion y transacciones.
 *
 * @author Henry Wong <hwong@ulima.edu.pe>
 */
public class ClienteEmpresaDAO extends Conexion<ClienteEmpresa> {

    // SQL para operaciones en tablas Cliente, ClienteEmpresa y Usuario
    private static final String INSERT_CLIENTE_SQL
            = "INSERT INTO PideYa.Cliente(fechaCreacion) VALUES(?)";
    private static final String INSERT_EMPRESA_SQL
            = "INSERT INTO PideYa.ClienteEmpresa(idCliente, razonSocial, ruc) VALUES(?, ?, ?)";
    private static final String INSERT_USUARIO_SQL
            = "INSERT INTO PideYa.Usuario(idCliente, correo, clave, tipoUsuario) VALUES(?, ?, ?, ?)";
    private static final String UPDATE_EMPRESA_SQL
            = "UPDATE PideYa.ClienteEmpresa SET razonSocial = ?, ruc = ? WHERE idCliente = ?";
    private static final String UPDATE_USUARIO_SQL
            = "UPDATE PideYa.Usuario SET correo = ?, clave = ?, tipoUsuario = ? WHERE idUsuario = ?";
    private static final String DELETE_USUARIO_SQL
            = "DELETE FROM PideYa.Usuario WHERE idCliente = ?";
    private static final String DELETE_EMPRESA_SQL
            = "DELETE FROM PideYa.ClienteEmpresa WHERE idCliente = ?";
    private static final String DELETE_CLIENTE_SQL
            = "DELETE FROM PideYa.Cliente WHERE idCliente = ?";
    private static final String SELECT_ONE_SQL
            = "SELECT c.idCliente, c.fechaCreacion, ce.razonSocial, ce.ruc "
            + "FROM PideYa.Cliente c "
            + "JOIN PideYa.ClienteEmpresa ce ON c.idCliente = ce.idCliente "
            + "WHERE c.idCliente = ?";
    private static final String SELECT_ALL_SQL
            = "SELECT c.idCliente, c.fechaCreacion, ce.razonSocial, ce.ruc "
            + "FROM PideYa.Cliente c "
            + "JOIN PideYa.ClienteEmpresa ce ON c.idCliente = ce.idCliente "
            + "ORDER BY ce.razonSocial";
    private static final String SELECT_BY_RAZON_SOCIAL_SQL
            = "SELECT c.idCliente, c.fechaCreacion, ce.razonSocial, ce.ruc, "
            + "u.idUsuario, u.correo, u.clave, u.tipoUsuario "
            + "FROM PideYa.Cliente c "
            + "JOIN PideYa.ClienteEmpresa ce ON c.idCliente = ce.idCliente "
            + "LEFT JOIN PideYa.Usuario u ON u.idCliente = c.idCliente "
            + "WHERE ce.razonSocial LIKE ? "
            + "ORDER BY ce.razonSocial";

    /**
     * Inserta en Cliente, ClienteEmpresa y Usuario.
     *
     * @param ce ClienteEmpresa con datos a insertar
     * @return ClienteEmpresa con idCliente y usuario asignados
     * @throws SQLException en caso de error SQL
     */
    @Override
    public ClienteEmpresa insertar(ClienteEmpresa ce) throws SQLException {
        // Paso 1: Insertar en tabla Cliente
        try (PreparedStatement psCl = getConnection().prepareStatement(
                INSERT_CLIENTE_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            LocalDateTime now = ce.getFechaCreacion() != null
                    ? ce.getFechaCreacion()
                    : LocalDateTime.now();
            psCl.setTimestamp(1, Timestamp.valueOf(now));
            psCl.executeUpdate();
            try (ResultSet rs = psCl.getGeneratedKeys()) {
                if (rs.next()) {
                    ce.setIdCliente(rs.getInt(1));
                    ce.setFechaCreacion(now);
                }
            }
        }

        // Paso 2: Insertar en tabla ClienteEmpresa
        try (PreparedStatement psEmp = getConnection().prepareStatement(INSERT_EMPRESA_SQL)) {
            psEmp.setInt(1, ce.getIdCliente());
            psEmp.setString(2, ce.getRazonSocial().toUpperCase().trim());
            psEmp.setString(3, ce.getRuc());
            psEmp.executeUpdate();
        }

        // Paso 3: Insertar en tabla Usuario
        try (PreparedStatement psUsu = getConnection().prepareStatement(
                INSERT_USUARIO_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            psUsu.setInt(1, ce.getIdCliente());
            psUsu.setString(2, ce.getUsuario().getCorreo());
            psUsu.setString(3, ce.getUsuario().getClave());
            psUsu.setString(4, TipoUsuario.CLIENTE_EMPRESA.name());
            psUsu.executeUpdate();
            try (ResultSet rs = psUsu.getGeneratedKeys()) {
                if (rs.next()) {
                    ce.getUsuario().setIdUsuario(rs.getInt(1));
                }
            }
        }

        commit();
        return ce;
    }

    /**
     * Actualiza datos de ClienteEmpresa y Usuario.
     *
     * @param ce ClienteEmpresa con idCliente y valores nuevos
     * @return ClienteEmpresa actualizado o null si no existe
     * @throws SQLException en caso de error SQL
     */
    @Override
    public ClienteEmpresa actualizar(ClienteEmpresa ce) throws SQLException {
        boolean ok;
        // Paso 1: Actualizar datos de ClienteEmpresa
        try (PreparedStatement psEmp = getConnection().prepareStatement(UPDATE_EMPRESA_SQL)) {
            psEmp.setString(1, ce.getRazonSocial().toUpperCase().trim());
            psEmp.setString(2, ce.getRuc());
            psEmp.setInt(3, ce.getIdCliente());
            ok = psEmp.executeUpdate() > 0;
        }

        // Paso 2: Actualizar datos de Usuario
        try (PreparedStatement psUsu = getConnection().prepareStatement(UPDATE_USUARIO_SQL)) {
            psUsu.setString(1, ce.getUsuario().getCorreo());
            psUsu.setString(2, ce.getUsuario().getClave());
            psUsu.setString(3, TipoUsuario.CLIENTE_EMPRESA.name());
            psUsu.setInt(4, ce.getUsuario().getIdUsuario());
            ok &= psUsu.executeUpdate() > 0;
        }

        commit();
        return ok ? ce : null;
    }

    /**
     * Elimina registros de Usuario, ClienteEmpresa y Cliente.
     *
     * @param ce ClienteEmpresa con idCliente a eliminar
     * @return ClienteEmpresa eliminado o null si no existia
     * @throws SQLException en caso de error SQL
     */
    @Override
    public ClienteEmpresa eliminar(ClienteEmpresa ce) throws SQLException {
        boolean okUsu;
        try (PreparedStatement psUsu = getConnection().prepareStatement(DELETE_USUARIO_SQL)) {
            psUsu.setInt(1, ce.getIdCliente());
            okUsu = psUsu.executeUpdate() > 0;
        }

        boolean okEmp;
        try (PreparedStatement psEmp = getConnection().prepareStatement(DELETE_EMPRESA_SQL)) {
            psEmp.setInt(1, ce.getIdCliente());
            okEmp = psEmp.executeUpdate() > 0;
        }

        boolean okCl;
        try (PreparedStatement psCl = getConnection().prepareStatement(DELETE_CLIENTE_SQL)) {
            psCl.setInt(1, ce.getIdCliente());
            okCl = psCl.executeUpdate() > 0;
        }

        commit();
        return (okCl && okEmp && okUsu) ? ce : null;
    }

    /**
     * Obtiene un ClienteEmpresa por su ID.
     *
     * @param ce ClienteEmpresa con idCliente a buscar
     * @return ClienteEmpresa completo o null si no existe
     * @throws SQLException en caso de error SQL
     */
    @Override
    public ClienteEmpresa obtener(ClienteEmpresa ce) throws SQLException {
        try (PreparedStatement ps = getConnection().prepareStatement(SELECT_ONE_SQL)) {
            ps.setInt(1, ce.getIdCliente());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ClienteEmpresa(
                            rs.getInt("idCliente"),
                            rs.getTimestamp("fechaCreacion").toLocalDateTime(),
                            null,
                            rs.getString("razonSocial"),
                            rs.getString("ruc")
                    );
                }
            }
        }
        return null;
    }

    /**
     * Lista todos los ClienteEmpresa.
     *
     * @return lista de ClienteEmpresa
     * @throws SQLException en caso de error SQL
     */
    @Override
    public List<ClienteEmpresa> listar() throws SQLException {
        List<ClienteEmpresa> lista = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(SELECT_ALL_SQL); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new ClienteEmpresa(
                        rs.getInt("idCliente"),
                        rs.getTimestamp("fechaCreacion").toLocalDateTime(),
                        null,
                        rs.getString("razonSocial").toUpperCase(),
                        rs.getString("ruc")
                ));
            }
        }
        return lista;
    }

    /**
     * Lista empresas con prefijo de razon social e incluye datos de Usuario.
     *
     * @param prefijo inicio para filtrar razon social
     * @return lista de ClienteEmpresa con Usuario asociado
     * @throws SQLException en caso de error SQL
     */
    public List<ClienteEmpresa> listarPorRazonSocial(String prefijo) throws SQLException {
        if (prefijo == null) {
            throw new IllegalArgumentException("El prefijo no puede ser null");
        }
        List<ClienteEmpresa> resultados = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(SELECT_BY_RAZON_SOCIAL_SQL)) {
            ps.setString(1, prefijo + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ClienteEmpresa ce = new ClienteEmpresa(
                            rs.getInt("idCliente"),
                            rs.getTimestamp("fechaCreacion").toLocalDateTime(),
                            null,
                            rs.getString("razonSocial").toUpperCase().trim(),
                            rs.getString("ruc")
                    );
                    int idUsu = rs.getInt("idUsuario");
                    if (!rs.wasNull()) {
                        Usuario user = new Usuario(
                                idUsu,
                                rs.getString("correo"),
                                rs.getString("clave"),
                                ce,
                                TipoUsuario.valueOf(rs.getString("tipoUsuario"))
                        );
                        ce.setUsuario(user);
                    }
                    resultados.add(ce);
                }
            }
        }
        return resultados;
    }
}
