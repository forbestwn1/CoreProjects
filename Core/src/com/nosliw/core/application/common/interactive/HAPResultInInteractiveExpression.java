package com.nosliw.core.application.common.interactive;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.criteria.HAPParserCriteria;

@HAPEntityWithAttribute
public class HAPResultInInteractiveExpression extends HAPSerializableImp{

	@HAPAttribute
	public static String DATACRITERIA = "criteria";

	private HAPDataTypeCriteria m_dataCriteria;
	
	public HAPResultInInteractiveExpression() {}
	
	public HAPResultInInteractiveExpression(HAPDataTypeCriteria dataCriteria) {
		this.m_dataCriteria = dataCriteria;
	}
	
	public HAPDataTypeCriteria getDataCriteria() {
		return this.m_dataCriteria;
	}

	@Override
	protected boolean buildObjectByJson(Object json){  
		JSONObject jsonObj = (JSONObject)json;
		this.m_dataCriteria = HAPParserCriteria.getInstance().parseCriteria(jsonObj.getString(DATACRITERIA));
		return true;  
	}
}
