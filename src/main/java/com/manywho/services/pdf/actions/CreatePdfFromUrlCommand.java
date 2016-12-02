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
import java.net.URL;

public class CreatePdfFromUrlCommand implements ActionCommand<ServiceConfiguration, CreatePdfFromUrl, CreatePdfFromUrl.Input, CreatePdfFromUrl.Output> {
    private FileManager fileManager;

    @Inject
    public CreatePdfFromUrlCommand(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public ActionResponse<CreatePdfFromUrl.Output> execute(ServiceConfiguration serviceConfiguration, ServiceRequest serviceRequest, CreatePdfFromUrl.Input input) {
        try {
            InputStream inputStream = new URL(input.getPdfUrl()).openStream();
            $File file = this.fileManager.uploadFile(inputStream);
            CreatePdfFromUrl.Output output = new CreatePdfFromUrl.Output(file);

            return new ActionResponse<>(output);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
