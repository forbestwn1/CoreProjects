package com.nosliw.data.core.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

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
	private List<HAPDataTypeCriteria> m_criteriaStack = new ArrayList<HAPDataTypeCriteria>();
	
	//status of variable, now there are two status
	//open: the criteria is open to change
	//close : the criteria is close to change
	private String m_status;

	private HAPInfo m_info;
	
	public HAPVariableInfo(){
		this(null);
		this.m_info = new HAPInfoImpSimple();
	}	
	
	public HAPVariableInfo(HAPDataTypeCriteria criteria, String status){
		this.m_status = status;
		this.m_criteriaStack.add(criteria);
		this.m_info = new HAPInfoImpSimple();
	}

	public HAPVariableInfo(HAPDataTypeCriteria criteria){
		this.m_criteriaStack.add(criteria);
		if(criteria==null)   this.m_status = HAPConstant.EXPRESSION_VARIABLE_STATUS_OPEN;
		else   this.m_status = HAPConstant.EXPRESSION_VARIABLE_STATUS_CLOSE;
		this.m_info = new HAPInfoImpSimple();
	}
	
	public String getStatus(){		return this.m_status;	}
	
	public void setStatus(String status){  this.m_status = status;   }
	
	public HAPDataTypeCriteria getCriteria(){		return m_criteriaStack.get(this.m_criteriaStack.size()-1);	}
	
	public void setCriteria(HAPDataTypeCriteria criteria){		this.m_criteriaStack.add(criteria);	}
	
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
	}
	
	@Override
	public String toString(){		return this.toStringValue(HAPSerializationFormat.JSON);	}
}
