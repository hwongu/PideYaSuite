package edu.pe.ulima.pideya.back.service;

import edu.pe.ulima.pideya.back.dao.CategoriaDAO;
import edu.pe.ulima.pideya.back.model.Categoria;
import java.util.List;
import java.util.Objects;

/**
 * Servicio para gestionar operaciones CRUD sobre
 * {@link edu.pe.ulima.pideya.back.model.Categoria}.
 * <p>
 * Esta clase encapsula la logica de negocio relacionada con categorias y delega
 * las operaciones de persistencia al
 * {@link edu.pe.ulima.pideya.back.dao.CategoriaDAO}.
 * </p>
 *
 * @author Henry Wong <hwong@ulima.edu.pe>
 */
public class CategoriaService {

    /**
     * DAO responsable de la persistencia de categorias.
     */
    private final CategoriaDAO categoriaDAO;

    /**
     * Construye el servicio usando el DAO por defecto.
     */
    public CategoriaService() {
        this(new CategoriaDAO());
    }

    /**
     * Construye el servicio con un DAO inyectado.
     *
     * @param categoriaDAO instancia de {@link CategoriaDAO}, no puede ser
     * {@code null}
     */
    private CategoriaService(CategoriaDAO categoriaDAO) {
        this.categoriaDAO = Objects.requireNonNull(categoriaDAO, "categoriaDAO no puede ser null");
    }

    /**
     * Inserta una nueva categoria en el sistema.
     *
     * @param categoria entidad a insertar, no puede ser {@code null}
     * @return la categoria creada con su ID generado
     * @throws Exception si ocurre un error durante la persistencia
     */
    public Categoria agregarCategoria(Categoria categoria) throws Exception {
        Objects.requireNonNull(categoria, "categoria no puede ser null");
        return categoriaDAO.insertar(categoria);
    }

    /**
     * Actualiza los datos de una categoria existente.
     *
     * @param categoria entidad con ID y nuevos atributos, no puede ser
     * {@code null}
     * @return la categoria actualizada
     * @throws IllegalArgumentException si el ID de la categoria es {@code null}
     * @throws Exception si ocurre un error durante la actualizacion
     */
    public Categoria editarInformacionCategoria(Categoria categoria) throws Exception {
        Objects.requireNonNull(categoria, "categoria no puede ser null");
        if (categoria.getIdCategoria() == null) {
            throw new IllegalArgumentException("El ID de la categoria es obligatorio para actualizar");
        }
        return categoriaDAO.actualizar(categoria);
    }

    /**
     * Elimina una categoria existente.
     *
     * @param categoria entidad con ID a eliminar, no puede ser null
     * @return la categoria eliminada o null si no existia
     * @throws IllegalArgumentException si el ID de la categoria es null
     * @throws Exception si ocurre un error durante la eliminacion
     */
    public Categoria eliminarCategoria(Categoria categoria) throws Exception {
        Objects.requireNonNull(categoria, "categoria no puede ser null");
        if (categoria.getIdCategoria() == null) {
            throw new IllegalArgumentException("El ID de la categoria es obligatorio para eliminar");
        }
        return categoriaDAO.eliminar(categoria);
    }

    /**
     * Obtiene el listado completo de categorias disponibles.
     *
     * @return lista de categorias; puede estar vacia si no hay registros
     * @throws Exception si ocurre un error al listar
     */
    public List<Categoria> listarCategorias() throws Exception {
        return categoriaDAO.listar();
    }

    /**
     * Obtiene el listado de categorias filtrado por nombre.
     *
     * @param nombre nombre de categorias a filtrar
     * @return lista de categorias; puede estar vacia si no hay registros
     * @throws Exception si ocurre un error al listar
     */
    public List<Categoria> filtrarCategoriasPorNombre(String nombre) throws Exception {
        return categoriaDAO.listarPorNombre(nombre);
    }
}
