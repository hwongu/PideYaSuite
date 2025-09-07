package edu.pe.ulima.pideya.back.dao;

import edu.pe.ulima.pideya.back.model.Usuario;
import edu.pe.ulima.pideya.back.model.Cliente;
import edu.pe.ulima.pideya.back.model.ClienteEmpresa;
import edu.pe.ulima.pideya.back.model.ClienteNatural;
import edu.pe.ulima.pideya.back.types.TipoUsuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la entidad Usuario. Implementa operaciones CRUD sobre la tabla
 * PideYa.Usuario, gestionando transacciones y mapeo a objeto Usuario.
 *
 * @author Henry Wong <hwong@ulima.edu.pe>
 */
public class UsuarioDAO extends Conexion<Usuario> {

    // Sentencias SQL parametrizadas para cada operacion
    private static final String INSERT_SQL =
        "INSERT INTO PideYa.Usuario(idCliente, correo, clave, tipoUsuario) VALUES(?, ?, ?, ?)";
    private static final String UPDATE_SQL =
        "UPDATE PideYa.Usuario SET correo = ?, clave = ?, tipoUsuario = ? WHERE idUsuario = ?";
    private static final String DELETE_SQL =
        "DELETE FROM PideYa.Usuario WHERE idUsuario = ?";
    private static final String SELECT_ONE_SQL =
        "SELECT idUsuario, idCliente, correo, clave, tipoUsuario " +
        "FROM PideYa.Usuario WHERE idUsuario = ?";
    private static final String SELECT_ALL_SQL =
        "SELECT idUsuario, idCliente, correo, clave, tipoUsuario FROM PideYa.Usuario";
    private static final String SELECT_BY_CREDENCIALES_SQL =
            "SELECT u.idUsuario, u.idCliente, u.correo, u.clave, u.tipoUsuario, " +
            "cn.nombre AS cn_nombre, cn.apellido AS cn_apellido, cn.dni AS cn_dni, " +
            "ce.razonSocial AS ce_razonSocial, ce.ruc AS ce_ruc " +
            "FROM PideYa.Usuario u " +
            "LEFT JOIN PideYa.ClienteNatural cn ON u.idCliente = cn.idCliente " +
            "LEFT JOIN PideYa.ClienteEmpresa ce ON u.idCliente = ce.idCliente " +
            "WHERE u.correo = ? AND u.clave = ?";

