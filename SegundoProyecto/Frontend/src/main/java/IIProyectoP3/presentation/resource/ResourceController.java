package IIProyectoP3.presentation.resource;

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
import domain.Resource;
import IIProyectoP3.logic.CategoryService;
import IIProyectoP3.logic.ResourceService;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;


public class ResourceController {
    private ResourceView view;
    private ResourceModel model;
    //private final ResourceService resourceService = new ResourceService();
    //private final CategoryService categoryService = new CategoryService();


    public ResourceController(ResourceView view, ResourceModel model) {
        this.view = view;
        this.model = model;

        view.setController(this);
        view.setModel(model);
        cargarCategorias();
        try {
            list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al inicializar la vista de recursos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void searchbyCategory(String cat) {
        try {
            List<Resource> list = ServiceProxy.instance().resourceFindbyCategory(cat);
            model.setResources(list);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void searchbyDes(String desc) {
        try {
            List<Resource> list = ServiceProxy.instance().resourceFindbyDesc(desc);
            model.setResources(list);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void cargarCategorias() {
        try {
            model.setCategories(ServiceProxy.instance().categoryFindAll());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void saveResource(Resource r){
        try{
            ServiceProxy.instance().resourceSave(r);
            model.setResources(ServiceProxy.instance().resourcesFindAll());

        }
        catch (Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }

    public void clear(){
        try {
            model.setCurrent(new Resource());
            list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al refrescar la lista: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void delete(String id){
        try{
            ServiceProxy.instance().resourceDelete(id);
            list();
            model.setCurrent(new Resource());

        }catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void list() throws Exception {
       // throw new Exception("Debe ingresar d¿los datos:");

        try {
            model.setResources(ServiceProxy.instance().resourcesFindAll());
        } catch (Exception e) {
            throw new Exception("Error al obtener la lista de recursos: " + e.getMessage());
        }
    }
    public void edit(int row) {
        Resource re = model.getResources().get(row);
        model.setCurrent(re);
    }

    public void print() throws  Exception{
        String dest = "recursos.pdf";
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);

        Document document = new Document(pdf);
        document.setMargins(20, 20, 20, 20);

        Table header = new Table(2);
        header.setWidth(400);
        header.setHorizontalAlignment(HorizontalAlignment.CENTER);

        header.addCell(getCell(new Paragraph("Listado de Recursos").setFont(fontBold).setFontSize(18), TextAlignment.CENTER, false));

        /*java.net.URL imageUrl = CategoryController.class.getResource("/logo.png"); // Revisa si tu imagen se llama logo.png
        if (imageUrl != null) {
            Image logo = new Image(ImageDataFactory.create(imageUrl));
            header.addCell(getCell(logo, HorizontalAlignment.CENTER, false));
        }*/

        document.add(header);
        document.add(new Paragraph("\n"));

        float[] columnWidths = {150f, 150f, 150f};
        Table table = new Table(columnWidths);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

        DeviceRgb redColor = new DeviceRgb(168, 31, 97);

        Cell headerId = new Cell()
                .add(new Paragraph("Id").setFont(fontBold).setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(redColor)
                .setTextAlignment(TextAlignment.CENTER);

        Cell headerDesc = new Cell()
                .add(new Paragraph("Descripcion").setFont(fontBold).setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(redColor)
                .setTextAlignment(TextAlignment.CENTER);
        Cell headerCat = new Cell()
                .add(new Paragraph("Categoria").setFont(fontBold).setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(redColor)
                .setTextAlignment(TextAlignment.CENTER);

        table.addHeaderCell(headerId);
        table.addHeaderCell(headerDesc);
        table.addHeaderCell(headerCat);


        List<Resource> list = model.getResources();

        if (list != null && !list.isEmpty()) {
            for (Resource r : list) {
                Cell cellId = new Cell()
                        .add(new Paragraph(r.getId()).setFont(font))
                        .setTextAlignment(TextAlignment.CENTER);

                Cell cellDesc = new Cell()
                        .add(new Paragraph(r.getDescription()).setFont(font))
                        .setTextAlignment(TextAlignment.CENTER);


                Cell cellCat = new Cell()
                        .add(new Paragraph(r.getCategory().toString()).setFont(font))
                        .setTextAlignment(TextAlignment.CENTER);

                table.addCell(cellId);
                table.addCell(cellDesc);
                table.addCell(cellCat);


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



