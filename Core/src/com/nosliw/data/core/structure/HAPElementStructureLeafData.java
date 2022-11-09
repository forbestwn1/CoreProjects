package com.nosliw.data.core.structure;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.variable.HAPVariableDataInfo;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPElementStructureLeafData extends HAPElementStructureLeafVariable{

	@HAPAttribute
	public static final String CRITERIA  = "criteria";

	//context definition of that node (criteria)
	private HAPVariableDataInfo m_dataInfo;
	
	public HAPElementStructureLeafData() {}
	
	public HAPElementStructureLeafData(HAPDataTypeCriteria dataTypeCriteria){
		this.m_dataInfo = new HAPVariableDataInfo(dataTypeCriteria);
	}

	public HAPElementStructureLeafData(HAPVariableDataInfo dataInfo){
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
	public HAPElementStructure cloneStructureElement() {
		HAPElementStructureLeafData out = new HAPElementStructureLeafData();
		this.toStructureElement(out);
		return out;
	}

	@Override
	public void toStructureElement(HAPElementStructure out) {
		super.toStructureElement(out);
		if(this.m_dataInfo!=null)		((HAPElementStructureLeafData)out).m_dataInfo = this.m_dataInfo.cloneVariableDataInfo();
	}

	@Override
	public HAPElementStructure solidateConstantScript(Map<String, Object> constants, HAPRuntimeEnvironment runtimeEnv) {	return this;}

	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj))  return false;

		boolean out = false;
		if(obj instanceof HAPElementStructureLeafData) {
			HAPElementStructureLeafData ele = (HAPElementStructureLeafData)obj;
			if(!HAPUtilityBasic.isEquals(this.m_dataInfo, ele.m_dataInfo))  return false;
			out = true;
		}
		return out;
	}
}
