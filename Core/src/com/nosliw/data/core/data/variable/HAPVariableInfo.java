package com.nosliw.data.core.data.variable;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.criteria.HAPCriteriaParser;
import com.nosliw.data.core.data.criteria.HAPCriteriaUtility;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;

/**
 * This is variable info for expression 
 */
@HAPEntityWithAttribute(baseName="VARIABLEINFO")
public class HAPVariableInfo extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String CRITERIA = "criteria";

	@HAPAttribute
	public static String STATUS = "status";

	@HAPAttribute
	public static String DEFAULTVALUE = "defaultValue";

	//use stack to store all the change applied for criteria
	private HAPDataTypeCriteria m_criteria;
	
	//status of variable, now there are two status
	//open: the criteria is open to change
	//close : the criteria is close to change
	private String m_status;

	private HAPData m_defaultValue;
	
	public static HAPVariableInfo buildUndefinedVariableInfo() {
		return buildVariableInfo(null);
	}
	
	public static HAPVariableInfo buildVariableInfoFromObject(Object def) {
		HAPVariableInfo out = new HAPVariableInfo();
		out.buildObject(def, null);
		return out;
	}

	public static HAPVariableInfo buildVariableInfo(HAPDataTypeCriteria criteria) {
		HAPVariableInfo out = new HAPVariableInfo();
		if(criteria!=null)		out.m_criteria = HAPCriteriaUtility.cloneDataTypeCriteria(criteria);
		out.initWithDefault();
		return out;
	}

	public static HAPVariableInfo buildVariableInfo(HAPDataTypeCriteria criteria, String status) {
		HAPVariableInfo out = new HAPVariableInfo();
		out.m_criteria = criteria;
		out.m_status = status;
		out.initWithDefault();
		return out;
	}
	
	private HAPVariableInfo() {}
	
	private void initWithDefault() {
		if(this.m_status==null) {
			if(this.m_criteria==null)   this.m_status = HAPConstant.EXPRESSION_VARIABLE_STATUS_OPEN;
			else   this.m_status = HAPConstant.EXPRESSION_VARIABLE_STATUS_CLOSE;
		}
	}
	
	public String getStatus(){		return this.m_status;	}
	
	public void setStatus(String status){  this.m_status = status;   }
	
	public HAPDataTypeCriteria getCriteria(){		return HAPCriteriaUtility.cloneDataTypeCriteria(this.m_criteria);	}
	
	public void setCriteria(HAPDataTypeCriteria criteria){
		this.m_criteria = HAPCriteriaUtility.cloneDataTypeCriteria(criteria);
	}
	
	public HAPData getDefaultValue() {   return this.m_defaultValue;   }
	public void setDefaultValue(HAPData data) {    this.m_defaultValue = data;    }
	
	@Override
	public boolean equals(Object obj){
		boolean out = false;
		if(obj instanceof HAPVariableInfo){
			HAPVariableInfo varInfo = (HAPVariableInfo)obj;
			if(HAPBasicUtility.isEquals(m_status, varInfo.m_status) && HAPBasicUtility.isEquals(this.getCriteria(), varInfo.getCriteria())){
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
			this.m_criteria = HAPCriteriaParser.getInstance().parseCriteria((String)value);
		}
		else if(value instanceof JSONObject){
			JSONObject jsonValue = (JSONObject)value;
			this.buildEntityInfoByJson(jsonValue);
			this.m_status = (String)jsonValue.opt(STATUS);
			this.m_criteria = HAPCriteriaParser.getInstance().parseCriteria((String)jsonValue.opt(CRITERIA));
		}
		this.initWithDefault();
		return true;
	}
	
	public HAPVariableInfo cloneVariableInfo() {
		HAPVariableInfo out = new HAPVariableInfo();
		this.cloneToEntityInfo(out);
		out.m_status = this.m_status;
		out.m_criteria = HAPCriteriaUtility.cloneDataTypeCriteria(this.m_criteria);
		return out;
	}
	
	@Override
	public String toString(){		return this.toStringValue(HAPSerializationFormat.JSON);	}
}
