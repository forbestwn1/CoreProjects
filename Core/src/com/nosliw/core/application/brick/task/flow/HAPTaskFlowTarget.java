package com.nosliw.core.application.brick.task.flow;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;

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
	protected boolean buildObjectByJson(Object json){
		this.buildEntityInfoByJson(json);
		
		JSONObject jsonObj = (JSONObject)json;
		this.m_targetActivity = (String)jsonObj.opt(ACTIVITY);
		this.m_adapter = (String)jsonObj.opt(ADAPTER);
		
		return true;  
	}
	
}
