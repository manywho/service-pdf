package com.manywho.services.pdf.services;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.manywho.services.pdf.types.FormField;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.util.List;

public class PdfGeneratorService {

    public InputStream generatePdfFromHtml(String html) {
        ITextRenderer iTextRenderer = new ITextRenderer();
        iTextRenderer.setDocumentFromString(html);

        iTextRenderer.layout();
        PipedInputStream in = new PipedInputStream();
        try {
            final PipedOutputStream out = new PipedOutputStream(in);
            iTextRenderer.createPDF(out);
            out.close();

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

        return in;
    }

    public ByteArrayInputStream populatePdfFromFields(InputStream originalPdf, List<FormField> fields) throws IOException, DocumentException {

        PdfReader reader = new PdfReader(originalPdf);
        ByteArrayOutputStream populatePDF = new ByteArrayOutputStream();

        PdfStamper stamper = new PdfStamper(reader, populatePDF);
        AcroFields form = stamper.getAcroFields();

        for (FormField field: fields) {
            form.setField(field.getFieldName(), field.getFieldValue());
        }

        stamper.close();
        reader.close();

        return new ByteArrayInputStream(populatePDF.toByteArray());
    }
}
