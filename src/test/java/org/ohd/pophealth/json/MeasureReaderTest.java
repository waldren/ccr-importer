/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.ohd.pophealth.json;

import java.util.ArrayList;
import java.util.Iterator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.JsonNode;
import java.io.IOException;
import org.ohd.pophealth.testsupport.Utility;
import org.ohd.pophealth.json.measuremodel.Measure.CAT;
import org.ohd.pophealth.json.measuremodel.Measure.TYPE;
import org.ohd.pophealth.json.measuremodel.QualityMeasure;
import org.ohd.pophealth.json.measuremodel.CodedValue;
import org.ohd.pophealth.json.measuremodel.Measure;
import java.io.InputStream;
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
public class MeasureReaderTest {

    public MeasureReaderTest() {
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
     * Test of extractMeasures method, of class MeasureReader.
     */
    @Test
    public void testExtractMeasures() throws Exception {
        System.out.println("extractMeasures");
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("extractItemsTest.json");

        String json = Utility.convertStreamToString(is);

       // ArrayList expResult = null;
        //System.out.println("JSON:   "+json);
        QualityMeasure result = MeasureReader.extractQualityMeasure(json);
        System.out.println("QualityMeasure:");
        System.out.println("\tid: "+result.getId());
        System.out.println("\tName: "+result.getName());
        for (Measure m : result.getMeasures()){
            System.out.println("MEASURE:");
            System.out.println("\tName: "+m.getName());
            System.out.println("\tDesc: "+m.getDescription());
            System.out.println("\tType: "+m.getItemType());
            System.out.println("\tCat: "+Measure.getCAT(m.getCategory()));
            System.out.println("\tCode: ");
            for (CodedValue cv : m.getCodes()){
                System.out.print("\t\t"+cv.getCodingSystem());
                for(String s : cv.getValues()){
                    System.out.print(s+" ");
                }
                System.out.println();
            }
        }
        //assertEquals(expResult, result);
        // TODO Need to visually inspect this test result.
        
    }

    @Test
    public void testExtractItems() throws Exception{
        System.out.println("extractItems");
        JsonNode root = getTestMeasure();
        JsonNode mNode = root.path("measure");
        Iterator<String> mDefs = mNode.getFieldNames();
        while (mDefs.hasNext()) {
            String subm = mDefs.next();
//            System.out.print(subm);
//            System.out.print(" -- ");
            JsonNode subM = mNode.path(subm);
            String itemsType = (subM.path("type").getTextValue());
            TYPE extractItems = MeasureReader.extractItems(itemsType, subM.path("items"));
//            System.out.print(extractItems);
//            System.out.println();
            assertEquals(extractItems.toString(), subm);
        }

    }

    @Test
    public void testExtractCategories()throws Exception{
        System.out.println("extractCategories");

        JsonNode root = getTestMeasure();
        JsonNode mNode = root.path("measure");
        Iterator<String> mDefs = mNode.getFieldNames();
        ArrayList<String> expected = new ArrayList<String>();
        expected.add("Condition");expected.add("PhysicalExam");expected.add("Allergy"); expected.add("Condition");
        int i =0;
        while (mDefs.hasNext()) {
            String subm = mDefs.next();
            JsonNode subM = mNode.path(subm);
            CAT extractCategories = MeasureReader.extractCategories(subM);
            //System.out.println(extractCategories);
            assertEquals(expected.get(i), extractCategories.toString());
            i++;
        }

    }

    private JsonNode getTestMeasure() throws Exception{
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("extractItemsTest.json");
        //InputStream is = this.getClass().getClassLoader().getResourceAsStream("0001_NQF_Asthma_Assessment.json");
        String json = Utility.convertStreamToString(is);
        return om.readValue(json, JsonNode.class);
    }static ObjectMapper om = new ObjectMapper();
}
