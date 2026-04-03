package utilidades;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

public class GeneradorPDF {

    // --- MÉTODO VIEJO (Por si lo usas en otro lado) ---
    public boolean crearTicket(String idOrden, String cliente, String equipo, String problema, String total, String trabajo) {
        return crearTicket(idOrden, cliente, equipo, problema, total, 
                "SAIRTECH", "1601-2003-XXXXXX", "Santa Bárbara, HN", "+504 9999-9999", 
                "Garantía de 30 días en mano de obra.", "Sistema", trabajo, true); 
    }

    // --- MÉTODO NUEVO COMERCIAL (Ahora pide confirmación: ¿esRecepcion?) ---
    public boolean crearTicket(String idOrden, String cliente, String equipo, String problema, 
                               String total, String nombreEmpresa, String rtn, String direccion, 
                               String telefono, String politicaGarantia, String nombreTecnico, String trabajo, 
                               boolean esRecepcion) { // <--- NUEVO PARÁMETRO AQUÍ

        Rectangle formatoTicket = new Rectangle(226, 800); 
        Document documento = new Document(formatoTicket, 10, 10, 10, 10);

        try {
            // 1. RUTA DINÁMICA A LA CARPETA DEL PROYECTO
            String rutaBase = System.getProperty("user.dir") + java.io.File.separator + "Tickets_Sairtech";
            java.io.File dirBase = new java.io.File(rutaBase);
            if (!dirBase.exists()) dirBase.mkdir();

            // 2. AHORA LA CARPETA DEPENDE DEL PARÁMETRO, NO DEL COSTO
            String nombreCarpeta = esRecepcion ? "Recepciones" : "Entregas";
            java.io.File directorioFinal = new java.io.File(rutaBase + java.io.File.separator + nombreCarpeta);
            if (!directorioFinal.exists()) directorioFinal.mkdir();

            String clienteLimpio = cliente.replace(" ", "_");
            String nombreArchivo = idOrden + "_" + clienteLimpio + ".pdf";
            String ruta = directorioFinal.getAbsolutePath() + java.io.File.separator + nombreArchivo;
            
            PdfWriter.getInstance(documento, new java.io.FileOutputStream(ruta));
            documento.open();

            Font fuenteLogo = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Font fuenteTitulo = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
            Font fuenteNormal = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
            Font fuenteNegrita = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
            Font fuenteMini = new Font(Font.FontFamily.HELVETICA, 7, Font.ITALIC);

            Paragraph header = new Paragraph();
            header.setAlignment(Element.ALIGN_CENTER);
            header.add(new Paragraph(nombreEmpresa.toUpperCase() + "\n", fuenteLogo));
            header.add(new Paragraph(direccion + "\nTel: " + telefono + "\n", fuenteNormal));
            documento.add(header);

            // 3. TÍTULO DEPENDIENTE DEL PARÁMETRO
            String tituloPrincipal = esRecepcion ? "COMPROBANTE DE RECEPCIÓN" : "FACTURA DE SERVICIO";

            Paragraph infoOrden = new Paragraph();
            infoOrden.setAlignment(Element.ALIGN_CENTER);
            infoOrden.add(new Paragraph("\n" + tituloPrincipal + "\n", fuenteTitulo));
            infoOrden.add(new Paragraph("Orden #: " + idOrden + "\n", fuenteNormal));
            infoOrden.add(new Paragraph("Atendido por: " + nombreTecnico + "\n", fuenteNegrita));
            infoOrden.add(new Paragraph("Fecha: " + new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date()) + "\n", fuenteNormal));
            infoOrden.add(new Paragraph("----------------------------------------\n", fuenteNormal));
            documento.add(infoOrden);

            documento.add(new Paragraph("CLIENTE: ", fuenteNegrita));
            documento.add(new Paragraph(cliente + "\n", fuenteNormal));
            documento.add(new Paragraph("EQUIPO: ", fuenteNegrita));
            documento.add(new Paragraph(equipo + "\n\n", fuenteNormal));

            com.itextpdf.text.pdf.PdfPTable tablaDetalle = new com.itextpdf.text.pdf.PdfPTable(1);
            tablaDetalle.setWidthPercentage(100);

            com.itextpdf.text.pdf.PdfPCell celda = new com.itextpdf.text.pdf.PdfPCell();
            celda.setBorder(Rectangle.BOX);
            celda.setBorderColor(BaseColor.LIGHT_GRAY);
            celda.setPadding(8);
            celda.setBackgroundColor(new BaseColor(245, 245, 245)); 
            
            Paragraph pContenido = new Paragraph("PROBLEMA/SÍNTOMAS:\n", fuenteNegrita);
            pContenido.add(new Paragraph(problema + "\n", fuenteNormal));
            
            if (!esRecepcion) {
                pContenido.add(new Paragraph("\nTRABAJO REALIZADO:\n", fuenteNegrita));
                pContenido.add(new Paragraph(trabajo, fuenteNormal));
            }

            celda.addElement(pContenido);
            tablaDetalle.addCell(celda);
            documento.add(tablaDetalle);
            documento.add(new Paragraph("\n----------------------------------------\n", fuenteNormal));

            // 4. COBRO (Ahora muestra el total así sea 0.00 en las Entregas)
            if (!esRecepcion) {
                Paragraph cobro = new Paragraph("TOTAL A PAGAR: L. " + total + "\n", fuenteTitulo);
                cobro.setAlignment(Element.ALIGN_RIGHT);
                documento.add(cobro);
            } else {
                Paragraph aviso = new Paragraph("EQUIPO SUJETO A REVISIÓN\n", fuenteNegrita);
                aviso.setAlignment(Element.ALIGN_CENTER);
                documento.add(aviso);
            }

            Paragraph footer = new Paragraph();
            footer.setAlignment(Element.ALIGN_CENTER);
            if(esRecepcion) {
                footer.add(new Paragraph("\nPresente este ticket para retirar su equipo.\nNo nos hacemos responsables por equipos olvidados después de 30 días.\n", fuenteMini));
            } else {
                footer.add(new Paragraph("\nPOLÍTICA DE GARANTÍA:\n", fuenteNegrita));
                footer.add(new Paragraph(politicaGarantia + "\n", fuenteMini));
            }
            footer.add(new Paragraph("\n\n__________________________\nFirma de Conformidad", fuenteNormal));
            documento.add(footer);

            documento.close();

            // ABRIR EL PDF AUTOMÁTICAMENTE
            try {
                java.io.File archivoGenerado = new java.io.File(ruta);
                if (archivoGenerado.exists()) {
                    java.awt.Desktop.getDesktop().open(archivoGenerado);
                }
            } catch (Exception ex) {
                System.err.println("Error al intentar abrir el PDF: " + ex.getMessage());
            }

            return true;

        } catch (Exception e) {
            System.err.println("Error al generar el PDF: " + e.getMessage());
            return false;
        }
    }
}