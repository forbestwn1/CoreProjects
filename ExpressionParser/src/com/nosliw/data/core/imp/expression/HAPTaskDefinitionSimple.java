package com.nosliw.data.core.imp.expression;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.application.common.dataexpression.HAPOperand;
import com.nosliw.data.core.application.common.dataexpression.HAPWrapperOperand;
import com.nosliw.data.core.data.HAPData;

public class HAPTaskDefinitionSimple{ 

/*	
extends HAPSerializableImp implements HAPDefinitionTask{

	private String m_name; 
	
	private List<HAPDefinitionTask> m_taskSteps;
	
	private Map<String, HAPData> m_constants;
	
	private Map<String, HAPVariableInfo> m_variableCriterias; 
	
	private Map<String, HAPReferenceInfo> m_references;
	
	private HAPInfo m_info;
	
	private HAPOperandWrapper m_operand = new HAPOperandWrapper();
	
	HAPTaskDefinitionSimple(){}
	
	HAPTaskDefinitionSimple(
			String name, 
			Map<String, HAPData> constants,
			Map<String, HAPVariableInfo> variableCriterias, 
			Map<String, HAPReferenceInfo> references, 
			HAPInfo info){
		this.m_name = name;
		this.m_constants = constants;
		if(this.m_constants==null)  this.m_constants = new LinkedHashMap<String, HAPData>();
		this.m_variableCriterias = variableCriterias;
		if(this.m_variableCriterias==null)  this.m_variableCriterias = new LinkedHashMap<String, HAPVariableInfo>();
		this.m_references = references;
		if(this.m_references==null)   this.m_references = new LinkedHashMap<String, HAPReferenceInfo>();
		this.m_info = info;
	}
	
	@Override
	public String getName() {		return this.m_name;	}
	@Override
	public void setName(String name) {  this.m_name = name;  }

	@Override
	public HAPInfo getInfo() {		return this.m_info;	}

	@Override
	public HAPDefinitionTask[] getSteps() {	return this.m_taskSteps.toArray(new HAPDefinitionTask[0]);	}

	
	@Override
	public Map<String, HAPData> getConstants() {		return this.m_constants;	}

	@Override
	public Map<String, HAPVariableInfo> getVariableCriterias() {		return this.m_variableCriterias;	}
	@Override
	public void setVariableCriterias(Map<String, HAPVariableInfo> varCriterias) {
		this.m_variableCriterias = new LinkedHashMap<String, HAPVariableInfo>();
		this.m_variableCriterias.putAll(varCriterias);
	}

	@Override
	public Map<String, HAPReferenceInfo> getReferences() {		return this.m_references;	}

	@Override
	public HAPOperandWrapper getOperand() {   return this.m_operand;  }
	public void setOperand(HAPOperand operand){   this.m_operand.setOperand(operand);  }

	@Override
	public HAPDefinitionTask cloneTaskDefinition() {
		HAPTaskDefinitionSimple out = new HAPTaskDefinitionSimple();
		
		out.m_name = this.m_name;
		out.m_expression = this.m_expression;
		
		if(this.m_constants!=null){
			out.m_constants = new LinkedHashMap<String, HAPData>();
			out.m_constants.putAll(this.m_constants);
		}
		
		if(this.m_variableCriterias!=null){
			out.m_variableCriterias = new LinkedHashMap<String, HAPVariableInfo>();
			out.m_variableCriterias.putAll(this.m_variableCriterias);
		}
		
		if(this.m_references!=null){
			out.m_references = new LinkedHashMap<String, HAPReferenceInfo>();
			out.m_references.putAll(this.m_references);
		}
		
		out.m_info = this.m_info;
		if(this.m_operand!=null)  out.m_operand = this.m_operand.cloneWrapper();
		
		return out;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(NAME, m_name);
		jsonMap.put(VALUE, m_expression);
		jsonMap.put(VARIABLECRITERIAS, HAPJsonUtility.buildJson(this.m_variableCriterias, HAPSerializationFormat.JSON));
		jsonMap.put(CONSTANTS, HAPJsonUtility.buildJson(this.m_constants, HAPSerializationFormat.JSON));
		jsonMap.put(REFERENCES, HAPJsonUtility.buildJson(this.m_references, HAPSerializationFormat.JSON));
	}
*/
}
