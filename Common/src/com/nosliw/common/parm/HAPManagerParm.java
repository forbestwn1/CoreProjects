package com.nosliw.common.parm;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPManagerParm {

	static private HAPManagerParm m_instance;
	
	private HAPManagerParm() {}
	
	public static HAPManagerParm getInstance() {
		if(m_instance==null) {
			m_instance = new HAPManagerParm();
		}
		return m_instance;
	}
	
	public HAPParms parseParms(JSONObject parmJsonObj) {
		HAPParms out = new HAPParms();
		out.buildObject(parmJsonObj, HAPSerializationFormat.JSON);
		return out;
	}

}
