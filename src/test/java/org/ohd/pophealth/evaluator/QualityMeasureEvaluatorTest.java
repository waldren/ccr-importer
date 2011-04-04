package org.ohd.pophealth.evaluator;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.ohd.pophealth.json.clinicalmodel.*;
import org.ohd.pophealth.json.measuremodel.*;
import org.ohd.pophealth.json.measuremodel.PopHealthPatientRecord.MeasureResult;

public class QualityMeasureEvaluatorTest {

	@Before
	public void setUp() throws Exception {
		// Test code array
		ArrayList<CodedValue> codes = new ArrayList<CodedValue>();
		ArrayList<String> values = new ArrayList<String>();
		values.add("123");
		codes.add(new CodedValue("testvocab", "v1", values));

		// Setup fake measure
		measures = new LinkedHashMap<String, Measure>();
		Measure bItem = new Measure();
		bItem.setName("Boolean Item Measure");
		bItem.setItemType(Measure.TYPE.BooleanItem);
		bItem.setCodes(codes);
		measures.put("BooleanItem", bItem);

		Measure dItem = new Measure();
		dItem.setName("Date Item Measure");
		dItem.setItemType(Measure.TYPE.DateItem);
		dItem.setCodes(codes);
		measures.put("DateItem", dItem);

		Measure drItem = new Measure();
		drItem.setName("Date Range Item Measure");
		drItem.setItemType(Measure.TYPE.DateRangeItem);
		drItem.setCodes(codes);
		measures.put("DateRangeItem", drItem);

		Measure vdItem = new Measure();
		vdItem.setName("Value Date Item Measure");
		vdItem.setItemType(Measure.TYPE.ValueDateItem);
		vdItem.setCodes(codes);
		measures.put("ValueDateItem", vdItem);

		// Setup fake Record

		long defStart = 318661505L;
		long defStop = 350283905L;

		record = new Record();
		Allergy a = new Allergy("a");
		a.addDescription(codes);
		a.setOnset(defStart);
		a.setResolution(defStop);
		ArrayList<Allergy> al = new ArrayList<Allergy>();
		record.setAllergies(al);

		Condition c = new Condition("b");
		c.addDescription(codes);
		c.setOnset(defStart);
		c.setResolution(defStop);
		ArrayList<Condition> cl = new ArrayList<Condition>();
		cl.add(c);
		record.setConditions(cl);

		Device d = new Device("c");
		d.addDescription(codes);
		d.setStarted(defStart);
		d.setStopped(defStop);
		ArrayList<Device> dl = new ArrayList<Device>();
		dl.add(d);
		record.setDevices(dl);

		Encounter e = new Encounter("d");
		e.addDescription(codes);
		e.setOccurred(defStart);
		e.setEnded(defStop);
		ArrayList<Encounter> el = new ArrayList<Encounter>();
		el.add(e);
		record.setEncounters(el);

		Goal g = new Goal("e");
		g.addDescription(codes);
		g.setGoalDate(defStart);
		g.setValue("goal_value");

		Order o = new Order("f");
		o.addDescription(codes);
		o.addGoal(g);
		o.setOrderDate(defStart);
		// TODO set order request object
		ArrayList<Order> ol = new ArrayList<Order>();
		ol.add(o);
		record.setOrders(ol);

		Medication m = new Medication("g");
		m.addDescription(codes);
		m.setStarted(defStart);
		m.setStopped(defStop);
		ArrayList<Medication> ml = new ArrayList<Medication>();
		ml.add(m);
		record.setMedications(ml);

		Patient p = new Patient();
		p.setBirthdate(3147010);
		p.setGender("M");
		p.setFirst("FirstName");
		p.setLast("LastName");
		record.setPatient(p);

		// Create QME instance
		qme = new QualityMeasureEvaluator();
		
		// Setup itemNameMap
		itemNameMap = new LinkedHashMap<String, String>();
		itemNameMap.put("bi", "org.ohd.pophealth.json.measuremodel.BooleanItem");
		itemNameMap.put("di", "org.ohd.pophealth.json.measuremodel.DateItem");
		itemNameMap.put("dri", "org.ohd.pophealth.json.measuremodel.DateRangeItem");
		itemNameMap.put("vdi", "org.ohd.pophealth.json.measuremodel.ValueDateItem");
	}

	@After
	public void tearDown() throws Exception {
		measures = null;
		record = null;
	}

	private LinkedHashMap<String, Measure> measures;
	private Record record;
	private QualityMeasureEvaluator qme;
	private LinkedHashMap<String, String> itemNameMap;

	@Test
	@Ignore
	public final void testEvaluateRecordArrayListOfQualityMeasure() {

	}

	@Test
	public void testEvaluateAllergy() {
		ArrayList<QualityMeasure> qList = new ArrayList<QualityMeasure>();
		QualityMeasure qm = new QualityMeasure();
		qm.setId("qmAllergy");
		for (Measure m : measures.values()) {
			m.setCategory(Measure.CAT.Allergy);
			qm.addMeasure(m);
		}
		qList.add(qm);
		String result = qme.evaluate(record, qList);
		PopHealthPatientRecord pop = qme.getLastPop();
		String[] exp = { "bi", "di", "dri" };
		assertTrue(verifyCorrect(pop, exp));
		System.out.println(result);
	}

	@Test
	public void testEvaluateCondition() {
		ArrayList<QualityMeasure> qList = new ArrayList<QualityMeasure>();
		QualityMeasure qm = new QualityMeasure();
		qm.setId("qmCondition");
		for (Measure m : measures.values()) {
			m.setCategory(Measure.CAT.Condition);
			qm.addMeasure(m);
		}
		qList.add(qm);
		String result = qme.evaluate(record, qList);
		PopHealthPatientRecord pop = qme.getLastPop();
		String[] exp = { "bi", "di", "dri" };
		assertTrue(verifyCorrect(pop, exp));
		System.out.println(result);
	}

	private boolean verifyCorrect(PopHealthPatientRecord pop, String[] exp) {
		for (PopHealthPatientRecord.MeasureResult mr : pop.getMeasures()) {
			for (String s : exp) {
				boolean foundIt = false;
				for (String k : mr.map.keySet()) {
					Item item = mr.map.get(k);
					if(item.getClass().getName().equals(itemNameMap.get(s))){
						foundIt = true;
						break;
					}
				}
				if (!foundIt){
					return false;
				}
			}
		}
		// Must of found each String match
		return true;
	}
}
