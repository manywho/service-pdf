package com.manywho.services.pdf.actions;

import com.manywho.sdk.api.run.elements.config.ServiceRequest;
import com.manywho.sdk.services.actions.ActionCommand;
import com.manywho.sdk.services.actions.ActionResponse;
import com.manywho.services.pdf.ServiceConfiguration;
import com.manywho.services.pdf.managers.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.inject.Inject;

public class ConcatenatePdfCommand implements ActionCommand<ServiceConfiguration, ConcatenatePdf, ConcatenatePdf.Input, ConcatenatePdf.Output> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConcatenatePdfCommand.class);
    private FileManager fileManager;

    @Inject
    public ConcatenatePdfCommand(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public ActionResponse<ConcatenatePdf.Output> execute(ServiceConfiguration serviceConfiguration, ServiceRequest serviceRequest, ConcatenatePdf.Input input) {
        try {
            ConcatenatePdf.Output output = new ConcatenatePdf.Output(this.fileManager.concatenatePdf(input.getFiles()));

            return new ActionResponse<>(output);
        } catch (Exception e) {
            e.printStackTrace();

            if ("PdfReader not opened with owner password".equals(e.getMessage())) {
                throw new RuntimeException("Concatenating PDF with owner password restrictions is not supported.", e);
            }

            LOGGER.error("There was a problem concatenating the PDF from the provided files: {}", e.getMessage(), e);

            throw new RuntimeException(String.format("There was a problem concatenating the PDF from the provided files: %s", e.getMessage()));
        }
    }
}
