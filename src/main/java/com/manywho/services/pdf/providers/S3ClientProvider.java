package com.manywho.services.pdf.providers;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import javax.inject.Inject;

public class S3ClientProvider {
    private S3ConfigurationProvider s3ConfigurationProvider;

    @Inject
    public S3ClientProvider(S3ConfigurationProvider s3ConfigurationProvider) {
        this.s3ConfigurationProvider = s3ConfigurationProvider;
    }

    public AmazonS3 getClient(){
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(s3ConfigurationProvider)
                .build();
    }
}
