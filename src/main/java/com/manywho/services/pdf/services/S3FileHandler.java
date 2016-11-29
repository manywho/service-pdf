package com.manywho.services.pdf.services;


import com.manywho.sdk.api.run.elements.type.FileListFilter;
import com.manywho.sdk.services.configuration.Configuration;
import com.manywho.sdk.services.files.FileHandler;
import com.manywho.sdk.services.files.FileUpload;
import com.manywho.sdk.services.types.system.$File;
import com.manywho.services.pdf.managers.FileManager;

import javax.inject.Inject;
import java.util.List;

public class S3FileHandler implements FileHandler {
    private final FileManager fileManager;

    @Inject
    public S3FileHandler(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public List<$File> findAll(Configuration configuration, FileListFilter fileListFilter, String s) {
        return null;
    }

    @Override
    public $File upload(Configuration configuration, String s, FileUpload fileUpload) {
        try {
            return fileManager.uploadFile(fileUpload.getContent());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
