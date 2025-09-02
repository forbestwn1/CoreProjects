package com.nosliw.core.application.common.dynamiccriteria;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.core.application.bricktypefacade.HAPEnumFacadeSingleBrickType;
import com.nosliw.core.application.bricktypefacade.HAPFacadeBrickTypeSingle;

public class HAPCriteriaDynamicSimpleoFacade extends HAPSerializableImp implements HAPCriteriaDynamic{

	public final static String FACADE = "facade"; 

	public final static String RESTRAIN = "restrain"; 

	private HAPFacadeBrickTypeSingle m_facade;
	
	private List<HAPRestrainBrickTypeFacade> m_restrains;

	public HAPCriteriaDynamicSimpleoFacade() {
		this.m_restrains = new ArrayList<HAPRestrainBrickTypeFacade>();
	}
	
	public HAPFacadeBrickTypeSingle getFacade() {
		return this.m_facade;
	}
	
	public List<HAPRestrainBrickTypeFacade> getRestains() {
		return this.m_restrains;
	}
	
	@Override
	public String getCriteriaType() {
		return HAPCriteriaDynamic.TYPE_FACADE_SIMPLE;
	}
	
	private void parseRestrain(JSONObject jsonObj) {
		this.m_restrains.add(HAPRestrainBrickTypeFacade.parseBrickTypeFacadeRestrain(jsonObj));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		
		this.m_facade = HAPEnumFacadeSingleBrickType.getFacadeByName(jsonObj.getString(FACADE));
		
		Object restrainObj = jsonObj.opt(RESTRAIN);
		if(restrainObj!=null) {
			if(restrainObj instanceof JSONObject) {
				this.parseRestrain((JSONObject)restrainObj);
			}
			else if(restrainObj instanceof JSONArray) {
				JSONArray restrainArray = (JSONArray)restrainObj;
				for(int i=0; i<restrainArray.length(); i++) {
					this.parseRestrain(restrainArray.getJSONObject(i));
				}
			}
		}
		return true;  
	}

}
