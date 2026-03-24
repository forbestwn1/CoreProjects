package com.nosliw.core.application.brick.task.flow;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;

@HAPEntityWithAttribute
public class HAPTaskFlowTarget extends HAPEntityInfoImp{

	@HAPAttribute
	public static final String ACTIVITY = "activity";

	@HAPAttribute
	public static final String ADAPTER = "adapter";
	
	private String m_targetActivity;
	
	private String m_adapter;

	public HAPTaskFlowTarget() {}

	
	public HAPTaskFlowTarget(String activity, String adapter) {
		this.m_targetActivity = activity;
		this.m_adapter = adapter;
	}
	
	public String getActivity() {	return this.m_targetActivity;	}
	
	public String getAdapter() {	return this.m_adapter;	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ADAPTER, this.m_adapter);
		jsonMap.put(ACTIVITY, this.m_targetActivity);
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		this.buildEntityInfoByJson(json);
		
		JSONObject jsonObj = (JSONObject)json;
		this.m_targetActivity = (String)jsonObj.opt(ACTIVITY);
		this.m_adapter = (String)jsonObj.opt(ADAPTER);
		
		return true;  
	}
}
