package com.nosliw.core.application.entity.brickcriteria.facade;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.entity.brickcriteria.HAPCriteriaBrick;
import com.nosliw.core.application.entity.brickfacade.HAPFacadeBrickType;
import com.nosliw.core.application.entity.brickfacade.HAPManagerBrickTypeFacade;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

public class HAPCriteriaBrickFacade extends HAPCriteriaBrick{

	public final static String FACADE = "facade"; 

	//what facade needed
	private HAPFacadeBrickType m_facade;
	
	public HAPCriteriaBrickFacade() {
		super(HAPConstantShared.BRICKTYPECRITERIA_TYPE_FACADE);
	}
	
	public HAPFacadeBrickType getFacade() {	return this.m_facade;	}
	public void setFacade(HAPFacadeBrickType facade) {   this.m_facade = facade;    }
	
	public static HAPCriteriaBrickFacade parse(JSONObject jsonObj, HAPManagerDataRule dataRuleMan) {
		HAPCriteriaBrickFacade out = new HAPCriteriaBrickFacade();
		out.setFacade(HAPManagerBrickTypeFacade.getFacadeByName(jsonObj.getString(FACADE)));
		return out;
	}
	
}
