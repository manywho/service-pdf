package com.manywho.services.pdf.actions;

import com.manywho.sdk.api.run.elements.config.ServiceRequest;
import com.manywho.sdk.services.actions.ActionCommand;
import com.manywho.sdk.services.actions.ActionResponse;
import com.manywho.services.pdf.ServiceConfiguration;
import com.manywho.services.pdf.managers.FileManager;
import com.manywho.services.pdf.services.PdfGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.inject.Inject;
import java.io.InputStream;

public class CreatePdfFromHtmlCommand implements ActionCommand<ServiceConfiguration, CreatePdfFromHtml, CreatePdfFromHtml.Input, CreatePdfFromHtml.Output> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreatePdfFromHtmlCommand.class);

    private FileManager fileManager;
    private PdfGeneratorService pdfGeneratorService;

    @Inject
    public CreatePdfFromHtmlCommand(FileManager fileManager, PdfGeneratorService pdfGeneratorService) {
        this.fileManager = fileManager;
        this.pdfGeneratorService = pdfGeneratorService;
    }

    @Override
    public ActionResponse<CreatePdfFromHtml.Output> execute(ServiceConfiguration serviceConfiguration, ServiceRequest serviceRequest, CreatePdfFromHtml.Input input) {
        try {
            InputStream inputPdf = pdfGeneratorService.generatePdfFromHtml(input.getHtmlContent());
            CreatePdfFromHtml.Output output = new CreatePdfFromHtml.Output(this.fileManager.uploadFile(inputPdf));

            return new ActionResponse<>(output);
        } catch (Exception e) {
            LOGGER.error("There was a problem generating a PDF from the provided HTML content: {}", e.getMessage(), e);

            throw new RuntimeException(String.format("There was a problem generating a PDF from the provided HTML content: %s", e.getMessage()));
        }
    }
}
