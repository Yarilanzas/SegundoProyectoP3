package IIProyectoP3.presentation.scheduling;

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
import domain.CalendarData;
import domain.Category;
import domain.Resource;
import IIProyectoP3.logic.CategoryService;
import IIProyectoP3.logic.ReservationQueryService;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;


public class SchedulingController {
    private SchedulingView view;
    private SchedulingModel model;
    private final CategoryService categoryService = new CategoryService();
    private final ReservationQueryService queryService = new ReservationQueryService();

    public SchedulingController(SchedulingView view, SchedulingModel model) {
        this.view = view;
        this.model = model;

        view.setController(this);
        view.setModel(model);

        cargarCategorias();

    }

    public void cargarCategorias() {
        try {
            model.setCategories(categoryService.findAll());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cargarCalendar(LocalDate fecha, Category categoria) {
        try {
            model.setCalendarData(queryService.getCalendar(fecha, categoria));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void print() throws  Exception{
        String dest = "calendarizacion.pdf";
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);

        Document document = new Document(pdf);
        document.setMargins(20, 20, 20, 20);

        Table header = new Table(2);
        header.setWidth(400);
        header.setHorizontalAlignment(HorizontalAlignment.CENTER);

        header.addCell(getCell(new Paragraph("Listado de Calendarización").setFont(fontBold).setFontSize(18), TextAlignment.CENTER, false));

        document.add(header);
        document.add(new Paragraph("\n"));

        float[] columnWidths = {150f, 150f, 150f};
        Table table = new Table(columnWidths);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

        DeviceRgb redColor = new DeviceRgb(168, 31, 97);

        Cell headerActi = new Cell()
                .add(new Paragraph("Actividad").setFont(fontBold).setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(redColor)
                .setTextAlignment(TextAlignment.CENTER);

        Cell headerRecurso = new Cell()
                .add(new Paragraph("Recurso").setFont(fontBold).setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(redColor)
                .setTextAlignment(TextAlignment.CENTER);

        Cell headerHora = new Cell()
                .add(new Paragraph("Hora").setFont(fontBold).setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(redColor)
                .setTextAlignment(TextAlignment.CENTER);

        table.addHeaderCell(headerActi);
        table.addHeaderCell(headerRecurso);
        table.addHeaderCell(headerHora);

        CalendarData data = model.getCalendarData();

        if (data != null && data.getResources() != null && data.getHours() != null){
            for (String hora: data.getHours()){
                for (Resource r : data.getResources()){
                    String valor = data.getCell(hora,r.getId());

                    if (valor != null && !valor.trim().isEmpty()){
                        Cell activitycell = new Cell()
                                .add(new Paragraph(valor).setFont(font))
                                .setTextAlignment(TextAlignment.CENTER);
                        Cell reccell = new Cell()
                                .add(new Paragraph(r.getDescription()).setFont(font))
                                .setTextAlignment(TextAlignment.CENTER);
                        Cell horacell = new Cell()
                                .add(new Paragraph(hora).setFont(font))
                                .setTextAlignment(TextAlignment.CENTER);

                        table.addCell(activitycell);
                        table.addCell(reccell);
                        table.addCell(horacell);
                    }
                }
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


}