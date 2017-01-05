package com.manywho.services.pdf.types;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.services.types.Type;

@Type.Element(name = FormType.NAME, summary = "Form Type")
public class FormType implements Type{
    public final static String NAME = "Form Type";

    @Type.Identifier
    @Type.Property(name = "Type Name", contentType = ContentType.String)
    private String typeName;

    @Type.Property(name = "URL of the PDF form", contentType = ContentType.String)
    private String pdfFormUrl;

    public String getTypeName() {
        return typeName;
    }

    public String getPdfFormUrl() {
        return pdfFormUrl;
    }

    public FormType(String typeName, String pdfFormUrl) {
        this.typeName = typeName;
        this.pdfFormUrl = pdfFormUrl;
    }
}
