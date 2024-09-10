package com.nosliw.core.application.common.interactive;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.criteria.HAPParserCriteria;

@HAPEntityWithAttribute
public class HAPInteractiveResultExpression extends HAPSerializableImp{

	@HAPAttribute
	public static String DATACRITERIA = "criteria";

	private HAPDataTypeCriteria m_dataCriteria;
	
	public HAPInteractiveResultExpression() {}
	
	public HAPInteractiveResultExpression(HAPDataTypeCriteria dataCriteria) {
		this.m_dataCriteria = dataCriteria;
	}
	
	public HAPDataTypeCriteria getDataCriteria() {	return this.m_dataCriteria;	}
	public void setDataCriteria(HAPDataTypeCriteria dataCriteria) {	this.m_dataCriteria = dataCriteria;	}
	
	@Override
	protected boolean buildObjectByJson(Object json){ 
		if(json instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject)json;
			String criteriaStr = (String)jsonObj.opt(DATACRITERIA);
			if(criteriaStr!=null) {
				this.m_dataCriteria = HAPParserCriteria.getInstance().parseCriteria(criteriaStr);
			}
		}
		else if(json instanceof String) {
			this.m_dataCriteria = HAPParserCriteria.getInstance().parseCriteria((String)json);
		}
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(DATACRITERIA, HAPManagerSerialize.getInstance().toStringValue(this.m_dataCriteria, HAPSerializationFormat.LITERATE));
	}
	
}
