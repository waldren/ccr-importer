/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ohd.pophealth.api;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ohd.pophealth.testsupport.Utility;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;
import org.ohd.pophealth.json.MeasureReaderTest;
import org.ohd.pophealth.json.measuremodel.QualityMeasure;

/**
 *
 * @author ohdohd
 */
public class EvaluatorTest {
    String ccrXML;
    public EvaluatorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    	try {
			variables.load(this.getClass().getClassLoader().getResourceAsStream("./config.properties"));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    protected Properties variables = new Properties();

    @After
    public void tearDown() {
    }
    
    @Test
    public void testCCRSamples(){
    	Evaluator instance = new Evaluator();
    	try {
    		File outputDirectory = new File (variables.getProperty("output_dir"));
    		if (!outputDirectory.isDirectory()){
    			fail(outputDirectory+" is not a directory");
    		}
			addMeasures(instance, variables.getProperty("measure_location"));
			HashMap<String, String> ccrs = getCCRSamples(variables.getProperty("ccrsample_dir"));
			instance.setPreProcess_fixEncounters(false);
			instance.setPreProcess_fixTobacco(false);
			instance.setPreProcess_inferCodes(false);
			for (String ccr : ccrs.keySet()){
				System.out.println("Evaluating "+ccr+"...");
				String ccrXML = ccrs.get(ccr);
				//System.out.println("ccrxml: "+ccrXML);
				String result = instance.evaluate(ccrXML);
				writeToFile(result, ccr, outputDirectory);
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void addMeasures(Evaluator instance, String location) throws Exception{
    	File mDir = new File(location);
    	System.out.println("Measure Directory: "+mDir.getAbsolutePath());
    	if (!mDir.isDirectory()){
    		throw new Exception(location+" is not a directory");
    	}
    	File[] ms = mDir.listFiles(Utility.jsonFilter);
    	System.out.println(ms.length+" Measures found.");
    	for (File m : ms){
    		System.out.println("Adding Measure: "+m.getName());
    		String json = Utility.convertStreamToString(new FileInputStream(m));
    		instance.addMeasure(json);
    	}
    }
    
    private void writeToFile(String result, String fileName, File directory){
    	try {
    		System.out.println("Writing To: "+directory.getAbsolutePath()+"  "+fileName);
    		File file = new File (directory, fileName+".json");
            BufferedWriter out = new BufferedWriter(new FileWriter(file, false));
            out.write(result);
            out.close();
        } catch (IOException e) {
        }
    }
    
    private HashMap<String, String> getCCRSamples(String location) throws IOException{
    	String[] ccrfiles = Utility.getResourceNames(location);
    	HashMap<String, String> result = new HashMap<String, String>();
    	for (String c : ccrfiles){
    		InputStream is = this.getClass().getClassLoader().getResourceAsStream(location+c);
    		String ccr = Utility.convertStreamToString(is);
    		result.put(c, ccr);
    	}
    	return result;
    }

    /**
     * Test of evaluate method, of class Evaluator.
     */
    @Test
    @Ignore
    public void testEvaluate() {
        try {
            // Read CCR from file
            InputStream isCCR = this.getClass().getClassLoader().getResourceAsStream("ccr_sample.xml");
            ccrXML = Utility.convertStreamToString(isCCR);
            InputStream isJSON = this.getClass().getClassLoader().getResourceAsStream("0001_NQF_Asthma_Assessment.json");
            String json = Utility.convertStreamToString(isJSON);
            System.out.println("MEASURE JSON");
            System.out.println(json);

            Evaluator instance = new Evaluator();
            System.out.println("addMeasure_string");
            instance.addMeasure(json);
            System.out.println("evaluate");
            String expResult = "";
            String result = instance.evaluate(ccrXML);
            System.out.println("RESULT");
            System.out.println(result);
            //assertEquals(expResult, result);
            // TODO review the generated test code and remove the default call to fail.
            fail("The test case is a prototype.");
        } catch (Exception ex) {
            Logger.getLogger(EvaluatorTest.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();;
        }
    }

}