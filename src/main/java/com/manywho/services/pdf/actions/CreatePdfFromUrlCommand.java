package com.manywho.services.pdf.actions;

import com.manywho.sdk.api.run.elements.config.ServiceRequest;
import com.manywho.sdk.services.actions.ActionCommand;
import com.manywho.sdk.services.actions.ActionResponse;
import com.manywho.sdk.services.types.system.$File;
import com.manywho.services.pdf.ServiceConfiguration;
import com.manywho.services.pdf.managers.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.InputStream;
import java.net.URL;

public class CreatePdfFromUrlCommand implements ActionCommand<ServiceConfiguration, CreatePdfFromUrl, CreatePdfFromUrl.Input, CreatePdfFromUrl.Output> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreatePdfFromUrlCommand.class);
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
            String error = String.format("There was a problem generating a PDF from the provided URL: %s", e.getMessage());
            LOGGER.error(error, e);

            throw new RuntimeException(error, e);
        }
    }
}
