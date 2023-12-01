/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilities;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import entity.Order;
import entity.OrderDetail;
import java.awt.Desktop;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Sides;

/**
 *
 * @author thanhcanhit
 */
public class OrderPrinter {

    private final Order order;
    public static final String FONT = "resources/fonts/arial-unicode-ms.ttf";
    private static final String FILE_PATH = "lastOrder.pdf";

    public static enum PrintStatus {
        NOT_FOUND_FILE,
        NOT_FOUND_PRINTER,
        COMPLETE
    }

    enum TextAlign {
        LEFT, CENTER, RIGHT;
    }

    public OrderPrinter(Order order) {
        this.order = order;
    }

    private String getVND(double number) {
        return utilities.FormatNumber.toVND(number);
    }

    private void addTableHeader(PdfPTable table, Font font) {
        Stream.of("VAT", "Giá", "Số lượng", "Tổng tiền")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.WHITE);
                    header.setBorderWidth(1);
                    header.setPhrase(new Phrase(columnTitle, font));
                    header.setPadding(4);
                    table.addCell(header);
                });
    }

    public PrintStatus printFile() {
//        Tìm kiếm máy in và kích hoạt sự kiện in
        DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PAGEABLE;
        PrintRequestAttributeSet patts = new HashPrintRequestAttributeSet();
        patts.add(Sides.DUPLEX);
        PrintService[] ps = PrintServiceLookup.lookupPrintServices(flavor, patts);
        if (ps.length == 0) {
            throw new IllegalStateException("No Printer found");
        }
        System.out.println("Available printers: " + Arrays.asList(ps));

        PrintService myService = null;
        for (PrintService printService : ps) {
            if (printService.getName().equals("Your printer name")) {
                myService = printService;
                break;
            }
        }

        if (myService == null) {
            throw new IllegalStateException("Printer not found");
        }

        try (FileInputStream fis = new FileInputStream(FILE_PATH)) {
            Doc pdfDoc = new SimpleDoc(fis, DocFlavor.INPUT_STREAM.AUTOSENSE, null);
            DocPrintJob printJob = myService.createPrintJob();
            printJob.print(pdfDoc, new HashPrintRequestAttributeSet());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OrderPrinter.class.getName()).log(Level.SEVERE, null, ex);
            return PrintStatus.NOT_FOUND_FILE;
        } catch (IOException ex) {
            Logger.getLogger(OrderPrinter.class.getName()).log(Level.SEVERE, null, ex);
            return PrintStatus.NOT_FOUND_FILE;

        } catch (PrintException ex) {
            Logger.getLogger(OrderPrinter.class.getName()).log(Level.SEVERE, null, ex);
            return PrintStatus.NOT_FOUND_PRINTER;
        }

        return PrintStatus.COMPLETE;
    }

    public static byte[] resizeImage(byte[] imageData, int targetWidth, int targetHeight) throws IOException {
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageData));
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        double widthRatio = (double) targetWidth / originalWidth;
        double heightRatio = (double) targetHeight / originalHeight;

        double ratio = Math.min(widthRatio, heightRatio);

        int scaledWidth = (int) Math.round(originalWidth * ratio);
        int scaledHeight = (int) Math.round(originalHeight * ratio);

        BufferedImage resizedImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.SCALE_SMOOTH);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        byte[] resizedImageData;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(resizedImage, "PNG", baos);
            resizedImageData = baos.toByteArray();
        }

        return resizedImageData;
    }

    public void generatePDF() {
        try {
            //Create Document instance.
            Document document = new Document();

            //Create OutputStream instance.
            OutputStream outputStream
                    = new FileOutputStream(new File(FILE_PATH));

            //Create PDFWriter instance.
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            writer.setLanguage("VN");

            //Open the document.
            document.open();
            BaseFont bf = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//            Header
            Font headingFont = new Font(bf, 14, Font.BOLD);
            Font subHeadingFont = new Font(bf, 13, Font.BOLD);
            Font bold = new Font(bf, 12, Font.BOLD);
            Font italic = new Font(bf, 12, Font.ITALIC);
            Font font = new Font(bf, 12);
            Font fontStrikeThrou = new Font(bf, 12, Font.STRIKETHRU);
            LineSeparator separator = new LineSeparator(font);

            Paragraph sofware = new Paragraph("OMEGA BOOK", headingFont);
            Paragraph desc = new Paragraph("\"Hãy xem sách như là một loại Vitamin\"", italic);
            Paragraph store = new Paragraph("12 Nguyễn Văn Bảo, Phường 4, Gò Vấp, Hồ Chí Minh", bold);

            sofware.setAlignment(TextAlign.CENTER.ordinal());
            desc.setAlignment(TextAlign.CENTER.ordinal());
            store.setAlignment(TextAlign.CENTER.ordinal());
            document.add(sofware);
            document.add(desc);
            document.add(store);
            document.add(separator);

//            Content
            Paragraph orderTitle = new Paragraph("HÓA ĐƠN THANH TOÁN", subHeadingFont);
            orderTitle.setAlignment(TextAlign.CENTER.ordinal());
            document.add(orderTitle);
            document.add(separator);

//            Order info
            document.add(new Paragraph(String.format("Số hóa đơn:  %s", order.getOrderID()), font));
            document.add(new Paragraph(String.format("Ngày tạo:  %s", order.getOrderAt()), font));
            document.add(new Paragraph(String.format("Nhân viên:  %s", order.getEmployee().getName()), font));
            document.add(new Paragraph(String.format("Khách hàng:  %s", order.getCustomer().getName()), font));
            document.add(separator);

//          Order detail  
            PdfPTable table = new PdfPTable(4);
            table.setSpacingBefore(20);
            table.setWidthPercentage(100);
            addTableHeader(table, subHeadingFont);

            int index = 0;
            for (OrderDetail detail : order.getOrderDetail()) {
                PdfPCell cell = new PdfPCell(new Phrase(String.format("%d. %s", ++index, detail.getProduct().getName()), font));
                cell.setColspan(4);

                table.addCell(new PdfPCell(cell));
                table.addCell(new PdfPCell(new Phrase(String.valueOf(detail.getVAT()), font)));

                boolean isHasDiscout = detail.getSeasonalDiscount() != 0;
                PdfPCell priceCell = new PdfPCell(new Phrase(getVND(detail.getPrice()), font));
                if (isHasDiscout) {
                    double pricePerItemAfterDiscount = detail.getPrice() - detail.getSeasonalDiscount() / detail.getQuantity();

                    priceCell.addElement(new Phrase(getVND(detail.getPrice()), fontStrikeThrou));
                    priceCell.addElement(new Chunk(getVND(pricePerItemAfterDiscount), font));
                }
                table.addCell(priceCell);

                table.addCell(new PdfPCell(new Phrase(String.valueOf(detail.getQuantity()), font)));
                table.addCell(new PdfPCell(new Phrase(getVND(detail.getLineTotal()), font)));

            }

            document.add(table);

//            Order footer    
            document.add(new Paragraph("Tổng tiền: " + getVND(order.getSubTotal()), font));
            document.add(new Paragraph("Chiết khấu đơn: " + getVND(order.getSubTotal() - order.getTotalDue()), font));
            document.add(new Paragraph("Thanh toán: " + getVND(order.getTotalDue()), subHeadingFont));
            document.add(separator);

            boolean isATMPayment = order.isPayment();
            document.add(new Paragraph("Hình thức thanh toán: " + (isATMPayment ? "ATM" : "Tiền mặt"), font));
            if (!isATMPayment) {
                document.add(new Paragraph("Tiền khách đưa: " + getVND(order.getMoneyGiven()), font));
                document.add(new Paragraph("Tiền trả lại: " + getVND(order.getMoneyGiven() - order.getTotalDue()), font));
            }

//            Footer
            document.add(separator);
            Paragraph hotline = new Paragraph("Tổng đài góp ý/khiếu nại: 1800 0000", bold);
            Paragraph note = new Paragraph("Lưu ý: OmegaBook chỉ xuất hóa đơn trong ngày, Quý khách vui lòng liên hệ thu ngân để được hỗ trợ nếu cần in lại hóa đơn.", italic);

            Paragraph note2 = new Paragraph("Cảm ơn quý khách. Hẹn gặp lại", font);

            hotline.setAlignment(TextAlign.CENTER.ordinal());
            note.setAlignment(TextAlign.CENTER.ordinal());
            note.setIndentationLeft(50);
            note.setIndentationRight(50);
            note2.setAlignment(TextAlign.CENTER.ordinal());

            document.add(hotline);
            document.add(note);
            document.add(note2);

//            Order barcode
            // Convert the BufferedImage to a byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(BarcodeGenerator.generateBarcode(order.getOrderID()), "PNG", baos);
            byte[] bytes = resizeImage(baos.toByteArray(), 300, 100);
            // Create an Image from the byte array
            Image image = Image.getInstance(bytes);
            image.setAlignment(1);
            document.add(image);

            //Close document and outputStream.
            document.close();
            outputStream.close();

            System.out.println("Pdf created successfully.");
            Desktop d = Desktop.getDesktop();
            d.open(new File(FILE_PATH));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
