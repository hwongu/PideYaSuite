package edu.pe.ulima.pideya.back.service;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import edu.pe.ulima.pideya.back.dao.CompraDAO;
import edu.pe.ulima.pideya.back.model.Cliente;
import edu.pe.ulima.pideya.back.model.ClienteEmpresa;
import edu.pe.ulima.pideya.back.model.ClienteNatural;
import edu.pe.ulima.pideya.back.model.Compra;
import edu.pe.ulima.pideya.back.model.DetallePedido;
import edu.pe.ulima.visapago.pago.VisaPago;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

/**
 * Servicio para gestionar operaciones de Compra.
 * <p>
 * Esta clase delega la persistencia de {@link Compra} al {@link CompraDAO}.
 * </p>
 *
 * @author Henry Wong <hwong@ulima.edu.pe>
 */
public class CompraService {

    /**
     * DAO responsable de la persistencia de compras.
     */
    private final CompraDAO compraDAO;

    /**
     * Construye el servicio usando el DAO por defecto.
     */
    public CompraService() {
        this(new CompraDAO());
    }

    /**
     * Construye el servicio con un DAO inyectado.
     *
     * @param compraDAO instancia de {@link CompraDAO}, no puede ser null
     */
    private CompraService(CompraDAO compraDAO) {
        this.compraDAO = Objects.requireNonNull(compraDAO, "compraDAO no puede ser null");
    }

    /**
     * Realizar una nueva compra en el sistema.
     *
     * 1. Valida que la compra no sea null. 2. Delegar inserción al DAO.
     *
     * @param compra entidad a insertar, no puede ser null
     * @return la compra creada con su ID generado
     * @throws Exception si ocurre un error durante la persistencia
     */
    public Compra realizarCompra(Compra compra) throws Exception {
        Objects.requireNonNull(compra, "compra no puede ser null");
        if (VisaPago.servicioOperativo()) {
            return compraDAO.insertar(compra);
        } else {
            return null;
        }
    }

    /**
     * Obtiene las compras cuyo nombre de cliente coincide con el filtro.
     *
     * 1. Valida que el filtro no sea null. 2. Delegar consulta al DAO.
     *
     * @param nombreClienteFiltro texto para buscar en nombre del cliente, no
     * puede ser null
     * @return lista de {@link Compra} que coinciden con el filtro
     * @throws IllegalArgumentException si nombreClienteFiltro es null
     * @throws Exception si ocurre un error al listar
     */
    public List<Compra> listarComprasPorCliente(String nombreClienteFiltro) throws Exception {
        Objects.requireNonNull(nombreClienteFiltro, "nombreClienteFiltro no puede ser null");
        return compraDAO.listarComprasPorCliente(nombreClienteFiltro);
    }
    
    public List<Compra> listarComprasPorIdCliente(Integer idCliente) throws Exception {
        Objects.requireNonNull(idCliente, "idCliente no puede ser null");
        return compraDAO.listarComprasPorCliente(idCliente);
    }

    /**
     * Obtiene la compra cuyo id de la compra coincide con el filtro.
     *
     * 1. Valida que el filtro no sea null. 2. Delegar consulta al DAO.
     *
     * @param idCompra el id de la compra, no puede ser null
     * @return lista de {@link Compra} que coinciden con el filtro
     * @throws IllegalArgumentException si nombreClienteFiltro es null
     * @throws Exception si ocurre un error al listar
     */
    public List<Compra> listarComprasPorIdCompra(Integer idCompra) throws Exception {
        Objects.requireNonNull(idCompra, "nombreClienteFiltro no puede ser null");
        return compraDAO.listarComprasPorIdCompra(idCompra);
    }

