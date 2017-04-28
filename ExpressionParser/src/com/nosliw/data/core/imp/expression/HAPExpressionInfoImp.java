package com.nosliw.data.core.imp.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPInfo;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaWrapperLiterate;
import com.nosliw.data.core.expression.HAPExpressionInfo;
import com.nosliw.data.core.expression.HAPReferenceInfo;

public class HAPExpressionInfoImp extends HAPStringableValueEntity implements HAPExpressionInfo{

	public static String _VALUEINFO_NAME;
	
	//As when this class is instantiated, the variable attribute is criteria in literate format which can not use directly
	//This attribute store the real criteria which can be converted from literate fromat
	Map<String, HAPDataTypeCriteria> m_variabesWithSolidCriteria;
	
	public HAPExpressionInfoImp(){	}
	
	@Override
	public String getName() {  return this.getAtomicAncestorValueString(NAME);	}
	public void setName(String name){  this.updateAtomicChildStrValue(NAME, name);  }
	
	@Override
	public String getExpression(){ return this.getAtomicAncestorValueString(EXPRESSION); }

	@Override
	public HAPInfo getInfo() {	return (HAPInfo)this.getEntityAncestorByPath(INFO); }

	@Override
	public Map<String, HAPData> getConstants(){return this.getMapAncestorByPath(CONSTANTS).getMapValue();}

	@Override
	public Map<String, HAPDataTypeCriteria> getVariables() {
		if(this.m_variabesWithSolidCriteria == null){
			this.m_variabesWithSolidCriteria = new LinkedHashMap<String, HAPDataTypeCriteria>(); 
			Map<String, HAPDataTypeCriteria> vars = this.getMapAncestorByPath(VARIABLES).getMapValue();
			for(String varName : vars.keySet()){
				m_variabesWithSolidCriteria.put(varName, ((HAPDataTypeCriteriaWrapperLiterate)vars.get(varName)).getSolidCriteria());
			}
		}
		return this.m_variabesWithSolidCriteria;
	}

	@Override
	public Map<String, HAPReferenceInfo> getReferences() {  return this.getMapAncestorByPath(REFERENCES).getMapValue();  }
}
