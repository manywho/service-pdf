package com.manywho.services.pdf.managers;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.BadPdfFormatException;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.manywho.sdk.services.types.system.$File;
import com.manywho.services.pdf.providers.S3ClientProvider;
import com.manywho.services.pdf.providers.S3ConfigurationProvider;
import com.manywho.services.pdf.services.PdfGeneratorService;
import com.manywho.services.pdf.types.FormField;
import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class FileManager {
    private String bucketName;
    private AmazonS3 s3client;
    private PdfGeneratorService pdfGeneratorService;

    final static private Integer MAX_TIME_LINK_AVAILABLE = 7200000;

    @Inject
    public FileManager(S3ConfigurationProvider s3ConfigurationProvider, S3ClientProvider s3ClientProvider, PdfGeneratorService pdfGeneratorService) {

        this.s3client = s3ClientProvider.getClient();
        this.bucketName = s3ConfigurationProvider.getBucketName();
        this.pdfGeneratorService = pdfGeneratorService;
    }

    public $File uploadFile(InputStream inputStream) {
        String id = UUID.randomUUID().toString();
        String fileName = String.format("%s.pdf", id);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("application/pdf");
        objectMetadata.setContentDisposition("filename=" + fileName);
        s3client.putObject(new PutObjectRequest(bucketName, id, inputStream, objectMetadata));

        return new $File(id, fileName, "application/pdf", getFileUrl(id));
    }

    public $File getFile(String id) {
        return new $File(id,id, "application/pdf", getFileUrl(id));
    }

    public InputStream getFileContent(String id) {
        S3Object object = s3client.getObject(new GetObjectRequest(bucketName, id));
        return object.getObjectContent();
    }

    private String getFileUrl(String fileId) {
        return s3client.generatePresignedUrl(bucketName, fileId, new Date(System.currentTimeMillis() + MAX_TIME_LINK_AVAILABLE)).toString();
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

    public void deleteFile(String fileId) {
        s3client.deleteObject(new DeleteObjectRequest(bucketName, fileId));
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

    public $File getS3FilePopulated(String fileId, List<FormField> fieldList) throws IOException {
        InputStream inputStream;

        if(fieldList.isEmpty()) {
            inputStream = this.getFileContent(fileId);
        } else {
            inputStream = pdfGeneratorService.populatePdfFromFields(this.getFileContent(fileId), fieldList);
        }

        return this.uploadFile(inputStream);
    }

    public $File getFilePopulated(String url, List<FormField> fieldList) throws IOException {
        InputStream inputStream;

        if(fieldList.isEmpty()) {
            inputStream = new URL(url).openStream();
        }else {
            inputStream = pdfGeneratorService.populatePdfFromFields(new URL(url).openStream(), fieldList);
        }

        return this.uploadFile(inputStream);
    }
}
