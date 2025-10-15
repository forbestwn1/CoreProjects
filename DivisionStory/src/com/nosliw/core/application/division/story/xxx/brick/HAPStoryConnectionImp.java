package com.nosliw.core.application.division.story.xxx.brick;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.story.brick.HAPStoryConnection;
import com.nosliw.core.application.division.story.brick.HAPStoryConnectionEnd;

public abstract class HAPStoryConnectionImp extends HAPStoryElementImp implements HAPStoryConnection{

	private HAPStoryConnectionEnd m_end1;
	
	private HAPStoryConnectionEnd m_end2;
	
	public HAPStoryConnectionImp(String type) {
		super(HAPConstantShared.STORYELEMENT_CATEGARY_CONNECTION, type);
		this.m_end1 = new HAPStoryConnectionEnd();
		this.m_end2 = new HAPStoryConnectionEnd();
	}

	public HAPStoryConnectionImp() {
		this(null);
	}

	@Override
	public void setId(String id) {
		super.setId(id);
		this.m_end1.setConnectionId(id);
		this.m_end2.setConnectionId(id);
	}
	
	@Override
	public HAPStoryConnectionEnd getEnd1() {  return this.m_end1;  }

	@Override
	public HAPStoryConnectionEnd getEnd2() {  return this.m_end2;  }

	public void cloneTo(HAPStoryConnectionImp connection) {
		super.cloneTo(connection);
		if(this.m_end1!=null)  connection.m_end1 = this.m_end1.cloneConnectionEnd();
		if(this.m_end2!=null)  connection.m_end2 = this.m_end2.cloneConnectionEnd();
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		
		this.m_end1.buildObject(jsonObj.getJSONObject(HAPStoryConnection.END1), HAPSerializationFormat.JSON);
		this.m_end1.setConnectionId(this.getId());
		
		this.m_end2.buildObject(jsonObj.getJSONObject(HAPStoryConnection.END2), HAPSerializationFormat.JSON);
		this.m_end2.setConnectionId(this.getId());
		
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(END1, HAPUtilityJson.buildJson(this.m_end1, HAPSerializationFormat.JSON));
		jsonMap.put(END2, HAPUtilityJson.buildJson(this.m_end2, HAPSerializationFormat.JSON));
	}
	
}
