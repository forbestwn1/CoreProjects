package com.nosliw.data.core.imp.expression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionDefinition;
import com.nosliw.data.core.expression.HAPExpressionTask;
import com.nosliw.data.core.expression.HAPExpressionUtility;
import com.nosliw.data.core.expression.HAPMatchers;
import com.nosliw.data.core.expression.HAPOperand;
import com.nosliw.data.core.expression.HAPOperandVariable;
import com.nosliw.data.core.expression.HAPProcessVariablesContext;
import com.nosliw.data.core.expression.HAPReferenceInfo;
import com.nosliw.data.core.expression.HAPVariableInfo;

/**
 * Parsed expression 
 */
public class HAPExpressionImp extends HAPSerializableImp implements HAPExpression{

	// id
	private String m_id;
	
	// original expressiong
	private HAPExpressionDefinitionImp m_expressionDefinition;

	// parsed operand in expression
	private HAPOperand m_operand;

	//referenced expression
	private Map<String, HAPExpression> m_references;
	
	// mutiple messages
	private List<String> m_errorMsgs;
	
	// store all variable information in expression (variable name -- variable data type infor)
	// for variable that we don't know data type, its value in this map is null
	private Map<String, HAPVariableInfo> m_varsInfo;
	
	// store all the converter for every variables in this expression
	// it convert variable from caller to variable in expression
	private Map<String, HAPMatchers> m_converters;
	
	public HAPExpressionImp(HAPExpressionDefinitionImp expressionDefinition, HAPOperand operand){
		this.m_references = new LinkedHashMap<String, HAPExpression>();
		this.m_errorMsgs = new ArrayList<String>();
		this.m_expressionDefinition = expressionDefinition;
		this.m_operand = operand;
		
		//find all variables, build default var criteria
		Set<String> varsName = HAPExpressionUtility.discoveryVariables(this.m_operand);
		Map<String, HAPDataTypeCriteria> varsCriteria = this.m_expressionDefinition.getVariableCriterias();
		for(String varName : varsName){
			if(varsCriteria.get(varName)==null){
				varsCriteria.put(varName, null);
			}
		}
		
		//build vars info
		this.m_varsInfo = new LinkedHashMap<String, HAPVariableInfo>();
		Map<String, HAPDataTypeCriteria> varCriterias = this.m_expressionDefinition.getVariableCriterias();
		for(String varName : varCriterias.keySet()){
			this.m_varsInfo.put(varName, new HAPVariableInfo(varCriterias.get(varName)));
		}
	}

	@Override
	public String getId(){  return this.m_id;  }
	
	@Override
	public void setId(String id){  this.m_id = id;  }
	
	@Override
	public HAPExpressionDefinition getExpressionDefinition() {		return this.m_expressionDefinition;	}

	@Override
	public HAPOperand getOperand() {  return this.m_operand;  }

	@Override
	public Map<String, HAPVariableInfo> getVariableInfos() {		return this.m_varsInfo;	}
	
	@Override
	public Map<String, HAPMatchers> getVariableMatchers(){		return this.m_converters;	}
	
	@Override
	public Map<String, HAPExpression> getReferences() {		return this.m_references;	}
	
	public HAPExpression getReference(String referenceName){  return this.m_references.get(referenceName);  }
	
	@Override
	public Set<String> getVariables(){
		Set<String> out = new HashSet<String>();
		out.addAll(this.m_varsInfo.keySet());
		for(String referenceName : this.m_references.keySet())		out.addAll(this.m_references.get(referenceName).getVariables());
		return out;
	}
	
