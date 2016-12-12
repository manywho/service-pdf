package com.manywho.services.pdf.managers;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BadPdfFormatException;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.manywho.sdk.services.types.system.$File;
import com.manywho.services.pdf.configuration.S3Configuration;
import com.manywho.services.pdf.providers.ConfigurationPdfProvider;
import com.manywho.services.pdf.services.PdfGeneratorService;
import com.manywho.services.pdf.types.FormField;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class FileManager {
    private S3Configuration s3Config;
    private AmazonS3 s3client;
    private PdfGeneratorService pdfGeneratorService;

    final static private Integer MAX_TIME_LINK_AVAILABLE = 300000;

    @Inject
    public FileManager(ConfigurationPdfProvider configurationPdfProvider, PdfGeneratorService pdfGeneratorService) {
        this.s3Config = configurationPdfProvider.configurationProvider().bind("s3", S3Configuration.class);
        BasicAWSCredentials credentials = new BasicAWSCredentials(this.s3Config.awsAccessKeyId(), this.s3Config.awsSecretAccessKey());
        this.s3client = new AmazonS3Client(credentials);
        this.pdfGeneratorService = pdfGeneratorService;
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

    public $File concatenatePdf(List<$File> files) throws Exception {
        ByteArrayOutputStream mergedPdfOutputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfCopy copy = new PdfCopy(document, mergedPdfOutputStream);
        document.open();
        for ($File file:files) {
            addPdfPages(this.getFileContent(file.getId()), copy);
        }
        document.close();

        return uploadFile(new ByteArrayInputStream(mergedPdfOutputStream.toByteArray()));
    }

    private void addPdfPages(InputStream originalInputStream, PdfCopy copy) throws IOException, BadPdfFormatException {
        PdfImportedPage page;
        PdfCopy.PageStamp stamp;
        PdfReader reader = new PdfReader(originalInputStream);
        int numberOfPages = reader.getNumberOfPages();

        for (int pageNumber = 0; pageNumber < numberOfPages; ) {
            page = copy.getImportedPage(reader, ++pageNumber);
            stamp = copy.createPageStamp(page);
            stamp.alterContents();
            copy.addPage(page);
        }
        reader.close();
    }

    public $File getFilePopulated(String fileId, List<FormField> fieldList) {
        InputStream inputStream;

        try {
            if(fieldList.isEmpty()) {
                inputStream = this.getFileContent(fileId);
            }else {
                inputStream = pdfGeneratorService.populatePdfFromFields(
                        this.getFileContent(fileId), fieldList);
            }

            return this.uploadFile(inputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
