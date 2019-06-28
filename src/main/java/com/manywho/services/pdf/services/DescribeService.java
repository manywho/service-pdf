package com.manywho.services.pdf.services;

import com.google.common.collect.Lists;
import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.api.draw.elements.type.TypeElement;
import com.manywho.sdk.api.draw.elements.type.TypeElementBinding;
import com.manywho.sdk.api.draw.elements.type.TypeElementProperty;
import com.manywho.sdk.api.draw.elements.type.TypeElementPropertyBinding;
import com.manywho.services.pdf.types.properties.DynamicTypePropertyName;
import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DescribeService {
    private PdfGeneratorService pdfGeneratorService;

    @Inject
    public DescribeService(PdfGeneratorService pdfGeneratorService) {
        this.pdfGeneratorService = pdfGeneratorService;
    }

    public TypeElement createTypeElementFromForm(String pdfFormUrl, String typeName) throws IOException {
        HashMap<String, ContentType> formInputs = pdfGeneratorService.getTypePropertiesFromPdfForm(pdfFormUrl);

        List<TypeElementProperty> properties = Lists.newArrayList();
        List<TypeElementPropertyBinding> propertyBindings = Lists.newArrayList();

        for(Map.Entry<String, ContentType> property: formInputs.entrySet()) {
            properties.add(new TypeElementProperty(property.getKey(), property.getValue()));
            propertyBindings.add(new TypeElementPropertyBinding(property.getKey(), property.getKey(), property.getValue().toString()));
        }

        TypeElementProperty typeElement = new TypeElementProperty(DynamicTypePropertyName.PDF_LIST_FIELDS, ContentType.List, "Form Field");
        properties.add(typeElement);

        TypeElementProperty typeElementOriginalFile = new TypeElementProperty(DynamicTypePropertyName.PDF_ORIGINAL_FILE_URL, ContentType.String);
        properties.add(typeElementOriginalFile);

        TypeElementProperty typeElementPopulatedFile = new TypeElementProperty(DynamicTypePropertyName.PDF_POPULATED_FILE_URL, ContentType.String);
        properties.add(typeElementPopulatedFile);

        propertyBindings.add(new TypeElementPropertyBinding(DynamicTypePropertyName.PDF_LIST_FIELDS, DynamicTypePropertyName.PDF_LIST_FIELDS, ContentType.List.toString()));
        propertyBindings.add(new TypeElementPropertyBinding(DynamicTypePropertyName.PDF_ORIGINAL_FILE_URL, DynamicTypePropertyName.PDF_ORIGINAL_FILE_URL, ContentType.String.toString()));
        propertyBindings.add(new TypeElementPropertyBinding(DynamicTypePropertyName.PDF_POPULATED_FILE_URL, DynamicTypePropertyName.PDF_POPULATED_FILE_URL, ContentType.String.toString()));
        List<TypeElementBinding> bindings = Lists.newArrayList();
        bindings.add(new TypeElementBinding(typeName, "The binding for " + typeName, typeName, propertyBindings));

        return new TypeElement(typeName, "Type generated using url in configuration", properties, bindings);
    }
}
