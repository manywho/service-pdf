package com.manywho.services.pdf.actions;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.services.actions.Action;
import com.manywho.sdk.services.types.system.$File;
import com.manywho.services.pdf.types.FormField;

import java.util.List;

@Action.Metadata(name = "Populate PDF forms", summary = "Populate PDF form", uri = "populate-pdf-forms")
public class PopulatePdf {

    public static class Input {
        @Action.Input(name = "PDF Fields", contentType = ContentType.List, required = true)
        private List<FormField> pdfFields;

        @Action.Input(name = "PDF File", contentType = ContentType.Object, required = true)
        private $File pdfFile;

        public List<FormField> getPdfFields() {
            return pdfFields;
        }

        public $File getPdfFile() {
            return pdfFile;
        }
    }

    public static class Output {
        @Action.Output(name = "PDF Populated", contentType = ContentType.Object)
        private $File pdfPopulated;

        public Output($File pdfPopulated) {
            this.pdfPopulated = pdfPopulated;
        }
    }
}
