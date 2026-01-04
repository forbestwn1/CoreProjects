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
import com.nosliw.core.application.HAPIdBrickInBundle;

public class HAPDynamicExecuteInputItemMultiple extends HAPDynamicExecuteInputItem{

	@HAPAttribute
	public final static String BRICKIDS = "brickIds"; 
	
	private List<HAPIdBrickInBundle> m_brickIds;
	
	public HAPDynamicExecuteInputItemMultiple() {
		this.m_brickIds = new ArrayList<HAPIdBrickInBundle>();
	}
	
	@Override
	public String getType() {   return HAPConstantShared.DYNAMICTASK_REF_TYPE_MULTIPLE;  }

	public List<HAPIdBrickInBundle> getBrickIds() {    return this.m_brickIds;     }
	public void addBrickId(HAPIdBrickInBundle brickId) {    this.m_brickIds.add(brickId);      }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		
		JSONArray brickIdJsonArray = jsonObj.getJSONArray(BRICKIDS);
		for(int i=0; i<brickIdJsonArray.length(); i++) {
			HAPIdBrickInBundle brickId = new HAPIdBrickInBundle();
			brickId.buildObject(brickIdJsonArray.getJSONObject(i), HAPSerializationFormat.JSON);
			this.addBrickId(brickId);
		}
		return true;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(BRICKIDS, HAPManagerSerialize.getInstance().toStringValue(m_brickIds, HAPSerializationFormat.JSON));
	}

}
