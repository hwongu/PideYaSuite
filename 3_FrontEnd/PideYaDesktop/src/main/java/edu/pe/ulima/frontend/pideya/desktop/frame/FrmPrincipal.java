/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/MDIApplication.java to edit this template
 */
package edu.pe.ulima.frontend.pideya.desktop.frame;

import edu.pe.ulima.frontend.pideya.desktop.internalframe.FrmAcercaDe;
import edu.pe.ulima.frontend.pideya.desktop.internalframe.FrmCategoria;
import edu.pe.ulima.frontend.pideya.desktop.internalframe.FrmClienteEmpresa;
import edu.pe.ulima.frontend.pideya.desktop.internalframe.FrmClienteNatural;
import edu.pe.ulima.frontend.pideya.desktop.internalframe.FrmCompra;
import edu.pe.ulima.frontend.pideya.desktop.internalframe.FrmCompraCliente;
import edu.pe.ulima.frontend.pideya.desktop.internalframe.FrmImprimirCompra;
import edu.pe.ulima.frontend.pideya.desktop.internalframe.FrmImprimirCompraCliente;
import edu.pe.ulima.frontend.pideya.desktop.internalframe.FrmPedido;
import edu.pe.ulima.frontend.pideya.desktop.internalframe.FrmPedidoCliente;
import edu.pe.ulima.frontend.pideya.desktop.internalframe.FrmProducto;
import edu.pe.ulima.permisoya.back.model.MenuItem;
import edu.pe.ulima.permisoya.back.model.Role;
import edu.pe.ulima.permisoya.back.model.RoleMenu;
import edu.pe.ulima.permisoya.back.service.RoleService;
import edu.pe.ulima.pideya.back.model.Usuario;
import java.awt.Dimension;
import java.beans.PropertyVetoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;

/**
 *
 * @author hwong
 */
public class FrmPrincipal extends javax.swing.JFrame {

    public static Usuario usuarioInicioSesion = new Usuario();
    private RoleService roleService = new RoleService();

