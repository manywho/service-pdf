package com.manywho.services.pdf.managers;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.manywho.sdk.services.types.system.$File;
import com.manywho.services.pdf.configuration.S3Configuration;
import com.manywho.services.pdf.providers.ConfigurationPdfProvider;
import javax.inject.Inject;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

public class FileManager {
    private S3Configuration s3Config;
    private AmazonS3 s3client;

    final static private Integer MAX_TIME_LINK_AVAILABLE = 300000;

    @Inject
    public FileManager(ConfigurationPdfProvider configurationPdfProvider) {
        this.s3Config = configurationPdfProvider.configurationProvider().bind("s3", S3Configuration.class);
        BasicAWSCredentials credentials = new BasicAWSCredentials(this.s3Config.awsAccessKeyId(), this.s3Config.awsSecretAccessKey());
        this.s3client = new AmazonS3Client(credentials);
    }

    public $File uploadFile(InputStream inputStream) throws Exception {
        String id = UUID.randomUUID().toString();
        String fileName = String.format("%s.pdf", id);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("application/pdf");
        objectMetadata.setContentDisposition("filename=" + fileName);
        s3client.putObject(new PutObjectRequest(this.s3Config.bucketName(), id, inputStream, objectMetadata));

        return new $File(id, fileName, "application/pdf", getFileUrl(id));
    }

    public $File getFile(String id) {
        return new $File(id,id, "application/pdf", getFileUrl(id));
    }

    public InputStream getFileContent(String id) {
        S3Object object = s3client.getObject(new GetObjectRequest(s3Config.bucketName(), id));
        return object.getObjectContent();
    }

    private String getFileUrl(String fileId) {
        return s3client.generatePresignedUrl(this.s3Config.bucketName(), fileId, new Date(System.currentTimeMillis() + MAX_TIME_LINK_AVAILABLE)).toString();
    }
}
