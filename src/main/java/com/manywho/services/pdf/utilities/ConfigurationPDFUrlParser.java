package com.manywho.services.pdf.utilities;

import com.google.common.base.Strings;
import com.manywho.services.pdf.types.FormType;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationPDFUrlParser {
    public static List<FormType> parsePdfForm(String value) {
        if (Strings.isNullOrEmpty(value)) {
            return new ArrayList<>();
        } else {
            ArrayList<FormType> formTypes = new ArrayList<>();
            String values[] = value.split(";");
            for (String valuePart: values) {
                formTypes.add(getUrl(valuePart));
            }
            return formTypes;
        }
    }

    public static FormType getUrl(String value) {
        String[] urlName = value.split("\\|");
        String name = "";
        if (urlName.length<2) {
            // there is no name, the name is the last part of the url without .pdf
            String[] urlParts = value.split("\\/");
            name = urlParts[urlParts.length -1].replace(".pdf", "");
        } else {
            name = urlName[1].trim();
        }

        return new FormType(name.trim(), urlName[0].trim());
    }
}
