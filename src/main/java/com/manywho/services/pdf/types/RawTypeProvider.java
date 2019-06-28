package com.manywho.services.pdf.types;

import com.google.common.base.Strings;
import com.manywho.sdk.api.describe.DescribeServiceRequest;
import com.manywho.sdk.api.draw.elements.type.TypeElement;
import com.manywho.sdk.services.configuration.Configuration;
import com.manywho.sdk.services.configuration.ConfigurationParser;
import com.manywho.sdk.services.types.TypeProvider;
import com.manywho.services.pdf.ServiceConfiguration;
import com.manywho.services.pdf.actions.GetPdfCommand;
import com.manywho.services.pdf.managers.DescribeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class RawTypeProvider implements TypeProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(RawTypeProvider.class);

    private DescribeManager describeTypeService;
    private ConfigurationParser configurationParser;

    @Inject
    public RawTypeProvider(DescribeManager describeManager, ConfigurationParser configurationParser) {
        this.describeTypeService = describeManager;
        this.configurationParser = configurationParser;
    }

    @Override
    public boolean doesTypeExist(Configuration configuration, String s) {
        return true;
    }

    @Override
    public List<TypeElement> describeTypes(Configuration configuration, DescribeServiceRequest describeServiceRequest) {

        try {
            if (describeServiceRequest.getConfigurationValues() != null && describeServiceRequest.getConfigurationValues().size()>0) {
                ServiceConfiguration serviceConfiguration = configurationParser.from(describeServiceRequest);
                if (serviceConfiguration.getPdfFormUrl() != null) {
                    if (Strings.isNullOrEmpty(serviceConfiguration.getPdfFormUrl())) {
                        throw new RuntimeException("The PDF Type Name can not be empty if PDF Form URL is populated");
                    }

                    return describeTypeService.getListTypeElement(serviceConfiguration.getPdfFormUrl());
                }
            }
        } catch (Exception e) {
            String error = String.format("There was a problem reading the PDF Form fields: %s", e.getMessage());
            LOGGER.error(error, e);

            throw new RuntimeException(error, e);
        }

        return new ArrayList<>();
    }
}