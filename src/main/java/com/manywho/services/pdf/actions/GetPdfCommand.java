package com.manywho.services.pdf.actions;

import com.manywho.sdk.api.run.elements.config.ServiceRequest;
import com.manywho.sdk.services.actions.ActionCommand;
import com.manywho.sdk.services.actions.ActionResponse;
import com.manywho.sdk.services.types.system.$File;
import com.manywho.services.pdf.ServiceConfiguration;
import com.manywho.services.pdf.managers.FileManager;
import com.manywho.services.pdf.services.PdfGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.InputStream;

public class GetPdfCommand implements ActionCommand<ServiceConfiguration, GetPdf, GetPdf.Input, GetPdf.Output> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetPdfCommand.class);
    private FileManager fileManager;

    @Inject
    public GetPdfCommand(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public ActionResponse<GetPdf.Output> execute(ServiceConfiguration serviceConfiguration, ServiceRequest serviceRequest, GetPdf.Input input) {
        try {
            GetPdf.Output output = new GetPdf.Output(this.fileManager.getFile(input.getFileId()));

            return new ActionResponse<>(output);
        } catch (Exception e) {
            String error = String.format("There was a problem fetching the PDF File: %s", e.getMessage());
            LOGGER.error(error, e);

            throw new RuntimeException(error, e);
        }
    }
}
