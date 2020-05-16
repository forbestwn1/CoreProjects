package com.nosliw.data.core.story;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPConnectionImp extends HAPStoryElementImp implements HAPConnection{

	private HAPConnectionEnd m_end1;
	
	private HAPConnectionEnd m_end2;
	
	public HAPConnectionImp(String type) {
		super(type);
	}

	public HAPConnectionImp() {}

	@Override
	public HAPConnectionEnd getEnd1() {  return this.m_end1;  }

	@Override
	public HAPConnectionEnd getEnd2() {  return this.m_end2;  }

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		
		this.m_end1 = new HAPConnectionEnd();
		this.m_end1.buildObject(jsonObj.getJSONObject(HAPConnection.END1), HAPSerializationFormat.JSON);
		this.m_end1.setConnectionId(this.getId());
		
		this.m_end2 = new HAPConnectionEnd();
		this.m_end2.buildObject(jsonObj.getJSONObject(HAPConnection.END2), HAPSerializationFormat.JSON);
		this.m_end2.setConnectionId(this.getId());
		
		return true;  
	}
	
}
