package com.nosliw.data.core.activity.plugin.temp;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.activity.HAPDefinitionActivity;
import com.nosliw.data.core.process1.HAPDefinitionSequenceFlow;

public class HAPStartActivityDefinition extends HAPDefinitionActivity{

	@HAPAttribute
	public static String FLOW = "flow";
	
	private HAPDefinitionSequenceFlow m_flow;
	
	public HAPStartActivityDefinition(String type) {
		super(type);
	}

	@Override
	public String getActivityType() {		return HAPConstantShared.ACTIVITY_TYPE_START;	}
	
	public HAPDefinitionSequenceFlow getFlow() {   return this.m_flow;   }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			super.buildObjectByJson(json);
			JSONObject jsonObj = (JSONObject)json;
			this.m_flow = new HAPDefinitionSequenceFlow();
			this.m_flow.buildObject(jsonObj.optJSONObject(FLOW), HAPSerializationFormat.JSON);
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
		if(this.m_flow!=null)		jsonMap.put(FLOW, this.m_flow.toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	public HAPDefinitionActivity cloneActivityDefinition() {
		HAPStartActivityDefinition out = new HAPStartActivityDefinition(this.getActivityType());
		this.cloneToActivityDefinition(out);
		out.m_flow = this.m_flow.cloneSequenceFlowDefinition();
		return out;
	}
}
