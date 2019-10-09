package com.manywho.services.pdf.actions;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.services.actions.Action;

@Action.Metadata(name = "Delete File By ID", summary = "Delete File By ID", uri = "delete-file")
public class DeleteFileById {
    public static class Input {
        @Action.Input(name = "$File ID", contentType = ContentType.String, required = true)
        private String fileId;

        public Input(String fileId) {
            this.fileId = fileId;
        }

        public String getFileId() {
            return fileId;
        }
    }
}
