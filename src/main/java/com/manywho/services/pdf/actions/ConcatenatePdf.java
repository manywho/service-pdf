package com.manywho.services.pdf.actions;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.services.actions.Action;
import com.manywho.sdk.services.types.system.$File;

import java.util.List;

@Action.Metadata(name = "Concatenate PDF", summary = "Concatenate PDF", uri = "concatenate-pdf")
public class ConcatenatePdf {

    public static class Input {
        @Action.Input(name = "PDF List", contentType = ContentType.List, required = true)
        private List<$File> files;

        public List<$File> getFiles() {
            return files;
        }
    }

    public static class Output {
        @Action.Output(name = "PDF Concatenated", contentType = ContentType.Object)
        private $File pdfConcatenated;

        public Output($File pdfConcatenated) {
            this.pdfConcatenated = pdfConcatenated;
        }
    }
}
