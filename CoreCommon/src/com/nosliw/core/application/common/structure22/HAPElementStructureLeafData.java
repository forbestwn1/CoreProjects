package com.nosliw.core.application.common.structure22;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.common.variable.HAPVariableDataInfo;
import com.nosliw.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.core.data.criteria.HAPInfoCriteria;

public class HAPElementStructureLeafData extends HAPElementStructureLeafVariable{

	@HAPAttribute
	public static final String CRITERIA  = "criteria";

	@HAPAttribute
	public static String STATUS = "status";

	//context definition of that node (criteria)
	private HAPVariableDataInfo m_dataInfo;
	
	//status of variable, now there are two status
	//open: the criteria is open to change
	//close : the criteria is close to change
	private String m_status = HAPConstantShared.EXPRESSION_VARIABLE_STATUS_OPEN;
	
	public HAPElementStructureLeafData() {}
	
	public HAPElementStructureLeafData(HAPDataTypeCriteria dataTypeCriteria){
		this.m_dataInfo = new HAPVariableDataInfo(dataTypeCriteria);
		if(dataTypeCriteria==null) {
			this.m_status = HAPConstantShared.EXPRESSION_VARIABLE_STATUS_OPEN;
		} else {
			this.m_status = HAPConstantShared.EXPRESSION_VARIABLE_STATUS_CLOSE;
		}
	}	

	public HAPElementStructureLeafData(HAPVariableDataInfo dataInfo){
		this.m_dataInfo = dataInfo;
		this.m_status = HAPConstantShared.EXPRESSION_VARIABLE_STATUS_CLOSE;
	}

	public String getStatus() {   return this.m_status;    }
	public void setStatus(String status) {    this.m_status = status;   }
	
	@Override
	public String getType() {	return HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA;	}

	public void setDataInfo(HAPVariableDataInfo criteria){	this.m_dataInfo = criteria;	}
	public HAPVariableDataInfo getDataInfo() {  return this.m_dataInfo;    } 
	
	public HAPDataTypeCriteria getCriteria(){   return this.m_dataInfo==null?null:this.m_dataInfo.getCriteria();  }
	public void setCriteria(HAPDataTypeCriteria criteria) {
		if(this.m_dataInfo==null) {
			this.m_dataInfo = new HAPVariableDataInfo();
		}
		this.m_dataInfo.setCriteria(criteria);;
	}
	
	public HAPInfoCriteria getCriteriaInfo() {   return HAPInfoCriteria.buildCriteriaInfo(this.getCriteria(), this.getStatus());      }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_dataInfo!=null) {
			jsonMap.put(CRITERIA, this.m_dataInfo.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(STATUS, this.m_status);
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
		HAPElementStructureLeafData dataEle = (HAPElementStructureLeafData)out;
		if(this.m_dataInfo!=null) {
			dataEle.m_dataInfo = this.m_dataInfo.cloneVariableDataInfo();
		}
		dataEle.m_status = this.m_status;
	}

	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj)) {
			return false;
		}

		boolean out = false;
		if(obj instanceof HAPElementStructureLeafData) {
			HAPElementStructureLeafData ele = (HAPElementStructureLeafData)obj;
			if(!HAPUtilityBasic.isEquals(this.m_dataInfo, ele.m_dataInfo)) {
				return false;
			}
			if(!HAPUtilityBasic.isEquals(this.m_status, ele.m_status)) {
				return false;
			}
			out = true;
		}
		return out;
	}
}
