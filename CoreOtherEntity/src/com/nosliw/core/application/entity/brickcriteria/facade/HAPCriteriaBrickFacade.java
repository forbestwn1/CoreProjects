package com.nosliw.core.application.entity.brickcriteria.facade;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.core.application.dynamic.HAPDynamicDefinitionCriteria;
import com.nosliw.core.application.entity.brickcriteria.HAPCriteriaBrick;
import com.nosliw.core.application.entity.brickfacade.HAPManagerBrickTypeFacade;
import com.nosliw.core.application.entity.brickfacade.HAPFacadeBrickType;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

public class HAPCriteriaBrickFacade extends HAPSerializableImp implements HAPCriteriaBrick{

	public final static String FACADE = "facade"; 

	public final static String RESTRAIN = "restrain"; 

	//what facade needed
	private HAPFacadeBrickType m_facade;
	
	//for the facad, what restrain
	private List<HAPRestrainBrickFacade> m_restrains;

	public HAPCriteriaBrickFacade() {
		this.m_restrains = new ArrayList<HAPRestrainBrickFacade>();
	}
	
	@Override
	public String getCriteriaType() {
		return HAPDynamicDefinitionCriteria.TYPE_FACADE_SIMPLE;
	}
	
	public HAPFacadeBrickType getFacade() {	return this.m_facade;	}
	public void setFacade(HAPFacadeBrickType facade) {   this.m_facade = facade;    }
	
	public List<HAPRestrainBrickFacade> getRestains() {
		return this.m_restrains;
	}
	
	public void parseRestrain(JSONObject jsonObj, HAPManagerDataRule dataRuleMan) {
		this.m_restrains.add(HAPRestrainBrickFacade.parseBrickTypeFacadeRestrain(jsonObj, dataRuleMan));
	}
	
	public static HAPCriteriaBrickFacade parse(JSONObject jsonObj, HAPManagerDataRule dataRuleMan) {
		HAPCriteriaBrickFacade out = new HAPCriteriaBrickFacade();
		
		out.setFacade(HAPManagerBrickTypeFacade.getFacadeByName(jsonObj.getString(FACADE)));
		
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
