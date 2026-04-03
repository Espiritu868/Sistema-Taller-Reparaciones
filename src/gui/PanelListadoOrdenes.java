
package gui;

import dao.OrdenReparacionDAO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import utilidades.GeneradorPDF;

public class PanelListadoOrdenes extends javax.swing.JPanel {

    
    private OrdenReparacionDAO daoOrden = new OrdenReparacionDAO();

    public PanelListadoOrdenes() {
        initComponents();
        aplicarDisenoModerno();
        refrescarTabla();
    }
    
    private void cargarTabla(List<Object[]> datos) {
        DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"N°", "Cliente", "Equipo", "Problema", "Estado", "Costo", "Tipo"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        for (Object[] fila : datos) {
            modelo.addRow(fila);
        }

        tablaGeneral.setModel(modelo);
        configurarColumnas();
        
        tablaGeneral.getColumnModel().removeColumn(tablaGeneral.getColumnModel().getColumn(6));
    }

    public void refrescarTabla() {
        cargarTabla(daoOrden.listarReporteCompleto());
    }

    private void configurarColumnas() {
        tablaGeneral.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int[] anchos = {50, 150, 120, 250, 100, 80};
        for (int i = 0; i < anchos.length; i++) {
            if (i < tablaGeneral.getColumnCount()) {
                tablaGeneral.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
            }
        }
    }

   private void aplicarDisenoModerno() {
        this.setLayout(new BorderLayout(20, 20));
        this.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        this.setBackground(new Color(240, 244, 248));
        JPanel panelNorte = new JPanel(new BorderLayout(10, 10));
        panelNorte.setOpaque(false);
        
        JPanel panelTitulos = new JPanel();
        panelTitulos.setLayout(new GridLayout(2, 1, 0, 0));
        panelTitulos.setOpaque(false);

        JLabel lblTitulo = new JLabel("Listado de Órdenes");
        JLabel lblTitulo2 = new JLabel("SairTech - Tecnología");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(new Color(44, 62, 80));
        lblTitulo2.setForeground(new Color(127, 140, 141));
        
        panelTitulos.add(lblTitulo);
        panelTitulos.add(lblTitulo2);

        JPanel panelHerramientas = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelHerramientas.setOpaque(false);
        txtBuscarOrden.setPreferredSize(new Dimension(200, 30));
        cmbFiltroEstado.setPreferredSize(new Dimension(150, 30));
        panelHerramientas.add(new JLabel("🔍"));
        panelHerramientas.add(txtBuscarOrden);
        panelHerramientas.add(new JLabel("Estado:"));
        panelHerramientas.add(cmbFiltroEstado);

        panelNorte.add(panelTitulos, BorderLayout.WEST);
        panelNorte.add(panelHerramientas, BorderLayout.EAST);

        JPanel panelGestion = new JPanel(new GridBagLayout());
        panelGestion.setBackground(Color.WHITE);
        panelGestion.setPreferredSize(new Dimension(320, 0));
        panelGestion.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        JLabel lblTituloInfo = new JLabel("ORDEN SELECCIONADA");
        lblTituloInfo.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lblTituloInfo.setForeground(Color.LIGHT_GRAY);
        gbc.gridy = 0; gbc.insets = new Insets(0, 0, 5, 0);
        panelGestion.add(lblTituloInfo, gbc);

        JLabel lblNombreCliente = new JLabel("Ninguna seleccionada");
        lblNombreCliente.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblNombreCliente.setForeground(new Color(41, 128, 185)); 
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 2, 0);
        panelGestion.add(lblNombreCliente, gbc);

