package com.manywho.services.pdf.configuration;

public interface S3Configuration {
    String bucketName();
    String awsAccessKeyId();
    String awsSecretAccessKey();
}
