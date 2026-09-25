package com.pisethjava.factoryspring.export;

import org.springframework.stereotype.Component;

@Component("PDF")
public class PdfExportFile implements ExportFile {
    @Override
    public void export(String content) {
        System.out.println("Export PDF: " + content);
    }
}
