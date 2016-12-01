package com.manywho.services.pdf.utilities;

import com.google.common.base.Strings;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.AcroFields;
import com.manywho.services.pdf.types.FormField;

import java.io.IOException;
import java.util.Objects;

public class FieldMapperUtility {
    public static void populateForm(AcroFields form, FormField field) throws IOException, DocumentException {
        switch (form.getFieldType(field.getFieldName())){
            case AcroFields.FIELD_TYPE_TEXT:
            case AcroFields.FIELD_TYPE_NONE:
            case AcroFields.FIELD_TYPE_LIST:
            case AcroFields.FIELD_TYPE_COMBO:
                if ( !Strings.isNullOrEmpty(field.getFieldValue()) ) {
                    form.setField(field.getFieldName(), field.getFieldValue());
                }
                break;
            case AcroFields.FIELD_TYPE_RADIOBUTTON:
                if (!Strings.isNullOrEmpty(field.getFieldValue())) {
                    if(field.getFieldValue().matches("^\\d+$")) {
                        form.setField(field.getFieldName(), field.getFieldValue());
                        return;
                    }
                    String[] statusRadioButton = form.getAppearanceStates(field.getFieldName());
                    form.setField(field.getFieldName(), guessOptionStatus(field.getFieldValue(), statusRadioButton));
                }
                break;
            case AcroFields.FIELD_TYPE_CHECKBOX:
                if (!Strings.isNullOrEmpty(field.getFieldValue())) {
                    String[] statusCheckBox = form.getAppearanceStates(field.getFieldName());
                    form.setField(field.getFieldName(), guessOptionStatus(field.getFieldValue(), statusCheckBox));
                }
                break;
            case AcroFields.FIELD_TYPE_PUSHBUTTON:
            case AcroFields.FIELD_TYPE_SIGNATURE:
            default:
                break;
        }
    }

    public static String guessOptionStatus(String value, String status[]) {
        if (Strings.isNullOrEmpty(value)
                || Objects.equals(value.toLowerCase(), "false")
                || Objects.equals(value.toLowerCase(), "off")
                || Objects.equals(value.toLowerCase(), "no")) {

            if (status.length> 1) {
                return status[1];
            } else {
                return "Off";
            }
        } else {
            return status[0];
        }
    }
}
