/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ohd.pophealth.json.measuremodel;

import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.ohd.pophealth.json.clinicalmodel.Patient;

import static org.junit.Assert.*;

/**
 *
 * @author ohdohd
 */
public class PopHealthPatientRecordTest {

    public PopHealthPatientRecordTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    
    /**
     * Test of toJson method, of class PopHealthPatientRecord.
     */
    @Test
    public void testToJson() throws Exception {
        System.out.println("toJson");
        boolean prettyPrint = true;
        PopHealthPatientRecord instance = new PopHealthPatientRecord();
        Patient pt = new Patient();
        pt.setBirthdate(182908800);
        instance.setPatient(pt);
        LinkedHashMap<String, Item> m = new LinkedHashMap<String, Item>();
        long[] l1 = {1255910400L};
        Item a = new DateItem(l1);
        m.put("diagnosis_asthma", a);
        long[] l2 = {1275177600L, 1277856000L};
        Item b = new DateItem(l2);
        m.put("encounter_office_and_outpatient_consult", b);
        long[] l3 = {1255910400L};
        Item c = new DateItem(l3);
        m.put("symptoms_daytime_asthma", c);
        long[] l4 = {1255910400L};
        Item d = new DateItem(l4);
        m.put("symptoms_daytime_asthma_quantified", d);
        instance.addMeasureResult("0001", m);
        String expResult = "";
        String result = instance.toJson(prettyPrint);
        System.out.println(result);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}