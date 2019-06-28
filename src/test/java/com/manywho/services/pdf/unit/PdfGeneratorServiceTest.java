package com.manywho.services.pdf.unit;

import com.lowagie.text.pdf.PdfReader;
import com.manywho.services.pdf.services.PdfGeneratorService;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.io.InputStream;

public class PdfGeneratorServiceTest {
    @Test
    public void testTransformHtmlToPdf() throws IOException {

        PdfGeneratorService pdfGeneratorService = new PdfGeneratorService();
        InputStream htmlStream = getClass().getClassLoader().getResourceAsStream("example.html");
        String html = IOUtils.toString(htmlStream);
        InputStream inputStream = pdfGeneratorService.generatePdfFromHtml(html);

        PdfReader reader = new PdfReader(inputStream);
        Assert.assertEquals(2, reader.getNumberOfPages());
        Assert.assertEquals("Lists, Tables and Forms", reader.getInfo().get("Title"));
        Assert.assertEquals(3, reader.getAcroFields().getFields().size());

        //Todo: improve those tests
        // confirm that the content is there
        Assert.assertTrue(reader.getPageContent(1).length > 41900);
    }
}
