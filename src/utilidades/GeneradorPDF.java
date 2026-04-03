package utilidades;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GeneradorPDF {

    // --- EL MÉTODO VIEJO (Sobrecarga para compatibilidad) ---
    public boolean crearTicket(String idOrden, String cliente, String equipo, String problema, String total) {
        return crearTicket(idOrden, cliente, equipo, problema, total, 
                "SAIRTECH", "1601-2003-XXXXXX", "Santa Bárbara, HN", "+504 9999-9999", 
                "Garantía de 30 días en mano de obra. No aplica en daños por líquido o software.", 
                "Sistema");
    }

    // --- EL MÉTODO NUEVO (Nivel Comercial) ---
    public boolean crearTicket(String idOrden, String cliente, String equipo, String problema, 
                               String total, String nombreEmpresa, String rtn, String direccion, 
                               String telefono, String politicaGarantia, String nombreTecnico) {
        
        Rectangle formatoTicket = new Rectangle(226, 800); 
        Document documento = new Document(formatoTicket, 10, 10, 10, 10);

        try {
            String ruta = "Ticket_Orden_" + idOrden + ".pdf";
            PdfWriter.getInstance(documento, new FileOutputStream(ruta));
            documento.open();

            Font fuenteLogo = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Font fuenteTitulo = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
            Font fuenteNormal = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL);
            Font fuenteNegrita = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
            Font fuenteMini = new Font(Font.FontFamily.HELVETICA, 7, Font.ITALIC);

            // 1. Encabezado
            Paragraph header = new Paragraph();
            header.setAlignment(Element.ALIGN_CENTER);
            header.add(new Paragraph(nombreEmpresa.toUpperCase() + "\n", fuenteLogo));
            header.add(new Paragraph(direccion + "\nTel: " + telefono + "\n", fuenteNormal));
            documento.add(header);

            // 2. Título dinámico según si hay costo o no
            // Si el total es "0.0", "0" o está vacío, es una RECEPCIÓN
            boolean esRecepcion = total.equals("0.0") || total.equals("0") || total.isEmpty();
            String tituloPrincipal = esRecepcion ? "COMPROBANTE DE RECEPCIÓN" : "ORDEN DE SERVICIO FINALIZADA";

            Paragraph infoOrden = new Paragraph();
            infoOrden.setAlignment(Element.ALIGN_CENTER);
            infoOrden.add(new Paragraph("\n" + tituloPrincipal + "\n", fuenteTitulo));
            infoOrden.add(new Paragraph("Orden #: " + idOrden + "\n", fuenteNormal));
            infoOrden.add(new Paragraph("Atendido por: " + nombreTecnico + "\n", fuenteNegrita));
            infoOrden.add(new Paragraph("Fecha: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()) + "\n", fuenteNormal));
            infoOrden.add(new Paragraph("----------------------------------------\n", fuenteNormal));
            documento.add(infoOrden);

            // 3. Detalles
            documento.add(new Paragraph("CLIENTE: ", fuenteNegrita));
            documento.add(new Paragraph(cliente + "\n", fuenteNormal));
            documento.add(new Paragraph("\nEQUIPO: ", fuenteNegrita));
            documento.add(new Paragraph(equipo + "\n", fuenteNormal));
            documento.add(new Paragraph("\nFALLA/SÍNTOMAS: ", fuenteNegrita));
            documento.add(new Paragraph(problema + "\n", fuenteNormal));
            documento.add(new Paragraph("----------------------------------------\n", fuenteNormal));

            // 4. Mostrar total solo si NO es recepción
            if (!esRecepcion) {
                Paragraph cobro = new Paragraph("TOTAL A PAGAR: L. " + total + "\n", fuenteTitulo);
                cobro.setAlignment(Element.ALIGN_RIGHT);
                documento.add(cobro);
            } else {
                Paragraph aviso = new Paragraph("EQUIPO SUJETO A REVISIÓN\n", fuenteNegrita);
                aviso.setAlignment(Element.ALIGN_CENTER);
                documento.add(aviso);
            }

            // 5. Pie de página
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
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}