package com.synex.component;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.synex.domain.TicketEvent;
import org.springframework.stereotype.Component;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

@Component
public class PdfGenerator {

	private final EmailClientService ecs;

	public PdfGenerator(EmailClientService ecs) {
		this.ecs = ecs;
	}

	public byte[] generatePdf(TicketEvent event) throws Exception {
		Document doc = new Document(PageSize.A4, 36, 36, 54, 36);
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			PdfWriter.getInstance(doc, baos);
			doc.open();

			Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
			Paragraph title = new Paragraph("Ticket Resolution Report", titleFont);
			title.setAlignment(Element.ALIGN_CENTER);
			doc.add(title);
			doc.add(Chunk.NEWLINE);

			Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
			Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
			addKV(doc, "Ticket ID:", event.getTicketId(), labelFont, valueFont);
			addKV(doc, "Status:", event.getStatus(), labelFont, valueFont);
			addKV(doc, "Message:", event.getMessage(), labelFont, valueFont);
			addKV(doc, "Resolved At:", event.getReceivedAt(), labelFont, valueFont);

			doc.add(Chunk.NEWLINE);

			Paragraph histTitle = new Paragraph("Ticket History", labelFont);
			histTitle.setSpacingBefore(10);
			doc.add(histTitle);
			doc.add(Chunk.NEWLINE);

			List<Map<String, Object>> entries = ecs.getTicketHistory(event.getTicketId());

			PdfPTable table = new PdfPTable(new float[] { 1f, 3f, 4f, 3f, 5f });
			table.setWidthPercentage(100);

			Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
			for (String h : new String[] { "#", "Action", "Action By", "Action Date", "Comments" }) {
				PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
				cell.setPadding(4);
				table.addCell(cell);
			}

			Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
			for (int i = 0; i < entries.size(); i++) {
				Map<String, Object> e = entries.get(i);
				table.addCell(new PdfPCell(new Phrase(String.valueOf(i + 1), cellFont)));
				table.addCell(new PdfPCell(new Phrase(String.valueOf(e.get("action")), cellFont)));
				table.addCell(new PdfPCell(new Phrase(String.valueOf(e.get("actionBy")), cellFont)));
				table.addCell(new PdfPCell(new Phrase(String.valueOf(e.get("actionDate")), cellFont)));
				table.addCell(new PdfPCell(new Phrase(String.valueOf(e.get("comments")), cellFont)));
			}

			doc.add(table);
			doc.close();
			return baos.toByteArray();

		}
	}

	private void addKV(Document doc, String key, String value, Font keyFont, Font valueFont) throws DocumentException {
		Paragraph p = new Paragraph();
		p.add(new Chunk(key + " ", keyFont));
		p.add(new Chunk(value + "\n", valueFont));
		doc.add(p);
	}
}
