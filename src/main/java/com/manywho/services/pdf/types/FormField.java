package com.manywho.services.pdf.types;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.services.types.Type;

@Type.Element(name = FormField.NAME, summary = "Form Field")
public class FormField implements Type {
    public final static String NAME = "Form Field";

    @Type.Identifier
    @Type.Property(name = "Field Name", contentType = ContentType.String)
    private String fieldName;

    @Type.Property(name = "Field Value", contentType = ContentType.String)
    private String fieldValue;

    public FormField() {}

    public FormField(String fieldName, String fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }
}
