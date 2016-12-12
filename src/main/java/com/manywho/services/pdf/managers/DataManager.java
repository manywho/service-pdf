package com.manywho.services.pdf.managers;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.api.run.elements.type.MObject;
import com.manywho.sdk.api.run.elements.type.Property;
import com.manywho.services.pdf.ServiceConfiguration;
import com.manywho.services.pdf.services.PdfGeneratorService;
import com.manywho.services.pdf.types.FormField;

import javax.inject.Inject;
import java.util.*;

public class DataManager {
    private PdfGeneratorService pdfGeneratorService;

    @Inject
    public DataManager(PdfGeneratorService pdfGeneratorService) {
        this.pdfGeneratorService = pdfGeneratorService;
    }

    public MObject createPdf(ServiceConfiguration serviceConfiguration, MObject mObject) {
        List<FormField> formFieldList = pdfGeneratorService.getFormFieldsFromProperties(mObject.getProperties());
        List<Property> properties = new ArrayList<>();

       mObject.getProperties().stream()
                .filter(p -> !Objects.equals(p.getDeveloperName(), "List of PDF Fields"))
                .forEach(properties::add);

        Property propertyListOfPdfFields = new Property("List of PDF Fields", getListFormField(formFieldList));
        propertyListOfPdfFields.setContentType(ContentType.List);
        properties.add(propertyListOfPdfFields);

        mObject.setProperties(properties);
        mObject.setExternalId(UUID.randomUUID().toString());

        return mObject;
    }

    public List<MObject> getListFormField(List<FormField> formFields) {
        List<MObject> mObjectList = new ArrayList<>();
        for (FormField formField: formFields) {
            if (!Objects.equals(formField.getFieldName(), "List of PDF Fields")) {
                    MObject object = new MObject("Form Field");
                    object.setExternalId(UUID.randomUUID().toString());

                    List<Property> properties = new ArrayList<>();
                    Property nameProperty = new Property("Field Name", formField.getFieldName(), ContentType.String);
                    Property valueProperty = new Property("Field Value", formField.getFieldValue(), ContentType.String);

                    properties.add(nameProperty);
                    properties.add(valueProperty);
                    object.setProperties(properties);

                    mObjectList.add(object);
            }
        }

        return mObjectList;
    }
}
