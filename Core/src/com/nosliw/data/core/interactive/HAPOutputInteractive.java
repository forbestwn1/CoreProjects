package com.nosliw.data.core.interactive;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.data.core.data.criteria.HAPCriteriaParser;
import com.nosliw.data.core.data.criteria.HAPCriteriaUtility;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;

@HAPEntityWithAttribute
public class HAPOutputInteractive extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String CRITERIA = "criteria";

	//data type
	private HAPDataTypeCriteria m_criteria;

	public HAPOutputInteractive() {}

	public HAPOutputInteractive(HAPDataTypeCriteria criteria) {
		this.m_criteria = criteria;
	}
	
	public HAPDataTypeCriteria getCriteria() {   return this.m_criteria; }
	public void setCriteria(HAPDataTypeCriteria criteria) {    this.m_criteria = criteria;     }

	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.getCriteria()!=null)	jsonMap.put(CRITERIA, HAPSerializeManager.getInstance().toStringValue(this.getCriteria(), HAPSerializationFormat.LITERATE));
	}
	
	@Override
	public boolean buildObject(Object value, HAPSerializationFormat format) {
		if(value instanceof String) {
			this.m_criteria = HAPCriteriaParser.getInstance().parseCriteria((String)value);
		}
		else if(value instanceof JSONObject){
			JSONObject jsonValue = (JSONObject)value;
			this.buildEntityInfoByJson(jsonValue);
			this.m_criteria = HAPCriteriaParser.getInstance().parseCriteria((String)jsonValue.opt(CRITERIA));
		}
		return true;
	}
	
	public HAPOutputInteractive cloneInteractiveOutput() {
		HAPOutputInteractive out = new HAPOutputInteractive();
		this.cloneToEntityInfo(out);
		out.m_criteria = HAPCriteriaUtility.cloneDataTypeCriteria(this.m_criteria);
		return out;
	}
	
	@Override
	public String toString(){		return this.toStringValue(HAPSerializationFormat.JSON);	}

}
