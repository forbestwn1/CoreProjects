package com.nosliw.core.application.dynamic;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPPackageBrickInBundle;

public class HAPDynamicExecuteInputItemSingle extends HAPDynamicExecuteInputItem{

	@HAPAttribute
	public final static String BRICKPACKAGE = "brickPackage"; 
	
	private HAPPackageBrickInBundle m_brickPackage;
	
	public HAPDynamicExecuteInputItemSingle() {	}
	
	public HAPDynamicExecuteInputItemSingle(HAPPackageBrickInBundle brickPackage) {
		this.m_brickPackage = brickPackage;
	}
	
	@Override
	public String getType() {   return HAPConstantShared.DYNAMICTASK_REF_TYPE_SINGLE;  }

	public HAPPackageBrickInBundle getBrickPackage() {    return this.m_brickPackage;    }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		
		JSONObject jsonObj = (JSONObject)json;
		
		JSONObject brickPackageJsonObj = jsonObj.optJSONObject(BRICKPACKAGE);
		if(brickPackageJsonObj==null) {
			brickPackageJsonObj = jsonObj;
		}
		this.m_brickPackage = new HAPPackageBrickInBundle();
		this.m_brickPackage.buildObject(brickPackageJsonObj, HAPSerializationFormat.JSON);
		
		return true;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(BRICKPACKAGE, this.m_brickPackage.toStringValue(HAPSerializationFormat.JSON));
	}
}
