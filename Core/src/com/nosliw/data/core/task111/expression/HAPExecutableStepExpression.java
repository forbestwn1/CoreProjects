package com.nosliw.data.core.task111.expression;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPMatchers;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.operand.HAPOperand;
import com.nosliw.data.core.operand.HAPOperandReference;
import com.nosliw.data.core.operand.HAPOperandTask;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.operand.HAPOperandVariable;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.runtime.HAPExecutableExpression;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.task111.HAPExecutableTask;

public class HAPExecutableStepExpression extends HAPExecutableStep implements HAPExecutableExpression{

	//Operand to represent the expression
	private HAPOperandWrapper m_operand;
	
	private Map<String, HAPMatchers> m_varsMatchers;
	
	Map<String, HAPVariableInfo> m_variablesInfo;
	
	Set<String> m_references;
	
	//output variable
	private String m_outputVariable;
	
	private boolean m_exits;
	
	public HAPExecutableStepExpression(HAPDefinitionStepExpression stepDef, int index, String name) {
		super(index, name);
		this.m_operand = stepDef.getOperand().cloneWrapper();
		HAPOperandUtility.replaceAttributeOpWithOperationOp(this.m_operand);
		
		this.m_outputVariable = stepDef.getOutputVariable();
		this.m_exits = stepDef.isExit();
		this.m_variablesInfo = new LinkedHashMap<String, HAPVariableInfo>();
		this.m_references = stepDef.getReferenceNames();
	}
	
	@Override
	public String getType(){   return HAPConstant.DATATASK_TYPE_EXPRESSION;   };

//	@Override
//	public List<HAPResourceId> getResourceDependency() {		return this.m_operand.getOperand().getResources();	}

	@Override
	public Set<String> getReferences(){   return this.m_references;    }

	@Override
	public Set<String> getVariables() {  return this.m_variablesInfo.keySet();  }

//	@Override
//	public String getId() {  return null; }

	@Override
	public Map<String, HAPMatchers> getVariableMatchers() {		return null;	}
	
	public boolean isExit() {  return this.m_exits;   }
	
	public String getOutputVariable(){  return this.m_outputVariable;  }

	
	public HAPOperandWrapper getOperand() {	return this.m_operand;	}

	public HAPDataTypeCriteria getExitDataTypeCriteria() {
		if(this.m_exits) return this.getOutput();
		return null;
	}

	@Override
	public void updateReferencedExecute(Map<String, HAPExecutableTask> references) {
		HAPOperandUtility.updateReferencedExecute(this.m_operand, references);
	}

	@Override
	public void discoverVariable(Map<String, HAPVariableInfo> variablesInfo, HAPDataTypeCriteria expectOutputCriteria,
			HAPProcessContext context) {
		Map<String, HAPVariableInfo> varsInfo = HAPOperandUtility.discover(new HAPOperand[]{this.m_operand.getOperand()}, variablesInfo, expectOutputCriteria, context);
		
		//handle output variable
		String outVarName = this.m_outputVariable;
		if(outVarName!=null) {
			HAPVariableInfo localOutVarInfo = new HAPVariableInfo(this.getOperand().getOperand().getOutputCriteria());
			localOutVarInfo.setInfoValue(HAPExecutableTaskExpression.INFO_LOCALVRIABLE, HAPExecutableTaskExpression.INFO_LOCALVRIABLE);
			varsInfo.put(outVarName, localOutVarInfo);
		}
		
		variablesInfo.clear();
		variablesInfo.putAll(varsInfo);
		m_variablesInfo.clear();
		m_variablesInfo.putAll(varsInfo);
	}

	@Override
	public HAPDataTypeCriteria getOutput() {	return this.m_operand.getOperand().getOutputCriteria();	}

	@Override
	public void updateVariable(HAPUpdateName updateVar) {
		
		if(this.m_outputVariable!=null) this.m_outputVariable = updateVar.getUpdatedName(this.m_outputVariable);
		HAPOperandUtility.updateVariableName(this.getOperand(), updateVar);
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		return true;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		HAPExecutableExpression.buildJsonMap(this, jsonMap, typeJsonMap);
	}

	@Override
	public List<HAPResourceDependent> getResourceDependency(HAPRuntimeInfo runtimeInfo) {
		// TODO Auto-generated method stub
		return null;
	}

}
