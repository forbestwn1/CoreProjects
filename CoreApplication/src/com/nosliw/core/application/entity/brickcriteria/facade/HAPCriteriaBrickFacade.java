package com.nosliw.core.application.entity.brickcriteria.facade;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.entity.brickcriteria.HAPCriteriaBrick;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

public class HAPCriteriaBrickFacade extends HAPCriteriaBrick{

	public final static String FACADE = "facade"; 

	//what facade needed
	private String m_facade;
	
	public HAPCriteriaBrickFacade() {
		super(HAPConstantShared.BRICKTYPECRITERIA_TYPE_FACADE);
	}
	
	public String getFacade() {	return this.m_facade;	}
	public void setFacade(String facade) {   this.m_facade = facade;    }
	
	public static HAPCriteriaBrickFacade parse(JSONObject jsonObj, HAPManagerDataRule dataRuleMan) {
		HAPCriteriaBrickFacade out = new HAPCriteriaBrickFacade();
		out.setFacade(jsonObj.getString(FACADE));
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(FACADE, this.m_facade);
	}
}
