package utilidades; // Cambia esto si lo pusiste en tu paquete 'dao'

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GeneradorPDF {

    public boolean crearTicket(String idOrden, String cliente, String equipo, String problema, String total) {
        
        // ¡LA MAGIA TÉRMICA! 
        // 226 puntos equivalen a 80mm de ancho. Le ponemos 800 de alto (el papel rodará lo que necesite)
        Rectangle formatoTicket = new Rectangle(226, 800); 
        
        // Creamos el documento con márgenes de 10 puntos a los lados para no imprimir al borde
        Document documento = new Document(formatoTicket, 10, 10, 10, 10);

        try {
            // Se guardará en la misma carpeta de tu proyecto con el nombre de la orden
            String ruta = "Ticket_Orden_" + idOrden + ".pdf";
            PdfWriter.getInstance(documento, new FileOutputStream(ruta));
            documento.open();

            // Tipografías
            Font fuenteTitulo = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font fuenteNormal = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL);
            Font fuenteNegrita = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);

            // 1. Encabezado del Taller
            Paragraph titulo = new Paragraph("TALLER DE REPARACIÓN\nSanta Bárbara, HN\n", fuenteTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            documento.add(titulo);

            // 2. Fecha y Número de Orden
            String fechaActual = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
            Paragraph fechaYOrden = new Paragraph("\nFecha: " + fechaActual + "\nOrden N°: " + idOrden + "\n----------------------------------------\n", fuenteNormal);
            fechaYOrden.setAlignment(Element.ALIGN_CENTER);
            documento.add(fechaYOrden);

            // 3. Datos del Cliente y Equipo
            documento.add(new Paragraph("Cliente: ", fuenteNegrita));
            documento.add(new Paragraph(cliente + "\n\n", fuenteNormal));
            
            documento.add(new Paragraph("Equipo: ", fuenteNegrita));
            documento.add(new Paragraph(equipo + "\n\n", fuenteNormal));
            
            documento.add(new Paragraph("Problema Reportado: ", fuenteNegrita));
            documento.add(new Paragraph(problema + "\n", fuenteNormal));
            
            documento.add(new Paragraph("----------------------------------------\n", fuenteNormal));

            // 4. Cobro
            Paragraph cobro = new Paragraph("TOTAL: L. " + total + "\n", fuenteTitulo);
            cobro.setAlignment(Element.ALIGN_RIGHT);
            documento.add(cobro);

            // 5. Despedida y Firma
            Paragraph piePagina = new Paragraph("\n\n\n__________________________\nFirma de Recibido\n\n¡Gracias por su preferencia!", fuenteNormal);
            piePagina.setAlignment(Element.ALIGN_CENTER);
            documento.add(piePagina);

            documento.close();
            return true;

        } catch (Exception e) {
            System.err.println("Error al generar ticket: " + e.getMessage());
            return false;
        }
    }
}