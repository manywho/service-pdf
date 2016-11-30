package com.manywho.services.pdf.services;

import com.google.common.base.Strings;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.manywho.services.pdf.types.FormField;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.util.List;
import java.util.Objects;

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
            populateForm(form, field);
        }

        stamper.close();
        reader.close();

        return new ByteArrayInputStream(populatePDF.toByteArray());
    }

    private void populateForm(AcroFields form, FormField field) throws IOException, DocumentException {
        switch (form.getFieldType(field.getFieldName())){
            case AcroFields.FIELD_TYPE_TEXT:
            case AcroFields.FIELD_TYPE_LIST:
            case AcroFields.FIELD_TYPE_COMBO:
                if ( !Strings.isNullOrEmpty(field.getFieldValue()) ) {
                    form.setField(field.getFieldName(), field.getFieldValue());
                }
                break;
            case AcroFields.FIELD_TYPE_CHECKBOX:
            case AcroFields.FIELD_TYPE_RADIOBUTTON:
                String[] status = form.getAppearanceStates(field.getFieldName());
                form.setField(field.getFieldName(), guessStatus(field.getFieldValue(), status));
                break;
            case AcroFields.FIELD_TYPE_NONE:
            case AcroFields.FIELD_TYPE_PUSHBUTTON:
            case AcroFields.FIELD_TYPE_SIGNATURE:
        }
    }

    private String guessStatus(String value, String status[]) {

        if (Strings.isNullOrEmpty(value)
                || Objects.equals(value.toLowerCase(), "false")
                || Objects.equals(value.toLowerCase(), "off")
                || Objects.equals(value.toLowerCase(), "no")) {

            if (status.length> 1) {
                return status[1];
            } else {
                return "Off";
            }
        } else {
            return status[0];
        }
    }
}
