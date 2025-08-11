package com.nosliw.core.application.common.datadefinition;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.core.data.criteria.HAPParserCriteria;
import com.nosliw.core.data.criteria.HAPUtilityCriteria;

@HAPEntityWithAttribute
public class HAPDataDefinition extends HAPSerializableImp{

	@HAPAttribute
	public static String CRITERIA = "criteria";

	//data type
	private HAPDataTypeCriteria m_criteria;
	
	public HAPDataDefinition() {}

	public HAPDataDefinition(HAPDataTypeCriteria criteria) {
		this.m_criteria = criteria;
	}

	public HAPDataTypeCriteria getCriteria() {   return this.m_criteria; }
	public void setCriteria(HAPDataTypeCriteria criteria) {    this.m_criteria = criteria;     }
	

	@Override
	public boolean equals(Object obj){
		boolean out = false;
		if(obj instanceof HAPDataDefinition){
			HAPDataDefinition dataInfo = (HAPDataDefinition)obj;
			if(HAPUtilityBasic.isEquals(this.getCriteria(), dataInfo.getCriteria())) 
			{
				out = true;
			}
		}
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.getCriteria()!=null) {
			jsonMap.put(CRITERIA, HAPManagerSerialize.getInstance().toStringValue(this.getCriteria(), HAPSerializationFormat.LITERATE));
		}
	}
	
	@Override
	public boolean buildObject(Object value, HAPSerializationFormat format) {
		if(value instanceof String) {
			this.m_criteria = HAPParserCriteria.getInstance().parseCriteria((String)value);
		}
		else if(value instanceof JSONObject){
			JSONObject jsonValue = (JSONObject)value;
			this.m_criteria = HAPParserCriteria.getInstance().parseCriteria((String)jsonValue.opt(CRITERIA));
		}
		return true;
	}
	
	protected void cloneToDataDefinition(HAPDataDefinition dataDef) {
		dataDef.m_criteria = HAPUtilityCriteria.cloneDataTypeCriteria(this.m_criteria);
	}
	
	public HAPDataDefinition cloneDataDefinition() {
		HAPDataDefinition out = new HAPDataDefinition();
		this.cloneToDataDefinition(out);
		return out;
	}
	
	@Override
	public String toString(){		return this.toStringValue(HAPSerializationFormat.JSON);	}
}
