package com.manywho.services.pdf.services;

import com.google.common.collect.Lists;
import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.api.draw.elements.type.TypeElement;
import com.manywho.sdk.api.draw.elements.type.TypeElementBinding;
import com.manywho.sdk.api.draw.elements.type.TypeElementProperty;
import com.manywho.sdk.api.draw.elements.type.TypeElementPropertyBinding;
import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DescribeService {
    PdfGeneratorService pdfGeneratorService;

    @Inject
    public DescribeService(PdfGeneratorService pdfGeneratorService) {
        this.pdfGeneratorService = pdfGeneratorService;
    }

    public TypeElement createTypeElementFromForm(String pdfFormUrl, String typeName) {
        HashMap<String, ContentType> formInputs;

        try {
            formInputs = pdfGeneratorService.getTypePropertiesFromPdfForm(pdfFormUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<TypeElementProperty> properties = Lists.newArrayList();
        List<TypeElementPropertyBinding> propertyBindings = Lists.newArrayList();

        for(Map.Entry<String, ContentType> property: formInputs.entrySet()) {
            properties.add(new TypeElementProperty(property.getKey(), property.getValue()));
            propertyBindings.add(new TypeElementPropertyBinding(property.getKey(), property.getKey(), property.getValue().toString()));
        }

        TypeElementProperty typeElement = new TypeElementProperty("List of PDF Fields", ContentType.List, "Form Field");
        properties.add(typeElement);

        propertyBindings.add(new TypeElementPropertyBinding("List of PDF Fields", "List of PDF Fields", ContentType.List.toString()));

        List<TypeElementBinding> bindings = Lists.newArrayList();
        bindings.add(new TypeElementBinding(typeName, "The binding for " + typeName, typeName, propertyBindings));

        return new TypeElement(typeName, "Type generated using url in configuration", properties, bindings);
    }
}
