package com.nosliw.data.core.dataassociation.none;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.dataassociation.HAPDefinitionDataAssociation;

public class HAPDefinitionDataAssociationNone extends HAPEntityInfoWritableImp implements HAPDefinitionDataAssociation{

	public HAPDefinitionDataAssociationNone() {
	}
 
	@Override
	public String getType() {  return HAPConstantShared.DATAASSOCIATION_TYPE_NONE;  }

	@Override
	public void updateInputVarName(HAPUpdateName updateName) {}

	@Override
	public void updateOutputVarName(HAPUpdateName updateName) {}

 	@Override
	public HAPDefinitionDataAssociationNone cloneDataAssocation() {
		HAPDefinitionDataAssociationNone out = new HAPDefinitionDataAssociationNone();
		this.cloneToEntityInfo(out);
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getType());
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
