package com.nosliw.core.application.brick.task.flow;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;

@HAPEntityWithAttribute
public class HAPTaskFlowNext extends HAPSerializableImp{

	@HAPAttribute
	public static final String DECISION = "decision";

	@HAPAttribute
	public static final String TARGET = "target";

	private HAPTaskFlowDecision m_decision;

	private Set<HAPTaskFlowTarget> m_targets;
	
	public HAPTaskFlowNext() {
		this.m_targets = new HashSet<HAPTaskFlowTarget>();
	}

	public void addTarget(HAPTaskFlowTarget target) {
		if(target.getName()==null) {
			target.setName(HAPConstantShared.NAME_DEFAULT);
		}
		this.m_targets.add(target);
	}
	
	public HAPTaskFlowDecision getDecision() {    return this.m_decision;     }
	public Set<HAPTaskFlowTarget> getTargets(){    return this.m_targets;     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(DECISION, HAPManagerSerialize.getInstance().toStringValue(this.m_decision, HAPSerializationFormat.JSON));
		
		Map<String, String> targetMapStr = new LinkedHashMap<String, String>();
		for(HAPTaskFlowTarget target : this.m_targets) {
			targetMapStr.put(target.getName(), target.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(TARGET, HAPUtilityJson.buildMapJson(targetMapStr));
	}

	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		
		JSONObject jsonObj = (JSONObject)json;
		
		JSONObject decisionJsonObj = jsonObj.optJSONObject(DECISION);
		if(decisionJsonObj!=null) {
			this.m_decision = HAPTaskFlowDecision.parseDecision(decisionJsonObj);
		}

		Object targetObj = jsonObj.opt(TARGET);
		if(targetObj!=null) {
			if(targetObj instanceof JSONObject) {
				HAPTaskFlowTarget target = new HAPTaskFlowTarget();
				target.buildObject(targetObj, HAPSerializationFormat.JSON);
				this.addTarget(target);
			}
			else if(targetObj instanceof JSONArray) {
				JSONArray targetJsonArray = (JSONArray)targetObj;
				for(int i=0; i<targetJsonArray.length(); i++) {
					HAPTaskFlowTarget target = new HAPTaskFlowTarget();
					target.buildObject(targetJsonArray.getJSONObject(i), HAPSerializationFormat.JSON);
					this.addTarget(target);
				}
			}
		}
		
		return true;  
	}
	
}