    /**
     * Creates new form frmPrincipal
     */
    public FrmPrincipal() {
        try {
            initComponents();
            this.setExtendedState(this.getExtendedState() | javax.swing.JFrame.MAXIMIZED_BOTH);
            //FrmPrincipal.usuarioInicioSesion = usuarioService.autenticarUsuario("juan.perez@ulima.edu.pe", "clave123");
            //FrmPrincipal.usuarioInicioSesion = usuarioService.autenticarUsuario("techsolutions@ulima.edu.pe", "empresa123");
        } catch (Exception ex) {
            Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void abrirInternalFrame(JInternalFrame nuevo, int anchoEnviado, int altoEnviado) {
        // 1) Definir un tamaño común para todos los formularios
        int ancho = anchoEnviado == 0 ? 800 : anchoEnviado;
        int alto = altoEnviado == 0 ? 600 : altoEnviado;
        nuevo.setSize(ancho, alto);

        // 2) Calcular posición centrada dentro del desktopPane
        Dimension dim = desktopPane.getSize();
        int x = (dim.width - ancho) / 2;
        int y = (dim.height - alto) / 2;

        // 3) Si ya existe uno del mismo tipo, lo traemos al frente
        for (JInternalFrame abierto : desktopPane.getAllFrames()) {
            if (abierto.getClass().equals(nuevo.getClass())) {
                try {
                    abierto.setIcon(false);
                    abierto.setSelected(true);
                    abierto.toFront();
                } catch (PropertyVetoException e) {
                    e.printStackTrace();
                }
                return;
            }
        }

        // 4) Añadimos, situamos y mostramos
        desktopPane.add(nuevo);
        nuevo.setLocation(x, y);
        nuevo.setVisible(true);
        try {
            nuevo.setSelected(true);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    public void aplicarPermisosMenu() {
        try {
            // 1) Traer el Role completo con sus RoleMenu
            Role role = roleService.listarMenusPorRole(usuarioInicioSesion.getTipoUsuario().name());

            // 2) Ocultar todo al inicio
            jmGestionarAlmacen.setVisible(false);
            jmiMantCategoria.setVisible(false);
            jmiMantProducto.setVisible(false);

            jmGestionarCliente.setVisible(false);
            jmiMantClienteNatural.setVisible(false);
            jmiMantClienteEmpresa.setVisible(false);

            jmGestionarPedido.setVisible(false);
            jmiRealPedido.setVisible(false);
            jmiRealizarPago.setVisible(false);
            jmiImprimirCompras.setVisible(false);

            jmGestionarPedidoClientes.setVisible(false);
            jmiRealizarPedidoCliente.setVisible(false);
            jmiRealizarPagoPedidoCliente.setVisible(false);
            jmiImprimirCompraPedidoCliente.setVisible(false);

            jfileAyuda.setVisible(false);
            jmiAcercaDe.setVisible(false);

            // 3) Mostrar solo los que el Role tiene asignados
            for (RoleMenu rm : role.getRoleMenus()) {
                MenuItem perm = rm.getMenuItem();
                switch (perm.getNombre()) {
                    case "jmiMantCategoria" -> {
                        jmGestionarAlmacen.setVisible(true);
                        jmiMantCategoria.setVisible(true);
                    }
                    case "jmiMantProducto" -> {
                        jmGestionarAlmacen.setVisible(true);
                        jmiMantProducto.setVisible(true);
                    }
                    case "jmiMantClienteNatural" -> {
                        jmGestionarCliente.setVisible(true);
                        jmiMantClienteNatural.setVisible(true);
                    }
                    case "jmiMantClienteEmpresa" -> {
                        jmGestionarCliente.setVisible(true);
                        jmiMantClienteEmpresa.setVisible(true);
                    }
                    case "jmiRealPedido" -> {
                        jmGestionarPedido.setVisible(true);
                        jmiRealPedido.setVisible(true);
                    }
                    case "jmiRealizarPago" -> {
                        jmGestionarPedido.setVisible(true);
                        jmiRealizarPago.setVisible(true);
                    }
                    case "jmiImprimirCompras" -> {
                        jmGestionarPedido.setVisible(true);
                        jmiImprimirCompras.setVisible(true);
                    }
                    case "jmiRealizarPedidoCliente" -> {
                        jmGestionarPedidoClientes.setVisible(true);
                        jmiRealizarPedidoCliente.setVisible(true);
                    }
                    case "jmiRealizarPagoPedidoCliente" -> {
                        jmGestionarPedidoClientes.setVisible(true);
                        jmiRealizarPagoPedidoCliente.setVisible(true);
                    }
                    case "jmiImprimirCompraPedidoCliente" -> {
                        jmGestionarPedidoClientes.setVisible(true);
                        jmiImprimirCompraPedidoCliente.setVisible(true);
                    }
                    case "jmiAcercaDe" -> {
                        jfileAyuda.setVisible(true);
                        jmiAcercaDe.setVisible(true);
                    }
                    default -> {
                        // Otros permisos no mapeados
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(FrmPrincipal.class.getName())
                    .log(Level.SEVERE, "Error aplicando permisos de menú", ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        desktopPane = new javax.swing.JDesktopPane();
        menuBar = new javax.swing.JMenuBar();
        jmGestionarAlmacen = new javax.swing.JMenu();
        jmiMantCategoria = new javax.swing.JMenuItem();
        jmiMantProducto = new javax.swing.JMenuItem();
        jmGestionarCliente = new javax.swing.JMenu();
        jmiMantClienteNatural = new javax.swing.JMenuItem();
        jmiMantClienteEmpresa = new javax.swing.JMenuItem();
        jmGestionarPedido = new javax.swing.JMenu();
        jmiRealPedido = new javax.swing.JMenuItem();
        jmiRealizarPago = new javax.swing.JMenuItem();
        jmiImprimirCompras = new javax.swing.JMenuItem();
        jmGestionarPedidoClientes = new javax.swing.JMenu();
        jmiRealizarPedidoCliente = new javax.swing.JMenuItem();
        jmiRealizarPagoPedidoCliente = new javax.swing.JMenuItem();
        jmiImprimirCompraPedidoCliente = new javax.swing.JMenuItem();
        jfileAyuda = new javax.swing.JMenu();
        jmiAcercaDe = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PideYa - Desktop");

        menuBar.setToolTipText("TAtiendo");

        jmGestionarAlmacen.setMnemonic('a');
        jmGestionarAlmacen.setText("Gestionar Almacen");

        jmiMantCategoria.setText("Mant. Categoría");
        jmiMantCategoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiMantCategoriaActionPerformed(evt);
            }
        });
        jmGestionarAlmacen.add(jmiMantCategoria);

        jmiMantProducto.setText("Mant. Producto");
        jmiMantProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiMantProductoActionPerformed(evt);
            }
        });
        jmGestionarAlmacen.add(jmiMantProducto);

        menuBar.add(jmGestionarAlmacen);

        jmGestionarCliente.setMnemonic('c');
        jmGestionarCliente.setText("Gestionar Cliente");

        jmiMantClienteNatural.setText("Mant. Cliente Natural");
        jmiMantClienteNatural.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiMantClienteNaturalActionPerformed(evt);
            }
        });
        jmGestionarCliente.add(jmiMantClienteNatural);

        jmiMantClienteEmpresa.setText("Mant. Cliente Empresa");
        jmiMantClienteEmpresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiMantClienteEmpresaActionPerformed(evt);
            }
        });
        jmGestionarCliente.add(jmiMantClienteEmpresa);

        menuBar.add(jmGestionarCliente);

        jmGestionarPedido.setMnemonic('p');
        jmGestionarPedido.setText("Gestionar Pedido");

        jmiRealPedido.setText("Realizar Pedido");
        jmiRealPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiRealPedidoActionPerformed(evt);
            }
        });
        jmGestionarPedido.add(jmiRealPedido);

        jmiRealizarPago.setText("Realizar Pago de Pedido");
        jmiRealizarPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiRealizarPagoActionPerformed(evt);
            }
        });
        jmGestionarPedido.add(jmiRealizarPago);

        jmiImprimirCompras.setText("Imprimir Compras");
        jmiImprimirCompras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiImprimirComprasActionPerformed(evt);
            }
        });
        jmGestionarPedido.add(jmiImprimirCompras);

        menuBar.add(jmGestionarPedido);

        jmGestionarPedidoClientes.setText("Gestionar Pedido Clientes");

        jmiRealizarPedidoCliente.setText("Realizar Pedido Cliente");
        jmiRealizarPedidoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiRealizarPedidoClienteActionPerformed(evt);
            }
        });
        jmGestionarPedidoClientes.add(jmiRealizarPedidoCliente);

        jmiRealizarPagoPedidoCliente.setText("Realizar Pago de Pedido Cliente");
        jmiRealizarPagoPedidoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiRealizarPagoPedidoClienteActionPerformed(evt);
            }
        });
        jmGestionarPedidoClientes.add(jmiRealizarPagoPedidoCliente);

        jmiImprimirCompraPedidoCliente.setText("Imprimir Compra de Pedido Cliente");
        jmiImprimirCompraPedidoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiImprimirCompraPedidoClienteActionPerformed(evt);
            }
        });
        jmGestionarPedidoClientes.add(jmiImprimirCompraPedidoCliente);

        menuBar.add(jmGestionarPedidoClientes);

        jfileAyuda.setMnemonic('y');
        jfileAyuda.setText("Ayuda");

        jmiAcercaDe.setMnemonic('a');
        jmiAcercaDe.setText("Acerca de ...");
        jmiAcercaDe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jmiAcercaDeActionPerformed(evt);
            }
        });
        jfileAyuda.add(jmiAcercaDe);

        menuBar.add(jfileAyuda);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktopPane, javax.swing.GroupLayout.DEFAULT_SIZE, 766, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktopPane, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jmiMantCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiMantCategoriaActionPerformed
        this.abrirInternalFrame(new FrmCategoria(), 0, 0);
    }//GEN-LAST:event_jmiMantCategoriaActionPerformed

    private void jmiMantProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiMantProductoActionPerformed
        // TODO add your handling code here:
        this.abrirInternalFrame(new FrmProducto(), 0, 0);
    }//GEN-LAST:event_jmiMantProductoActionPerformed

    private void jmiMantClienteEmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiMantClienteEmpresaActionPerformed
        // TODO add your handling code here:
        this.abrirInternalFrame(new FrmClienteEmpresa(), 0, 0);
    }//GEN-LAST:event_jmiMantClienteEmpresaActionPerformed

    private void jmiMantClienteNaturalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiMantClienteNaturalActionPerformed
        // TODO add your handling code here:
        this.abrirInternalFrame(new FrmClienteNatural(), 0, 0);
    }//GEN-LAST:event_jmiMantClienteNaturalActionPerformed

    private void jmiRealPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiRealPedidoActionPerformed
        // TODO add your handling code here:
        this.abrirInternalFrame(new FrmPedido(), 0, 0);
    }//GEN-LAST:event_jmiRealPedidoActionPerformed

    private void jmiRealizarPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiRealizarPagoActionPerformed
        // TODO add your handling code here:
        this.abrirInternalFrame(new FrmCompra(), 0, 0);
    }//GEN-LAST:event_jmiRealizarPagoActionPerformed

    private void jmiImprimirComprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiImprimirComprasActionPerformed
        // TODO add your handling code here:
        this.abrirInternalFrame(new FrmImprimirCompra(), 1000, 800);
    }//GEN-LAST:event_jmiImprimirComprasActionPerformed

    private void jmiAcercaDeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiAcercaDeActionPerformed
        // TODO add your handling code here:
        this.abrirInternalFrame(new FrmAcercaDe(), 610, 420);
    }//GEN-LAST:event_jmiAcercaDeActionPerformed

    private void jmiRealizarPedidoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiRealizarPedidoClienteActionPerformed
        // TODO add your handling code here:
        this.abrirInternalFrame(new FrmPedidoCliente(), 0, 0);
    }//GEN-LAST:event_jmiRealizarPedidoClienteActionPerformed

    private void jmiRealizarPagoPedidoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiRealizarPagoPedidoClienteActionPerformed
        // TODO add your handling code here:
        this.abrirInternalFrame(new FrmCompraCliente(), 0, 0);
    }//GEN-LAST:event_jmiRealizarPagoPedidoClienteActionPerformed

    private void jmiImprimirCompraPedidoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jmiImprimirCompraPedidoClienteActionPerformed
        // TODO add your handling code here:
        this.abrirInternalFrame(new FrmImprimirCompraCliente(), 0, 0);
    }//GEN-LAST:event_jmiImprimirCompraPedidoClienteActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane desktopPane;
    private javax.swing.JMenu jfileAyuda;
    private javax.swing.JMenu jmGestionarAlmacen;
    private javax.swing.JMenu jmGestionarCliente;
    private javax.swing.JMenu jmGestionarPedido;
    private javax.swing.JMenu jmGestionarPedidoClientes;
    private javax.swing.JMenuItem jmiAcercaDe;
    private javax.swing.JMenuItem jmiImprimirCompraPedidoCliente;
    private javax.swing.JMenuItem jmiImprimirCompras;
    private javax.swing.JMenuItem jmiMantCategoria;
    private javax.swing.JMenuItem jmiMantClienteEmpresa;
    private javax.swing.JMenuItem jmiMantClienteNatural;
    private javax.swing.JMenuItem jmiMantProducto;
    private javax.swing.JMenuItem jmiRealPedido;
    private javax.swing.JMenuItem jmiRealizarPago;
    private javax.swing.JMenuItem jmiRealizarPagoPedidoCliente;
    private javax.swing.JMenuItem jmiRealizarPedidoCliente;
    private javax.swing.JMenuBar menuBar;
    // End of variables declaration//GEN-END:variables

}
