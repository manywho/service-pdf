package com.manywho.services.pdf.actions;

import com.manywho.sdk.api.run.elements.config.ServiceRequest;
import com.manywho.sdk.services.actions.ActionCommand;
import com.manywho.sdk.services.actions.ActionResponse;
import com.manywho.sdk.services.types.system.$File;
import com.manywho.services.pdf.ServiceConfiguration;
import com.manywho.services.pdf.managers.FileManager;
import com.manywho.services.pdf.services.PdfGeneratorService;

import javax.inject.Inject;
import java.io.*;

public class PopulatePdfCommand implements ActionCommand<ServiceConfiguration, PopulatePdf, PopulatePdf.Input, PopulatePdf.Output> {
    private FileManager fileManager;
    private PdfGeneratorService pdfGeneratorService;

    @Inject
    public PopulatePdfCommand(FileManager fileManager, PdfGeneratorService pdfGeneratorService) {
        this.fileManager = fileManager;
        this.pdfGeneratorService = pdfGeneratorService;
    }

    @Override
    public ActionResponse<PopulatePdf.Output> execute(ServiceConfiguration serviceConfiguration, ServiceRequest serviceRequest, PopulatePdf.Input input) {
        try {
            InputStream inputStream;
            if(input.getPdfFields().isEmpty()) {
                inputStream = fileManager.getFileContent(input.getPdfFile().getId());
            }else {
                inputStream = pdfGeneratorService.populatePdfFromFields(
                        fileManager.getFileContent(input.getPdfFile().getId()), input.getPdfFields());
            }

            $File file = fileManager.uploadFile(inputStream);

            return new ActionResponse<>(new PopulatePdf.Output(file));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