    /**
     * Genera un PDF con la informacion de una compra.
     *
     * Cabecera: idCompra, idPedido, fechaCompra, tipoCliente, nombreCliente.
     * Seccion detalle: tabla con idProducto, nombreProducto, precioUnitario,
     * cantidad, subtotal. Pie: montoTotal.
     *
     * Ahora utiliza CompraService.listarComprasPorIdCompra para obtener la
     * Compra completa con sus detalles.
     *
     * @param idCompra identificador de la compra, no puede ser null
     * @return arreglo de bytes del PDF generado
     * @throws Exception si falla recuperacion de datos o generacion del PDF
     */
    public byte[] generarReporteCompraPDF(Integer idCompra) throws Exception {
        Objects.requireNonNull(idCompra, "idCompra no puede ser null");
        List<Compra> compras = listarComprasPorIdCompra(idCompra);
        if (compras.isEmpty()) {
            throw new IllegalArgumentException("No se encontró la compra con id " + idCompra);
        }
        Compra compra = compras.get(0);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // --- Título ---
        Paragraph title = new Paragraph("PIDE YA - Reporte de Compras (Universidad de Lima)")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(14)
                .setBold();
        doc.add(title);
        doc.add(new Paragraph("\n"));

        // --- Cabecera de datos de compra (2 columnas) ---
        float[] headerCols = {1, 2};
        Table header = new Table(UnitValue.createPercentArray(headerCols))
                .useAllAvailableWidth();
        Cell lbl, val;

        // ID Compra
        lbl = new Cell().add(new Paragraph("ID Compra"))
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setBold();
        val = new Cell().add(new Paragraph(compra.getIdCompra().toString()));
        header.addCell(lbl);
        header.addCell(val);

        // ID Pedido
        lbl = new Cell().add(new Paragraph("ID Pedido"))
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setBold();
        val = new Cell().add(new Paragraph(compra.getPedido().getIdPedido().toString()));
        header.addCell(lbl);
        header.addCell(val);

        // Fecha Compra
        lbl = new Cell().add(new Paragraph("Fecha Compra"))
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setBold();
        val = new Cell().add(new Paragraph(compra.getFechaCompra().format(fmt)));
        header.addCell(lbl);
        header.addCell(val);

        // Tipo Cliente
        Cliente cli = compra.getPedido().getCliente();
        String tipo = cli instanceof ClienteNatural ? "CLIENTE NATURAL"
                : cli instanceof ClienteEmpresa ? "CLIENTE EMPRESA"
                        : "DESCONOCIDO";
        lbl = new Cell().add(new Paragraph("Tipo Cliente"))
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setBold();
        val = new Cell().add(new Paragraph(tipo));
        header.addCell(lbl);
        header.addCell(val);

        // Nombre Cliente
        String nombreCli = (cli instanceof ClienteNatural cn)
                ? cn.getApellido() + " " + cn.getNombre()
                : (cli instanceof ClienteEmpresa ce)
                        ? ce.getRazonSocial() + " (RUC: " + ce.getRuc() + ")"
                        : "";
        lbl = new Cell().add(new Paragraph("Nombre Cliente"))
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setBold();
        val = new Cell().add(new Paragraph(nombreCli.toUpperCase()));
        header.addCell(lbl);
        header.addCell(val);

        doc.add(header);
        doc.add(new Paragraph("\n"));

        // --- Tabla de detalle (5 columnas) ---
        float[] detailCols = {1, 3, 2, 2, 2};
        Table table = new Table(UnitValue.createPercentArray(detailCols))
                .useAllAvailableWidth();

        // Encabezados con fondo gris
        String[] cols = {"ID Prod", "Producto", "Precio", "Cantidad", "Subtotal"};
        for (String c : cols) {
            table.addHeaderCell(
                    new Cell()
                            .add(new Paragraph(c))
                            .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                            .setBold()
            );
        }

        // Filas de detalle
        for (DetallePedido dp : compra.getPedido().getDetallePedido()) {
            table.addCell(dp.getProducto().getIdProducto().toString());
            table.addCell(dp.getProducto().getNombre().toUpperCase());
            table.addCell(String.format("S/.%.2f", dp.getPrecioUnitario()));
            table.addCell(String.format("%.2f", dp.getCantidad()));
            table.addCell(String.format("S/.%.2f", dp.getSubTotal()));
        }

        doc.add(table);
        doc.add(new Paragraph("\n"));

        // --- Pie con total ---
        Paragraph total = new Paragraph("Total: S/. "
                + String.format("%.2f", compra.getPedido().getMontoTotal()))
                .setBold();
        doc.add(total);

        // --- Pie de página con autor y fecha de generación ---
        String autor = "Henry Wong";
        String correo = "hwong@ulima.edu.pe";
        String fechaGen = LocalDateTime.now().format(fmt);
        Paragraph footer = new Paragraph(
                String.format("Generado por: %s <%s>   Fecha de generación: %s",
                        autor, correo, fechaGen)
        )
                .setFontSize(9)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(20);
        doc.add(footer);

        doc.close();
        return baos.toByteArray();
    }

}
