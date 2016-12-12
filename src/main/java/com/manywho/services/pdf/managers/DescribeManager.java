package com.manywho.services.pdf.managers;

import com.manywho.sdk.api.draw.elements.type.TypeElement;
import com.manywho.services.pdf.services.DescribeService;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class

DescribeManager {
    private DescribeService describeService;

    @Inject
    public DescribeManager(DescribeService describeService) {
        this.describeService = describeService;
    }

    public List<TypeElement> getListTypeElement(String formURL, String typeName) {
        List<TypeElement> listOfTypeElements = new ArrayList<>();
        listOfTypeElements.add(describeService.createTypeElementFromForm(formURL, typeName));

        return listOfTypeElements;
    }
}
