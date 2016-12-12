package com.manywho.services.pdf;

import com.google.inject.AbstractModule;
import com.manywho.sdk.services.types.TypeProvider;
import com.manywho.services.pdf.types.RawTypeProvider;

public class ApplicationPdfModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TypeProvider.class).to(RawTypeProvider.class);
    }
}
