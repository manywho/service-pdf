package com.manywho.services.pdf.providers;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.manywho.services.pdf.configuration.S3Configuration;
import javax.inject.Inject;

public class S3ConfigurationProvider implements AWSCredentialsProvider {
    private S3Configuration s3Configuration;

    @Inject
    public S3ConfigurationProvider(ConfigurationPdfProvider configurationPdfProvider) {
        this.s3Configuration = configurationPdfProvider.configurationProvider().bind("s3", S3Configuration.class);
    }

    @Override
    public AWSCredentials getCredentials() {
        return new AWSCredentials() {
            @Override
            public String getAWSAccessKeyId() {
                return s3Configuration.awsAccessKeyId();
            }

            @Override
            public String getAWSSecretKey() {
                return s3Configuration.awsSecretAccessKey();
            }
        };
    }

    public String getBucketName() {
        return s3Configuration.bucketName();
    }

    @Override
    public void refresh() {

    }
}
