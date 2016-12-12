package com.manywho.services.pdf;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.services.configuration.Configuration;

public class ServiceConfiguration implements Configuration {
    @Configuration.Setting(name="PDF Form URL", contentType = ContentType.String, required = false)
    private String pdfFormUrl;

    @Configuration.Setting(name="PDF Type Name", contentType = ContentType.String, required = false)
    private String pdfTypeName;

    public String getPdfFormUrl() {
        return pdfFormUrl;
    }

    public String getPdfTypeName() {
        return pdfTypeName;
    }
}
