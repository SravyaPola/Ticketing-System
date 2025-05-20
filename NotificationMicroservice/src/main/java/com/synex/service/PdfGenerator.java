package com.synex.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.springframework.stereotype.Component;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.synex.dto.NotificationRecord;

@Component
public class PdfGenerator {

	public byte[] generateResolutionReport(NotificationRecord rec) throws DocumentException, IOException {
		Document doc = new Document();
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			PdfWriter.getInstance(doc, baos);
			doc.open();
			doc.add(new Paragraph("Ticket Resolution Report"));
			doc.add(new Paragraph("Ticket ID: " + rec.getTicketId()));
			doc.add(new Paragraph("Details: " + rec.getDetails()));
			doc.close();
			return baos.toByteArray();
		}
	}
}
