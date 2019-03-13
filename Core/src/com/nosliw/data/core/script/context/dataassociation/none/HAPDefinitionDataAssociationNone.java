package com.nosliw.data.core.script.context.dataassociation.none;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionDataAssociation;

public class HAPDefinitionDataAssociationNone extends HAPEntityInfoWritableImp implements HAPDefinitionDataAssociation{

	public HAPDefinitionDataAssociationNone() {
	}
 
	@Override
	public String getType() {  return HAPConstant.DATAASSOCIATION_TYPE_NONE;  }

 	@Override
	public HAPDefinitionDataAssociationNone cloneDataAssocation() {
		HAPDefinitionDataAssociationNone out = new HAPDefinitionDataAssociationNone();
		this.cloneToEntityInfo(out);
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject daJsonObj = (JSONObject)json;
			this.buildEntityInfoByJson(daJsonObj);
			return true;  
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
}
