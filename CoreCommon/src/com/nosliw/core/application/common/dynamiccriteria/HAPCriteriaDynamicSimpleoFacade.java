package com.nosliw.core.application.common.dynamiccriteria;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.core.application.bricktypefacade.HAPEnumFacadeSingleBrickType;
import com.nosliw.core.application.bricktypefacade.HAPFacadeBrickTypeSingle;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

public class HAPCriteriaDynamicSimpleoFacade extends HAPSerializableImp implements HAPCriteriaDynamic{

	public final static String FACADE = "facade"; 

	public final static String RESTRAIN = "restrain"; 

	private HAPFacadeBrickTypeSingle m_facade;
	
	private List<HAPRestrainBrickTypeFacade> m_restrains;

	public HAPCriteriaDynamicSimpleoFacade() {
		this.m_restrains = new ArrayList<HAPRestrainBrickTypeFacade>();
	}
	
	public HAPFacadeBrickTypeSingle getFacade() {	return this.m_facade;	}
	public void setFacade(HAPFacadeBrickTypeSingle facade) {   this.m_facade = facade;    }
	
	public List<HAPRestrainBrickTypeFacade> getRestains() {
		return this.m_restrains;
	}
	
	@Override
	public String getCriteriaType() {
		return HAPCriteriaDynamic.TYPE_FACADE_SIMPLE;
	}
	
	public void parseRestrain(JSONObject jsonObj, HAPManagerDataRule dataRuleMan) {
		this.m_restrains.add(HAPRestrainBrickTypeFacade.parseBrickTypeFacadeRestrain(jsonObj, dataRuleMan));
	}
	
	public static HAPCriteriaDynamicSimpleoFacade parse(JSONObject jsonObj, HAPManagerDataRule dataRuleMan) {
		HAPCriteriaDynamicSimpleoFacade out = new HAPCriteriaDynamicSimpleoFacade();
		
		out.setFacade(HAPEnumFacadeSingleBrickType.getFacadeByName(jsonObj.getString(FACADE)));
		
		Object restrainObj = jsonObj.opt(RESTRAIN);
		if(restrainObj!=null) {
			if(restrainObj instanceof JSONObject) {
				out.parseRestrain((JSONObject)restrainObj, dataRuleMan);
			}
			else if(restrainObj instanceof JSONArray) {
				JSONArray restrainArray = (JSONArray)restrainObj;
				for(int i=0; i<restrainArray.length(); i++) {
					out.parseRestrain(restrainArray.getJSONObject(i), dataRuleMan);
				}
			}
		}
		return out;
	}
	
}
