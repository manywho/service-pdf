package com.manywho.services.pdf.unit;

import com.manywho.services.pdf.types.FormType;
import com.manywho.services.pdf.utilities.ConfigurationPDFUrlParser;
import org.junit.Test;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ConfigurationPDFUrlParserTest {
    @Test
    public void testEmptyValue(){
        List<FormType> formTypes = ConfigurationPDFUrlParser.parsePdfForm("");
        assertTrue(formTypes.isEmpty());
    }

    @Test
    public void testNullValue(){
        List<FormType> formTypes = ConfigurationPDFUrlParser.parsePdfForm(null);
        assertTrue(formTypes.isEmpty());
    }

    @Test
    public void testTwoUrlOnlyOneWithNameValue(){
        List<FormType> formTypes = ConfigurationPDFUrlParser.parsePdfForm("http://mypdf.com/file1.pdf|Name of Type; http://mypdf.com/file2.pdf ");
        assertEquals(formTypes.get(0).getPdfFormUrl(),"http://mypdf.com/file1.pdf");
        assertEquals(formTypes.get(0).getTypeName(),"Name of Type");

        assertEquals(formTypes.get(1).getPdfFormUrl(),"http://mypdf.com/file2.pdf");
        assertEquals(formTypes.get(1).getTypeName(),"file2");

        assertEquals(formTypes.size(), 2);
    }

    @Test
    public void testTwoUrlOnlyWithNameBothValue(){
        List<FormType> formTypes = ConfigurationPDFUrlParser.parsePdfForm("http://mypdf.com/file1.pdf|Name of Type; http://mypdf.com/file2.pdf|Name2");
        assertEquals(formTypes.get(0).getPdfFormUrl(),"http://mypdf.com/file1.pdf");
        assertEquals(formTypes.get(0).getTypeName(),"Name of Type");

        assertEquals(formTypes.get(1).getPdfFormUrl(),"http://mypdf.com/file2.pdf");
        assertEquals(formTypes.get(1).getTypeName(),"Name2");

        assertEquals(formTypes.size(), 2);
    }

    @Test
    public void testOnlyOneUrlValue(){
        List<FormType> formTypes = ConfigurationPDFUrlParser.parsePdfForm("http://mypdf.com/file1.pdf");

        assertEquals(formTypes.get(0).getPdfFormUrl(),"http://mypdf.com/file1.pdf");
        assertEquals(formTypes.get(0).getTypeName(),"file1");

        assertEquals(formTypes.size(), 1);
    }

    @Test
    public void testOnlyOneUrlWithNameValue(){
        List<FormType> formTypes = ConfigurationPDFUrlParser.parsePdfForm("http://mypdf.com/file1.pdf | Name");

        assertEquals(formTypes.get(0).getPdfFormUrl(),"http://mypdf.com/file1.pdf");
        assertEquals(formTypes.get(0).getTypeName(),"Name");

        assertEquals(formTypes.size(), 1);
    }

    @Test
    public void testTwoUrlOneWithNameOtherNoNameAndNotExtensionValue(){
        List<FormType> formTypes = ConfigurationPDFUrlParser.parsePdfForm("http://mypdf.com/xxxxx|Name of Type; http://mypdf.com/yyyyyy");
        assertEquals(formTypes.get(0).getPdfFormUrl(),"http://mypdf.com/xxxxx");
        assertEquals(formTypes.get(0).getTypeName(),"Name of Type");

        assertEquals(formTypes.get(1).getPdfFormUrl(),"http://mypdf.com/yyyyyy");
        assertEquals(formTypes.get(1).getTypeName(),"yyyyyy");

        assertEquals(formTypes.size(), 2);
    }
}
