package com.manywho.services.pdf.actions;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.services.actions.Action;
import com.manywho.sdk.services.types.system.$File;

@Action.Metadata(name = "Get PDF", summary = "Get PDF", uri = "get-pdf-file")
public class GetPdf {

    public static class Input {
        @Action.Input(name = "File Id", contentType = ContentType.String, required = true)
        private String fileId;

        public String getFileId() {
            return fileId;
        }
    }

    public static class Output {
        @Action.Output(name = "PDF File", contentType = ContentType.String)
        private $File file;

        public Output($File file) {
            this.file = file;
        }
    }
}
