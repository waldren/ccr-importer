package org.ohd.pophealth.ccr.generator;

import java.util.Map;
import java.util.Random;

import org.astm.ccr.ActorReferenceType;
import org.astm.ccr.ActorType;
import org.astm.ccr.Agent;
import org.astm.ccr.AlertType;
import org.astm.ccr.CodeType;
import org.astm.ccr.CodedDescriptionType;
import org.astm.ccr.ContinuityOfCareRecord;
import org.astm.ccr.DateTimeType;
import org.astm.ccr.EncounterType;
import org.astm.ccr.PersonNameType;
import org.astm.ccr.ProblemType;
import org.astm.ccr.ProcedureType;
import org.astm.ccr.ResultType;
import org.astm.ccr.SourceType;
import org.astm.ccr.StructuredProductType;
import org.astm.ccr.StructuredProductType.Product;
import org.astm.ccr.TestType;
import org.ohd.pophealth.testsupport.Utility;

public class CCRGenerator {
	private static final String sourceID = "ohd";

	public ContinuityOfCareRecord getBaseCCR(String ptID, String gender, String birthdate, String firstName, String lastName) {
		ContinuityOfCareRecord ccr = new ContinuityOfCareRecord();
		DateTimeType created = new DateTimeType();
		created.setExactDateTime(Utility.convertMSToISO(System.currentTimeMillis()));
		ccr.setDateTime(created);
		// Set Default Language
		ccr.setLanguage(generateDescription("en-us", "ISO639-1"));
		// Set Version
		ccr.setVersion("V1.0");
		// Set Default source actor
		ccr.setActors(new ContinuityOfCareRecord.Actors());
		ccr.getActors().getActor().add(generateActor(sourceID, null, null, null, null));
		// Set From
		ContinuityOfCareRecord.From from = new ContinuityOfCareRecord.From();
		ActorReferenceType art_from = new ActorReferenceType();
		art_from.setActorID(sourceID);
		from.getActorLink().add(art_from);
		ccr.setFrom(from);
		// Set Patient
		ccr.getActors().getActor().add(generateActor(ptID, gender, birthdate, firstName, lastName));
		ContinuityOfCareRecord.Patient pt = new ContinuityOfCareRecord.Patient();
		pt.setActorID(ptID);
		ccr.getPatient().add(pt);
		return ccr;
	}
	
	public ActorType generateActor(String id, String gender, String birthdate, String firstName, String lastName){
		ActorType at = new ActorType();
		at.setActorObjectID(id);
		if (birthdate != null && !"".equalsIgnoreCase(birthdate)){
			DateTimeType dob = new DateTimeType();
			dob.setExactDateTime(birthdate);
			ActorType.Person p = new ActorType.Person();
			p.setDateOfBirth(dob);
			at.setPerson(p);
		}
		if (gender != null && !"".equalsIgnoreCase(gender)){
			if (at.getPerson() == null){
				at.setPerson(new ActorType.Person());
			}
			at.getPerson().setGender(generateDescription(gender, "HL7V3.0"));
		}
		if (firstName != null || lastName != null){
			if (at.getPerson() == null){
				at.setPerson(new ActorType.Person());
			}
			ActorType.Person.Name n = new ActorType.Person.Name();
			PersonNameType pnt = new PersonNameType();
			pnt.getFamily().add(lastName);
			pnt.getGiven().add(firstName);
			n.setBirthName(pnt);
			at.getPerson().setName(n);
		}
		// Set Source
		at.getSource().add(generateSource());
		return at;
	}
	
	public ProcedureType generateProcedure(String id, String code, String codesystem){
		ProcedureType pt = new ProcedureType();
		pt.setCCRDataObjectID("pro-"+id);
		pt.setDescription(generateDescription(code, codesystem));
		pt.getDateTime().add(generateDate());
		pt.getSource().add(generateSource());
		return pt;
	}
	
	public ProblemType generateProblem(String id, String code, String codesystem){
		ProblemType pt = new ProblemType();
		pt.setCCRDataObjectID(id);
		pt.setDescription(generateDescription(code, codesystem));
		pt.getDateTime().add(generateDate());
		pt.getSource().add(generateSource());
		return pt;
	}
	
	public AlertType generateAllergy(String id, String code, String codesystem){
		AlertType at = generateBaseAlert(id);
		at.setDescription(generateDescription(code, codesystem));
		return at;
	}
	
	public AlertType generateAllergy(String id, StructuredProductType spt){
		AlertType at = generateBaseAlert(id);
		Agent ag = new Agent();
		Agent.Products ps = new Agent.Products();
		ps.getProduct().add(spt);
		ag.setProducts(ps);
		at.getAgent().add(ag);
		return at;
	}
	
	private AlertType generateBaseAlert(String id){
		AlertType at = new AlertType();
		at.setCCRDataObjectID(id);
		at.getDateTime().add(generateDate());
		at.getSource().add(generateSource());
		return at;
	}
	
	public StructuredProductType generateMedication(String idPrefix, String id, String code, String codesystem){
		StructuredProductType spt = new StructuredProductType();
		spt.setCCRDataObjectID(idPrefix+"-"+id);
		spt.getDateTime().add(generateDate());
		spt.getSource().add(generateSource());
		Product p = new StructuredProductType.Product();
		p.setProductName(generateDescription(code, codesystem));
		spt.getProduct().add(p);
		return spt;
	}
	
	public EncounterType generateEncounter(String id, String code, String codesystem){
		EncounterType et = new EncounterType();
		et.setCCRDataObjectID("enc-"+id);
		et.getDateTime().add(generateDate());
		et.getSource().add(generateSource());
		et.setDescription(generateDescription(code, codesystem));
		return et;
	}
	
	public ResultType generateResult(String idPrefix, String id, Map<String, String> codes){
		ResultType rt = new ResultType();
		rt.setCCRDataObjectID(idPrefix+"-"+id);
		DateTimeType date = generateDate();
		rt.getDateTime().add(date);
		rt.getSource().add(generateSource());
		for (String c : codes.keySet()){
			rt.getTest().add(generateTest(id, c, codes.get(c), date));
		}
		return rt;
	}
	
	public TestType generateTest(String id, String code, String codesystem, DateTimeType date){
		TestType tt = new TestType();
		tt.setCCRDataObjectID("tst-"+id);
		tt.getDateTime().add(date);
		tt.getSource().add(generateSource());
		tt.setDescription(generateDescription(code, codesystem));
		return tt;
	}

	private SourceType generateSource() {
		SourceType st = new SourceType();
		ActorReferenceType art = new ActorReferenceType();
		art.setActorID(sourceID);
		st.getActor().add(art);
		return st;
	}

	private DateTimeType generateDate() {
		DateTimeType dt = new DateTimeType();
		dt.setExactDateTime(Utility.convertMSToISO(getRandomTime()));
		return dt;
	}
	Random rand = new Random();
	int maxBack = 1000*60*60*24*365*3;
	private long getRandomTime(){
		int backtrack = rand.nextInt(maxBack);
		return System.currentTimeMillis()-backtrack;
	}

	private CodedDescriptionType generateDescription(String code,
			String codesystem) {
		CodeType ct = new CodeType();
		ct.setCodingSystem(codesystem);
		ct.setValue(code);
		CodedDescriptionType cdt = new CodedDescriptionType();
		cdt.getCode().add(ct);
		return cdt;
	}
}
