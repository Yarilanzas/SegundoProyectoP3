package IIProyectoP3.presentation.reservations;

import IIProyectoP3.logic.ServiceProxy;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import domain.Category;
import domain.ReservaExtraccion;
import domain.Reservation;
import IIProyectoP3.logic.CategoryService;
import IIProyectoP3.logic.ReservationQueryService;
import IIProyectoP3.logic.ReservationService;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;


public class ReservationController {
    private ReservationView view;
    private ReservationModel model;
    //private CategoryService categoryService= new CategoryService();
    //private ReservationService reservationService= new ReservationService();


    private final ReservationQueryService queryService = new ReservationQueryService();

    public ReservationController(ReservationView view, ReservationModel model) {
        this.view = view;
        this.model = model;

        view.setController(this);
        view.setModel(model);

        this.model.addPropertyChangeListener(this.view::propertyChange);

        this.loadCategories();



    }
    public void print() throws Exception {
        String dest = "reservaciones.pdf";
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);

        Document document = new Document(pdf);
        document.setMargins(20, 20, 20, 20);

        Table header = new Table(1);
        header.setWidth(500);
        header.setHorizontalAlignment(HorizontalAlignment.CENTER);

        header.addCell(getCell(new Paragraph("Listado de Reservaciones").setFont(fontBold).setFontSize(18), TextAlignment.CENTER, false));

        document.add(header);
        document.add(new Paragraph("\n"));

        float[] columnWidths = {70f, 100f, 70f, 90f, 120f, 70f};
        Table table = new Table(columnWidths);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

        DeviceRgb redColor = new DeviceRgb(168, 31, 97);

        table.addHeaderCell(new Cell().add(new Paragraph("ID").setFont(fontBold).setFontColor(ColorConstants.WHITE)).setBackgroundColor(redColor).setTextAlignment(TextAlignment.CENTER));
        table.addHeaderCell(new Cell().add(new Paragraph("Actividad").setFont(fontBold).setFontColor(ColorConstants.WHITE)).setBackgroundColor(redColor).setTextAlignment(TextAlignment.CENTER));
        table.addHeaderCell(new Cell().add(new Paragraph("Fecha").setFont(fontBold).setFontColor(ColorConstants.WHITE)).setBackgroundColor(redColor).setTextAlignment(TextAlignment.CENTER));
        table.addHeaderCell(new Cell().add(new Paragraph("Horario").setFont(fontBold).setFontColor(ColorConstants.WHITE)).setBackgroundColor(redColor).setTextAlignment(TextAlignment.CENTER));
        table.addHeaderCell(new Cell().add(new Paragraph("Recurso(s)").setFont(fontBold).setFontColor(ColorConstants.WHITE)).setBackgroundColor(redColor).setTextAlignment(TextAlignment.CENTER));
        table.addHeaderCell(new Cell().add(new Paragraph("Estado").setFont(fontBold).setFontColor(ColorConstants.WHITE)).setBackgroundColor(redColor).setTextAlignment(TextAlignment.CENTER));

        List<Reservation> list = model.getReservations();

        if (list != null && !list.isEmpty()) {
            for (Reservation r : list) {
                String id = r.getId() != null ? r.getId() : "";
                String actividad = r.getActivity() != null ? r.getActivity() : "";
                String fecha = r.getDate() != null ? r.getDate().toString() : "";
                String horario = (r.getStartTime() != null ? r.getStartTime() : "") + " - " + (r.getEndTime() != null ? r.getEndTime() : "");
                String estado = r.getStatus() != null ? r.getStatus() : "";

                String recursos = "";
                if (r.getResources() != null && !r.getResources().isEmpty()) {
                    recursos = r.getResources().stream()
                            .map(rec -> rec.getDescription() != null ? rec.getDescription() : rec.getId())
                            .collect(Collectors.joining(", "));
                } else {
                    recursos = "Sin asignación";
                }

                table.addCell(new Cell().add(new Paragraph(id).setFont(font)).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(actividad).setFont(font)).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(fecha).setFont(font)).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(horario).setFont(font)).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(recursos).setFont(font)).setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(estado).setFont(font)).setTextAlignment(TextAlignment.CENTER));
            }
        }

        document.add(table);
        document.close();
        openPdf(dest);
    }

    private void openPdf(String path) {
        try {
            File pdfFile = new File(path);
            if (pdfFile.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(pdfFile);
                } else {
                    System.out.println("AWT Desktop is not supported on this platform.");
                }
            } else {
                System.out.println("The target PDF file does not exist.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private Cell getCell(Paragraph paragraph, TextAlignment alignment, boolean hasBorder) {
        Cell cell = new Cell().add(paragraph);
        cell.setPadding(0);
        cell.setTextAlignment(alignment);
        if (!hasBorder) cell.setBorder(Border.NO_BORDER);
        return cell;
    }
    public void loadCategories() {
        try {
            List<Category> list = ServiceProxy.instance().categoryFindAll();
            model.setCategories(list);
            List<Reservation> listRE = ServiceProxy.instance().reservationFindAll();
            model.setReservations(listRE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void clear(){
        Reservation r= new Reservation();
        model.setCurrent(r);
    }

    public void saveReservation(Reservation r, List<Category> categorias) throws Exception {
        ServiceProxy.instance().reservationSave(r, categorias);

        model.setReservations(ServiceProxy.instance().reservationFindAll());
        clear();
    }
    public void cancelReservation(String id) throws Exception {
        ServiceProxy.instance().reservationCancel(id);
        model.setReservations(ServiceProxy.instance().reservationFindAll());
        clear();
    }
    public ReservaExtraccion extraerDeFrase(String frase) throws Exception {
        return ServiceProxy.instance().reservationExtraer(frase);
    }

    public void delete(String id)throws Exception{
        ServiceProxy.instance().reservationDelete(id);
        list();
        model.setCurrent(new Reservation());
    }

    public void list() {
        try {
            model.setReservations(ServiceProxy.instance().reservationFindAll());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}