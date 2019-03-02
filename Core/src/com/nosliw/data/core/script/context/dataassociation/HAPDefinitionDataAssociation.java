package com.nosliw.data.core.script.context.dataassociation;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.script.context.HAPContext;

public class HAPDefinitionDataAssociation extends HAPContext{

	@HAPAttribute
	public static String INFO = "info";

	private HAPInfo m_info;
	
	public HAPDefinitionDataAssociation() {
		this.m_info = new HAPInfoImpSimple();
	}
	
	public HAPInfo getInfo() {   return this.m_info;   }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			super.buildObjectByJson(json);
			JSONObject jsonObj = (JSONObject)json;
			this.m_info.buildObject(jsonObj.opt(INFO), HAPSerializationFormat.JSON);
			return true;  
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(INFO, HAPJsonUtility.buildJson(this.m_info, HAPSerializationFormat.JSON));
	}
	
	public HAPDefinitionDataAssociation cloneDataAssocationGroup() {
		HAPDefinitionDataAssociation out = new HAPDefinitionDataAssociation();
		this.toContext(out);
		out.m_info = this.m_info.cloneInfo();
		return out;
	}
	
	public HAPDefinitionDataAssociation cloneDataAssocationGroupBase() {
		HAPDefinitionDataAssociation out = new HAPDefinitionDataAssociation();
		out.m_info = this.m_info.cloneInfo();
		return out;
	}
}
