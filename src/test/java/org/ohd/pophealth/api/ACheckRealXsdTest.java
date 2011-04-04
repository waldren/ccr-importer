package org.ohd.pophealth.api;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class ACheckRealXsdTest {

    @Test
    public void checkNotDummyFile() throws IOException {
        InputStream xsdFile = getClass().getResourceAsStream("/org/ohd/CCRV1.xsd");
        Assert.assertNotNull("CCRV1.xsd not found in src/main/java/org/ohd", xsdFile);
        Writer writer = new StringWriter();

        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(
                    new InputStreamReader(xsdFile, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            xsdFile.close();
        }

        String xsdContent = writer.toString();
        if ("\r\n".equals(System.getProperty("line.separator"))) {
          xsdContent = xsdContent.replaceAll("\r[^\n]", System.getProperty("line.separator"));
        } else {
          xsdContent = xsdContent.replaceAll("\r\n", System.getProperty("line.separator"));
        }

        Assert.assertFalse("Dummy file found, replace CCRV1.xsd with your own licensed version", dummyContent.equals(xsdContent));

    }

    private static final String dummyContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<xs:schema xmlns=\"urn:astm-org:CCR\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:ccr=\"urn:astm-org:CCR\" targetNamespace=\"urn:astm-org:CCR\" elementFormDefault=\"qualified\" attributeFormDefault=\"unqualified\">\n" +
            "\t<!--E2369-05, Standard Specification for the Continuity of Care (CCR) - Final Version 1.0 (V1.0) November 7, 2005,  ASTM E31.28 CCR Subcommittee-->\n" +
            "\t<!--Copyright 2004-2005 ASTM, 100 Barr Harbor Drive, West Conshohocken, PA 19428-2959. All rights reserved.-->\n" +
            "\n" +
            "</xs:schema>\n";
}
