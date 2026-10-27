package IIProyectoP3.presentation.category;

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
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import domain.Category;

import IIProyectoP3.logic.CategoryService;
import domain.IService;


import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class CategoryController {
    private CategoryView view;
    private CategoryModel model;
   // private final CategoryService categoryService = new CategoryService();
   // private final ResourceController resourceController = new ResourceController();


    public CategoryController(CategoryView view, CategoryModel model) {
        this.view = view;
        this.model = model;


        view.setController(this);
        view.setModel(model);

        this.cargarCategorias();
    }

    public void cargarCategorias() {
        try {
            model.setList(ServiceProxy.instance().categoryFindAll());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void saveCategory(Category category) throws Exception {
            ServiceProxy.instance().categorySave(category);
            this.cargarCategorias();
    }

    public void edit(int row) {
        Category cat = model.getList().get(row);
        model.setCurrent(cat);
    }

    public void delete(String id) throws Exception {
        ServiceProxy.instance().categoryDelete(id);
        list();
        model.setCurrent(new Category());
    }

    public void list() {
        try {
            model.setList(ServiceProxy.instance().categoryFindAll());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void clear() {

        try {
            model.setCurrent(new Category());
            list();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al refrescar la lista: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void searchDesc(String desc) {
        try {
            List<Category> categorias = ServiceProxy.instance().categoryFindbyDesc(desc);

            model.setList(categorias != null ? categorias : List.of());

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void print() throws Exception {
        String dest = "categorias.pdf";
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdf = new PdfDocument(writer);

        Document document = new Document(pdf);
        document.setMargins(20, 20, 20, 20);

        Table header = new Table(1);
        header.setWidth(400);
        header.setHorizontalAlignment(HorizontalAlignment.CENTER);

        header.addCell(getCell(new Paragraph("Listado de Categorias").setFont(fontBold).setFontSize(18), TextAlignment.CENTER, false));

        /*java.net.URL imageUrl = CategoryController.class.getResource("/logo.png"); // Revisa si tu imagen se llama logo.png
        if (imageUrl != null) {
            Image logo = new Image(ImageDataFactory.create(imageUrl));
            header.addCell(getCell(logo, HorizontalAlignment.CENTER, false));
        }*/

        document.add(header);
        document.add(new Paragraph("\n")); // Espacio de separación

        float[] columnWidths = {150f, 250f};
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

        table.addHeaderCell(headerId);
        table.addHeaderCell(headerDesc);

        List<Category> list = model.getList();

        if (list != null && !list.isEmpty()) {
            for (Category category : list) {
                Cell cellId = new Cell()
                        .add(new Paragraph(category.getId()).setFont(font))
                        .setTextAlignment(TextAlignment.CENTER);

                Cell cellDesc = new Cell()
                        .add(new Paragraph(category.getDescription()).setFont(font))
                        .setTextAlignment(TextAlignment.CENTER);

                table.addCell(cellId);
                table.addCell(cellDesc);
            }
        }

        document.add(table);

        document.close();
        openPdf(dest);
    }

    private Cell getCell(Paragraph paragraph, TextAlignment alignment, boolean hasBorder) {
        Cell cell = new Cell().add(paragraph);
        cell.setPadding(0);
        cell.setTextAlignment(alignment);
        if (!hasBorder) cell.setBorder(Border.NO_BORDER);
        return cell;
    }

    private Cell getCell(Image image, HorizontalAlignment alignment, boolean hasBorder) {
        Cell cell = new Cell().add(image);
        image.setHorizontalAlignment(alignment);
        cell.setPadding(0);
        if (!hasBorder) cell.setBorder(Border.NO_BORDER);
        return cell;
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
}