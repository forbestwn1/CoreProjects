package com.nosliw.core.application.dynamic;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPIdBrickInBundle;

public class HAPInputDynamicSingle extends HAPInputDynamic{

	@HAPAttribute
	public final static String BRICKID = "brickId"; 
	
	private HAPIdBrickInBundle m_brickId;
	
	public HAPInputDynamicSingle() {	}
	
	public HAPInputDynamicSingle(HAPIdBrickInBundle brickId) {
		this.m_brickId = brickId;
	}
	
	@Override
	public String getType() {   return HAPConstantShared.DYNAMICTASK_REF_TYPE_SINGLE;  }

	public HAPIdBrickInBundle getTaskId() {    return this.m_brickId;     }
	public void setTaskId(HAPIdBrickInBundle taskId) {    this.m_brickId = taskId;      }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		
		JSONObject jsonObj = (JSONObject)json;
		
		JSONObject taskIdJsonObj = jsonObj.getJSONObject(BRICKID);
		this.m_brickId = new HAPIdBrickInBundle();
		this.m_brickId.buildObject(taskIdJsonObj, HAPSerializationFormat.JSON);
		
		return true;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(BRICKID, this.m_brickId.toStringValue(HAPSerializationFormat.JSON));
	}

}
