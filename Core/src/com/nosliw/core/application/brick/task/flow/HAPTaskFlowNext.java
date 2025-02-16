package com.nosliw.core.application.brick.task.flow;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPTaskFlowNext extends HAPSerializableImp{

	@HAPAttribute
	public static final String DECISION = "decision";

	@HAPAttribute
	public static final String TARGET = "target";

	private HAPTaskFlowDecision m_decision;

	private Map<String, HAPTaskFlowTarget> m_target;
	
	public HAPTaskFlowNext() {
		this.m_target = new LinkedHashMap<String, HAPTaskFlowTarget>();
	}

	public void addTarget(HAPTaskFlowTarget target) {
		this.m_target.put(target.getName(), target);
	}
	
	public HAPTaskFlowDecision getDecision() {    return this.m_decision;     }
	public Set<HAPTaskFlowTarget> getTargets(){    return new HashSet(this.m_target.values());     }
	
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
