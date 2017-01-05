package com.manywho.services.pdf.types.properties;

import java.util.Objects;

public class DynamicTypePropertyName {
    public final static String PDF_LIST_FIELDS = "List of PDF Fields";
    public final static String PDF_ORIGINAL_FILE_URL = "Original File URL";
    public final static String PDF_POPULATED_FILE_URL = "Populated File URL";

    public static boolean isDefaultProperty(String propertyName) {

        return Objects.equals(propertyName, PDF_LIST_FIELDS) ||
                Objects.equals(propertyName, PDF_ORIGINAL_FILE_URL) ||
                Objects.equals(propertyName, PDF_POPULATED_FILE_URL);
    }
}
