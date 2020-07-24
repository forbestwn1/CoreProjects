package com.nosliw.data.core.story;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;

public abstract class HAPConnectionImp extends HAPStoryElementImp implements HAPConnection{

	private HAPConnectionEnd m_end1;
	
	private HAPConnectionEnd m_end2;
	
	public HAPConnectionImp(String type) {
		super(HAPConstant.STORYELEMENT_CATEGARY_CONNECTION, type);
		this.m_end1 = new HAPConnectionEnd(this.getId());
		this.m_end2 = new HAPConnectionEnd(this.getId());
	}

	public HAPConnectionImp() {  
		super(HAPConstant.STORYELEMENT_CATEGARY_CONNECTION);  
		this.m_end1 = new HAPConnectionEnd(this.getId());
		this.m_end2 = new HAPConnectionEnd(this.getId());
	}

	@Override
	public void setId(String id) {
		super.setId(id);
		this.m_end1.setConnectionId(id);
		this.m_end2.setConnectionId(id);
	}
	
	@Override
	public HAPConnectionEnd getEnd1() {  return this.m_end1;  }

	@Override
	public HAPConnectionEnd getEnd2() {  return this.m_end2;  }

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		
		this.m_end1.buildObject(jsonObj.getJSONObject(HAPConnection.END1), HAPSerializationFormat.JSON);
		this.m_end1.setConnectionId(this.getId());
		
		this.m_end2.buildObject(jsonObj.getJSONObject(HAPConnection.END2), HAPSerializationFormat.JSON);
		this.m_end2.setConnectionId(this.getId());
		
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(END1, HAPJsonUtility.buildJson(this.m_end1, HAPSerializationFormat.JSON));
		jsonMap.put(END2, HAPJsonUtility.buildJson(this.m_end2, HAPSerializationFormat.JSON));
	}
	
}
