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

public class PopulatePdfCommand implements ActionCommand<ServiceConfiguration, PopulatePdf, PopulatePdf.Input, PopulatePdf.Output> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PopulatePdfCommand.class);
    private FileManager fileManager;

    @Inject
    public PopulatePdfCommand(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public ActionResponse<PopulatePdf.Output> execute(ServiceConfiguration serviceConfiguration, ServiceRequest serviceRequest, PopulatePdf.Input input) {
        try {
            $File file = fileManager.getS3FilePopulated(input.getPdfFile().getId(), input.getPdfFields() );

            return new ActionResponse<>(new PopulatePdf.Output(file));
        } catch (Exception e) {
            LOGGER.error("There was a problem populating the PDF: {}", e.getMessage(), e);

            throw new RuntimeException(String.format("There was a problem populating the PDF: %s", e.getMessage()));
        }
    }
}
