/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ohd.pophealth.json.clinicalmodel;

import org.ohd.pophealth.json.clinicalmodel.Condition;
import org.joda.time.DateTime;
import java.util.ArrayList;
import java.util.Date;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.ohd.pophealth.json.measuremodel.CodedValue;
import static org.junit.Assert.*;

/**
 *
 * @author swaldren
 */
public class ConditionTest {
    private static Condition c;

    public ConditionTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        c = new Condition("1111-1112-2223");
        // setup attributes
        CodedValue desc = new CodedValue();
        desc.setCodingSystem("ICD9CM");
        desc.addValue("250.0");
        c.addDescription(desc);


        CodedValue type = new CodedValue();
        type.setCodingSystem("SNOMEDCT");
        type.addValue("0000000");
        c.addType(type);

        CodedValue status = new CodedValue();
        status.setCodingSystem("TEXT");
        status.addValue("active");
        c.addStatus(status);

        DateTime onset = new DateTime();
        c.setOnset(onset.getMillis()/1000);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        c = null;
    }

    @Test
    public void testGetCategory() {
        assertEquals("diagnosis_condition_problem", c.getCategory());
    }

    @Test
    public void testToJson() throws Exception {
        String js = c.toJson(true);
        //assertEquals(jsonResult, js);
        //js = c.toJson(true);
        System.out.println(js);
    }

}