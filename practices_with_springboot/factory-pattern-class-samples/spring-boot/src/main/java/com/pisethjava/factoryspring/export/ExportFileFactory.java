package com.pisethjava.factoryspring.export;

import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ExportFileFactory {
    private final Map<String, ExportFile> exportFiles;

    public ExportFileFactory(Map<String, ExportFile> exportFiles) {
        this.exportFiles = exportFiles;
    }

    public ExportFile getExportFile(String type) {
        ExportFile exportFile = exportFiles.get(type.toUpperCase());
        if (exportFile == null) {
            throw new IllegalArgumentException("Unsupported export type: " + type);
        }
        return exportFile;
    }
}
