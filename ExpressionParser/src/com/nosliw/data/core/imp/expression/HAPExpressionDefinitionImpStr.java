package com.nosliw.data.core.imp.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.strvalue.HAPStringableValueEntity;
import com.nosliw.data.core.HAPData;

import com.nosliw.data.core.criteria.HAPDataTypeCriteriaWrapperLiterate;
import com.nosliw.data.core.operand.HAPOperand;
import com.nosliw.data.core.operand.HAPOperandWrapper;

public class HAPExpressionDefinitionImpStr{ 
/*
extends HAPStringableValueEntity implements HAPDefinitionTask{

	public static String _VALUEINFO_NAME;
	
	//As when this class is instantiated, the variable attribute is criteria in literate format which can not use directly
	//This attribute store the real criteria which can be converted from literate fromat
	Map<String, HAPVariableInfo> m_variabesWithSolidCriteria;
	
	private HAPOperandWrapper m_operand  = new HAPOperandWrapper();
	
	public HAPExpressionDefinitionImpStr(){	}
	
	@Override
	public HAPOperandWrapper getOperand() {  return this.m_operand;  }
	@Override
	public void setOperand(HAPOperand operand){  this.m_operand.setOperand(operand);   }
	
	@Override
	public String getName() {  return this.getAtomicAncestorValueString(NAME);	}
	@Override
	public void setName(String name){  this.updateAtomicChildStrValue(NAME, name);  }
	
	@Override
	public String getExpression(){ return this.getAtomicAncestorValueString(EXPRESSION); }

	@Override
	public HAPInfo getInfo() {	return (HAPInfo)this.getEntityAncestorByPath(INFO); }

	@Override
	public Map<String, HAPData> getConstants(){return this.getMapAncestorByPath(CONSTANTS).getMapValue();}

	@Override
	public Map<String, HAPVariableInfo> getVariableCriterias() {
		if(this.m_variabesWithSolidCriteria == null){
			this.m_variabesWithSolidCriteria = new LinkedHashMap<String, HAPVariableInfo>(); 
			Map<String, HAPVariableInfo> vars = this.getMapAncestorByPath(VARIABLECRITERIAS).getMapValue();
			for(String varName : vars.keySet()){
				m_variabesWithSolidCriteria.put(varName, ((HAPDataTypeCriteriaWrapperLiterate)vars.get(varName)).getSolidCriteria());
			}
		}
		return this.m_variabesWithSolidCriteria;
	}

	public void setVariableCriterias(Map<String, HAPVariableInfo> varCriterias){
		this.m_variabesWithSolidCriteria = varCriterias;
	}
	
	
	@Override
	public Map<String, HAPReferenceInfo> getReferences() {  return this.getMapAncestorByPath(REFERENCES).getMapValue();  }
	
	public HAPDefinitionTask cloneTaskDefinition(){
		HAPExpressionDefinitionImpStr out = this.clone(HAPExpressionDefinitionImpStr.class);

		Map<String, HAPVariableInfo> vars = this.getMapAncestorByPath(VARIABLECRITERIAS).getMapValue();
		
		for(String varName : vars.keySet()){
			getVariableCriterias().put(varName, ((HAPDataTypeCriteriaWrapperLiterate)vars.get(varName)).getSolidCriteria());
		}
		return out;
	}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		this.buildJsonMap(jsonMap, typeJsonMap);
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VARIABLECRITERIAS, HAPJsonUtility.buildJson(m_variabesWithSolidCriteria, HAPSerializationFormat.JSON));
	}
*/
}
