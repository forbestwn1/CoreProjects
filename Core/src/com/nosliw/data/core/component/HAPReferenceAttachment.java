package com.nosliw.data.core.component;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPReferenceAttachment extends HAPSerializableImp{

	@HAPAttribute
	public static String TYPE = "type";

	@HAPAttribute
	public static String NAME = "name";

	private String m_type;
	
	private String m_name;
	
	public String getType() {   return this.m_type;  }
	
	public String getName() {   return this.m_name;  }

	public static HAPReferenceAttachment newInstance(String name, String type) {
		HAPReferenceAttachment out = new HAPReferenceAttachment();
		out.m_name = name;
		out.m_type = type;
		return out;
	}

	public static HAPReferenceAttachment newInstance(JSONObject jsonObj, String type) {
		HAPReferenceAttachment out = new HAPReferenceAttachment();
		out.m_type = type;
		out.buildObjectByJson(jsonObj);
		return out;
	}

	public static HAPReferenceAttachment newInstance(JSONObject jsonObj) {
		return newInstance(jsonObj, null);
	}

	private HAPReferenceAttachment() {}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		Object typeObj = jsonObj.opt(TYPE);
		if(typeObj!=null)		this.m_type = (String)typeObj;
		this.m_name = jsonObj.getString(NAME);
		return true;  
	}
	
}
