package com.pisethjava.factoryspring.export;

import org.springframework.stereotype.Service;

@Service
public class ExportService {
    private final ExportFileFactory factory;

    public ExportService(ExportFileFactory factory) {
        this.factory = factory;
    }

    public void export(String type, String content) {
        ExportFile exportFile = factory.getExportFile(type);
        exportFile.export(content);
    }
}
