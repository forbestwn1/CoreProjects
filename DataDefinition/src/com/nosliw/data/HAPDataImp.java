package com.nosliw.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.utils.HAPAttributeConstant;
import com.nosliw.data.utils.HAPDataUtility;

public abstract class HAPDataImp implements HAPData{

	private HAPDataType m_dataType;
	
	public HAPDataImp(HAPDataType dataType){
		this.m_dataType = dataType;
	}

	
	//*************************  Operate
	@Override
	public HAPServiceData operate(String operation, List<HAPData> parms, HAPOperationContext opContext){
		List<HAPData> p = new ArrayList<HAPData>();
		p.add(this);
		p.addAll(parms);
		return this.getDataType().operate(operation, p.toArray(new HAPData[0]), opContext);
	}
	
	//*************************  Clear Up
	@Override
	public final void clearUp(Map<String, Object> parms) {
		this.doClearUp(parms);
		this.m_dataType = null;
	}

	protected void doClearUp(Map<String, Object> parms){}
	
	
	//********************  Basic Info
	
	@Override
	public HAPDataType getDataType(){return this.m_dataType;}
	
	@Override
	public boolean isEmpty(){	return false;	}
	
	//**********************  Parse
	/*
	 * method need to implementd by data type
	 * data type does not need to implement JSON_DATATYPE, it is defined in toStringValue
	 * data type need to implement JSON
	 */
	abstract public String toDataStringValue(String format);
	
	@Override
	public final String toStringValue(String format){
		String out = this.toDataStringValue(format);
		if(out!=null)  return out;
		
		if(format.equals(HAPConstant.SERIALIZATION_JSON_FULL)){
			Map<String, String> jsonMap = new HashMap<String, String>();
			Map<String, Class> jsonType = null;
			
			jsonMap.put(HAPAttributeConstant.DATA_DATATYPEINFO, HAPDataUtility.getDataTypeInfo(this).toStringValue(HAPConstant.SERIALIZATION_JSON));			
			jsonMap.put(HAPAttributeConstant.DATA_VALUE, this.toStringValue(HAPConstant.SERIALIZATION_JSON));
			
			if(HAPDataUtility.isBooleanType(this)){
				if(jsonType==null)  jsonType = new LinkedHashMap<String, Class>();
				jsonType.put(HAPAttributeConstant.DATA_VALUE, Boolean.class);
			}
			else if(HAPDataUtility.isIntegerType(this)){
				if(jsonType==null)  jsonType = new LinkedHashMap<String, Class>();
				jsonType.put(HAPAttributeConstant.DATA_VALUE, Integer.class);
			}
			
			return HAPJsonUtility.getMapJson(jsonMap, jsonType);
		}
		return out;
	}
	
	@Override
	public String toString(){
		return HAPJsonUtility.formatJson(this.toStringValue(HAPConstant.SERIALIZATION_JSON));
	}
	
	//*********************  Clone
	protected void cloneBasic(HAPDataImp data){
		data.m_dataType = this.m_dataType;
	}

	protected HAPDataTypeManager getDataTypeManager(){	return ((HAPDataTypeImp)this.m_dataType).getDataTypeManager();	}
}
