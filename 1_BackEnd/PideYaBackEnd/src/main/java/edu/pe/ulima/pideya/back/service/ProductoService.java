package edu.pe.ulima.pideya.back.service;

import edu.pe.ulima.pideya.back.dao.ProductoDAO;
import edu.pe.ulima.pideya.back.model.Producto;
import java.util.List;
import java.util.Objects;

/**
 * Servicio para gestionar operaciones CRUD sobre
 * {@link edu.pe.ulima.pideya.back.model.Producto}.
 * <p>
 * Esta clase agrupa la logica de negocio relacionada con productos y delega las
 * operaciones de persistencia al
 * {@link edu.pe.ulima.pideya.back.dao.ProductoDAO}.
 * </p>
 *
 * @author Henry Wong <hwong@ulima.edu.pe>
 */
public class ProductoService {

    /**
     * DAO responsable de la persistencia de productos.
     */
    private final ProductoDAO productoDAO;

    /**
     * Construye el servicio usando el DAO por defecto.
     */
    public ProductoService() {
        this(new ProductoDAO());
    }

    /**
     * Construye el servicio con un DAO inyectado.
     *
     * @param productoDAO instancia de {@link ProductoDAO}, no puede ser null
     */
    private ProductoService(ProductoDAO productoDAO) {
        this.productoDAO = Objects.requireNonNull(productoDAO, "productoDAO no puede ser null");
    }

    /**
     * Agrega un nuevo producto al sistema.
     *
     * @param producto entidad a insertar, no puede ser null
     * @return el producto creado con su ID generado
     * @throws Exception si ocurre un error durante la persistencia
     */
    public Producto agregarProducto(Producto producto) throws Exception {
        Objects.requireNonNull(producto, "producto no puede ser null");
        return productoDAO.insertar(producto);
    }

    /**
     * Actualiza los datos de un producto existente.
     *
     * @param producto entidad con ID y nuevos atributos, no puede ser null
     * @return el producto actualizado o null si no existia
     * @throws IllegalArgumentException si el ID del producto es null
     * @throws Exception si ocurre un error durante la actualizacion
     */
    public Producto editarInformacionProducto(Producto producto) throws Exception {
        Objects.requireNonNull(producto, "producto no puede ser null");
        if (producto.getIdProducto() == null) {
            throw new IllegalArgumentException("El ID del producto es obligatorio para actualizar");
        }
        return productoDAO.actualizar(producto);
    }

    /**
     * Elimina un producto existente.
     *
     * @param producto entidad con ID a eliminar, no puede ser null
     * @return el producto eliminado o null si no existia
     * @throws IllegalArgumentException si el ID del producto es null
     * @throws Exception si ocurre un error durante la eliminacion
     */
    public Producto eliminarProducto(Producto producto) throws Exception {
        Objects.requireNonNull(producto, "producto no puede ser null");
        if (producto.getIdProducto() == null) {
            throw new IllegalArgumentException("El ID del producto es obligatorio para eliminar");
        }
        return productoDAO.eliminar(producto);
    }

    /**
     * Recupera un producto segun su identificador.
     *
     * @param producto entidad con el ID a buscar, no puede ser null
     * @return el producto encontrado o null si no existe
     * @throws IllegalArgumentException si el ID del producto es null
     * @throws Exception si ocurre un error durante la consulta
     */
    public Producto buscarProducto(Producto producto) throws Exception {
        Objects.requireNonNull(producto, "producto no puede ser null");
        if (producto.getIdProducto() == null) {
            throw new IllegalArgumentException("El ID del producto es obligatorio para buscar");
        }
        return productoDAO.obtener(producto);
    }

    /**
     * Obtiene el listado de productos filtrado por nombre.
     *
     * @param nombre cadena de inicio para filtrar nombres; no puede ser null
     * @return lista de productos cuyo nombre comienza con el prefijo
     * @throws IllegalArgumentException si nombre es null
     * @throws Exception si ocurre un error al filtrar
     */
    public List<Producto> filtrarProductosPorNombre(String nombre) throws Exception {
        if (nombre == null) {
            throw new IllegalArgumentException("El nombre no puede ser null");
        }
        return productoDAO.listarPorNombre(nombre);
    }

    /**
     * Filtra productos que pertenecen a la categoria indicada.
     *
     * @param idCategoria ID de la categoria para filtrar; no puede ser null
     * @return lista de productos en la categoria
     * @throws IllegalArgumentException si idCategoria es null
     * @throws Exception si ocurre un error al filtrar
     */
    public List<Producto> filtrarProductosPorCategoria(Integer idCategoria) throws Exception {
        if (idCategoria == null) {
            throw new IllegalArgumentException("El ID de la categoria no puede ser null");
        }
        return productoDAO.listarPorCategoria(idCategoria);
    }
}
