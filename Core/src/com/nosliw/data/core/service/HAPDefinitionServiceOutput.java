package com.nosliw.data.core.service;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPExpressionManager;

@HAPEntityWithAttribute
public class HAPDefinitionServiceOutput extends HAPEntityInfoImp{

	@HAPAttribute
	public static String CRITERIA = "criteria";
	
	private HAPDataTypeCriteria m_criteria;
	
	public HAPDataTypeCriteria getCriteria(){   return this.m_criteria;   }
	
	public void setCriteria(HAPDataTypeCriteria criteria) {   this.m_criteria = criteria;   }
	
	public HAPDefinitionServiceOutput(){
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject objJson = (JSONObject)json;
			super.buildObjectByJson(objJson);
			
			this.m_criteria = HAPExpressionManager.criteriaParser.parseCriteria(objJson.getString(CRITERIA));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return true;  
	}
}
