/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ohd.pophealth.ccr.importer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.astm.ccr.ContinuityOfCareRecord;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.ohd.pophealth.json.clinicalmodel.Record;

/**
 *
 * @author ohdohd
 */
public class RecordCreatorTest {
    private ContinuityOfCareRecord ccr;
    public RecordCreatorTest() {
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
    
    @Test
    public void testAvailableVocabulary(){
    	try {
			Vocabulary v = Vocabulary.fromJson(this.getClass().getClassLoader().getResourceAsStream("org/ohd/pophealth/ccr/importer/ccrvocabulary.json"));
			String[] expected = {"gender_female","collected","onset","ended","occurred","rxnorm",
	    			"icd9","resolved","gender_male","ordered","snomed"};
			for (String ve : expected){
				assertTrue(v.isValidTermSet(ve));
			}
    	} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * Test of createRecord method, of class RecordCreator.
     */
    @Test
    public void testCreateRecord() {
        try {
            // Read CCR from file and unmarshal from XML
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("ccrsamples/generic/ccr-sample-0000.xml");
            JAXBContext jc = JAXBContext.newInstance("org.astm.ccr");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            ccr = (ContinuityOfCareRecord) unmarshaller.unmarshal(is);
            if (ccr == null){
                System.err.println("CCR NOT FOUND or BAD");
            }else{
                System.out.println("CCR Found: "+ccr.getCCRDocumentObjectID());
            }
        } catch (JAXBException ex) {
            System.out.println(ex.getMessage());
        }


        System.out.println("createRecord");
        RecordCreator instance = getInstance();
        //Record expResult = null;
        Record result = instance.createRecord(ccr);
            try {
                System.out.println(result.toJson(true));
            } catch (JsonMappingException ex) {
                Logger.getLogger(RecordCreatorTest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JsonGenerationException ex) {
                Logger.getLogger(RecordCreatorTest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(RecordCreatorTest.class.getName()).log(Level.SEVERE, null, ex);
            }

        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    private RecordCreator rc = null;
    private RecordCreator getInstance(){
        if (rc != null){
            return rc;
        }else{
            try {
                Vocabulary v = Vocabulary.fromJson(this.getClass().getClassLoader().getResourceAsStream("org/ohd/pophealth/ccr/importer/ccrvocabulary.json"));
//                System.out.println("Available Terms: ");
//                Iterator<String> i = v.getAvailableTermSets();
//                while(i.hasNext()){
//                    System.out.println("\t"+i.next());
//                }
                rc = new RecordCreator(v);
                return rc;
            } catch (InCompleteVocabularyException ex) {
                Logger.getLogger(RecordCreatorTest.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                return null;
            } catch (JsonMappingException ex) {
                Logger.getLogger(RecordCreatorTest.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                return null;
            } catch (JsonParseException ex) {
                Logger.getLogger(RecordCreatorTest.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                return null;
            } catch (IOException ex) {
                Logger.getLogger(RecordCreatorTest.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                return null;
            }
        }
    }

//    /**
//     * Test of convertISO8601toMSfromEpoch method, of class RecordCreator.
//     */
//    @Test
//    public void testConvertISO8601toMSfromEpoch() {
//        System.out.println("convertISO8601toMSfromEpoch");
//        String iso = "";
//        RecordCreator instance = new RecordCreator();
//        long expResult = 0L;
//        long result = instance.convertISO8601toMSfromEpoch(iso);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of codeMatch method, of class RecordCreator.
//     */
//    @Test
//    public void testCodeMatch() {
//        System.out.println("codeMatch");
//        ArrayList<CodedValue> cvList = null;
//        CodedDescriptionType cdt = null;
//        boolean expResult = false;
//        boolean result = RecordCreator.codeMatch(cvList, cdt);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

}