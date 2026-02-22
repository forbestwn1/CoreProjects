package com.nosliw.core.application.dynamic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPPackageBrickInBundle;

public class HAPDynamicExecuteInputItemMultiple extends HAPDynamicExecuteInputItem{

	@HAPAttribute
	public final static String BRICKPACKAGES = "brickPackages"; 
	
	private List<HAPPackageBrickInBundle> m_brickPackages;
	
	public HAPDynamicExecuteInputItemMultiple() {
		this.m_brickPackages = new ArrayList<HAPPackageBrickInBundle>();
	}
	
	@Override
	public String getType() {   return HAPConstantShared.DYNAMICTASK_REF_TYPE_MULTIPLE;  }

	public List<HAPPackageBrickInBundle> getBrickPackages() {    return this.m_brickPackages;     }
	public void addBrickPackage(HAPPackageBrickInBundle brickPackage) {    this.m_brickPackages.add(brickPackage);      }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		
		JSONArray brickIdJsonArray = jsonObj.getJSONArray(BRICKPACKAGES);
		for(int i=0; i<brickIdJsonArray.length(); i++) {
			HAPPackageBrickInBundle brickPackage = new HAPPackageBrickInBundle();
			brickPackage.buildObject(brickIdJsonArray.getJSONObject(i), HAPSerializationFormat.JSON);
			this.addBrickPackage(brickPackage);
		}
		return true;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(BRICKPACKAGES, HAPManagerSerialize.getInstance().toStringValue(m_brickPackages, HAPSerializationFormat.JSON));
	}

}
