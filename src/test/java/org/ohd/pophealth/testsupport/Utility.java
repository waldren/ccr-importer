/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ohd.pophealth.testsupport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.astm.ccr.ContinuityOfCareRecord;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 *
 * @author ohdohd
 */
public class Utility {

   public static String convertStreamToString(InputStream is)
            throws IOException {
        /*
         * To convert the InputStream to String we use the
         * Reader.read(char[] buffer) method. We iterate until the
         * Reader return -1 which means there's no more data to
         * read. We use the StringWriter class to produce the string.
         */
        if (is != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return writer.toString();
        } else {
            return "";
        }
    }

   public static ContinuityOfCareRecord getCCR(InputStream is){
       try {
                JAXBContext jc = JAXBContext.newInstance("org.astm.ccr");
                Unmarshaller unmarshaller = jc.createUnmarshaller();
                return (ContinuityOfCareRecord) unmarshaller.unmarshal(is);
            } catch (JAXBException ex) {
                Logger.getLogger(Utility.class.getName()).log(Level.WARNING, ex.getLocalizedMessage());
                return null;
            }
   }

    public static void writeCCR(ContinuityOfCareRecord result, File dir, String fileName, boolean prettyPrint) {
        try {
            JAXBContext jc = JAXBContext.newInstance("org.astm.ccr");
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, prettyPrint);
            File f = new File(dir, fileName);
            //f.createNewFile();
            marshaller.marshal(result, f);
        }
        catch (Exception ex) {
            Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void writeCCR(ContinuityOfCareRecord result, String directory, String fileName, boolean prettyPrint){
    	File dir = new File (directory);
    	if (dir.isDirectory()){
    		writeCCR(result, dir, fileName, prettyPrint);
    	}else{
    		Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, "Not a directory: "+directory);
    	}
    }
    
    private static DateTimeFormatter fmt = ISODateTimeFormat.dateTimeParser(); // Formatter to parse ISO8601 Date Strings
    private static DateTimeFormatter prt = ISODateTimeFormat.dateTime(); // Formatter to parse ISO8601 Date Strings
    public static String convertDateToISO(Date d){
    	DateTime dt = new DateTime(d);
    	return prt.print(dt);
    }
    public static Date convertISOToDate(String iso){
    	DateTime dt = fmt.parseDateTime(iso);
    	return dt.toDate();
    }
    public static String convertMSToISO(long ms){
    	DateTime dt = new DateTime(ms);
    	return prt.print(dt);
    }

    public static String getNow() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
    
    public static String[] getResourceNames(String pkg) throws IOException {
        Enumeration<URL> resources = Utility.class.getClassLoader().getResources(pkg);
        String dirName = resources.nextElement().getFile();
        File dir = new File(dirName);
        return dir.list(xmlFilter);
    }
    public static FilenameFilter xmlFilter = new FilenameFilter() {

        public boolean accept(File dir, String name) {
            return name.endsWith(".xml");
        }
    };
    public static FilenameFilter jsonFilter = new FilenameFilter() {

        public boolean accept(File dir, String name) {
            return name.endsWith(".json");
        }
    };
}
