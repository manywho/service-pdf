package com.manywho.services.pdf.actions;

import com.manywho.sdk.api.run.elements.config.ServiceRequest;
import com.manywho.sdk.services.actions.ActionCommand;
import com.manywho.sdk.services.actions.ActionResponse;
import com.manywho.services.pdf.ServiceConfiguration;
import com.manywho.services.pdf.managers.FileManager;

import javax.inject.Inject;

public class SignPdfCommand implements ActionCommand<ServiceConfiguration, SignPdf, SignPdf.Input, SignPdf.Output> {
    private FileManager fileManager;

    @Inject
    public SignPdfCommand(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public ActionResponse<SignPdf.Output> execute(ServiceConfiguration serviceConfiguration, ServiceRequest serviceRequest, SignPdf.Input input) {
        try {
            SignPdf.Output output = new SignPdf.Output(this.fileManager.signPdf(input.getFile()));

            return new ActionResponse<>(output);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
