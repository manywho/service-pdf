package com.manywho.services.pdf.database;

import com.manywho.sdk.api.run.elements.type.ListFilter;
import com.manywho.sdk.api.run.elements.type.MObject;
import com.manywho.sdk.api.run.elements.type.ObjectDataType;
import com.manywho.sdk.services.database.RawDatabase;
import com.manywho.services.pdf.ServiceConfiguration;
import com.manywho.services.pdf.managers.DataManager;
import com.manywho.services.pdf.types.FormField;
import com.manywho.services.pdf.types.FormType;
import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

public class DynamicTypeDatabase implements RawDatabase<ServiceConfiguration> {
    private DataManager dataManager;

    @Inject
    public DynamicTypeDatabase(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public MObject find(ServiceConfiguration serviceConfiguration, ObjectDataType objectDataType, String s) {
        return null;
    }

    @Override
    public List<MObject> findAll(ServiceConfiguration serviceConfiguration, ObjectDataType objectDataType, ListFilter listFilter) {
        return null;
    }

    @Override
    public MObject create(ServiceConfiguration serviceConfiguration, MObject mObject) {
        try {
            if (!Objects.equals(mObject.getDeveloperName(), FormField.NAME) && !Objects.equals(mObject.getDeveloperName(), FormType.NAME)) {
                return dataManager.createPdf(serviceConfiguration, mObject);
            }
        } catch (Exception e) {
            throw new RuntimeException("problem creating object" + e.getMessage());
        }

        throw new RuntimeException("Error creating object");
    }

    @Override
    public List<MObject> create(ServiceConfiguration serviceConfiguration, List<MObject> list) {
        return null;
    }

    @Override
    public void delete(ServiceConfiguration serviceConfiguration, MObject mObject) {

    }

    @Override
    public void delete(ServiceConfiguration serviceConfiguration, List<MObject> list) {

    }

    @Override
    public MObject update(ServiceConfiguration serviceConfiguration, MObject mObject) {
        return null;
    }

    @Override
    public List<MObject> update(ServiceConfiguration serviceConfiguration, List<MObject> list) {
        return null;
    }
}
