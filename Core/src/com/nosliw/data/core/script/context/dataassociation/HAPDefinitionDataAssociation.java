package com.nosliw.data.core.script.context.dataassociation;

import java.util.Map;

import com.nosliw.data.core.script.context.HAPContextEntity;

public class HAPDefinitionDataAssociation extends HAPContextEntity{

	public HAPDefinitionDataAssociation() {
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			super.buildObjectByJson(json);
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
	}
	
	public HAPDefinitionDataAssociation cloneDataAssocation() {
		HAPDefinitionDataAssociation out = new HAPDefinitionDataAssociation();
		this.toContextEntity(out);
		return out;
	}
	
	public HAPDefinitionDataAssociation cloneDataAssocationBase() {
		HAPDefinitionDataAssociation out = new HAPDefinitionDataAssociation();
		this.toContextEntityBaseInfo(out);
		return out;
	}
}
