/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ohd.pophealth.json.clinicalmodel;

import org.ohd.pophealth.json.clinicalmodel.BaseClinicalObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.ohd.pophealth.json.measuremodel.CodedValue;
import static org.junit.Assert.*;

/**
 *
 * @author swaldren
 */
public class BaseMeasureObjectTest {
    private static BaseClinicalObject bmo;

    private static String jsonResult = "{\"type\":[{\"codingSystem\":\"SNOMEDCT\",\"version\":null,\"values\":[\"0000000\"]}],\"description\":[{\"codingSystem\":\"ICD9CM\",\"version\":null,\"values\":[\"250.0\"]}],\"id\":\"0001-1112-2223\"}";

    public BaseMeasureObjectTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        // Create test object
        bmo = new BaseClinicalObject("0001-1112-2223");
        // setup attributes
        CodedValue desc = new CodedValue();
        desc.setCodingSystem("ICD9CM");
        desc.addValue("250.0");
        bmo.addDescription(desc);

        CodedValue type = new CodedValue();
        type.setCodingSystem("SNOMEDCT");
        type.addValue("0000000");
        bmo.addType(type);

    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        bmo = null;
    }

    @Test
    public void testToJson() throws Exception {
        String js = bmo.toJson(false);
        assertEquals(jsonResult, js);
        js = bmo.toJson(true);
        System.out.println(js);
    }

}