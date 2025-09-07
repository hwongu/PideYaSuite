/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package edu.pe.ulima.frontend.pideya.desktop.internalframe;

import edu.pe.ulima.pideya.back.model.Compra;
import edu.pe.ulima.pideya.back.model.DetallePedido;
import edu.pe.ulima.pideya.back.model.Pedido;
import edu.pe.ulima.pideya.back.model.Producto;
import edu.pe.ulima.pideya.back.service.CompraService;
import edu.pe.ulima.pideya.back.service.DetallePedidoService;
import edu.pe.ulima.pideya.back.service.PedidoService;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author hwong
 */
public class FrmCompra extends javax.swing.JInternalFrame {

    private PedidoService pedidoService = new PedidoService();
    private DetallePedidoService detallePedidoService = new DetallePedidoService();
    private CompraService compraService = new CompraService();
    /**
     * Creates new form FrmCompra
     */
    public FrmCompra() {
        try {
            initComponents();
            URL url = getClass().getClassLoader().getResource("Logo.png");
            if (url != null) {
                this.lblTituloFormulario.setIcon(new ImageIcon(url));
            } else {
                System.err.println("¡Recurso no encontrado: miIcono.png!");
            }
            this.lblTituloFormulario.setText("Realizar Compra de Pedidos");
            this.setTitle("Realizar Compra de Pedidos");
            inicializarTablaPedidosSinCompra();
        } catch (Exception ex) {
            Logger.getLogger(FrmCategoria.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void inicializarTablaPedidosSinCompra() {
        // 1) Columnas fijas
        String[] columnas = {"OBJ_PEDIDO", "ID Pedido", "Cliente", "Fecha Pedido", "Total"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int c) {
                return switch (c) {
                    case 1 ->
                        Integer.class;
                    case 4 ->
                        Double.class;
                    default ->
                        Object.class;
                };
            }
        };

        try {
            // 2) Cargar datos
            List<Pedido> pedidos = pedidoService.listarPedidosSinCompra();
            for (Pedido ped : pedidos) {
                model.addRow(new Object[]{
                    ped,
                    ped.getIdPedido(),
                    ped.getCliente().toString(),
                    ped.getFechaPedido(), // se formatea en el renderer
                    ped.getMontoTotal() // se formatea en el renderer
                });
            }
        } catch (Exception ex) {
            Logger.getLogger(FrmPedido.class.getName())
                    .log(Level.SEVERE, "Error al listar pedidos", ex);
            // dejamos sólo encabezados
        }

        // 3) Asignar modelo y configurar tabla
        jtPedido.setModel(model);
        jtPedido.getTableHeader().setReorderingAllowed(false);
        TableColumnModel cm = jtPedido.getColumnModel();

        // Ocultar columna de objeto
        cm.getColumn(0).setMinWidth(0);
        cm.getColumn(0).setMaxWidth(0);
        cm.getColumn(0).setWidth(0);

        // ID Pedido ancho fijo 80px
        cm.getColumn(1).setMinWidth(80);
        cm.getColumn(1).setMaxWidth(80);
        cm.getColumn(1).setPreferredWidth(80);

        // Fecha ancho fijo 150px
        cm.getColumn(3).setMinWidth(150);
        cm.getColumn(3).setMaxWidth(150);
        cm.getColumn(3).setPreferredWidth(150);

        // Total ancho fijo 100px
        cm.getColumn(4).setMinWidth(100);
        cm.getColumn(4).setMaxWidth(100);
        cm.getColumn(4).setPreferredWidth(100);

        jtPedido.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        // 4) Renderer para Fecha (dd/MM/yyyy HH:mm)
        DefaultTableCellRenderer dateRenderer = new DefaultTableCellRenderer() {
            private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            @Override
            protected void setValue(Object value) {
                if (value instanceof LocalDateTime dt) {
                    setText(dt.format(fmt));
                } else {
                    super.setValue(value);
                }
            }
        };
        dateRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        cm.getColumn(3).setCellRenderer(dateRenderer);

        // 5) Renderer para Total (S/.0.00)
        DefaultTableCellRenderer totalRenderer = new DefaultTableCellRenderer() {
            private final DecimalFormat df;

            {
                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                symbols.setDecimalSeparator('.');
                df = new DecimalFormat("#0.00", symbols);
                df.setPositivePrefix("S/.");
                df.setNegativePrefix("-S/.");
            }

            @Override
            protected void setValue(Object value) {
                if (value instanceof Number num) {
                    setText(df.format(num.doubleValue()));
                } else {
                    super.setValue(value);
                }
            }
        };
        totalRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        cm.getColumn(4).setCellRenderer(totalRenderer);

        // 6) Listener de selección: carga detalle al cambiar de fila
        jtPedido.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = jtPedido.getSelectedRow();
                if (row >= 0) {
                    Pedido pedido = (Pedido) jtPedido.getModel().getValueAt(row, 0);
                    llenarTablaDetallePorPedido(pedido);
                }
            }
        });

