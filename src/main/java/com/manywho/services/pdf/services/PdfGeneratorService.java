package com.manywho.services.pdf.services;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.api.run.elements.type.Property;
import com.manywho.services.pdf.types.FormField;
import com.manywho.services.pdf.utilities.FieldMapperUtility;
import io.woo.htmltopdf.HtmlToPdf;
import io.woo.htmltopdf.HtmlToPdfException;
import io.woo.htmltopdf.HtmlToPdfObject;
import java.io.*;
import java.net.URL;
import java.util.*;

public class PdfGeneratorService {

    public InputStream generatePdfFromHtml(String html) {
        HtmlToPdf htmlToPdf = HtmlToPdf.create()
                .object(HtmlToPdfObject.forHtml(html));

        try (InputStream in = htmlToPdf.convert()) {
            return in;
        } catch (HtmlToPdfException  | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error converting HTML to PDF", e);

        }
    }

    public ByteArrayInputStream populatePdfFromFields(InputStream originalPdf, List<FormField> fields) throws IOException, DocumentException {

        PdfReader reader = new PdfReader(originalPdf);
        ByteArrayOutputStream populatePDF = new ByteArrayOutputStream();

        PdfStamper stamper = new PdfStamper(reader, populatePDF);
        AcroFields form = stamper.getAcroFields();

        for (FormField field: fields) {
            FieldMapperUtility.populateForm(form, field);
        }

        stamper.close();
        reader.close();

        return new ByteArrayInputStream(populatePDF.toByteArray());
    }

    public HashMap<String, ContentType> getTypePropertiesFromPdfForm(String pdfFormUrl) throws IOException {
        InputStream inputStream = new URL(pdfFormUrl).openStream();
        PdfReader reader = new PdfReader(inputStream);
        AcroFields fields = reader.getAcroFields();
        Set<String> formFieldNames = fields.getFields().keySet();
        HashMap inputFields = new HashMap<>();

        for (String fieldName : formFieldNames) {
            ContentType contentType = FieldMapperUtility.getContentTypeForFieldType(fields.getFieldType(fieldName));

            if(contentType != null) {
                inputFields.put(fieldName, contentType);
            }
        }

        return inputFields;
    }

    public List<FormField> getFormFieldsFromProperties(List<Property> properties) {
        List<FormField> formFields = new ArrayList<>();

        for (Property p: properties) {
            switch(p.getContentType()){
                case Boolean:
                    if (Objects.equals(p.getContentValue(), "true")) {
                        formFields.add(new FormField(p.getDeveloperName(), "Yes"));
                    } else {
                        formFields.add(new FormField(p.getDeveloperName(), "No"));
                    }
                case String:
                default:
                    formFields.add(new FormField(p.getDeveloperName(), p.getContentValue()));
                    break;
            }
        }

        return formFields;
    }
}
