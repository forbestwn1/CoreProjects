package com.nosliw.data.core.imp;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.strvalue.HAPStringableValueEntityWithID;
import com.nosliw.data.core.HAPOperationOutInfo;
import com.nosliw.data.core.HAPOperationParmInfo;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaWrapperLiterate;

public class HAPOperationVarInfoImp extends HAPStringableValueEntityWithID implements HAPOperationParmInfo, HAPOperationOutInfo{

	public static String _VALUEINFO_NAME;

	@HAPAttribute
	public static String DATATYPEID = "dataTypeId";

	@HAPAttribute
	public static String OPERATIONID = "operationId";

	@HAPAttribute
	public static String TYPE = "type";
	
	//As when this class is instantiated, the criteria attribute is criteria in literate format which can not use directly
	//This attribute store the real criteria which can be converted from literate fromat
	private HAPDataTypeCriteria m_criteria;
	
	@Override
	public String getName() {		return this.getAtomicAncestorValueString(NAME);	}

	public void setName(String name){  this.updateAtomicChildStrValue(NAME, name);  }
	
	@Override
	public String getDescription() {		return this.getAtomicAncestorValueString(DESCRIPTION);  }

	@Override
	public HAPDataTypeCriteria getCriteria() {
		if(this.m_criteria==null){
			HAPDataTypeCriteriaWrapperLiterate criteriaLiterate = (HAPDataTypeCriteriaWrapperLiterate)this.getAtomicValueAncestorByPath(CRITERIA);
			if(criteriaLiterate!=null){
				this.m_criteria = criteriaLiterate.getSolidCriteria();
			}
		}
		return this.m_criteria;
	}

	public void setCriteria(HAPDataTypeCriteria criteria){  
		if(criteria instanceof HAPDataTypeCriteriaWrapperLiterate){
			this.m_criteria = ((HAPDataTypeCriteriaWrapperLiterate)criteria).getSolidCriteria();
		}
		else{
			this.m_criteria = criteria;  
		}
	}
	
	public String getDataTypeId() {		return this.getAtomicAncestorValueString(DATATYPEID);	}
	public String getOperationId() {		return this.getAtomicAncestorValueString(OPERATIONID);	}
	public String getType() {		return this.getAtomicAncestorValueString(TYPE);	}
	public void setType(String type){  this.updateAtomicChildStrValue(TYPE, type);  }
	
	@Override
	public boolean getIsBase() { return this.getAtomicAncestorValueBoolean(ISBASE); }
}
