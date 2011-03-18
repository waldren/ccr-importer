/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ohd.pophealth.preprocess;

import java.net.URL;
import org.ohd.pophealth.api.Configuration;
import org.ohd.pophealth.testsupport.Utility;
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

/**
 *
 * @author ohdohd
 */
public class PreProcessorTest {

    public PreProcessorTest() {
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
     * Test of preProcess method, of class PreProcessor.
     */
    @Test
    public void testPreProcess() {
        try {
            String ccrfilename = "ccr-sample-0032.xml";
            System.out.println("preProcess");
            ContinuityOfCareRecord ccr = Utility.getCCR(this.getClass().getClassLoader().getResourceAsStream("ccrsamples/generic/"+ccrfilename));
            Configuration conf = new Configuration();
            URL umlsConf = this.getClass().getClassLoader().getResource(conf.getUmlsConfLocation());
            PreProcessor instance = new PreProcessor(umlsConf.getFile(), conf.getLvgConfLocation());
            //ContinuityOfCareRecord expResult = null;
            ContinuityOfCareRecord result = instance.preProcess(ccr);
            Utility.writeCCR(result, "./test/ccrsamples/preprocessed", ccrfilename, true);
        } catch (Exception ex) {
            Logger.getLogger(PreProcessorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of fixTobaccoHx method, of class PreProcessor.
     */
    @Test
    public void testFixTobaccoHx() {
        System.out.println("fixTobaccoHx");
        ContinuityOfCareRecord ccr = null;
        PreProcessor instance = null;
        ContinuityOfCareRecord expResult = null;
        ContinuityOfCareRecord result = instance.fixTobaccoHx(ccr);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of fixEncounters method, of class PreProcessor.
     */
    @Test
    public void testFixEncounters() {
        System.out.println("fixEncounters");
        ContinuityOfCareRecord ccr = null;
        PreProcessor instance = null;
        ContinuityOfCareRecord expResult = null;
        ContinuityOfCareRecord result = instance.fixEncounters(ccr);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of inferCodes method, of class PreProcessor.
     */
    @Test
    public void testInferCodes() {
        System.out.println("inferCodes");
        ContinuityOfCareRecord ccr = null;
        PreProcessor instance = null;
        ContinuityOfCareRecord expResult = null;
        ContinuityOfCareRecord result = instance.inferCodes(ccr);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}