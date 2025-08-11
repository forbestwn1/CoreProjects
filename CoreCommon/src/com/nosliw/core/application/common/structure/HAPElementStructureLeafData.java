package com.nosliw.core.application.common.structure;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.common.datadefinition.HAPDataDefinitionWritable;
import com.nosliw.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.core.data.criteria.HAPInfoCriteria;

public class HAPElementStructureLeafData extends HAPElementStructureLeafVariable{

	@HAPAttribute
	public static final String DATA  = "data";

	@HAPAttribute
	public static String STATUS = "status";

	//context definition of that node (criteria)
	private HAPDataDefinitionWritable m_dataDefinition;
	
	//status of variable, now there are two status
	//open: the criteria is open to change
	//close : the criteria is close to change
	private String m_status = HAPConstantShared.EXPRESSION_VARIABLE_STATUS_OPEN;
	
	public HAPElementStructureLeafData() {}
	
	public HAPElementStructureLeafData(HAPDataTypeCriteria dataTypeCriteria){
		this.m_dataDefinition = new HAPDataDefinitionWritable(dataTypeCriteria);
		if(dataTypeCriteria==null) {
			this.m_status = HAPConstantShared.EXPRESSION_VARIABLE_STATUS_OPEN;
		} else {
			this.m_status = HAPConstantShared.EXPRESSION_VARIABLE_STATUS_CLOSE;
		}
	}	

	public HAPElementStructureLeafData(HAPDataDefinitionWritable dataInfo){
		this.m_dataDefinition = dataInfo;
		this.m_status = HAPConstantShared.EXPRESSION_VARIABLE_STATUS_CLOSE;
	}

	public String getStatus() {   return this.m_status;    }
	public void setStatus(String status) {    this.m_status = status;   }
	
	@Override
	public String getType() {	return HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA;	}

	public void setDataDefinition(HAPDataDefinitionWritable criteria){	this.m_dataDefinition = criteria;	}
	public HAPDataDefinitionWritable getDataDefinition() {  return this.m_dataDefinition;    } 
	
	public HAPDataTypeCriteria getCriteria(){   return this.m_dataDefinition==null?null:this.m_dataDefinition.getCriteria();  }
	public void setCriteria(HAPDataTypeCriteria criteria) {
		if(this.m_dataDefinition==null) {
			this.m_dataDefinition = new HAPDataDefinitionWritable();
		}
		this.m_dataDefinition.setCriteria(criteria);;
	}
	
	public HAPInfoCriteria getCriteriaInfo() {   return HAPInfoCriteria.buildCriteriaInfo(this.getCriteria(), this.getStatus());      }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_dataDefinition!=null) {
			jsonMap.put(DATA, this.m_dataDefinition.toStringValue(HAPSerializationFormat.JSON));
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
		if(this.m_dataDefinition!=null) {
			dataEle.m_dataDefinition = this.m_dataDefinition.cloneDataDefinitionWritable();
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
			if(!HAPUtilityBasic.isEquals(this.m_dataDefinition, ele.m_dataDefinition)) {
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
