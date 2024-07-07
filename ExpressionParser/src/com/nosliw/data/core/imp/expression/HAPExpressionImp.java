package com.nosliw.data.core.imp.expression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.common.dataexpression.HAPOperand;
import com.nosliw.core.application.common.dataexpression.HAPOperandVariable;
import com.nosliw.core.application.common.dataexpression.HAPUtilityOperand;
import com.nosliw.core.application.common.dataexpression.HAPWrapperOperand;
import com.nosliw.core.application.common.variable.HAPVariableInfo;
import com.nosliw.core.application.division.manual.common.dataexpression.HAPInterfaceProcessOperand;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.matcher.HAPMatchers;

/**
 * Parsed expression 
 */
public class HAPExpressionImp{
/*
extends HAPSerializableImp implements HAPExpression{

	//unique in system
	private String m_id;
	
	// name (unique within expression, used to name sub expression)
	private String m_name;
	
	// original expression definition
	private HAPDefinitionTask m_expressionDefinition;

	// parsed operand in expression
	private HAPOperandWrapper m_operand;

	//referenced expression
	private Map<String, HAPExpression> m_references;
	
	// mutiple messages
	private List<String> m_errorMsgs;
	
	// store all variable information in expression (variable name -- variable data type infor)
	// for variable that we don't know data type, its value in this map is null
	// this variable info initially inherited from expression definition
	// and affected by variable definition in parent and operand discovery 
	// it is for internal use during processing
	private Map<String, HAPVariableInfo> m_localVarsInfo;

	// external variables info for expression
	// it is what this expression required for input
	private Map<String, HAPVariableInfo> m_varsInfo;
	
	// store all the matchers from variables info to internal variables info in expression
	// it convert variable from caller to variable in expression
	private Map<String, HAPMatchers> m_varsMatchers;
	
	public HAPExpressionImp(HAPDefinitionTask expressionDefinition, HAPOperand operand){
		this.m_references = new LinkedHashMap<String, HAPExpression>();
		this.m_errorMsgs = new ArrayList<String>();
		this.m_expressionDefinition = expressionDefinition;
		this.m_operand = new HAPOperandWrapper(operand);
		this.m_varsMatchers = new LinkedHashMap<String, HAPMatchers>();
		
		//find all variables, build default var criteria
		Set<String> varsName = HAPOperandUtility.discoveryVariables(this.m_operand);
		Map<String, HAPVariableInfo> varsCriteria = this.m_expressionDefinition.getVariableCriterias();
		for(String varName : varsName){
			if(varsCriteria.get(varName)==null){
				varsCriteria.put(varName, null);
			}
		}
		
		//build vars info
		this.m_localVarsInfo = new LinkedHashMap<String, HAPVariableInfo>();
		Map<String, HAPVariableInfo> varCriterias = this.m_expressionDefinition.getVariableCriterias();
		if(varCriterias!=null){
			for(String varName : varCriterias.keySet()){
				this.m_localVarsInfo.put(varName, new HAPVariableInfo(varCriterias.get(varName)));
			}
		}
	}

	public Map<String, HAPVariableInfo> getLocalVarsInfo(){  return this.m_localVarsInfo;  }
	
	public void setLocalVarsInfo(Map<String, HAPVariableInfo> vars){
		this.m_localVarsInfo.clear();
		this.m_localVarsInfo.putAll(vars);
	}
	
	@Override
	public String getId() {		return this.m_id;	}

	@Override
	public void setId(String id) {		this.m_id = id;	}
	
	@Override
	public String getName(){  return this.m_name;  }
	
	@Override
	public void setName(String name){  
		this.m_name = name;  
	
		Set<String> variables = this.getVariables();
		Map<String, String> varMapping = new LinkedHashMap<String, String>();
		for(String variableName : variables){
			varMapping.put(variableName, HAPExpressionTaskUtility.buildFullVariableName(name, variableName));
		}
		this.updateVariablesName(varMapping);
	}
	
	@Override
	public HAPDefinitionTask getExpressionDefinition() {		return this.m_expressionDefinition;	}

	@Override
	public HAPOperandWrapper getOperand() {  return this.m_operand;  }

	@Override
	public Map<String, HAPVariableInfo> getVariableInfos() {		return this.m_varsInfo;	}
	
	@Override
	public Map<String, HAPMatchers> getVariableMatchers(){		return this.m_varsMatchers;	}
	
	@Override
	public Map<String, HAPExpression> getReferences() {		return this.m_references;	}
	
	public HAPExpression getReference(String referenceName){  return this.m_references.get(referenceName);  }
	
	@Override
	public Set<String> getVariables(){
		Set<String> out = new HashSet<String>();
		out.addAll(this.m_localVarsInfo.keySet());
		for(String referenceName : this.m_references.keySet())		out.addAll(this.m_references.get(referenceName).getVariables());
		return out;
	}
	
	
//	 * Rename variables, including
//	 *  	variable in variable operand
//	 *  	variable in variable criteria in definition
//	 *  	variable in variable info
//	 *  	variable in variable mapping in reference
	 
	public void updateVariablesName(Map<String, String> varChanges){
		//update variable operand
		HAPOperandUtility.processAllOperand(this.getOperand(), varChanges, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				Map<String, String> nameChanges = (Map<String, String>)data;
				String opType = operand.getOperand().getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_VARIABLE)){
					HAPOperandVariable variableChild = (HAPOperandVariable)operand.getOperand();
					String newName = nameChanges.get(variableChild.getVariableName());
					if(newName!=null)	variableChild.setVariableName(newName);
				}
				return true;
			}
		});	
		
		//update variable criteria in definition
		Map<String, HAPVariableInfo> oldVarsDef = this.getExpressionDefinition().getVariableCriterias();
		Map<String, HAPVariableInfo> newVarsDef = new LinkedHashMap<String, HAPVariableInfo>();
		for(String oldName : oldVarsDef.keySet()){
			String newName = varChanges.get(oldName);
			if(newName==null)  newName = oldName;
			newVarsDef.put(newName, oldVarsDef.get(oldName));
		}
		this.getExpressionDefinition().setVariableCriterias(newVarsDef);
		
		//update variable info
		Map<String, HAPVariableInfo> newVarsInfo = new LinkedHashMap<String, HAPVariableInfo>();
		for(String oldVarName : this.m_localVarsInfo.keySet()){
			String newName = varChanges.get(oldVarName);
			if(newName==null)  newName = oldVarName;
			HAPVariableInfo varInfo = this.m_localVarsInfo.get(oldVarName);
			newVarsInfo.put(newName, varInfo);
		}
		this.m_localVarsInfo = newVarsInfo;
		
		//update variables mapping in reference info
		Map<String, HAPReferenceInfo> references = this.getExpressionDefinition().getReferences();
		for(String refName : references.keySet()){
			HAPReferenceInfoImp reference = (HAPReferenceInfoImp)references.get(refName);
			Map<String, String> newVarsMap = new LinkedHashMap<String, String>();
			Map<String, String> oldVarsMap = reference.getVariablesMap();
			for(String oldVarName : oldVarsMap.keySet()){
				String newNameFrom = varChanges.get(oldVarName);
				if(newNameFrom==null)  newNameFrom = oldVarName;
				String newNameTo = varChanges.get(oldVarsMap.get(oldVarName));
				if(newNameTo==null)  newNameTo = oldVarsMap.get(oldVarName);
				newVarsMap.put(newNameFrom, newNameTo);
			}
			reference.setVariableMap(newVarsMap);
		}
		
		//update variables in referenced expression
		for(String referenceName : this.m_references.keySet()){
			((HAPExpressionImp)this.m_references.get(referenceName)).updateVariablesName(varChanges);
		}
	}
	
	public void addReference(String referenceName, HAPExpressionImp expression){
		//modify variables in referenced expression by add prefix
		String prefix = HAPExpressionTaskUtility.buildFullVariableName(this.getName(), referenceName); 
		expression.setName(prefix);

		this.m_references.put(referenceName, expression);   
	}
	
	@Override
	public HAPMatchers discover(Map<String, HAPVariableInfo> parentVariablesInfo, HAPVariableInfo expectOutputCriteria, HAPProcessExpressionDefinitionContext context,	HAPDataTypeHelper dataTypeHelper){
		this.m_varsInfo = parentVariablesInfo;
		if(this.m_varsInfo==null)   this.m_varsInfo = new LinkedHashMap<String, HAPVariableInfo>();      
		
		//update variables info in expression according to parent variable info (parent variable info affect the child variable info)
		for(String varName : this.m_localVarsInfo.keySet()){
			HAPVariableInfo varInfo = this.m_localVarsInfo.get(varName);
			HAPVariableInfo parentVarInfo = this.m_varsInfo.get(varName);
			if(parentVarInfo!=null){
				if(varInfo.getStatus().equals(HAPConstant.EXPRESSION_VARIABLE_STATUS_OPEN)){
					HAPVariableInfo adjustedCriteria = dataTypeHelper.merge(varInfo.getCriteria(), parentVarInfo.getCriteria());
					varInfo.setCriteria(adjustedCriteria);
				}
			}
		}
		
		//do discovery on operand
		Map<String, HAPVariableInfo> varsInfo = new LinkedHashMap<String, HAPVariableInfo>();
		varsInfo.putAll(this.m_localVarsInfo);
		
		HAPMatchers matchers = null;
		Map<String, HAPVariableInfo> oldVarsInfo;
		//Do discovery until local vars definition not change or fail 
		do{
			oldVarsInfo = new LinkedHashMap<String, HAPVariableInfo>();
			oldVarsInfo.putAll(varsInfo);
			
			context.clear();
			matchers = this.getOperand().getOperand().discoverVariable(varsInfo, expectOutputCriteria, context, dataTypeHelper);
		}while(!HAPBasicUtility.isEqualMaps(varsInfo, oldVarsInfo) && context.isSuccess());
		this.m_localVarsInfo = varsInfo;
		
		//merge back, cal variable matchers, update parent variable
		for(String varName : this.m_localVarsInfo.keySet()){
			HAPVariableInfo varInfo = this.m_localVarsInfo.get(varName);
			HAPVariableInfo parentVarInfo = this.m_varsInfo.get(varName);
			if(parentVarInfo==null){
				parentVarInfo = new HAPVariableInfo();
				parentVarInfo.setCriteria(varInfo.getCriteria());
				this.m_varsInfo.put(varName, parentVarInfo);
			}
			else{
				if(parentVarInfo.getStatus().equals(HAPConstant.EXPRESSION_VARIABLE_STATUS_OPEN)){
					HAPVariableInfo adjustedCriteria = dataTypeHelper.merge(varInfo.getCriteria(), parentVarInfo.getCriteria());
					parentVarInfo.setCriteria(adjustedCriteria);
				}
			}

			//cal var converters
			HAPMatchers varMatchers = dataTypeHelper.buildMatchers(parentVarInfo.getCriteria(), varInfo.getCriteria());
			this.m_varsMatchers.put(varName, varMatchers);
		}
		
		return matchers;
	}
	
	
	@Override
	public String[] getErrorMessages() {
		if(this.m_errorMsgs==null || this.m_errorMsgs.size()==0)  return null;
		return this.m_errorMsgs.toArray(new String[0]);	
	}
	public void addErrorMessage(String msg){  this.m_errorMsgs.add(msg);  } 
	public void addErrorMessages(List<String> msgs){  this.m_errorMsgs.addAll(msgs);  } 
	
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		this.buildJsonMap(jsonMap, typeJsonMap);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ID, this.getId());
		jsonMap.put(NAME, this.getName());
		jsonMap.put(EXPRESSIONDEFINITION, HAPManagerSerialize.getInstance().toStringValue(this.m_expressionDefinition, HAPSerializationFormat.JSON));
		jsonMap.put(OPERAND, HAPManagerSerialize.getInstance().toStringValue(this.m_operand, HAPSerializationFormat.JSON));
		jsonMap.put(VARIABLEINFOS, HAPJsonUtility.buildJson(this.getVariableInfos(), HAPSerializationFormat.JSON));
		jsonMap.put(ERRORMSGS, HAPJsonUtility.buildJson(m_errorMsgs, HAPSerializationFormat.JSON));
		jsonMap.put(VARIABLESMATCHERS, HAPJsonUtility.buildJson(this.m_varsMatchers, HAPSerializationFormat.JSON));
		jsonMap.put(REFERENCES, HAPJsonUtility.buildJson(this.m_references, HAPSerializationFormat.JSON));
	}
	*/
}
