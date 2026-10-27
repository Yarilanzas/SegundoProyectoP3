package IIProyectoP3.presentation.employees;

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
import domain.Employee;

import java.awt.*;
import java.io.File;
import java.util.List;

public class EmployeeController {
    //private final EmployeeService service = new EmployeeService();
    private  EmployeeModel model;
    private  EmployeeView view;

    public EmployeeController(EmployeeModel model, EmployeeView view) {
        this.model = model;
        this.view = view;

        view.setController(this);
        view.setModel(model);

        list();
    }

    public void list() {
        try {
            model.setList(ServiceProxy.instance().employeeFindAll());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void create(Employee e) throws Exception {
        ServiceProxy.instance().employeeSave(e);
        list();
        model.setCurrent(new Employee());
    }

    public void edit(int row) {
        model.setCurrent(model.getList().get(row));
    }

    public void search(String txt, boolean porId) {
        try {
            if (porId){
                Employee e = ServiceProxy.instance().employeeFindbyId(txt);
                model.setList(e != null ? List.of(e) : List.of());
            } else {
                model.setList(txt.isEmpty() ? ServiceProxy.instance().employeeFindAll() : ServiceProxy.instance().employeeFindbyName(txt));
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void delete(String id) throws Exception {
        ServiceProxy.instance().employeeDelete(id);
        list();
        model.setCurrent(new Employee());
    }

    public void clear() {
        model.setCurrent(new Employee());
    }

    public void print() throws  Exception{
        String dest = "funcionarios.pdf";
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);

        Document document = new Document(pdf);
        document.setMargins(20, 20, 20, 20);

        Table header = new Table(2);
        header.setWidth(400);
        header.setHorizontalAlignment(HorizontalAlignment.CENTER);

        header.addCell(getCell(new Paragraph("Listado de Funcionarios").setFont(fontBold).setFontSize(18), TextAlignment.CENTER, false));

        document.add(header);
        document.add(new Paragraph("\n"));

        float[] columnWidths = {150f, 150f, 150f};
        Table table = new Table(columnWidths);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

        DeviceRgb redColor = new DeviceRgb(168, 31, 97);

        Cell headerId = new Cell()
                .add(new Paragraph("ID").setFont(fontBold).setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(redColor)
                .setTextAlignment(TextAlignment.CENTER);

        Cell headerNombre = new Cell()
                .add(new Paragraph("Nombre").setFont(fontBold).setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(redColor)
                .setTextAlignment(TextAlignment.CENTER);

        Cell headerCel = new Cell()
                .add(new Paragraph("Telefono").setFont(fontBold).setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(redColor)
                .setTextAlignment(TextAlignment.CENTER);

        table.addHeaderCell(headerId);
        table.addHeaderCell(headerNombre);
        table.addHeaderCell(headerCel);

        List<Employee> emp = model.getList();

        if (emp != null && !emp.isEmpty()){
            for (Employee e : emp) {
                Cell cellId = new Cell()
                        .add(new Paragraph(e.getId()).setFont(font))
                        .setTextAlignment(TextAlignment.CENTER);

                Cell cellNombre = new Cell()
                        .add(new Paragraph(e.getName()).setFont(font))
                        .setTextAlignment(TextAlignment.CENTER);


                Cell cellTel = new Cell()
                        .add(new Paragraph(e.getPhone()).setFont(font))
                        .setTextAlignment(TextAlignment.CENTER);

                table.addCell(cellId);
                table.addCell(cellNombre);
                table.addCell(cellTel);


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