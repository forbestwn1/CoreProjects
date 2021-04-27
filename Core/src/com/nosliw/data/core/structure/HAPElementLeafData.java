package com.nosliw.data.core.structure;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.variable.HAPVariableDataInfo;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPElementLeafData extends HAPElementLeafVariable{

	@HAPAttribute
	public static final String CRITERIA  = "criteria";

	//context definition of that node (criteria)
	private HAPVariableDataInfo m_dataInfo;
	
	public HAPElementLeafData() {}
	
	public HAPElementLeafData(HAPDataTypeCriteria dataTypeCriteria){
		this.m_dataInfo = new HAPVariableDataInfo(dataTypeCriteria);
	}

	public HAPElementLeafData(HAPVariableDataInfo dataInfo){
		this.m_dataInfo = dataInfo;
	}

	@Override
	public String getType() {	return HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA;	}

	public void setDataInfo(HAPVariableDataInfo criteria){	this.m_dataInfo = criteria;	}
	public HAPVariableDataInfo getDataInfo() {  return this.m_dataInfo;    } 
	
	public HAPDataTypeCriteria getCriteria(){   return this.m_dataInfo==null?null:this.m_dataInfo.getCriteria();  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_dataInfo!=null)  	jsonMap.put(CRITERIA, this.m_dataInfo.toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	public HAPElement cloneContextDefinitionElement() {
		HAPElementLeafData out = new HAPElementLeafData();
		this.toContextDefinitionElement(out);
		return out;
	}

	@Override
	public void toContextDefinitionElement(HAPElement out) {
		super.toContextDefinitionElement(out);
		if(this.m_dataInfo!=null)		((HAPElementLeafData)out).m_dataInfo = this.m_dataInfo.cloneVariableDataInfo();
	}

	@Override
	public HAPElement toSolidContextDefinitionElement(Map<String, Object> constants, HAPRuntimeEnvironment runtimeEnv) {	return this;}

	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj))  return false;

		boolean out = false;
		if(obj instanceof HAPElementLeafData) {
			HAPElementLeafData ele = (HAPElementLeafData)obj;
			if(!HAPBasicUtility.isEquals(this.m_dataInfo, ele.m_dataInfo))  return false;
			out = true;
		}
		return out;
	}
}
