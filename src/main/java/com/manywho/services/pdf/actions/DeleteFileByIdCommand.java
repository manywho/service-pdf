package com.manywho.services.pdf.actions;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.manywho.sdk.api.InvokeType;
import com.manywho.sdk.api.run.elements.config.ServiceRequest;
import com.manywho.sdk.services.actions.ActionCommand;
import com.manywho.sdk.services.actions.ActionResponse;
import com.manywho.services.pdf.ServiceConfiguration;
import com.manywho.services.pdf.managers.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.inject.Inject;

public class DeleteFileByIdCommand implements ActionCommand<ServiceConfiguration, DeleteFileById, DeleteFileById.Input, Void> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteFileByIdCommand.class);
    private FileManager fileManager;

    @Inject
    public DeleteFileByIdCommand(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public ActionResponse<Void> execute(ServiceConfiguration serviceConfiguration, ServiceRequest serviceRequest, DeleteFileById.Input input) {
        try {
            this.fileManager.deleteFile(input.getFileId());

            return new ActionResponse<>(InvokeType.Forward);

        } catch (AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        } catch (SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }

        return new ActionResponse<>(InvokeType.Forward);
    }
}