	/**
	 * Rename variables, including
	 *  	variable in variable operand
	 *  	variable in variable criteria in definition
	 *  	variable in variable info
	 *  	variable in variable mapping in reference
	 */
	public void updateVariablesName(Map<String, String> varChanges){
		//update variable operand
		HAPExpressionUtility.processAllOperand(this.getOperand(), varChanges, new HAPExpressionTask(){
			@Override
			public boolean processOperand(HAPOperand operand, Object data) {
				Map<String, String> nameChanges = (Map<String, String>)data;
				String opType = operand.getType();
				if(opType.equals(HAPConstant.EXPRESSION_OPERAND_VARIABLE)){
					HAPOperandVariable variableChild = (HAPOperandVariable)operand;
					String newName = nameChanges.get(variableChild.getVariableName());
					if(newName!=null)	variableChild.setVariableName(newName);
				}
				return true;
			}
		});	
		
		//update variable criteria in definition
		Map<String, HAPDataTypeCriteria> oldVarsDef = this.getExpressionDefinition().getVariableCriterias();
		Map<String, HAPDataTypeCriteria> newVarsDef = new LinkedHashMap<String, HAPDataTypeCriteria>();
		for(String oldName : oldVarsDef.keySet()){
			String newName = varChanges.get(oldName);
			if(newName==null)  newName = oldName;
			newVarsDef.put(newName, oldVarsDef.get(oldName));
		}
		((HAPExpressionDefinitionImp)this.getExpressionDefinition()).setVariableCriterias(newVarsDef);
		
		//update variable info
		Map<String, HAPVariableInfo> newVarsInfo = new LinkedHashMap<String, HAPVariableInfo>();
		for(String oldVarName : this.m_varsInfo.keySet()){
			String newName = varChanges.get(oldVarName);
			if(newName==null)  newName = oldVarName;
			HAPVariableInfo varInfo = this.m_varsInfo.get(oldVarName);
			newVarsInfo.put(newName, varInfo);
		}
		this.m_varsInfo = newVarsInfo;
		
		//update variables mapping in reference info
		Map<String, HAPReferenceInfo> references = this.getExpressionDefinition().getReferences();
		for(String refName : references.keySet()){
			HAPReferenceInfoImp reference = (HAPReferenceInfoImp)references.get(refName);
			Map<String, String> newVarsMap = new LinkedHashMap<String, String>();
			Map<String, String> oldVarsMap = reference.getVariablesMap();
			for(String oldVarName : oldVarsMap.keySet()){
				String newName = varChanges.get(oldVarName);
				if(newName==null)  newName = oldVarName;
				newVarsMap.put(newName, oldVarsMap.get(oldVarName));
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
		String prefix = HAPExpressionUtility.buildFullVariableName(this.getId(), referenceName); 
		expression.setId(prefix);
		
		Set<String> variables = expression.getVariables();
		Map<String, String> varMapping = new LinkedHashMap<String, String>();
		for(String variableName : variables){
			varMapping.put(variableName, HAPExpressionUtility.buildFullVariableName(prefix, variableName));
		}
		expression.updateVariablesName(varMapping);

		this.m_references.put(referenceName, expression);   
	}
	
	@Override
	public HAPMatchers discover(Map<String, HAPVariableInfo> expectVariablesInfo, HAPDataTypeCriteria expectCriteria, HAPProcessVariablesContext context,	HAPDataTypeHelper dataTypeHelper){

		//update variables in expression
		for(String varName : this.getVariableInfos().keySet()){
			HAPVariableInfo expressionVar = this.getVariableInfos().get(varName);
			HAPVariableInfo expectVar = expectVariablesInfo.get(varName);
			if(expectVar==null){
			}
			if(expressionVar.getStatus().equals(HAPConstant.EXPRESSION_VARIABLE_STATUS_CLOSE)){
			}
			else{
				HAPDataTypeCriteria adjustedCriteria = dataTypeHelper.merge(expressionVar.getCriteria(), expectVar.getCriteria());
				expressionVar.setCriteria(adjustedCriteria);
			}
		}
		
		//do discovery
		Map<String, HAPVariableInfo> expressionVars = new LinkedHashMap<String, HAPVariableInfo>();
		expressionVars.putAll(this.getVariableInfos());
		
		HAPMatchers converter = null;
		Map<String, HAPVariableInfo> oldVars;
		//Do discovery until vars not change or fail 
		do{
			oldVars = new LinkedHashMap<String, HAPVariableInfo>();
			oldVars.putAll(expressionVars);
			
			context.clear();
			converter = this.getOperand().discover(expressionVars, expectCriteria, context, dataTypeHelper);
		}while(!HAPBasicUtility.isEqualMaps(expressionVars, oldVars) && context.isSuccess());
		
		//merge again, cal variable converters, update expect variable
		for(String varName : this.getVariableInfos().keySet()){
			HAPVariableInfo expressionVar = this.getVariableInfos().get(varName);
			HAPVariableInfo expectVar = expectVariablesInfo.get(varName);
			if(expectVar==null){
				expectVar = new HAPVariableInfo();
				expectVar.setCriteria(expressionVar.getCriteria());
				expectVariablesInfo.put(varName, expectVar);
			}
			if(expectVar.getStatus().equals(HAPConstant.EXPRESSION_VARIABLE_STATUS_CLOSE)){
			}
			else{
				expectVar.setCriteria(expressionVar.getCriteria());
			}

			//cal var converters
			HAPMatchers varConverters = dataTypeHelper.buildConvertor(expectVar.getCriteria(), expressionVar.getCriteria());
			this.m_converters.put(varName, varConverters);
		}
		
		return converter;
	}
	
	
	public void setVariables(Map<String, HAPVariableInfo> vars){
		this.m_varsInfo.clear();
		this.m_varsInfo.putAll(vars);
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
		jsonMap.put(EXPRESSIONDEFINITION, HAPSerializeManager.getInstance().toStringValue(this.m_expressionDefinition, HAPSerializationFormat.JSON));
		jsonMap.put(OPERAND, HAPSerializeManager.getInstance().toStringValue(this.m_operand, HAPSerializationFormat.JSON));
		jsonMap.put(VARIABLEINFOS, HAPJsonUtility.buildJson(this.getVariableInfos(), HAPSerializationFormat.JSON));
		jsonMap.put(ERRORMSGS, HAPJsonUtility.buildJson(m_errorMsgs, HAPSerializationFormat.JSON));
		jsonMap.put(CONVERTERS, HAPJsonUtility.buildJson(this.m_converters, HAPSerializationFormat.JSON));
	}

}
