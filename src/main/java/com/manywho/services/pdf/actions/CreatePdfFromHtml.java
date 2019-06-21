package com.manywho.services.pdf.actions;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.services.actions.Action;
import com.manywho.sdk.services.types.system.$File;

@Action.Metadata(name = "Create PDF from HTML", summary = "Convert HTML to PDF", uri = "html-to-pdf")
public class CreatePdfFromHtml {

    public static class Input {
        @Action.Input(name = "HTML Content", contentType = ContentType.Content, required = true)
        private String htmlContent;

        public String getHtmlContent() {
            return htmlContent;
        }
    }

    public static class Output {
        @Action.Output(name = "PDF File", contentType = ContentType.Object)
        private $File pdfFile;

        public Output($File pdfFile) {
            this.pdfFile = pdfFile;
        }
    }
}