        JLabel lblTipoEquipo = new JLabel(" ");
        lblTipoEquipo.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblTipoEquipo.setForeground(new Color(127, 140, 141));
        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 2, 0);
        panelGestion.add(lblTipoEquipo, gbc);

        JLabel lblEquipoDetalle = new JLabel(" ");
        lblEquipoDetalle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblEquipoDetalle.setForeground(Color.DARK_GRAY);
        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 15, 0); 
        panelGestion.add(lblEquipoDetalle, gbc);

        gbc.gridy = 4; gbc.insets = new Insets(0, 0, 15, 0);
        panelGestion.add(new JSeparator(), gbc);

        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.gridy = 4; panelGestion.add(new JLabel("ID de Orden:"), gbc);
        gbc.gridy = 5; panelGestion.add(txtIdOrden, gbc);
        
        gbc.gridy = 6; gbc.insets = new Insets(10, 0, 5, 0);
        panelGestion.add(new JLabel("Estado:"), gbc);
        gbc.gridy = 7; panelGestion.add(cmbNuevoEstado, gbc);

        gbc.gridy = 8; gbc.insets = new Insets(10, 0, 5, 0);
        panelGestion.add(new JLabel("Costo Final (L.):"), gbc);
        gbc.gridy = 9; panelGestion.add(txtCostoFinal, gbc);

        // BOTONES
        estilizarBoton(btnActualizarOrden, new Color(52, 152, 219));
        estilizarBoton(btnEditarDetalles, new Color(155, 89, 182));
        estilizarBoton(btnEntregar, new Color(46, 204, 113));
        estilizarBoton(btnEliminar, new Color(231, 76, 60));

        gbc.insets = new Insets(20, 0, 0, 0);
        gbc.gridy = 10; panelGestion.add(btnActualizarOrden, gbc);
        gbc.insets = new Insets(8, 0, 0, 0);
        gbc.gridy = 11; panelGestion.add(btnEditarDetalles, gbc);
        gbc.gridy = 12; panelGestion.add(btnEntregar, gbc);
        
        gbc.insets = new Insets(30, 0, 0, 0);
        gbc.gridy = 13; panelGestion.add(btnEliminar, gbc);

        // ENSAMBLADO
        this.add(panelNorte, BorderLayout.NORTH);
        this.add(scrollTablaGeneral, BorderLayout.CENTER);
        this.add(panelGestion, BorderLayout.EAST);
        
        this.revalidate();
        this.repaint();
    }

    private void estilizarBoton(JButton btn, Color fondo) {
        btn.setBackground(fondo);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        panelContenedor = new javax.swing.JPanel();
        btnEliminar = new javax.swing.JButton();
        btnActualizarOrden = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        scrollTablaGeneral = new javax.swing.JScrollPane();
        tablaGeneral = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        txtIdOrden = new javax.swing.JTextField();
        cmbNuevoEstado = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        txtCostoFinal = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtBuscarOrden = new javax.swing.JTextField();
        btnEditarDetalles = new javax.swing.JButton();
        btnEntregar = new javax.swing.JButton();
        cmbFiltroEstado = new javax.swing.JComboBox<>();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelContenedor.setBackground(new java.awt.Color(255, 255, 255));
        panelContenedor.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnEliminar.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(this::btnEliminarActionPerformed);
        panelContenedor.add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 440, -1, 44));

        btnActualizarOrden.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        btnActualizarOrden.setText("Actualizar Orden");
        btnActualizarOrden.addActionListener(this::btnActualizarOrdenActionPerformed);
        panelContenedor.add(btnActualizarOrden, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 20, -1, 90));

        btnGuardar.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(this::btnGuardarActionPerformed);
        panelContenedor.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 440, -1, 44));

        tablaGeneral.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tablaGeneral.setModel(new javax.swing.table.DefaultTableModel(
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
        tablaGeneral.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaGeneralMouseClicked(evt);
            }
        });
        scrollTablaGeneral.setViewportView(tablaGeneral);

        panelContenedor.add(scrollTablaGeneral, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 790, 310));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Buscar Orden");
        panelContenedor.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, -1, -1));

        txtIdOrden.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtIdOrden.setEnabled(false);
        panelContenedor.add(txtIdOrden, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 90, 30));

        cmbNuevoEstado.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cmbNuevoEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Recibido", "En Revision", "Reparado", "Entregado", "Sin Reparacion" }));
        panelContenedor.add(cmbNuevoEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 20, 150, 30));

        jLabel3.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Orden N°");
        panelContenedor.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, -1, -1));

        txtCostoFinal.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        panelContenedor.add(txtCostoFinal, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 20, 80, 30));

        jLabel4.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Costo Final");
        panelContenedor.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 20, -1, -1));

        txtBuscarOrden.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        txtBuscarOrden.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarOrdenKeyReleased(evt);
            }
        });
        panelContenedor.add(txtBuscarOrden, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 70, 290, 30));

        btnEditarDetalles.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        btnEditarDetalles.setText("Editar Detalles");
        btnEditarDetalles.addActionListener(this::btnEditarDetallesActionPerformed);
        panelContenedor.add(btnEditarDetalles, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 440, 150, 40));

        btnEntregar.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        btnEntregar.setText("Entregar");
        btnEntregar.addActionListener(this::btnEntregarActionPerformed);
        panelContenedor.add(btnEntregar, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 440, -1, 44));

        cmbFiltroEstado.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cmbFiltroEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "Recibido", "En Reparacion", "Listo para Entrega", "Entregado" }));
        cmbFiltroEstado.addItemListener(this::cmbFiltroEstadoItemStateChanged);
        panelContenedor.add(cmbFiltroEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 70, 130, 30));

        jPanel1.add(panelContenedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 810, 490));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1027, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 627, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cmbFiltroEstadoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbFiltroEstadoItemStateChanged
        if (evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            String filtro = cmbFiltroEstado.getSelectedItem().toString();
            cargarTabla(filtro.equals("Todos") ? daoOrden.listarReporteCompleto() : daoOrden.filtrarPorEstado(filtro));
        }
    }//GEN-LAST:event_cmbFiltroEstadoItemStateChanged

    private void btnEntregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntregarActionPerformed
        int fila = tablaGeneral.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una orden.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idOrden = tablaGeneral.getValueAt(fila, 0).toString().trim();
        String estadoActual = tablaGeneral.getValueAt(fila, 4).toString().trim();
        String costoTotal = tablaGeneral.getValueAt(fila, 5).toString().trim();

        if (estadoActual.equals("Entregado")) {
            JOptionPane.showMessageDialog(this, "Esta orden ya fue entregada anteriormente.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (JOptionPane.showConfirmDialog(this, "¿Confirmar entrega y cobro de L. " + costoTotal + "?", "SairTech", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                gui.VentanaPrincipal v = (gui.VentanaPrincipal) SwingUtilities.getWindowAncestor(this);
                if (daoOrden.marcarComoEntregado(Integer.parseInt(idOrden), v.getIdUsuarioActivo())) {

                    // Generar PDF
                    GeneradorPDF generador = new GeneradorPDF();
                    String[] detalles = daoOrden.obtenerTextosOrden(Integer.parseInt(idOrden));
                    String trabajoRealizado = (detalles[1] != null && !detalles[1].isEmpty()) ? detalles[1] : "Revisión técnica general.";

                    boolean ticketCreado = generador.crearTicket(
                        idOrden,
                        tablaGeneral.getValueAt(fila, 1).toString().trim(),
                        tablaGeneral.getValueAt(fila, 2).toString().trim(),
                        tablaGeneral.getValueAt(fila, 3).toString().trim(),
                        costoTotal,
                        "SAIRTECH - TECNOLOGIA", "1601-2003-XXXXXX", "Santa Bárbara, HN", "+504 8951-8040",
                        "OJO no aplica garantia en equipos mojados, pantallas no cuentan con garantía.", v.getNombreUsuarioActivo(), trabajoRealizado,
                        false
                    );

                    if (ticketCreado) {
                        JOptionPane.showMessageDialog(this, "¡Entrega exitosa! Factura generada.");
                        refrescarTabla();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error: No se pudo crear el ticket.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error critico al entregar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnEntregarActionPerformed

    private void btnEditarDetallesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarDetallesActionPerformed
        if (txtIdOrden.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden de la tabla primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idOrden = Integer.parseInt(txtIdOrden.getText());
        String[] textosActuales = daoOrden.obtenerTextosOrden(idOrden);

        JTextArea txtProblema = new JTextArea(5, 30);
        txtProblema.setText(textosActuales[0] != null ? textosActuales[0] : "");
        txtProblema.setLineWrap(true); txtProblema.setWrapStyleWord(true);

        JTextArea txtTrabajo = new JTextArea(5, 30);
        txtTrabajo.setText(textosActuales[1] != null ? textosActuales[1] : "");
        txtTrabajo.setLineWrap(true); txtTrabajo.setWrapStyleWord(true);

        JPanel panelEdicion = new JPanel(new GridLayout(4, 1, 5, 5));
        panelEdicion.add(new JLabel("Problema Reportado:"));
        panelEdicion.add(new JScrollPane(txtProblema));
        panelEdicion.add(new JLabel("Trabajo Realizado:"));
        panelEdicion.add(new JScrollPane(txtTrabajo));

        if (JOptionPane.showConfirmDialog(this, panelEdicion, "Editando Orden N° " + idOrden, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
            if (daoOrden.actualizarTextosOrden(idOrden, txtProblema.getText().trim(), txtTrabajo.getText().trim())) {
                JOptionPane.showMessageDialog(this, "Detalles actualizados correctamente.");
                refrescarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar los detalles.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnEditarDetallesActionPerformed

    private void txtBuscarOrdenKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarOrdenKeyReleased
        String texto = txtBuscarOrden.getText().trim();
        cargarTabla(texto.isEmpty() ? daoOrden.listarReporteCompleto() : daoOrden.buscarOrden(texto));
    }//GEN-LAST:event_txtBuscarOrdenKeyReleased

    private void tablaGeneralMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaGeneralMouseClicked
        int fila = tablaGeneral.getSelectedRow();
        if (fila >= 0) {
            int modelRow = tablaGeneral.convertRowIndexToModel(fila);
            DefaultTableModel model = (DefaultTableModel) tablaGeneral.getModel();

            txtIdOrden.setText(model.getValueAt(modelRow, 0).toString());
            String cliente = model.getValueAt(modelRow, 1).toString();
            String equipo = model.getValueAt(modelRow, 2).toString();
            String tipo = model.getValueAt(modelRow, 6).toString();

            Object valorEstado = model.getValueAt(modelRow, 4);
            if (valorEstado != null) cmbNuevoEstado.setSelectedItem(valorEstado.toString().trim());
            Object valorCosto = model.getValueAt(modelRow, 5);
            txtCostoFinal.setText(valorCosto != null ? valorCosto.toString() : "0.0");

            try {
                BorderLayout bl = (BorderLayout) this.getLayout();
                JPanel pGestion = (JPanel) bl.getLayoutComponent(BorderLayout.EAST);

                JLabel lblNom = (JLabel) pGestion.getComponent(1);
                JLabel lblTipo = (JLabel) pGestion.getComponent(2);
                JLabel lblEq = (JLabel) pGestion.getComponent(3);

                lblNom.setText(cliente.toUpperCase());
                lblTipo.setText(tipo.toUpperCase());
                lblEq.setText(equipo);

                pGestion.repaint();
            } catch (Exception e) { }
        }
    }//GEN-LAST:event_tablaGeneralMouseClicked

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed

    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnActualizarOrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarOrdenActionPerformed
        if (txtIdOrden.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden de la tabla.");
            return;
        }
        try {
            int id = Integer.parseInt(txtIdOrden.getText());
            String estado = cmbNuevoEstado.getSelectedItem().toString();
            double costo = Double.parseDouble(txtCostoFinal.getText().trim());

            if (daoOrden.actualizarEstadoYCosto(id, estado, costo)) {
                JOptionPane.showMessageDialog(this, "¡Orden actualizada correctamente!");
                refrescarTabla();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un costo válido (ej: 150.00)");
        }
    }//GEN-LAST:event_btnActualizarOrdenActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        if (txtIdOrden.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una orden de la tabla para eliminarla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar la Orden N° " + txtIdOrden.getText() + "?\nEsta acción no se puede deshacer.", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(txtIdOrden.getText());
            if (daoOrden.eliminar(id)) {
                JOptionPane.showMessageDialog(this, "Orden eliminada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                txtIdOrden.setText("");
                cmbNuevoEstado.setSelectedIndex(0);
                txtCostoFinal.setText("");
                refrescarTabla();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar la orden.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnEliminarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizarOrden;
    private javax.swing.JButton btnEditarDetalles;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnEntregar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JComboBox<String> cmbFiltroEstado;
    private javax.swing.JComboBox<String> cmbNuevoEstado;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel panelContenedor;
    private javax.swing.JScrollPane scrollTablaGeneral;
    private javax.swing.JTable tablaGeneral;
    private javax.swing.JTextField txtBuscarOrden;
    private javax.swing.JTextField txtCostoFinal;
    private javax.swing.JTextField txtIdOrden;
    // End of variables declaration//GEN-END:variables
}
