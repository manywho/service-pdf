package com.manywho.services.pdf.managers;

import com.google.common.base.Strings;
import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.api.run.elements.type.MObject;
import com.manywho.sdk.api.run.elements.type.Property;
import com.manywho.sdk.services.types.system.$File;
import com.manywho.services.pdf.ServiceConfiguration;
import com.manywho.services.pdf.services.PdfGeneratorService;
import com.manywho.services.pdf.types.FormField;
import com.manywho.services.pdf.types.properties.DynamicTypePropertyName;
import javax.inject.Inject;
import java.util.*;

public class DataManager {
    private PdfGeneratorService pdfGeneratorService;
    private FileManager fileManager;

    @Inject
    public DataManager(PdfGeneratorService pdfGeneratorService, FileManager fileManager)
    {
        this.pdfGeneratorService = pdfGeneratorService;
        this.fileManager = fileManager;
    }

    public MObject createPdf(ServiceConfiguration serviceConfiguration, MObject mObject) {
        Optional<Property> originalFileUrl = mObject.getProperties().stream()
                .filter(p -> Objects.equals(p.getDeveloperName(), DynamicTypePropertyName.PDF_ORIGINAL_FILE_URL))
                .findFirst();

        if(!originalFileUrl.isPresent() || Strings.isNullOrEmpty(originalFileUrl.get().getContentValue())) {
            throw new RuntimeException("The " + DynamicTypePropertyName.PDF_ORIGINAL_FILE_URL + " is mandatory");
        }

        List<FormField> formFieldList = pdfGeneratorService.getFormFieldsFromProperties(mObject.getProperties());
        List<Property> properties = new ArrayList<>();
        properties.add(originalFileUrl.get());

        mObject.getProperties().stream()
                .filter(p -> !DynamicTypePropertyName.isDefaultProperty(p.getDeveloperName()))
                .forEach(properties::add);

        Property propertyListOfPdfFields = new Property(DynamicTypePropertyName.PDF_LIST_FIELDS, getListFormField(formFieldList));
        propertyListOfPdfFields.setContentType(ContentType.List);
        properties.add(propertyListOfPdfFields);
        $File filePopulated = fileManager.getFilePopulated(originalFileUrl.get().getContentValue(), formFieldList);
        Property propertyFilePopulated = new Property(DynamicTypePropertyName.PDF_POPULATED_FILE_URL, filePopulated.getDownloadUri());

        propertyFilePopulated.setContentType(ContentType.Object);
        properties.add(propertyFilePopulated);

        mObject.setProperties(properties);
        mObject.setExternalId(UUID.randomUUID().toString());

        return mObject;
    }

    public List<MObject> getListFormField(List<FormField> formFields) {
        List<MObject> mObjectList = new ArrayList<>();
        for (FormField formField: formFields) {
            if (!DynamicTypePropertyName.isDefaultProperty(formField.getFieldName())) {
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
