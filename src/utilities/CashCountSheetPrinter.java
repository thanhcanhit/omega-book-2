/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilities;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.TabStop;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import entity.CashCount;
import entity.CashCountSheet;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
 * @author Hoàng Khang
 */
public class CashCountSheetPrinter {

//    private final Order order;
    private CashCountSheet cashCountSheet;
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

    public CashCountSheetPrinter(CashCountSheet cashCountSheet) {
        this.cashCountSheet = cashCountSheet;
    }

    private String getVND(double number) {
        return utilities.FormatNumber.toVND(number);
    }

    private void addTableHeader(PdfPTable table, Font font) {
        Stream.of("STT", "Mệnh giá", "Số lượng", "Tổng tiền")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.WHITE);
                    header.setBorderWidth(1);
                    header.setPhrase(new Phrase(columnTitle, font));
                    header.setPadding(4);
                    table.addCell(header);
                });
    }

//    public PrintStatus printFile() {
////        Tìm kiếm máy in và kích hoạt sự kiện in
//        DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PAGEABLE;
//        PrintRequestAttributeSet patts = new HashPrintRequestAttributeSet();
//        patts.add(Sides.DUPLEX);
//        PrintService[] ps = PrintServiceLookup.lookupPrintServices(flavor, patts);
//        if (ps.length == 0) {
//            throw new IllegalStateException("No Printer found");
//        }
//
//        PrintService myService = null;
//        for (PrintService printService : ps) {
//            if (printService.getName().equals("Your printer name")) {
//                myService = printService;
//                break;
//            }
//        }
//
//        if (myService == null) {
//            throw new IllegalStateException("Printer not found");
//        }
//
//        try (FileInputStream fis = new FileInputStream(FILE_PATH)) {
//            Doc pdfDoc = new SimpleDoc(fis, DocFlavor.INPUT_STREAM.AUTOSENSE, null);
//            DocPrintJob printJob = myService.createPrintJob();
//            printJob.print(pdfDoc, new HashPrintRequestAttributeSet());
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(OrderPrinter.class.getName()).log(Level.SEVERE, null, ex);
//            return PrintStatus.NOT_FOUND_FILE;
//        } catch (IOException ex) {
//            Logger.getLogger(OrderPrinter.class.getName()).log(Level.SEVERE, null, ex);
//            return PrintStatus.NOT_FOUND_FILE;
//
//        } catch (PrintException ex) {
//            Logger.getLogger(OrderPrinter.class.getName()).log(Level.SEVERE, null, ex);
//            return PrintStatus.NOT_FOUND_PRINTER;
//        }
//
//        return PrintStatus.COMPLETE;
//    }

    public static byte[] resizeImage(byte[] imageData, int targetWidth, int targetHeight) throws IOException {
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageData));
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        double widthRatio = (double) targetWidth / originalWidth;
        double heightRatio = (double) targetHeight / originalHeight;

        double ratio = Math.min(widthRatio, heightRatio);

        int scaledWidth = (int) Math.round(originalWidth * ratio);
        int scaledHeight = (int) Math.round(originalHeight * ratio);

        BufferedImage resizedImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
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
            Font font = new Font(bf, 12);
            Font fontStrikeThrou = new Font(bf, 12, Font.STRIKETHRU);
            LineSeparator separator = new LineSeparator(font);

            Paragraph sofware = new Paragraph("OMEGA BOOK", headingFont);
            Paragraph desc = new Paragraph("Hãy xem sách như là một loại Vitamin", font);
//            Paragraph addressStore = new Paragraph(cashCountSheet.getCashCountSheetDetailList().get(0).getEmployee().getStore().getAddress(), font);

            sofware.setAlignment(TextAlign.CENTER.ordinal());
            desc.setAlignment(TextAlign.CENTER.ordinal());
            document.add(sofware);
            document.add(desc);
            document.add(separator);

//            Content
            Paragraph orderTitle = new Paragraph("PHIẾU KIỂM TIỀN DỰ PHÒNG", subHeadingFont);
            orderTitle.setAlignment(TextAlign.CENTER.ordinal());
            document.add(orderTitle);
            document.add(separator);

//            Order barcode
            // Convert the BufferedImage to a byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(BarcodeGenerator.generateBarcode(cashCountSheet.getCashCountSheetID()), "PNG", baos);
            byte[] bytes = resizeImage(baos.toByteArray(), 200, 100);
//            byte[] bytes = baos.toByteArray();
            // Create an Image from the byte array
            Image image = Image.getInstance(bytes);
            image.setAlignment(1);
            document.add(image);

//            Order info
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            document.add(new Paragraph(String.format("Mã phiếu:  %s", cashCountSheet.getCashCountSheetID()), font));
            document.add(new Paragraph(String.format("Ngày tạo:  %s", dateFormat.format(cashCountSheet.getEndedDate())), font));
            document.add(new Paragraph(String.format("Nhân viên kiểm:  %s", cashCountSheet.getCashCountSheetDetailList().get(0).getEmployee()), font));
            document.add(new Paragraph(String.format("Nhân viên giám sát:  %s", cashCountSheet.getCashCountSheetDetailList().get(1).getEmployee()), font));
            document.add(separator);

//          Order detail  
            PdfPTable table = new PdfPTable(4);
            table.setSpacingBefore(20);
            table.setWidthPercentage(100);
            addTableHeader(table, subHeadingFont);

            int index = 1;
//            double valueIndex = 1;

            double[] denominations = {1000, 2000, 5000, 10000, 20000, 100000, 200000, 500000};

            for (double denomination : denominations) {
                CashCount cash = isValueInArray(denomination, (ArrayList<CashCount>) cashCountSheet.getCashCountList());
                if (cash != null) {
                    table.addCell(createRightAlignedCell(Integer.toString(index++), font));
                    table.addCell(createRightAlignedCell(utilities.FormatNumber.toVND(cash.getValue()), font));
                    table.addCell(createRightAlignedCell(String.valueOf(cash.getQuantity()), font));
                    table.addCell(createRightAlignedCell(getVND(cash.getTotal()), font));
                } else {
                    table.addCell(createRightAlignedCell(Integer.toString(index++), font));
                    table.addCell(createRightAlignedCell(utilities.FormatNumber.toVND(denomination), font));
                    table.addCell(createRightAlignedCell(String.valueOf(0), font));
                    table.addCell(createRightAlignedCell(getVND(0), font));
                }
            }

//            for (double denomination : denominations) {
//                CashCount cash = isValueInArray(denomination, cashCountSheet.getCashCountList());
//            }
            document.add(table);

//            Order footer    
            document.add(
                    new Paragraph("Tổng tiền: " + getVND(cashCountSheet.getTotal()), font));
            document.add(
                    new Paragraph("Chênh lệch: " + getVND(cashCountSheet.getDifference()), font));

            document.add(separator);

            //Close document and outputStream.
            document.close();

            outputStream.close();

            Desktop d = Desktop.getDesktop();

            d.open(
                    new File(FILE_PATH));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Hàm hỗ trợ để tạo một cell nằm sát lề phải
    private PdfPCell createRightAlignedCell(String content, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return cell;
    }

    // Hàm kiểm tra giá trị có nằm trong mảng hay không
    public CashCount isValueInArray(double value, ArrayList<CashCount> list) {
        CashCount temp = null;
        for (CashCount cashCount : list) {
            if (value == cashCount.getValue()) {
                temp = cashCount;
            }
        }
        return temp;
    }
}