        // 7) Seleccionar la primera fila por defecto
        if (model.getRowCount() > 0) {
            jtPedido.setRowSelectionInterval(0, 0);
        }
    }

    private void llenarTablaDetallePorPedido(Pedido pedido) {
        // Columnas fijas
        String[] columnas = {"OBJ_DETALLE", "ID Prod", "Nombre", "Cantidad", "Precio Unit.", "Subtotal"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int c) {
                return switch (c) {
                    case 3 ->
                        Integer.class;
                    case 4, 5 ->
                        Double.class;
                    default ->
                        Object.class;
                };
            }
        };

        try {
            // Cargar detalles
            List<DetallePedido> detalles = detallePedidoService.listarDetallesPorPedido(pedido.getIdPedido());
            for (DetallePedido det : detalles) {
                Producto prod = det.getProducto();
                model.addRow(new Object[]{
                    det,
                    prod.getIdProducto(),
                    prod.getNombre(),
                    det.getCantidad(),
                    det.getPrecioUnitario(),
                    det.getSubTotal()
                });
            }
        } catch (Exception ex) {
            Logger.getLogger(FrmPedido.class.getName())
                    .log(Level.SEVERE, "Error al listar detalles", ex);
        }

        // Asignar modelo y configurar tabla
        jtDetallePedido.setModel(model);
        jtDetallePedido.getTableHeader().setReorderingAllowed(false);
        TableColumnModel cm = jtDetallePedido.getColumnModel();

        // Ocultar columna objeto
        cm.getColumn(0).setMinWidth(0);
        cm.getColumn(0).setMaxWidth(0);

        // ID Prod y Cantidad fijos a 80px
        cm.getColumn(1).setMinWidth(80);
        cm.getColumn(1).setMaxWidth(80);
        cm.getColumn(3).setMinWidth(80);
        cm.getColumn(3).setMaxWidth(80);

        // Precio Unit. y Subtotal fijos a 100px
        for (int c : new int[]{4, 5}) {
            cm.getColumn(c).setMinWidth(100);
            cm.getColumn(c).setMaxWidth(100);
            cm.getColumn(c).setPreferredWidth(100);
        }
        jtDetallePedido.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        // Renderer para precios
        DefaultTableCellRenderer moneyRenderer = new DefaultTableCellRenderer() {
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
        moneyRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        cm.getColumn(4).setCellRenderer(moneyRenderer);
        cm.getColumn(5).setCellRenderer(moneyRenderer);
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
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtPedido = new javax.swing.JTable();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtDetallePedido = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        btnRealizarPago = new javax.swing.JButton();
        btnRefrescarPedidos = new javax.swing.JButton();

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
                .addComponent(lblTituloFormulario, javax.swing.GroupLayout.DEFAULT_SIZE, 998, Short.MAX_VALUE)
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

        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Pedido"));

        jtPedido.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(jtPedido);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 976, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalle Pedido"));

        jtDetallePedido.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jtDetallePedido);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.add(jPanel7, java.awt.BorderLayout.CENTER);

        jPanel8.setPreferredSize(new java.awt.Dimension(776, 40));

        btnRealizarPago.setMnemonic('r');
        btnRealizarPago.setText("Pagar");
        btnRealizarPago.setToolTipText("Realizar Pago de Pedido");
        btnRealizarPago.setMaximumSize(new java.awt.Dimension(76, 23));
        btnRealizarPago.setMinimumSize(new java.awt.Dimension(76, 23));
        btnRealizarPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRealizarPagoActionPerformed(evt);
            }
        });

        btnRefrescarPedidos.setText("Refrescar Pedidos");
        btnRefrescarPedidos.setToolTipText("Refrescar Pedidos");
        btnRefrescarPedidos.setMaximumSize(new java.awt.Dimension(76, 23));
        btnRefrescarPedidos.setMinimumSize(new java.awt.Dimension(76, 23));
        btnRefrescarPedidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefrescarPedidosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnRefrescarPedidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 803, Short.MAX_VALUE)
                .addComponent(btnRealizarPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRealizarPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefrescarPedidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel4.add(jPanel8, java.awt.BorderLayout.SOUTH);

        jtbPrincipal.addTab("Operación", jPanel4);

        jPanel2.add(jtbPrincipal, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRealizarPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRealizarPagoActionPerformed
        // TODO add your handling code here:
        int row = jtPedido.getSelectedRow();
        if (row >= 0) {
            try {
                int opc = JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea pagar el Pedido seleccionado?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );
                if (opc != JOptionPane.YES_OPTION) {
                    return;
                }
                Pedido pedido = (Pedido) jtPedido.getModel().getValueAt(row, 0);
                Compra compra = compraService.realizarCompra(new Compra(null, pedido, LocalDateTime.now()));
                if(compra != null){
                    enviarMensajeExito("Se realizó el pago con ID: " + compra.getIdCompra());
                    inicializarTablaPedidosSinCompra();
                }else{
                    enviarMensajeError("No se encuentra operativo el servicio de Pago, intente luego");
                }
            } catch (Exception ex) {
                enviarMensajeError(ex.getMessage());
                Logger.getLogger(FrmCompra.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            enviarMensajeAdvertencia("No existe Pedido seleccionado para pagar");
        }


    }//GEN-LAST:event_btnRealizarPagoActionPerformed

    private void btnRefrescarPedidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefrescarPedidosActionPerformed
        // TODO add your handling code here:
        inicializarTablaPedidosSinCompra();
    }//GEN-LAST:event_btnRefrescarPedidosActionPerformed

    public void enviarMensajeAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(
                this, // parent component (tu JFrame o JInternalFrame)
                mensaje,
                "Advertencia", // título de la ventana
                JOptionPane.WARNING_MESSAGE // icono de información
        );
    }
    
    
    public void enviarMensajeExito(String mensaje) {
        JOptionPane.showMessageDialog(
                this, // parent component (tu JFrame o JInternalFrame)
                mensaje,
                "Operación exitosa", // título de la ventana
                JOptionPane.INFORMATION_MESSAGE // icono de información
        );
    }

    public void enviarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(
                this, // parent component (tu JFrame o JInternalFrame)
                mensaje,
                "Error", // título de la ventana
                JOptionPane.ERROR_MESSAGE // icono de información
        );
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRealizarPago;
    private javax.swing.JButton btnRefrescarPedidos;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jtDetallePedido;
    private javax.swing.JTable jtPedido;
    private javax.swing.JTabbedPane jtbPrincipal;
    private javax.swing.JLabel lblTituloFormulario;
    // End of variables declaration//GEN-END:variables
}
