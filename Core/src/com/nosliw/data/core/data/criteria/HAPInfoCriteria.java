package com.nosliw.data.core.data.criteria;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;

public class HAPInfoCriteria extends HAPSerializableImp{

	@HAPAttribute
	public static String CRITERIA = "criteria";

	@HAPAttribute
	public static String STATUS = "status";

	private HAPDataTypeCriteria m_criteria;

	//status of variable, now there are two status
	//open: the criteria is open to change
	//close : the criteria is close to change
	private String m_status;

	public HAPInfoCriteria() {
		
	}
	
	public static HAPInfoCriteria buildUndefinedCriteriaInfo() {
		return buildCriteriaInfo(null);
	}
	
	public static HAPInfoCriteria buildCriteriaInfoFromObject(Object def) {
		HAPInfoCriteria out = new HAPInfoCriteria();
		out.buildObject(def, null);
		return out;
	}

	public static HAPInfoCriteria buildCriteriaInfo(HAPDataTypeCriteria criteria) {
		HAPInfoCriteria out = new HAPInfoCriteria();
		if(criteria!=null)		out.m_criteria = HAPUtilityCriteria.cloneDataTypeCriteria(criteria);
		out.initWithDefault();
		return out;
	}

	public static HAPInfoCriteria buildCriteriaInfo(HAPDataTypeCriteria criteria, String status) {
		HAPInfoCriteria out = new HAPInfoCriteria();
		out.m_criteria = criteria;
		out.m_status = status;
		out.initWithDefault();
		return out;
	}
	
	public String getStatus() {   return this.m_status;    }
	public void setStatus(String status) {   this.m_status = status;      }
	public HAPDataTypeCriteria getCriteria() {    return this.m_criteria;    }
	public void setCriteria(HAPDataTypeCriteria criteria) {    this.m_criteria = criteria;     }
	
	private void initWithDefault() {
		if(this.m_status==null) {
			if(this.m_criteria==null)   this.m_status = HAPConstantShared.EXPRESSION_VARIABLE_STATUS_OPEN;
			else   this.m_status = HAPConstantShared.EXPRESSION_VARIABLE_STATUS_CLOSE;
		}
	}

	@Override
	public boolean equals(Object obj){
		boolean out = false;
		if(obj instanceof HAPInfoCriteria){
			HAPInfoCriteria criteriaInfo = (HAPInfoCriteria)obj;
			if(HAPUtilityBasic.isEquals(m_status, criteriaInfo.m_status) && HAPUtilityBasic.isEquals(this.getCriteria(), criteriaInfo.getCriteria())){
				out = true;
			}
		}
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(STATUS, this.getStatus());
		if(this.getCriteria()!=null){
			jsonMap.put(CRITERIA, HAPSerializeManager.getInstance().toStringValue(this.getCriteria(), HAPSerializationFormat.LITERATE));
		}
	}
	
	@Override
	public boolean buildObject(Object value, HAPSerializationFormat format) {
		if(value instanceof String) {
			this.m_criteria = HAPParserCriteria.getInstance().parseCriteria((String)value);
		}
		else if(value instanceof JSONObject){
			JSONObject jsonValue = (JSONObject)value;
			this.m_status = (String)jsonValue.opt(STATUS);
			this.m_criteria = HAPParserCriteria.getInstance().parseCriteria((String)jsonValue.opt(CRITERIA));
		}
		this.initWithDefault();
		return true;
	}
	
	public HAPInfoCriteria cloneCriteriaInfo() {
		HAPInfoCriteria out = new HAPInfoCriteria();
		out.m_status = this.m_status;
		out.m_criteria = HAPUtilityCriteria.cloneDataTypeCriteria(this.m_criteria);
		return out;
	}
	
	@Override
	public String toString(){		return this.toStringValue(HAPSerializationFormat.JSON);	}
}
