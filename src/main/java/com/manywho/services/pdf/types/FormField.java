package com.manywho.services.pdf.types;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.services.types.Type;

@Type.Element(name = FormField.NAME, summary = "Form Field")
public class FormField implements Type {
    public final static String NAME = "Form Field";

    @Type.Identifier
    @Type.Property(name = "Field Name", contentType = ContentType.String)
    private String fieldName;

    @Type.Property(name = "Is Boolean?", contentType = ContentType.Boolean)
    private boolean isBoolean;

    @Type.Property(name = "Field Value", contentType = ContentType.String)
    private String fieldValue;

    public FormField() {}

    public FormField(Boolean isBoolean, String fieldName, String fieldValue) {
        this.isBoolean = isBoolean;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public boolean getBoolean() {
        return isBoolean;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }
}
