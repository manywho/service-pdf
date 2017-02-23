package com.manywho.services.pdf.actions;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.services.actions.Action;
import com.manywho.sdk.services.types.system.$File;

@Action.Metadata(name = "Concatenate PDF", summary = "Concatenate PDF", uri = "concatenate-pdf")
public class SignPdf {

    public static class Input {
        @Action.Input(name = "PDF To Sign", contentType = ContentType.Object, required = true)
        private $File file;

        public $File getFile() {
            return this.file;
        }
    }

    public static class Output {
        @Action.Output(name = "PDF Signed", contentType = ContentType.Object)
        private $File pdfSigned;

        public Output($File pdfSigned) {
            this.pdfSigned = pdfSigned;
        }
    }
}
