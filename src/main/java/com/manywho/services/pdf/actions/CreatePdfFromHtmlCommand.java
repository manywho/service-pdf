package com.manywho.services.pdf.actions;

import com.manywho.sdk.api.run.elements.config.ServiceRequest;
import com.manywho.sdk.services.actions.ActionCommand;
import com.manywho.sdk.services.actions.ActionResponse;
import com.manywho.services.pdf.ServiceConfiguration;
import com.manywho.services.pdf.managers.FileManager;
import com.manywho.services.pdf.services.PdfGeneratorService;

import javax.inject.Inject;
import java.io.InputStream;

public class CreatePdfFromHtmlCommand implements ActionCommand<ServiceConfiguration, CreatePdfFromHtml, CreatePdfFromHtml.Input, CreatePdfFromHtml.Output> {
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

            return new ActionResponse<>(new CreatePdfFromHtml.Output(this.fileManager.uploadFile(inputPdf)));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
