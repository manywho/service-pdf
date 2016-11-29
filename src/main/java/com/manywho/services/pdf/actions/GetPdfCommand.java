package com.manywho.services.pdf.actions;

import com.manywho.sdk.api.run.elements.config.ServiceRequest;
import com.manywho.sdk.services.actions.ActionCommand;
import com.manywho.sdk.services.actions.ActionResponse;
import com.manywho.sdk.services.types.system.$File;
import com.manywho.services.pdf.ServiceConfiguration;
import com.manywho.services.pdf.managers.FileManager;
import com.manywho.services.pdf.services.PdfGeneratorService;

import javax.inject.Inject;
import java.io.InputStream;

public class GetPdfCommand implements ActionCommand<ServiceConfiguration, GetPdf, GetPdf.Input, GetPdf.Output> {
    private FileManager fileManager;

    @Inject
    public GetPdfCommand(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public ActionResponse<GetPdf.Output> execute(ServiceConfiguration serviceConfiguration, ServiceRequest serviceRequest, GetPdf.Input input) {
        try {
            return new ActionResponse<>(new GetPdf.Output(this.fileManager.getFile(input.getFileId())));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
