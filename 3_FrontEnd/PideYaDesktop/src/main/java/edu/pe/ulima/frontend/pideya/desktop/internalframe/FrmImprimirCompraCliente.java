/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package edu.pe.ulima.frontend.pideya.desktop.internalframe;

import edu.pe.ulima.frontend.pideya.desktop.frame.FrmPrincipal;
import edu.pe.ulima.pideya.back.model.ClienteNatural;
import edu.pe.ulima.pideya.back.model.Compra;
import edu.pe.ulima.pideya.back.model.DetallePedido;
import edu.pe.ulima.pideya.back.model.Pedido;
import edu.pe.ulima.pideya.back.service.CompraService;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 *
 * @author hwong
 */
public class FrmImprimirCompraCliente extends javax.swing.JInternalFrame {

    private CompraService compraService = new CompraService();
    private byte[] pdfBytes = null;

    /**
     * Creates new form FrmConsultarCompra
     */
    public FrmImprimirCompraCliente() {
        try {
            initComponents();
            URL url = getClass().getClassLoader().getResource("Logo.png");
            if (url != null) {
                this.lblTituloFormulario.setIcon(new ImageIcon(url));
            } else {
                System.err.println("¡Recurso no encontrado: miIcono.png!");
            }
            this.lblTituloFormulario.setText("Imprimir Compras - Cliente");
            this.setTitle("Imprimir Compras - Cliente");
            inicializarEncabezadoJTFiltro();
        } catch (Exception ex) {
            Logger.getLogger(FrmImprimirCompraCliente.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void inicializarEncabezadoJTFiltro() {
        String[] columnas = {
            "OBJ_COMPRA", // oculta
            "OBJ_PEDIDO", // oculta
            "ID Compra",
            "ID Pedido",
            "Fecha Compra",
            "Tipo Cliente",
            "Nombre Cliente",
            "Monto Total",
            "Nombre Producto",
            "Precio Unit.",
            "Cantidad",
            "Subtotal"
        };
        DefaultTableModel model = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int c) {
                return switch (c) {
                    case 2, 3 ->
                        Integer.class;
                    case 4 ->
                        LocalDateTime.class;
                    case 7, 9, 11 ->
                        Double.class;
                    case 10 ->
                        Integer.class;
                    default ->
                        Object.class;
                };
            }
        };
        jtFiltro.setModel(model);
        jtFiltro.getTableHeader().setReorderingAllowed(false);

        TableColumnModel cm = jtFiltro.getColumnModel();
        // ocultar columnas de objeto
        cm.getColumn(0).setMinWidth(0);
        cm.getColumn(0).setMaxWidth(0);
        cm.getColumn(1).setMinWidth(0);
        cm.getColumn(1).setMaxWidth(0);
        // anchos fijos sencillos
        cm.getColumn(2).setMinWidth(60);
        cm.getColumn(2).setMaxWidth(60);
        cm.getColumn(3).setMinWidth(60);
        cm.getColumn(3).setMaxWidth(60);
        cm.getColumn(4).setMinWidth(140);
        cm.getColumn(4).setMaxWidth(140);
        cm.getColumn(5).setMinWidth(140);
        cm.getColumn(5).setMaxWidth(140);
        cm.getColumn(7).setMinWidth(100);
        cm.getColumn(7).setMaxWidth(100);
        cm.getColumn(9).setMinWidth(100);
        cm.getColumn(9).setMaxWidth(100);
        cm.getColumn(10).setMinWidth(60);
        cm.getColumn(10).setMaxWidth(60);
        cm.getColumn(11).setMinWidth(100);
        cm.getColumn(11).setMaxWidth(100);
        jtFiltro.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        // renderer fecha dd/MM/yyyy HH:mm
        DefaultTableCellRenderer dateRend = new DefaultTableCellRenderer() {
            private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            @Override
            protected void setValue(Object val) {
                if (val instanceof LocalDateTime dt) {
                    setText(dt.format(fmt));
                } else {
                    super.setValue(val);
                }
            }
        };
        dateRend.setHorizontalAlignment(SwingConstants.CENTER);
        cm.getColumn(4).setCellRenderer(dateRend);

        // renderer dinero S/.0.00
        DefaultTableCellRenderer moneyRend = new DefaultTableCellRenderer() {
            private final DecimalFormat df;

            {
                DecimalFormatSymbols s = new DecimalFormatSymbols();
                s.setDecimalSeparator('.');
                df = new DecimalFormat("#0.00", s);
                df.setPositivePrefix("S/.");
            }

            @Override
            protected void setValue(Object val) {
                if (val instanceof Number n) {
                    setText(df.format(n.doubleValue()));
                } else {
                    super.setValue(val);
                }
            }
        };
        moneyRend.setHorizontalAlignment(SwingConstants.RIGHT);
        cm.getColumn(7).setCellRenderer(moneyRend);
        cm.getColumn(9).setCellRenderer(moneyRend);
        cm.getColumn(11).setCellRenderer(moneyRend);
    }

    private void llenarDatosJTFiltro() {
        DefaultTableModel model = (DefaultTableModel) jtFiltro.getModel();
        model.setRowCount(0);

        try {
            // 1) Obtener lista de compras con detalle
            List<Compra> compras = compraService.listarComprasPorIdCliente(FrmPrincipal.usuarioInicioSesion.getCliente().getIdCliente());

            // 2) Iterar compras y sus pedidos/detalles
            for (Compra compra : compras) {
                Pedido pedido = compra.getPedido();
                // extraer info de cabecera
                Integer idCompra = compra.getIdCompra();
                Integer idPedido = pedido.getIdPedido();
                LocalDateTime fechaCompra = compra.getFechaCompra();
                String tipoCliente = pedido.getCliente() instanceof ClienteNatural ? "CLIENTE_NATURAL" : "CLIENTE_EMPRESA";
                String nombreCliente = pedido.getCliente().toString(); // o getNombre()

                Double montoTotal = pedido.getMontoTotal();

                // 3) Para cada detalle, añadimos una fila
                for (DetallePedido det : pedido.getDetallePedido()) {
                    model.addRow(new Object[]{
                        compra, // 0: objeto Compra
                        pedido, // 1: objeto Pedido
                        idCompra, // 2: ID Compra
                        idPedido, // 3: ID Pedido
                        fechaCompra, // 4: Fecha Compra
                        tipoCliente, // 5: Tipo Cliente
                        nombreCliente, // 6: Nombre Cliente
                        montoTotal, // 7: Monto Total
                        det.getProducto().getNombre(), // 8: Nombre Producto
                        det.getPrecioUnitario(), // 9: Precio Unit.
                        det.getCantidad(), // 10: Cantidad
                        det.getSubTotal() // 11: Subtotal
                    });
                }
            }

            jtFiltro.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2 && !e.isConsumed()) {
                        e.consume();
                        int row = jtFiltro.getSelectedRow();
                        if (row < 0) {
                            return;
                        }

                        DefaultTableModel model = (DefaultTableModel) jtFiltro.getModel();
                        // columna 0: objeto Compra, 1: objeto Pedido
                        Compra compra = (Compra) model.getValueAt(row, 0);
                        Pedido pedido = (Pedido) model.getValueAt(row, 1);

                        // Volcar a los JTextField
                        txtCodigoCompra.setText(String.valueOf(compra.getIdCompra()));
                        txtCodigoPedido.setText(String.valueOf(pedido.getIdPedido()));

                        // Fecha Compra: formatear a dd/MM/yyyy HH:mm
                        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                        txtFechaCompra.setText(compra.getFechaCompra().format(fmt));

                        // Cliente (toString() o getNombre())
                        txtCliente.setText(pedido.getCliente().toString());

                        // Monto Total con dos decimales
                        DecimalFormatSymbols sym = new DecimalFormatSymbols();
                        sym.setDecimalSeparator('.');
                        DecimalFormat df = new DecimalFormat("#0.00", sym);
                        df.setPositivePrefix("S/.");
                        txtMontoTotal.setText(df.format(pedido.getMontoTotal()));

                        // Si quisieras habilitar el botón PDF:
                        btnGenerarPdf.setEnabled(true);
                        limpiarVisorPDF();
                        jtbPrincipal.setSelectedIndex(1);
                    }
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void mostrarReporteCompra() {
        try {
            // 1) Llamada al servicio (asegúrate que el método se llama exactamente así)
            pdfBytes = compraService.generarReporteCompraPDF(Integer.valueOf(txtCodigoCompra.getText()));

            // 2) Carga el PDF directamente desde el byte[]
            try (PDDocument document = Loader.loadPDF(pdfBytes)) {
                PDFRenderer renderer = new PDFRenderer(document);

                JPanel pdfPanel = new JPanel();
                pdfPanel.setLayout(new BoxLayout(pdfPanel, BoxLayout.Y_AXIS));

                // 3) Renderiza cada página como imagen
                for (int i = 0; i < document.getNumberOfPages(); i++) {
                    BufferedImage pageImage = renderer.renderImageWithDPI(i, 150);
                    JLabel pageLabel = new JLabel(new ImageIcon(pageImage));
                    pageLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
                    pdfPanel.add(pageLabel);
                }

                // 4) Vuelca el panel al JScrollPane ya existente (jcpPDF)
                jcpPDF.setViewportView(pdfPanel);
                //jcpPDF.getViewport().setPreferredSize(new Dimension(800, 600));
                jcpPDF.getVerticalScrollBar().setUnitIncrement(16);
            }

            // 5) Asegura que el internal frame esté al frente
            if (!this.isVisible()) {
                this.setVisible(true);
            }
            this.setSelected(true);

        } catch (NoSuchMethodError e) {
            JOptionPane.showMessageDialog(this,
                    "El método generarReporteCompraPdf(int) no existe en CompraService.",
                    "Error de servicio",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo mostrar el reporte PDF:\n" + ex.getMessage(),
                    "Error al visualizar PDF",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void limpiarVisorPDF() {
        pdfBytes = null;
        jcpPDF.getViewport().removeAll();
        jcpPDF.revalidate();
        jcpPDF.repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblTituloFormulario = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jtbPrincipal = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtFiltro = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        btnFiltar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jcpPDF = new javax.swing.JScrollPane();
        jPanel9 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtCodigoCompra = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtFechaCompra = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtCodigoPedido = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtCliente = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtMontoTotal = new javax.swing.JTextField();
        btnGenerarPdf = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(589, 60));

        lblTituloFormulario.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTituloFormulario.setText("Mantenimiento");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTituloFormulario, javax.swing.GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTituloFormulario)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.BorderLayout());

        jtFiltro.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jtFiltro);

        jPanel5.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel6.setPreferredSize(new java.awt.Dimension(776, 50));

        btnFiltar.setText("Refrescar Compras");
        btnFiltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiltarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnFiltar, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(688, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnFiltar)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel6, java.awt.BorderLayout.NORTH);

        jtbPrincipal.addTab("Listar", jPanel3);

        jPanel4.setLayout(new java.awt.BorderLayout());

        jcpPDF.setBorder(javax.swing.BorderFactory.createTitledBorder("Reporte"));

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Compras"));

        jLabel2.setText("Código Compra:");

        txtCodigoCompra.setEditable(false);
        txtCodigoCompra.setFocusable(false);

        jLabel3.setText("Fecha Compra:");

        txtFechaCompra.setEditable(false);
        txtFechaCompra.setFocusable(false);

        jLabel4.setText("Código Pedido:");

        txtCodigoPedido.setEditable(false);
        txtCodigoPedido.setFocusable(false);

        jLabel5.setText("Monto Total:");

        txtCliente.setEditable(false);
        txtCliente.setFocusable(false);

        jLabel6.setText("Cliente:");

        txtMontoTotal.setEditable(false);
        txtMontoTotal.setFocusable(false);

        btnGenerarPdf.setText("Generar PDF");
        btnGenerarPdf.setEnabled(false);
        btnGenerarPdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarPdfActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCliente)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(txtMontoTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnGenerarPdf, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(txtCodigoCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCodigoPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtFechaCompra, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 140, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtCodigoCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtFechaCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(txtCodigoPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtMontoTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnGenerarPdf)))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jcpPDF))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcpPDF, javax.swing.GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.add(jPanel7, java.awt.BorderLayout.CENTER);

        jPanel8.setPreferredSize(new java.awt.Dimension(776, 40));

        btnGuardar.setText("Guardar");
        btnGuardar.setMaximumSize(new java.awt.Dimension(76, 23));
        btnGuardar.setMinimumSize(new java.awt.Dimension(76, 23));
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnImprimir.setText("Imprimir");
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnImprimir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 677, Short.MAX_VALUE)
                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnImprimir))
                .addContainerGap())
        );

        jPanel4.add(jPanel8, java.awt.BorderLayout.SOUTH);

        jtbPrincipal.addTab("Operación", jPanel4);

        jPanel2.add(jtbPrincipal, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFiltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltarActionPerformed
        // TODO add your handling code here:
        inicializarEncabezadoJTFiltro();
        llenarDatosJTFiltro();
    }//GEN-LAST:event_btnFiltarActionPerformed

    private void btnGenerarPdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarPdfActionPerformed
        // TODO add your handling code here:
        mostrarReporteCompra();
    }//GEN-LAST:event_btnGenerarPdfActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
        if (this.pdfBytes == null) {
            JOptionPane.showMessageDialog(this, "No hay PDF cargado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("reporte_compra_" + txtCodigoCompra.getText() + ".pdf"));
        int opc = chooser.showSaveDialog(this);
        if (opc == JFileChooser.APPROVE_OPTION) {
            File out = chooser.getSelectedFile();
            try (FileOutputStream fos = new FileOutputStream(out)) {
                fos.write(pdfBytes);
                JOptionPane.showMessageDialog(this, "Guardado en:\n" + out.getAbsolutePath(),
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar:\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        // TODO add your handling code here:
        if (pdfBytes == null) {
            JOptionPane.showMessageDialog(this, "No hay PDF cargado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try (PDDocument document = Loader.loadPDF(pdfBytes)) {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPageable(new PDFPageable(document));
            if (job.printDialog()) {
                job.print();
            }
        } catch (PrinterException | IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al imprimir:\n" + ex.getMessage(),
                    "Error de impresión",
                    JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_btnImprimirActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFiltar;
    private javax.swing.JButton btnGenerarPdf;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jcpPDF;
    private javax.swing.JTable jtFiltro;
    private javax.swing.JTabbedPane jtbPrincipal;
    private javax.swing.JLabel lblTituloFormulario;
    private javax.swing.JTextField txtCliente;
    private javax.swing.JTextField txtCodigoCompra;
    private javax.swing.JTextField txtCodigoPedido;
    private javax.swing.JTextField txtFechaCompra;
    private javax.swing.JTextField txtMontoTotal;
    // End of variables declaration//GEN-END:variables
}
