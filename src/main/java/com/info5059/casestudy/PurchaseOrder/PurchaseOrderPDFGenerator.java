package com.info5059.casestudy.PurchaseOrder;

import com.google.zxing.qrcode.encoder.QRCode;
import com.info5059.casestudy.product.Product;
import com.info5059.casestudy.product.ProductRepository;
import com.info5059.casestudy.product.QRCodeGenerator;
import com.info5059.casestudy.vendor.Vendor;
import com.info5059.casestudy.vendor.VendorRepository;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import org.springframework.web.servlet.view.document.AbstractPdfView;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDateTime;

public abstract class PurchaseOrderPDFGenerator extends AbstractPdfView {
        public static ByteArrayInputStream generateReport(String poid, PurchaseOrderRepository purchaseRepository,
                        ProductRepository productRepository, VendorRepository vendorRepository) throws IOException {
                PurchaseOrder po = new PurchaseOrder();

                URL imageUrl = PurchaseOrderPDFGenerator.class.getResource("/static/images/Logo.jpg");
                ByteArrayOutputStream boas = new ByteArrayOutputStream();
                PdfWriter writer = new PdfWriter(boas);
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf, PageSize.A4);
                PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
                PageSize pagesize = PageSize.A4;
                Image img = new Image(ImageDataFactory.create(imageUrl)).scaleAbsolute(140, 100)
                                .setFixedPosition(pagesize.getWidth() / 2 - 60, 730).setMarginTop(5);
                document.add(img);
                document.add(new Paragraph("\n\n"));
                Locale locale = Locale.of("en", "US");
                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
                try {
                        document.add(new Paragraph("\n"));
                        Optional<PurchaseOrder> purchaseOrder = purchaseRepository.findById(Long.parseLong(poid));
                        document.add(new Paragraph("Purchase Order#: " + poid).setFont(font).setFontSize(15).setBold()
                                        .setTextAlignment(TextAlignment.LEFT)
                                        .setMarginRight(pagesize.getWidth() / 2 - 75)
                                        .setMarginTop(-10));
                        document.add(new Paragraph("\n\n"));

                        Table vendorTable = new Table(2).setWidth(new UnitValue(UnitValue.PERCENT, 30))
                                        .setHorizontalAlignment(HorizontalAlignment.LEFT);

                        if (purchaseOrder.isPresent()) {
                                po = purchaseOrder.get();
                                Optional<Vendor> vendor = vendorRepository.findById(purchaseOrder.get().getVendorid());
                                if (vendor.isPresent()) {
                                        Vendor ven = vendor.get();
                                        Image qrcode = addSummaryQRCode(ven, po);
                                        Cell cell = new Cell()
                                                        .add(new Paragraph("Vendor: ").setFont(font).setFontSize(12)
                                                                        .setBold())
                                                        .setBorder(Border.NO_BORDER);
                                        vendorTable.addCell(cell);

                                        Cell cell2 = new Cell()
                                                        .add(new Paragraph(ven.getName() + "\n" + ven.getAddress1()
                                                                        + "\n" + ven.getCity() + ", "
                                                                        + ven.getProvince() + "\n" + ven.getEmail())
                                                                        .setFont(font).setFontSize(12))
                                                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                                                        .setBorder(Border.NO_BORDER);
                                        vendorTable.addCell(cell2);
                                        if (qrcode != null) {
                                                document.add(qrcode);
                                        }
                                }
                        }
                        document.add(vendorTable);
                        document.add(new Paragraph("\n"));

                        Table productTable = new Table(5).setWidth(new UnitValue(UnitValue.PERCENT, 100))
                                        .setHorizontalAlignment(HorizontalAlignment.LEFT);
                        Cell cell = new Cell()
                                        .add(new Paragraph("Product Code").setFont(font).setFontSize(12).setBold())
                                        .setTextAlignment(TextAlignment.CENTER);
                        productTable.addCell(cell);

                        cell = new Cell().add(new Paragraph("Description").setFont(font).setFontSize(12).setBold())
                                        .setTextAlignment(TextAlignment.CENTER);
                        productTable.addCell(cell);

                        cell = new Cell().add(new Paragraph("QTY Sold").setFont(font).setFontSize(12).setBold())
                                        .setTextAlignment(TextAlignment.CENTER);
                        productTable.addCell(cell);

                        cell = new Cell().add(new Paragraph("Price").setFont(font).setFontSize(12).setBold())
                                        .setTextAlignment(TextAlignment.CENTER);
                        productTable.addCell(cell);

                        cell = new Cell().add(new Paragraph("EXT. Price").setFont(font).setFontSize(12).setBold())
                                        .setTextAlignment(TextAlignment.CENTER);
                        productTable.addCell(cell);

                        BigDecimal sub_total = new BigDecimal(0.0);
                        BigDecimal TAX_AMOUNT = new BigDecimal(0.0);
                        BigDecimal TOTAL = new BigDecimal(0.0);

                        for (PurchaseOrderLineItem POLineItem : po.getItems()) {
                                Optional<Product> product = productRepository.findById(POLineItem.getProductid());
                                if (product.isPresent()) {
                                        Product prod = product.get();
                                        cell = new Cell().add(new Paragraph(prod.getId()).setFont(font).setFontSize(12))
                                                        .setTextAlignment(TextAlignment.CENTER);
                                        productTable.addCell(cell);

                                        cell = new Cell()
                                                        .add(new Paragraph(prod.getName()).setFont(font)
                                                                        .setFontSize(12))
                                                        .setTextAlignment(TextAlignment.CENTER);
                                        productTable.addCell(cell);

                                        cell = new Cell()
                                                        .add(new Paragraph(String.valueOf(POLineItem.getQty()))
                                                                        .setFont(font).setFontSize(12))
                                                        .setTextAlignment(TextAlignment.CENTER);
                                        productTable.addCell(cell);

                                        cell = new Cell()
                                                        .add(new Paragraph(
                                                                        currencyFormatter.format(prod.getCostprice()))
                                                                        .setFont(font)
                                                                        .setFontSize(12))
                                                        .setTextAlignment(TextAlignment.CENTER);
                                        productTable.addCell(cell);

                                        BigDecimal extPrice = prod.getCostprice()
                                                        .multiply(new BigDecimal(POLineItem.getQty()));
                                        sub_total = sub_total.add(extPrice);
                                        TAX_AMOUNT = sub_total.multiply(new BigDecimal(0.13));
                                        TOTAL = sub_total.add(TAX_AMOUNT);
                                        cell = new Cell()
                                                        .add(new Paragraph(currencyFormatter.format(extPrice))
                                                                        .setFont(font).setFontSize(12))
                                                        .setTextAlignment(TextAlignment.CENTER);
                                        productTable.addCell(cell);
                                }
                        }

                        cell = new Cell(1, 4).add(new Paragraph("Sub Total:"))
                                        .setBorder(Border.NO_BORDER)
                                        .setTextAlignment(TextAlignment.RIGHT);
                        productTable.addCell(cell);
                        cell = new Cell().add(new Paragraph(currencyFormatter.format(sub_total)))
                                        .setTextAlignment(TextAlignment.CENTER);
                        productTable.addCell(cell);

                        cell = new Cell(1, 4).add(new Paragraph("TAX:"))
                                        .setBorder(Border.NO_BORDER)
                                        .setTextAlignment(TextAlignment.RIGHT);
                        productTable.addCell(cell);
                        cell = new Cell().add(new Paragraph(currencyFormatter.format(TAX_AMOUNT)))
                                        .setTextAlignment(TextAlignment.CENTER);
                        productTable.addCell(cell);

                        cell = new Cell(1, 4).add(new Paragraph("PO Total:"))
                                        .setBorder(Border.NO_BORDER)
                                        .setTextAlignment(TextAlignment.RIGHT);
                        productTable.addCell(cell);
                        cell = new Cell().add(new Paragraph(currencyFormatter.format(TOTAL)))
                                        .setTextAlignment(TextAlignment.CENTER)
                                        .setBackgroundColor(ColorConstants.YELLOW);
                        productTable.addCell(cell);

                        document.add(productTable);
                        document.add(new Paragraph("\n\n"));
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a");
                        document.add(new Paragraph(dateFormatter.format(LocalDateTime.now()))
                                        .setTextAlignment(TextAlignment.CENTER));
                        document.close();

                } catch (Exception e) {
                        Logger.getLogger(PurchaseOrderPDFGenerator.class.getName()).log(Level.SEVERE, null, e);
                }
                return new ByteArrayInputStream(boas.toByteArray());
        }

        private static Image addSummaryQRCode(Vendor vendor, PurchaseOrder po) {
                QRCodeGenerator qrCodeGenerator = new QRCodeGenerator();
                try {
                        // Format the summary string
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd h:mm a");
                        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.of("en", "US"));

                        String qrContent = "Summary for Purchase Order: " + po.getId() +
                                        "\nDate: " + dateFormatter.format(po.getPodate()) +
                                        "\nVendor: " + vendor.getName() +
                                        "\nTotal: " + currencyFormatter.format(po.getAmount());

                        // Generate QR Code byte array
                        byte[] qrcodebin = qrCodeGenerator.generateQRCode(qrContent);

                        // Create and position QR Code image
                        Image qrcode = new Image(ImageDataFactory.create(qrcodebin))
                                        .scaleAbsolute(100, 100)
                                        .setFixedPosition(460, 60);

                        return qrcode;
                } catch (Exception e) {
                        Logger.getLogger(PurchaseOrderPDFGenerator.class.getName()).log(Level.SEVERE, null, e);
                        return null;
                }
        }
}
