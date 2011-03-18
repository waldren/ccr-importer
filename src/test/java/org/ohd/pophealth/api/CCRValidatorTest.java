/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ohd.pophealth.api;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.astm.ccr.ContinuityOfCareRecord;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.ohd.pophealth.testsupport.*;
/**
 *
 * @author ohdohd
 */
public class CCRValidatorTest {

    public CCRValidatorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    	instance = new CCRValidator(new Configuration());
    }
    protected CCRValidator instance;
    
    @After
    public void tearDown() {
    }

    /**
     * Test of validateCCR method, of class CCRValidator.
     */
    @Test
    public void testValidateCCRValid() {
        try {
            String ccrValid = Utility.convertStreamToString(this.getClass().getClassLoader().getResourceAsStream("./ccrsamples/generic/ccr-sample-0001.xml"));
            System.out.println("validateCCR");
            String ccrXML = ccrValid;
            ContinuityOfCareRecord result = instance.validateCCR(ccrXML);
            System.out.println("Errors:");
            System.out.println(instance.getLastErrors(true));
            assertFalse(instance.hasErrors());
        } catch (IOException ex) {
            Logger.getLogger(CCRValidatorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of validateCCR method, of class CCRValidator.
     */
    @Test
    public void testValidateCCRInValid() {
        try {
            String ccrInvalid = Utility.convertStreamToString(this.getClass().getClassLoader().getResourceAsStream("./ccrsamples/generic/ccr-sample-0004.xml"));
            System.out.println("validateCCR");
            String ccrXML = ccrInvalid;
            ContinuityOfCareRecord result = instance.validateCCR(ccrXML);
            System.out.println("Errors:");
            System.out.println(instance.getLastErrors(true));
            assertTrue(instance.hasErrors());
        } catch (IOException ex) {
            Logger.getLogger(CCRValidatorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}