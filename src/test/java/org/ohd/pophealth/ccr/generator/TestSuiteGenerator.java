package org.ohd.pophealth.ccr.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.astm.ccr.AlertType;
import org.astm.ccr.ContinuityOfCareRecord;
import org.astm.ccr.ContinuityOfCareRecord.Body.Problems;
import org.astm.ccr.EncounterType;
import org.astm.ccr.ProblemType;
import org.astm.ccr.ProcedureType;
import org.astm.ccr.StructuredProductType;
import org.ohd.pophealth.json.MeasureReader;
import org.ohd.pophealth.json.measuremodel.Measure;
import org.ohd.pophealth.json.measuremodel.QualityMeasure;
import org.ohd.pophealth.testsupport.Utility;

public class TestSuiteGenerator {
	private static Logger log = Logger.getLogger(TestSuiteGenerator.class
			.getName());
	private ArrayList<QualityMeasure> qms = new ArrayList<QualityMeasure>();
	private CCRGenerator gen = new CCRGenerator();
	private File outputDir;
	private File inputDir;

	public TestSuiteGenerator(File inputDir, File outputDir) {
		if (!inputDir.isDirectory()) {
			log.log(Level.SEVERE, "InputDir is not a Directory");
			System.exit(1);
		}
		if (!outputDir.isDirectory()) {
			log.log(Level.SEVERE, "OutputDir is not a Directory");
			System.exit(1);
		}
		this.inputDir = inputDir;
		this.outputDir = outputDir;
		try {
			generateQualityMeasures();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public TestSuiteGenerator(String inputDirFileName, String outputDirFileName) {
		this(new File(inputDirFileName), new File(outputDirFileName));
	}

	public void generateTestCcrFiles() {
		for (QualityMeasure qm : this.qms) {
			log.log(Level.INFO, "Processing:  ["+qm.getId()+"] "+qm.getName());
			ContinuityOfCareRecord ccr = generateSampleCCR(qm);
			String fileName = "ccrsample-" + qm.getId() + ".xml";
			Utility.writeCCR(ccr, outputDir, fileName, true);
		}
	}

	private ContinuityOfCareRecord generateSampleCCR(QualityMeasure qm) {
		ContinuityOfCareRecord ccr = gen.getBaseCCR("patient-id", "F", "1967-01-13T00:00:00.000Z", "Jane", "Doe");
		ccr.setBody(new ContinuityOfCareRecord.Body());
		ccr.setCCRDocumentObjectID("ccrsample-" + qm.getId() + "-"
				+ System.currentTimeMillis());
		for (Measure m : qm.getMeasures()) {
			log.log(Level.INFO, "Processing Measure: "+m.getName());
			GenData gd = getGenerationData(m);
			switch (m.getCategory()) {
			case Condition:
				if (ccr.getBody().getProblems() == null) {
					ccr.getBody().setProblems(new Problems());
				}
				ProblemType pt = gen.generateProblem(gd.id, gd.code,
						gd.codesystem);
				ccr.getBody().getProblems().getProblem().add(pt);
				break;
			case Characteristic:
				if (ccr.getBody().getProblems() == null) {
					ccr.getBody().setProblems(new Problems());
				}
				ProblemType cr = gen.generateProblem(gd.id, gd.code,
						gd.codesystem);
				ccr.getBody().getProblems().getProblem().add(cr);
				break;
			case Encounter:
				if (ccr.getBody().getEncounters() == null){
					ccr.getBody().setEncounters(new ContinuityOfCareRecord.Body.Encounters());
				}
				EncounterType et = gen.generateEncounter(gd.id, gd.code, gd.codesystem);
				ccr.getBody().getEncounters().getEncounter().add(et);
				break;
			case Result:
//				if (ccr.getBody().getResults() == null){
//					ccr.getBody().setResults(new ContinuityOfCareRecord.Body.Results());
//				}
				break;
			case VitalSign:
//				if (ccr.getBody().getVitalSigns() == null){
//					ccr.getBody().setVitalSigns(new ContinuityOfCareRecord.Body.VitalSigns());
//				}
				break;
			case Medication:
				if (ccr.getBody().getMedications() == null) {
					ccr.getBody().setMedications(
							new ContinuityOfCareRecord.Body.Medications());
				}
				StructuredProductType sptMd = gen.generateMedication("med",
						gd.id, gd.code, gd.codesystem);
				ccr.getBody().getMedications().getMedication().add(sptMd);
				break;
			case Immunization:
				if (ccr.getBody().getMedications() == null) {
					ccr.getBody().setMedications(
							new ContinuityOfCareRecord.Body.Medications());
				}
				StructuredProductType sptIm = gen.generateMedication("med",
						gd.id, gd.code, gd.codesystem);
				ccr.getBody().getMedications().getMedication().add(sptIm);
				break;
			case PhysicalExam:
				// Create them all as Procedures
				if (ccr.getBody().getProcedures() == null){
					ccr.getBody().setProcedures(new ContinuityOfCareRecord.Body.Procedures());
				}
				ProcedureType pe = gen.generateProcedure(gd.id, gd.code, gd.codesystem);
				ccr.getBody().getProcedures().getProcedure().add(pe);
				break;
			case Communication:
				if (ccr.getBody().getEncounters() == null){
					ccr.getBody().setEncounters(new ContinuityOfCareRecord.Body.Encounters());
				}
				EncounterType etC = gen.generateEncounter(gd.id, gd.code, gd.codesystem);
				ccr.getBody().getEncounters().getEncounter().add(etC);
				break;
			case Allergy:
				if (ccr.getBody().getAlerts() == null){
					ccr.getBody().setAlerts(new ContinuityOfCareRecord.Body.Alerts());
				}
				StructuredProductType spt = gen.generateMedication("med", gd.id+"-02", gd.code, gd.codesystem);
				AlertType at = gen.generateAllergy(gd.id, spt);
				ccr.getBody().getAlerts().getAlert().add(at);
				break;
			case Procedure:
				if (ccr.getBody().getProcedures() == null){
					ccr.getBody().setProcedures(new ContinuityOfCareRecord.Body.Procedures());
				}
				ProcedureType proc = gen.generateProcedure(gd.id, gd.code, gd.codesystem);
				ccr.getBody().getProcedures().getProcedure().add(proc);
				break;
			case Order:
				break;
			case Goal:
				break;
			default:
				log.log(Level.WARNING,
						"Found Unknown or Unsupported Category Type [{0}]",
						m.getCategory());
			}
		}
		return ccr;
	}

	private GenData getGenerationData(Measure m) {
		String id = m.getName();
		if (m.getCodes().isEmpty()){
			// Some measures do not have a code - which is ridiculous
			return new GenData(id, "none", "none");
		}
		// Grab first code add Random selection in future
		String cs = m.getCodes().get(0).getCodingSystem();
		String code = m.getCodes().get(0).getValues().get(0);
		return new GenData(id, code, cs);
	}

	public void generateQualityMeasures() throws Exception {
		for (File f : inputDir.listFiles(jsonFilter)) {
			FileInputStream fis = new FileInputStream(f);
			String json = Utility.convertStreamToString(fis);
			qms.add(MeasureReader.extractQualityMeasure(json));
		}
	}

	// This filter only returns .json files
	FilenameFilter jsonFilter = new FilenameFilter() {
		public boolean accept(File dir, String name) {
			return name.endsWith(".json");
		}
	};

	class GenData {
		String id, code, codesystem;

		protected GenData(String id, String code, String codesystem) {
			this.id = id;
			this.code = code;
			this.codesystem = codesystem;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String inputDir = "/WindowsHD/OHD/popHealth/CleanCode/measures/tmp";
		String outputDir = "/Users/swaldren/gitrepo/ccr-importer/src/test/resources/gen";
		TestSuiteGenerator tsg = new TestSuiteGenerator(inputDir, outputDir);
		tsg.generateTestCcrFiles();
	}

}
