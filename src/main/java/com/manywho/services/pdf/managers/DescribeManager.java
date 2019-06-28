package com.manywho.services.pdf.managers;

import com.manywho.sdk.api.draw.elements.type.TypeElement;
import com.manywho.services.pdf.providers.ConfigurationPdfProvider;
import com.manywho.services.pdf.services.DescribeService;
import com.manywho.services.pdf.types.FormType;
import com.manywho.services.pdf.utilities.ConfigurationPDFUrlParser;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class

DescribeManager {
    private DescribeService describeService;

    @Inject
    public DescribeManager(DescribeService describeService) {
        this.describeService = describeService;
    }

    public List<TypeElement> getListTypeElement(String formURL) throws IOException {
        List<TypeElement> listOfTypeElements = new ArrayList<>();
        for (FormType formType:ConfigurationPDFUrlParser.parsePdfForm(formURL)) {
            listOfTypeElements.add(describeService.createTypeElementFromForm(formType.getPdfFormUrl(), formType.getTypeName()));
        }

        return listOfTypeElements;
    }
}
