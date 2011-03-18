package org.ohd.pophealth.json.clinicalmodel;

import java.util.ArrayList;

import org.ohd.pophealth.json.measuremodel.CodedValue;

public class Device extends Medication{

	public Device(String id) {
		super(id);
	}

	public Device(String id, ArrayList<CodedValue> type,
			ArrayList<CodedValue> description, long started, long stopped,
			ArrayList<CodedValue> status) {
		super(id, type, description, started, stopped, status);
	}

}
