package com.nosliw.data.core.component;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionWrapperTask;

public class HAPEmbededEntity extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String REFERENCE = "reference";

	private HAPDefinitionWrapperTask<HAPReferenceAttachment> m_entity;


	public void buildEmbededEntityByJson(JSONObject jsonObj, String type) {
		this.buildEntityInfoByJson(jsonObj);
		
		this.m_entity = new HAPDefinitionWrapperTask<HAPReferenceAttachment>();
		this.m_entity.setTaskDefinition(HAPReferenceAttachment.newInstance(jsonObj.getJSONObject(REFERENCE), type));
		this.m_entity.buildMapping(jsonObj);
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEmbededEntityByJson(jsonObj, null);
		return true;  
	}

}
