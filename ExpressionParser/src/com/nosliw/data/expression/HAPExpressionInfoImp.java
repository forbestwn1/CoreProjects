package com.nosliw.data.expression;

import java.util.Map;

import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPInfo;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPExpressionInfo;
import com.nosliw.data.core.expression.HAPReferenceInfo;

public class HAPExpressionInfoImp extends HAPStringableValueEntity implements HAPExpressionInfo{

	public HAPExpressionInfoImp(){	}
	
	@Override
	public String getName() {  return this.getAtomicAncestorValueString(NAME);	}

	@Override
	public String getExpression(){ return this.getAtomicAncestorValueString(EXPRESSION); }

	@Override
	public HAPInfo getInfo() {	return (HAPInfo)this.getEntityAncestorByPath(INFO); }

	@Override
	public Map<String, HAPData> getConstants(){return this.getMapAncestorByPath(CONSTANTS).getMapValue();}

	@Override
	public Map<String, HAPDataTypeCriteria> getVariables() {  return this.getMapAncestorByPath(VARIABLES).getMapValue(); }

	@Override
	public Map<String, HAPReferenceInfo> getReferences() {  return this.getMapAncestorByPath(REFERENCES).getMapValue();  }

	
	

//	public HAPExpressionInfoImp addVariable(Map<String, HAPDataTypeCriteria> m_variableInfos){
//		this.m_variables.putAll(m_variableInfos);
//		return this;
//	}
	

}