    /**
     * Inserta un nuevo usuario en la base de datos.
     * 
     * 1. Prepara sentencia INSERT con parametros.
     * 2. Ejecuta y obtiene la clave generada.
     * 3. Commit o rollback segun resultado.
     *
     * @param u Usuario sin idUsuario
     * @return Usuario con idUsuario asignado
     * @throws SQLException en error SQL
     */
    @Override
    public Usuario insertar(Usuario u) throws SQLException {
        try (PreparedStatement ps = getConnection()
                .prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, u.getCliente().getIdCliente());
            ps.setString(2, u.getCorreo());
            ps.setString(3, u.getClave());
            ps.setString(4, u.getTipoUsuario().name());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    u.setIdUsuario(rs.getInt(1));
                }
            }
            commit();
            return u;
        } catch (SQLException ex) {
            rollback();
            throw ex;
        }
    }

    /**
     * Actualiza un usuario existente.
     * 
     * 1. Prepara sentencia UPDATE con nuevos valores y idUsuario.
     * 2. Ejecuta actualizacion.
     * 3. Commit o rollback.
     *
     * @param u Usuario con idUsuario y campos modificados
     * @return Usuario actualizado o null si no existe
     * @throws SQLException en error SQL
     */
    @Override
    public Usuario actualizar(Usuario u) throws SQLException {
        try (PreparedStatement ps = getConnection().prepareStatement(UPDATE_SQL)) {
            ps.setString(1, u.getCorreo());
            ps.setString(2, u.getClave());
            ps.setString(3, u.getTipoUsuario().name());
            ps.setInt(4, u.getIdUsuario());
            int rows = ps.executeUpdate();
            commit();
            return rows > 0 ? u : null;
        } catch (SQLException ex) {
            rollback();
            throw ex;
        }
    }

    /**
     * Elimina un usuario por su ID.
     * 
     * 1. Prepara sentencia DELETE.
     * 2. Ejecuta eliminacion.
     * 3. Commit o rollback.
     *
     * @param u Usuario con idUsuario a eliminar
     * @return Usuario eliminado o null si no existia
     * @throws SQLException en error SQL
     */
    @Override
    public Usuario eliminar(Usuario u) throws SQLException {
        try (PreparedStatement ps = getConnection().prepareStatement(DELETE_SQL)) {
            ps.setInt(1, u.getIdUsuario());
            int rows = ps.executeUpdate();
            commit();
            return rows > 0 ? u : null;
        } catch (SQLException ex) {
            rollback();
            throw ex;
        }
    }

    /**
     * Obtiene un usuario por su ID.
     * 
     * Realiza consulta SELECT_ONE_SQL y mapea resultados.
     *
     * @param u Usuario con idUsuario a buscar
     * @return Usuario completo o null si no existe
     * @throws SQLException en error SQL
     */
    @Override
    public Usuario obtener(Usuario u) throws SQLException {
        try (PreparedStatement ps = getConnection().prepareStatement(SELECT_ONE_SQL)) {
            ps.setInt(1, u.getIdUsuario());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario user = new Usuario();
                    user.setIdUsuario(rs.getInt("idUsuario"));
                    Cliente c = new Cliente() {};
                    c.setIdCliente(rs.getInt("idCliente"));
                    user.setCliente(c);
                    user.setCorreo(rs.getString("correo"));
                    user.setClave(rs.getString("clave"));
                    user.setTipoUsuario(TipoUsuario.valueOf(rs.getString("tipoUsuario")));
                    return user;
                }
            }
        }
        return null;
    }

    /**
     * Lista todos los usuarios.
     * 
     * Ejecuta SELECT_ALL_SQL y mapea cada fila a Usuario.
     *
     * @return lista de Usuarios
     * @throws SQLException en error SQL
     */
    @Override
    public List<Usuario> listar() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Usuario user = new Usuario();
                user.setIdUsuario(rs.getInt("idUsuario"));
                Cliente c = new Cliente() {};
                c.setIdCliente(rs.getInt("idCliente"));
                user.setCliente(c);
                user.setCorreo(rs.getString("correo"));
                user.setClave(rs.getString("clave"));
                user.setTipoUsuario(TipoUsuario.valueOf(rs.getString("tipoUsuario")));
                lista.add(user);
            }
        }
        return lista;
    }
    

    /**
     * Busca un usuario por correo y clave e incluye los datos de ClienteNatural
     * o ClienteEmpresa según corresponda.
     *
     * @param correo correo del usuario
     * @param clave  clave del usuario
     * @return Usuario completo o null si no existe o credenciales incorrectas
     * @throws SQLException en error SQL
     */
    public Usuario autenticar(String correo, String clave) throws SQLException {
        try (PreparedStatement ps = getConnection().prepareStatement(SELECT_BY_CREDENCIALES_SQL)) {
            ps.setString(1, correo);
            ps.setString(2, clave);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("idUsuario"));
                u.setCorreo(rs.getString("correo"));
                u.setClave(rs.getString("clave"));
                u.setTipoUsuario(TipoUsuario.valueOf(rs.getString("tipoUsuario")));

                // Construir cliente según tipoUsuario
                Cliente cliente;
                if (null == u.getTipoUsuario()) {
                    cliente = null;
                } else switch (u.getTipoUsuario()) {
                    case CLIENTE_NATURAL -> {
                        ClienteNatural cn = new ClienteNatural();
                        cn.setIdCliente(rs.getInt("idCliente"));
                        cn.setNombre(rs.getString("cn_nombre"));
                        cn.setApellido(rs.getString("cn_apellido"));
                        cn.setDNI(rs.getInt("cn_dni"));
                        cliente = cn;
                    }
                    case CLIENTE_EMPRESA -> {
                        ClienteEmpresa ce = new ClienteEmpresa();
                        ce.setIdCliente(rs.getInt("idCliente"));
                        ce.setRazonSocial(rs.getString("ce_razonSocial"));
                        ce.setRuc(rs.getString("ce_ruc"));
                        cliente = ce;
                    }
                    default -> cliente = null;
                }
                u.setCliente(cliente);
                return u;
            }
        }
    }
    
}
