package com.manywho.services.pdf.database;

import com.manywho.sdk.api.draw.content.Command;
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
    public MObject find(ServiceConfiguration configuration, ObjectDataType objectDataType, Command command, String id) {
        return null;
    }

    @Override
    public List<MObject> findAll(ServiceConfiguration configuration, ObjectDataType objectDataType, Command command, ListFilter filter, List<MObject> objects) {
        return null;
    }

    @Override
    public MObject create(ServiceConfiguration configuration, ObjectDataType objectDataType, MObject object) {
        try {
            if (!Objects.equals(object.getDeveloperName(), FormField.NAME) && !Objects.equals(object.getDeveloperName(), FormType.NAME)) {
                return dataManager.createPdf(configuration, object);
            }
        } catch (Exception e) {
            throw new RuntimeException("problem creating object" + e.getMessage());
        }

        throw new RuntimeException("Error creating object");
    }

    @Override
    public List<MObject> create(ServiceConfiguration configuration, ObjectDataType objectDataType, List<MObject> objects) {
        return null;
    }

    @Override
    public void delete(ServiceConfiguration configuration, ObjectDataType objectDataType, MObject object) {

    }

    @Override
    public void delete(ServiceConfiguration configuration, ObjectDataType objectDataType, List<MObject> objects) {

    }

    @Override
    public MObject update(ServiceConfiguration configuration, ObjectDataType objectDataType, MObject object) {
        return null;
    }

    @Override
    public List<MObject> update(ServiceConfiguration configuration, ObjectDataType objectDataType, List<MObject> objects) {
        return null;
    }
}
