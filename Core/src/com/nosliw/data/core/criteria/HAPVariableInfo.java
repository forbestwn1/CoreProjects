package com.nosliw.data.core.criteria;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;

/**
 * This is variable info for expression 
 */
@HAPEntityWithAttribute(baseName="VARIABLEINFO")
public class HAPVariableInfo extends HAPSerializableImp{

	@HAPAttribute
	public static String CRITERIA = "criteria";

	@HAPAttribute
	public static String STATUS = "status";

	@HAPAttribute
	public static String INFO = "info";
	
	//use stack to store all the change applied for criteria
	private HAPDataTypeCriteria m_criteria;
	
	//status of variable, now there are two status
	//open: the criteria is open to change
	//close : the criteria is close to change
	private String m_status;

	private HAPInfo m_info;
	
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
	
//	public HAPVariableInfo(){
//		this(null);
//	}	
	
//	private HAPVariableInfo(HAPDataTypeCriteria criteria, String status){
//		this.m_status = status;
//		this.m_criteria = HAPCriteriaUtility.cloneDataTypeCriteria(criteria);
//		this.initWithDefault();
//	}
//
//	public HAPVariableInfo(HAPDataTypeCriteria criteria){
//		this.m_criteria = HAPCriteriaUtility.cloneDataTypeCriteria(criteria);
//		this.initWithDefault();
//	}
	
	private void initWithDefault() {
		if(this.m_status==null) {
			if(this.m_criteria==null)   this.m_status = HAPConstant.EXPRESSION_VARIABLE_STATUS_OPEN;
			else   this.m_status = HAPConstant.EXPRESSION_VARIABLE_STATUS_CLOSE;
		}
		if(this.m_info==null)	this.m_info = new HAPInfoImpSimple();
	}
	
	public String getStatus(){		return this.m_status;	}
	
	public void setStatus(String status){  this.m_status = status;   }
	
	public HAPDataTypeCriteria getCriteria(){		return HAPCriteriaUtility.cloneDataTypeCriteria(this.m_criteria);	}
	
	public void setCriteria(HAPDataTypeCriteria criteria){
		this.m_criteria = HAPCriteriaUtility.cloneDataTypeCriteria(criteria);
	}
	
	public String getInfoValue(String name) {   return (String)this.m_info.getValue(name);   }
	
	public void setInfoValue(String name, String value) {  this.m_info.setValue(name, value);    }
	
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
		jsonMap.put(STATUS, this.getStatus());
		if(this.getCriteria()!=null){
			jsonMap.put(CRITERIA, HAPSerializeManager.getInstance().toStringValue(this.getCriteria(), HAPSerializationFormat.LITERATE));
		}
		jsonMap.put(INFO, this.m_info.toStringValue(HAPSerializationFormat.JSON));
	}
	
	@Override
	public boolean buildObject(Object value, HAPSerializationFormat format) {
		if(value instanceof String) {
			this.m_criteria = HAPCriteriaParser.getInstance().parseCriteria((String)value);
		}
		else if(value instanceof JSONObject){
			JSONObject jsonValue = (JSONObject)value;
			this.m_status = (String)jsonValue.opt(STATUS);
			this.m_info = new HAPInfoImpSimple();
			this.m_info.buildObject(jsonValue.opt(INFO), HAPSerializationFormat.JSON);
			this.m_criteria = HAPCriteriaParser.getInstance().parseCriteria((String)jsonValue.opt(CRITERIA));
		}
		this.initWithDefault();
		return true;
	}
	
	public HAPVariableInfo cloneVariableInfo() {
		HAPVariableInfo out = new HAPVariableInfo();
		out.m_status = this.m_status;
		out.m_criteria = HAPCriteriaUtility.cloneDataTypeCriteria(this.m_criteria);
		out.m_info = this.m_info.cloneInfo();
		return out;
	}
	
	@Override
	public String toString(){		return this.toStringValue(HAPSerializationFormat.JSON);	}
}