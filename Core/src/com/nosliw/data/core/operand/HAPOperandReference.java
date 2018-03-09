package com.nosliw.data.core.operand;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.HAPDataTypeConverter;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPMatchers;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.task.HAPExecuteTask;

public class HAPOperandReference extends HAPOperandImp{

	@HAPAttribute
	public static final String REFERENCENAME = "referenceName";
	
	private String m_referenceName;
	
	private HAPExecuteTask m_referencedTask;

	private HAPOperandReference(){}
	
	public HAPOperandReference(String expressionName){
		super(HAPConstant.EXPRESSION_OPERAND_REFERENCE);
		this.m_referenceName = expressionName;
	}

	public String getReferenceName(){  return this.m_referenceName;  }
	
	public void updateReferenceExecute(Map<String, HAPExecuteTask> refTasks) { this.m_referencedTask = refTasks.get(this.m_referenceName);   }
	
	@Override
	public Set<HAPDataTypeConverter> getConverters(){
		Set<HAPDataTypeConverter> out = new HashSet<HAPDataTypeConverter>();
//		Map<String, HAPMatchers> varConverters = this.m_referencedTask.getVariableMatchers();
//		for(String var : varConverters.keySet()){
//			out.addAll(HAPResourceUtility.getConverterResourceIdFromRelationship(varConverters.get(var).discoverRelationships()));
//		}
		return out;	
	}

	@Override
	public List<HAPResourceId> getResources() {
		List<HAPResourceId> out = super.getResources();
		List<HAPResourceId> referenceResources = this.m_referencedTask.discoverResources(); 
		out.addAll(referenceResources);
		return out;
	}
	
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(REFERENCENAME, m_referenceName);
	}

	@Override
	public HAPMatchers discover(
			Map<String, HAPVariableInfo> variablesInfo,
			HAPDataTypeCriteria expectCriteria, 
			HAPProcessContext context,
			HAPDataTypeHelper dataTypeHelper) {
		return this.m_referencedTask.discoverVariable(variablesInfo, expectCriteria, context, dataTypeHelper);
	}
	
	@Override
	public HAPOperand cloneOperand() {
		HAPOperandReference out = new HAPOperandReference();
		this.cloneTo(out);
		return out;
	}
	
	protected void cloneTo(HAPOperandReference operand){
		super.cloneTo(operand);
		operand.m_referenceName = this.m_referenceName;
	}

}
