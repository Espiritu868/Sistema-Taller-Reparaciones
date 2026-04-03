
package gui;


public class VentanaPrincipal extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName());

    private int idUsuarioActivo;
    
    public VentanaPrincipal() {
        initComponents();
        this.setLocationRelativeTo(null);
        aplicarDisenoPrincipal();
        
        // Apagamos todos los botones del menú lateral
        btnClientes.setEnabled(false);
        btnEquipos.setEnabled(false);
        btnOrden.setEnabled(false);
        btnListado.setEnabled(false);
        btnUsuarios.setEnabled(false);
        btnUsuarios.setVisible(false);
        btnEstadisticas.setEnabled(false);
        btnSalir.setVisible(false);

        // Cargamos el PanelLogin por defecto en el centro
        PanelLogin login = new PanelLogin();
        mostrarPanel(login);
    }
    
    public void habilitarSistema(String rol, String nombreUsuario) { 
        
        lblPerfil.setText(nombreUsuario + " | " + rol);
        btnClientes.setEnabled(true);
        btnEquipos.setEnabled(true);
        btnOrden.setEnabled(true); 
        btnListado.setEnabled(true);
        btnEstadisticas.setEnabled(true);
        btnSalir.setVisible(true);
        
        // Control de acceso por roles 
        if (rol.equals("Administrador")) {
            btnUsuarios.setEnabled(true);
            btnUsuarios.setVisible(true);
        } else {
            btnUsuarios.setEnabled(false);
            btnUsuarios.setVisible(false);
        }
        
        // Mandamos al usuario al Dashboard (Estadísticas)
        PanelEstadisticas dashboard = new PanelEstadisticas(); 
        mostrarPanel(dashboard); 
        seleccionarBotonMenu(btnEstadisticas); 
    }
    
    private void mostrarPanel(javax.swing.JPanel p) {
        p.setSize(panelContenedor.getWidth(), panelContenedor.getHeight());
        p.setLocation(0,0);
        
        // Limpiamos el contenedor y le metemos el nuevo panel
        panelContenedor.removeAll();
        panelContenedor.add(p, java.awt.BorderLayout.CENTER);
        
        // Le decimos a Java que refresque la pantalla
        panelContenedor.revalidate();
        panelContenedor.repaint();
    }
    private void seleccionarBotonMenu(javax.swing.JButton botonActivo) {
        //ENCENDEMOS todos los botones
        btnEstadisticas.setEnabled(true);
        btnClientes.setEnabled(true);
        btnEquipos.setEnabled(true);
        btnOrden.setEnabled(true);
        btnListado.setEnabled(true);
        btnUsuarios.setEnabled(true);

        // Apagamos el botOn en el que estamos ahorita
        if (botonActivo != null) {
            botonActivo.setEnabled(false);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        btnClientes = new javax.swing.JButton();
        btnEquipos = new javax.swing.JButton();
        btnOrden = new javax.swing.JButton();
        btnListado = new javax.swing.JButton();
        btnUsuarios = new javax.swing.JButton();
        btnEstadisticas = new javax.swing.JButton();
        panelContenedor = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(204, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnClientes.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        btnClientes.setText("Clientes");
        btnClientes.addActionListener(this::btnClientesActionPerformed);
        btnClientes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnClientesKeyPressed(evt);
            }
        });

        btnEquipos.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        btnEquipos.setText("Equipos");
        btnEquipos.addActionListener(this::btnEquiposActionPerformed);
        btnEquipos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnEquiposKeyPressed(evt);
            }
        });

        btnOrden.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        btnOrden.setText("Orden");
        btnOrden.addActionListener(this::btnOrdenActionPerformed);
        btnOrden.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnOrdenKeyPressed(evt);
            }
        });

        btnListado.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        btnListado.setText("Listado");
        btnListado.addActionListener(this::btnListadoActionPerformed);
        btnListado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnListadoKeyPressed(evt);
            }
        });

        btnUsuarios.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        btnUsuarios.setText("Usuarios");
        btnUsuarios.addActionListener(this::btnUsuariosActionPerformed);
        btnUsuarios.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnUsuariosKeyPressed(evt);
            }
        });

        btnEstadisticas.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        btnEstadisticas.setText("Estadisticas");
        btnEstadisticas.addActionListener(this::btnEstadisticasActionPerformed);
        btnEstadisticas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnEstadisticasKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(btnClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnEquipos, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnListado, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnEstadisticas)
                .addGap(18, 18, 18)
                .addComponent(btnUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEquipos, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnListado, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnUsuarios, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEstadisticas, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        panelContenedor.setBackground(new java.awt.Color(255, 255, 255));
        panelContenedor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout panelContenedorLayout = new javax.swing.GroupLayout(panelContenedor);
        panelContenedor.setLayout(panelContenedorLayout);
        panelContenedorLayout.setHorizontalGroup(
            panelContenedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelContenedorLayout.setVerticalGroup(
            panelContenedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 612, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panelContenedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelContenedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnEstadisticasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnEstadisticasKeyPressed

    }//GEN-LAST:event_btnEstadisticasKeyPressed

    private void btnEstadisticasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEstadisticasActionPerformed
        PanelEstadisticas p1 = new PanelEstadisticas();

        mostrarPanel(p1);
        seleccionarBotonMenu(btnEstadisticas);
        btnClientes.setEnabled(true);
        btnEquipos.setEnabled(true);
        btnOrden.setEnabled(true);
        btnListado.setEnabled(true);
        btnUsuarios.setEnabled(true);
        
    }//GEN-LAST:event_btnEstadisticasActionPerformed

    private void btnUsuariosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnUsuariosKeyPressed

    }//GEN-LAST:event_btnUsuariosKeyPressed

    private void btnUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUsuariosActionPerformed
        PanelUsuarios p1 = new PanelUsuarios();

        mostrarPanel(p1);
        seleccionarBotonMenu(btnUsuarios);
        btnClientes.setEnabled(true);
        btnEquipos.setEnabled(true);
        btnOrden.setEnabled(true);
        btnEstadisticas.setEnabled(true);
    }//GEN-LAST:event_btnUsuariosActionPerformed

    private void btnListadoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnListadoKeyPressed

    }//GEN-LAST:event_btnListadoKeyPressed

    private void btnListadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnListadoActionPerformed
        PanelListadoOrdenes p1 = new PanelListadoOrdenes();

        mostrarPanel(p1);
        seleccionarBotonMenu(btnListado);
        btnClientes.setEnabled(true);
        btnEquipos.setEnabled(true);
        btnOrden.setEnabled(true);
        btnEstadisticas.setEnabled(true);
        btnUsuarios.setEnabled(true);
    }//GEN-LAST:event_btnListadoActionPerformed

    private void btnOrdenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnOrdenKeyPressed
    }//GEN-LAST:event_btnOrdenKeyPressed

    private void btnOrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOrdenActionPerformed
        PanelOrdenes p1 = new PanelOrdenes();

        mostrarPanel(p1);
        seleccionarBotonMenu(btnOrden);
        btnClientes.setEnabled(true);
        btnEquipos.setEnabled(true);
        btnListado.setEnabled(true);
        btnEstadisticas.setEnabled(true);
        btnUsuarios.setEnabled(true);
    }//GEN-LAST:event_btnOrdenActionPerformed

    private void btnEquiposKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnEquiposKeyPressed

    }//GEN-LAST:event_btnEquiposKeyPressed

    private void btnEquiposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEquiposActionPerformed
        // Creamos una instancia del panel de clientes
        PanelEquipos p1 = new PanelEquipos();

        // Llamamos al metodo para que lo muestre
        mostrarPanel(p1);
        seleccionarBotonMenu(btnEquipos);
        btnClientes.setEnabled(true);
        btnOrden.setEnabled(true);
        btnListado.setEnabled(true);
        btnEstadisticas.setEnabled(true);
        btnUsuarios.setEnabled(true);
    }//GEN-LAST:event_btnEquiposActionPerformed

    private void btnClientesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnClientesKeyPressed
    }//GEN-LAST:event_btnClientesKeyPressed

    private void btnClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClientesActionPerformed
        // Creamos una instancia del panel de clientes
        PanelClientes p1 = new PanelClientes();

        // Llamamos al metodo para que lo muestre
        mostrarPanel(p1);
        seleccionarBotonMenu(btnClientes);
        btnEquipos.setEnabled(true);
        btnOrden.setEnabled(true);
        btnListado.setEnabled(true);
        btnEstadisticas.setEnabled(true);
        btnUsuarios.setEnabled(true);

    }//GEN-LAST:event_btnClientesActionPerformed
    
    
    /**
     * @param args the command line arguments
     */
    
    public static void main(String args[]) {
        // Ponemos el tema moderno
        try {
            javax.swing.UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Error al iniciar FlatLaf: " + ex.getMessage());
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaPrincipal().setVisible(true);
            }
        });
    }
    
    private void aplicarDisenoPrincipal() {
        // Limpieza total de la ventana
        this.getContentPane().removeAll();
        this.setLayout(new java.awt.BorderLayout());

        // Panel lateral (Sidebar)
        javax.swing.JPanel panelMenu = new javax.swing.JPanel();
        panelMenu.setBackground(new java.awt.Color(44, 62, 80)); 
        panelMenu.setPreferredSize(new java.awt.Dimension(250, 0)); 
        panelMenu.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 10));

        // Logo y Perfil
        javax.swing.JLabel lblLogo = new javax.swing.JLabel("SAIRTECH");
        lblLogo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 26));
        lblLogo.setForeground(java.awt.Color.WHITE);
        lblLogo.setBorder(javax.swing.BorderFactory.createEmptyBorder(30, 0, 5, 0)); 
        panelMenu.add(lblLogo);

        lblPerfil = new javax.swing.JLabel("Iniciando...");
        lblPerfil.setFont(new java.awt.Font("Segoe UI", java.awt.Font.ITALIC, 14));
        lblPerfil.setForeground(new java.awt.Color(189, 195, 199));
        lblPerfil.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 30, 0)); 
        panelMenu.add(lblPerfil);

        // Inicializar y estilizar TODOS los botones
        btnSalir = new javax.swing.JButton(); 
        
        estilizarBotonMenu(btnEstadisticas, "Dashboard");
        estilizarBotonMenu(btnClientes, "Clientes");
        estilizarBotonMenu(btnEquipos, "Equipos");
        estilizarBotonMenu(btnOrden, "Órdenes");
        estilizarBotonMenu(btnListado, "Listado");
        estilizarBotonMenu(btnUsuarios, "Usuarios");
        estilizarBotonMenu(btnSalir, "Cerrar Sesión");
        btnSalir.setForeground(new java.awt.Color(231, 76, 60)); // Rojo
        btnSalir.addActionListener(this::btnSalirActionPerformed);

        // AGREGAR AL PANEL EN EL ORDEN CORRECTO (De arriba a abajo)
        panelMenu.add(btnEstadisticas);
        panelMenu.add(btnClientes);
        panelMenu.add(btnEquipos);
        panelMenu.add(btnOrden);
        panelMenu.add(btnListado);
        panelMenu.add(btnUsuarios);
        
        // EL EMPUJADOR
        javax.swing.JLabel empujador = new javax.swing.JLabel();
        empujador.setPreferredSize(new java.awt.Dimension(250, 150)); 
        panelMenu.add(empujador);
        
        // El botón de salir queda de último
        panelMenu.add(btnSalir);

        // Contenedor central
        panelContenedor.setLayout(new java.awt.BorderLayout());
        panelContenedor.setBackground(new java.awt.Color(240, 244, 248));

        // Ensamblado final
        this.add(panelMenu, java.awt.BorderLayout.WEST);       
        this.add(panelContenedor, java.awt.BorderLayout.CENTER); 

        this.revalidate();
        this.repaint();
    }

    private void estilizarBotonMenu(javax.swing.JButton btn, String texto) {
        btn.setText(texto);
        btn.setPreferredSize(new java.awt.Dimension(250, 50)); 
        btn.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
        btn.setForeground(java.awt.Color.WHITE);
        btn.setBackground(new java.awt.Color(44, 62, 80));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btn.isEnabled()) { // Solo ilumina si el botón no está bloqueado
                    btn.setBackground(new java.awt.Color(52, 73, 94)); 
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new java.awt.Color(44, 62, 80)); 
            }
        });
        
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClientes;
    private javax.swing.JButton btnEquipos;
    private javax.swing.JButton btnEstadisticas;
    private javax.swing.JButton btnListado;
    private javax.swing.JButton btnOrden;
    private javax.swing.JButton btnUsuarios;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel panelContenedor;
    // End of variables declaration//GEN-END:variables
    private javax.swing.JLabel lblPerfil;
    private javax.swing.JButton btnSalir;
    
    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {
        int respuesta = javax.swing.JOptionPane.showConfirmDialog(this, 
            "¿Desea cerrar la sesión actual?", "Cerrar Sesión", 
            javax.swing.JOptionPane.YES_NO_OPTION, 
            javax.swing.JOptionPane.QUESTION_MESSAGE);

        if (respuesta == javax.swing.JOptionPane.YES_OPTION) {
            // Ocultamos la ventana principal
            this.setVisible(false);
            
            // Creamos una nueva ventana desde cero
            VentanaPrincipal nuevaVentana = new VentanaPrincipal();
            nuevaVentana.setVisible(true);
            
            // Destruimos la ventana vieja para liberar memoria
            this.dispose();
        }
    }
    
    public String getNombreUsuarioActivo() {
        return lblPerfil.getText().split(" \\| ")[0];
    }

    public int getIdUsuarioActivo() {
        return idUsuarioActivo;
    }

    public void setIdUsuarioActivo(int idUsuarioActivo) {
        this.idUsuarioActivo = idUsuarioActivo;
    }

}
