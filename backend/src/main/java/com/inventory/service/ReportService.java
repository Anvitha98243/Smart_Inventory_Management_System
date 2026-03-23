package com.inventory.service;
import com.inventory.entity.*;
import com.inventory.repository.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service @Transactional(readOnly=true)
public class ReportService {
    @Autowired private TransactionRepository txRepo;
    @Autowired private ProductRepository productRepo;
    @Autowired private UserRepository userRepo;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public byte[] excel(String adminUsername, String startDate, String endDate, String type) throws Exception {
        User admin = userRepo.findByUsername(adminUsername).orElseThrow(()->new Exception("Admin not found"));
        LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
        LocalDateTime end = LocalDate.parse(endDate).atTime(23,59,59);
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            if (type.equalsIgnoreCase("ALL")||type.equalsIgnoreCase("TRANSACTIONS")) {
                List<Transaction> list = txRepo.findByRange(admin.getId(),start,end);
                Sheet sh = wb.createSheet("Transactions");
                org.apache.poi.ss.usermodel.Font f = wb.createFont(); f.setBold(true);
                CellStyle hs = wb.createCellStyle(); hs.setFont(f);
                String[] cols={"ID","Product","Type","Qty Before","Qty After","Quantity","Unit Price","Total","By","Notes","Date"};
                Row hr=sh.createRow(0);
                for(int i=0;i<cols.length;i++){Cell c=hr.createCell(i);c.setCellValue(cols[i]);c.setCellStyle(hs);}
                int rn=1;
                for(Transaction t:list){
                    Row row=sh.createRow(rn++);
                    row.createCell(0).setCellValue(t.getId());
                    row.createCell(1).setCellValue(t.getProduct().getName());
                    row.createCell(2).setCellValue(t.getType().name());
                    row.createCell(3).setCellValue(t.getQuantityBefore()!=null?t.getQuantityBefore():0);
                    row.createCell(4).setCellValue(t.getQuantityAfter()!=null?t.getQuantityAfter():0);
                    row.createCell(5).setCellValue(t.getQuantity());
                    row.createCell(6).setCellValue(t.getUnitPrice()!=null?t.getUnitPrice().doubleValue():0);
                    row.createCell(7).setCellValue(t.getTotalAmount()!=null?t.getTotalAmount().doubleValue():0);
                    row.createCell(8).setCellValue(t.getPerformedBy().getFullName());
                    row.createCell(9).setCellValue(t.getNotes()!=null?t.getNotes():"");
                    row.createCell(10).setCellValue(t.getCreatedAt().format(FMT));
                }
                for(int i=0;i<cols.length;i++) sh.autoSizeColumn(i);
            }
            if (type.equalsIgnoreCase("ALL")||type.equalsIgnoreCase("INVENTORY")) {
                List<Product> list = productRepo.findByAdminIdAndActiveTrue(admin.getId());
                Sheet sh = wb.createSheet("Inventory");
                org.apache.poi.ss.usermodel.Font f = wb.createFont(); f.setBold(true);
                CellStyle hs = wb.createCellStyle(); hs.setFont(f);
                String[] cols={"ID","Name","Category","SKU","Quantity","Min Threshold","Price","Unit","Status"};
                Row hr=sh.createRow(0);
                for(int i=0;i<cols.length;i++){Cell c=hr.createCell(i);c.setCellValue(cols[i]);c.setCellStyle(hs);}
                int rn=1;
                for(Product p:list){
                    Row row=sh.createRow(rn++);
                    row.createCell(0).setCellValue(p.getId()); row.createCell(1).setCellValue(p.getName());
                    row.createCell(2).setCellValue(p.getCategory()); row.createCell(3).setCellValue(p.getSku()!=null?p.getSku():"");
                    row.createCell(4).setCellValue(p.getQuantity()); row.createCell(5).setCellValue(p.getMinThreshold());
                    row.createCell(6).setCellValue(p.getPrice().doubleValue()); row.createCell(7).setCellValue(p.getUnit()!=null?p.getUnit():"");
                    row.createCell(8).setCellValue(p.isBelowThreshold()?"LOW STOCK":"OK");
                }
                for(int i=0;i<cols.length;i++) sh.autoSizeColumn(i);
            }
            ByteArrayOutputStream out=new ByteArrayOutputStream(); wb.write(out); return out.toByteArray();
        }
    }

    public byte[] pdf(String adminUsername, String startDate, String endDate, String type) throws Exception {
        User admin = userRepo.findByUsername(adminUsername).orElseThrow(()->new Exception("Admin not found"));
        LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
        LocalDateTime end = LocalDate.parse(endDate).atTime(23,59,59);
        Document doc = new Document(PageSize.A4.rotate());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(doc,out); doc.open();
        com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA,16,com.itextpdf.text.Font.BOLD);
        com.itextpdf.text.Font hdrFont  = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA,9,com.itextpdf.text.Font.BOLD,BaseColor.WHITE);
        com.itextpdf.text.Font cellFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA,8);
        com.itextpdf.text.Font secFont  = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA,12,com.itextpdf.text.Font.BOLD);
        doc.add(new Paragraph("Inventory Report",titleFont));
        doc.add(new Paragraph("Admin: "+admin.getFullName()+" | Period: "+startDate+" to "+endDate));
        doc.add(new Paragraph("Generated: "+LocalDateTime.now().format(FMT))); doc.add(Chunk.NEWLINE);
        BaseColor bg = new BaseColor(52,73,94);
        if (type.equalsIgnoreCase("ALL")||type.equalsIgnoreCase("TRANSACTIONS")) {
            List<Transaction> list = txRepo.findByRange(admin.getId(),start,end);
            doc.add(new Paragraph("Transactions ("+list.size()+")",secFont)); doc.add(Chunk.NEWLINE);
            PdfPTable t=new PdfPTable(8); t.setWidthPercentage(100);
            t.setWidths(new float[]{1f,2.5f,1.5f,1f,1f,1.5f,2f,2.5f});
            for(String h:new String[]{"ID","Product","Type","Qty","Total","By","Notes","Date"}){PdfPCell c=new PdfPCell(new Phrase(h,hdrFont));c.setBackgroundColor(bg);c.setPadding(5);t.addCell(c);}
            for(Transaction tx:list){
                t.addCell(new Phrase(String.valueOf(tx.getId()),cellFont));
                t.addCell(new Phrase(tx.getProduct().getName(),cellFont));
                t.addCell(new Phrase(tx.getType().name(),cellFont));
                t.addCell(new Phrase(String.valueOf(tx.getQuantity()),cellFont));
                t.addCell(new Phrase(tx.getTotalAmount()!=null?"Rs."+tx.getTotalAmount():"-",cellFont));
                t.addCell(new Phrase(tx.getPerformedBy().getFullName(),cellFont));
                t.addCell(new Phrase(tx.getNotes()!=null?tx.getNotes():"",cellFont));
                t.addCell(new Phrase(tx.getCreatedAt().format(FMT),cellFont));
            }
            doc.add(t); doc.add(Chunk.NEWLINE);
        }
        if (type.equalsIgnoreCase("ALL")||type.equalsIgnoreCase("INVENTORY")) {
            List<Product> list = productRepo.findByAdminIdAndActiveTrue(admin.getId());
            doc.add(new Paragraph("Inventory ("+list.size()+")",secFont)); doc.add(Chunk.NEWLINE);
            PdfPTable t=new PdfPTable(7); t.setWidthPercentage(100);
            for(String h:new String[]{"ID","Name","Category","Qty","Min Threshold","Price","Status"}){PdfPCell c=new PdfPCell(new Phrase(h,hdrFont));c.setBackgroundColor(bg);c.setPadding(5);t.addCell(c);}
            for(Product p:list){
                t.addCell(new Phrase(String.valueOf(p.getId()),cellFont)); t.addCell(new Phrase(p.getName(),cellFont));
                t.addCell(new Phrase(p.getCategory(),cellFont)); t.addCell(new Phrase(String.valueOf(p.getQuantity()),cellFont));
                t.addCell(new Phrase(String.valueOf(p.getMinThreshold()),cellFont)); t.addCell(new Phrase("Rs."+p.getPrice(),cellFont));
                PdfPCell sc=new PdfPCell(new Phrase(p.isBelowThreshold()?"LOW":"OK",cellFont));
                sc.setBackgroundColor(p.isBelowThreshold()?new BaseColor(231,76,60):new BaseColor(46,204,113)); t.addCell(sc);
            }
            doc.add(t);
        }
        doc.close(); return out.toByteArray();
    }
}
