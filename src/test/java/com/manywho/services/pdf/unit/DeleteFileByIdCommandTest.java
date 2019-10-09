package com.manywho.services.pdf.unit;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.manywho.services.pdf.actions.DeleteFileById;
import com.manywho.services.pdf.actions.DeleteFileByIdCommand;
import com.manywho.services.pdf.configuration.S3Configuration;
import com.manywho.services.pdf.managers.FileManager;
import com.manywho.services.pdf.providers.ConfigurationPdfProvider;
import com.manywho.services.pdf.providers.S3ClientProvider;
import com.manywho.services.pdf.providers.S3ConfigurationProvider;
import com.manywho.services.pdf.services.PdfGeneratorService;
import org.cfg4j.provider.ConfigurationProvider;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DeleteFileByIdCommandTest {
    @Test
    public void testDeleteFileById() {

        S3ConfigurationProvider s3ConfigurationProvider =
                new S3ConfigurationProvider(mockPdfConfigurationProvider("bucket-name-example", "private-key-example", "private-key-example"));

        PdfGeneratorService pdfGeneratorService = mock(PdfGeneratorService.class);

        S3ClientProvider s3ClientProvider = mock(S3ClientProvider.class);
        AmazonS3 s3Client = mock(AmazonS3.class);
        when(s3ClientProvider.getClient()).thenReturn(s3Client);

        String fileId = "file-id-example";
        FileManager fileManager = new FileManager(s3ConfigurationProvider, s3ClientProvider, pdfGeneratorService);
        DeleteFileByIdCommand command = new DeleteFileByIdCommand(fileManager);
        DeleteFileById.Input input = new DeleteFileById.Input(fileId);
        command.execute(null, null, input);

        ArgumentCaptor<DeleteObjectRequest> argument = ArgumentCaptor.forClass(DeleteObjectRequest.class);
        verify(s3Client, times(1))
                .deleteObject(argument.capture());

        assertEquals("bucket-name-example", argument.getAllValues().get(0).getBucketName());
        assertEquals("file-id-example", argument.getAllValues().get(0).getKey());

    }


    private ConfigurationPdfProvider mockPdfConfigurationProvider(String bucketName, String publicKey, String secretKey) {
        S3Configuration s3Configuration = mock(S3Configuration.class);
        when(s3Configuration.bucketName())
                .thenReturn(bucketName);
        when(s3Configuration.awsAccessKeyId())
                .thenReturn(publicKey);
        when(s3Configuration.awsSecretAccessKey())
                .thenReturn(secretKey);

        ConfigurationPdfProvider configurationPdfProvider = mock(ConfigurationPdfProvider.class);
        ConfigurationProvider configurationProvider = mock(ConfigurationProvider.class);
        when(configurationProvider.bind(any(), any()))
                .thenReturn(new S3Configuration() {
                    @Override
                    public String bucketName() {
                        return bucketName;
                    }

                    @Override
                    public String awsAccessKeyId() {
                        return publicKey;
                    }

                    @Override
                    public String awsSecretAccessKey() {
                        return secretKey;                    }
                });

        when(configurationPdfProvider.configurationProvider())
                .thenReturn(configurationProvider);

        return configurationPdfProvider;
    }
}
