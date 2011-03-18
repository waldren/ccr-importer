
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.ohd.pophealth.testsupport.Utility;
import org.ohd.pophealth.api.Evaluator;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ohdohd
 */
public class SimpleTest {

    private final static Logger LOG = Logger.getLogger(SimpleTest.class.getName());

    public static void main(String[] args) {
        try {
            LogManager.getLogManager().readConfiguration(SimpleTest.class.getResourceAsStream("logging.properties"));
            SimpleTest st = new SimpleTest();
            try {
                //sst.testEvaluation();
                //st.testCore("ccrsamples/measures/", ccrSampleFiles);
                String pkg = "ccrsamples/generic/";
                String[] files = st.getResourceNames(pkg);
                st.testCore(pkg, files);

            } catch (Exception ex) {
                Logger.getLogger(SimpleTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(SimpleTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(SimpleTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void testEvaluation() throws Exception {
        // Read CCR from file
        InputStream isCCR = this.getClass().getClassLoader().getResourceAsStream("ccr_sample.xml");
        String ccrXML = Utility.convertStreamToString(isCCR);
        InputStream isJSON = this.getClass().getClassLoader().getResourceAsStream("0001_NQF_Asthma_Assessment.json");
        String json = Utility.convertStreamToString(isJSON);
        //System.out.println("MEASURE JSON");
        //System.out.println(json);

        Evaluator instance = new Evaluator();
        instance.addMeasure(json);
        instance.setPreProcess_fixEncounters(true);
        instance.setPreProcess_fixTobacco(true);
        instance.setPreProcess_inferCodes(false);
        String result = instance.evaluate(ccrXML);
    }
    static String[] coreMeasures = {"0013.json", "0024a.json", "0028a.json",
        "0038a.json", "0041.json", "0421a.json", "0055.json", "0056.json",
        "0059.json", "0061.json", "0062.json", "0064a.json", "0575.json"};
    static String[] ccrSampleFiles = {"ccrsample-0013.xml", "ccrsample-0024.xml", "ccrsample-0028.xml",
        "ccrsample-0038.xml", "ccrsample-0041.xml", "ccrsample-0421.xml",
        "ccrsample-diabetes.xml", "ccrsample-blank.xml"};
    ArrayList<String> ccrs = new ArrayList<String>();

    private void testCore(String pkg, String[] files) throws Exception {
        for (String f : files) {
            InputStream isCCR = this.getClass().getClassLoader().getResourceAsStream(pkg + f);
            String ccrXML = Utility.convertStreamToString(isCCR);
            ccrs.add(ccrXML);
        }

        Evaluator instance = new Evaluator();
        for (String m : coreMeasures) {
            String pkgM = "coremeasures/" + m;
            LOG.log(Level.INFO, "Adding {0}", pkgM);
            InputStream mIS = this.getClass().getClassLoader().getResourceAsStream(pkgM);
            String mJSON = Utility.convertStreamToString(mIS);
            instance.addMeasure(mJSON);
            instance.setPreProcess_fixEncounters(true);
            instance.setPreProcess_fixTobacco(true);
            instance.setPreProcess_inferCodes(false);
        }
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("./test/results.json", true));
            out.write("Runtime: " + getNow());
            out.write("\n=======================\n");
            for (int i = 0; i < files.length; i++) {
                LOG.log(Level.INFO, "Running CCR: {0}", files[i]);
                String result = instance.evaluate(ccrs.get(i));
                out.write("CCR:  " + files[i]);
                out.write("\n");
                out.write(result);
                out.write("\n");
            }
            out.write("\n=======================\n");
            out.close();
        } catch (IOException e) {
        }

    }

    private void testNYCCCRSamples() {
    }

    private String[] getResourceNames(String pkg) throws IOException {
        Enumeration<URL> resources = this.getClass().getClassLoader().getResources(pkg);
        String dirName = resources.nextElement().getFile();
        File dir = new File(dirName);
        return dir.list(xmlFilter);
    }

    private String getNow() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
    FilenameFilter xmlFilter = new FilenameFilter() {

        public boolean accept(File dir, String name) {
            return name.endsWith(".xml");
        }
    };
}
