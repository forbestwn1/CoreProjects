package com.nosliw.data.core.script.expression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.data.core.expression.HAPExecutableExpression;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.resource.HAPResourceData;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;
import com.nosliw.data.core.runtime.js.HAPRuntimeJSScriptUtility;

/**
 * Represent script expression executable
 * Script expression is a mix of data expression and javascript expression
 * The result of script expression is javascript entity (string, boolean number, or object)
 * It is used in:
 * 		part of html text
 * 		tag attribute
 * 		constant definition
 */
@HAPEntityWithAttribute
public class HAPScriptExpression extends HAPExecutableImp{

	@HAPAttribute
	public static final String DEFINITION = "definition";

	@HAPAttribute
	public static final String SCRIPTFUNCTION = "scriptFunction";

	@HAPAttribute
	public static final String EXPRESSIONS = "expressions";
	
	@HAPAttribute
	public static final String VARIABLENAMES = "variableNames";

	private HAPDefinitionScriptExpression m_definition;
	
	//ScriptInScriptExpression
	//HAPExecutableExpression
	private List<Object> m_elements;
	
	//expressions used in script expression
	//element index ---- processed expression
	private Map<String, HAPExecutableExpression> m_expressions;

	private Map<Integer, String> m_indexToId;
	
	//when script expression does not contain any variable
	//it means that the script expression can be executed and get result during expression processing stage
	//then script expression turn to constant instead
	private boolean m_isConstant;
	private Object m_value;

	public HAPScriptExpression(HAPDefinitionScriptExpression definition){
		this.m_elements = new ArrayList<Object>();
		this.m_expressions = new LinkedHashMap<String, HAPExecutableExpression>();
		this.m_indexToId = new LinkedHashMap<Integer, String>();
		this.m_definition = definition;
		this.m_isConstant = false;
	}
	
	public void updateExpressionId(HAPUpdateName update) {
		Map<String, HAPExecutableExpression> expressions = new LinkedHashMap<String, HAPExecutableExpression>();
		for(String id : this.m_expressions.keySet()) 		expressions.put(update.getUpdatedName(id), this.m_expressions.get(id));
		this.m_expressions.clear();
		this.m_expressions.putAll(expressions);
		
		Map<Integer, String> indexToId = new LinkedHashMap<Integer, String>();
		for(Integer index : this.m_indexToId.keySet())    indexToId.put(index, update.getUpdatedName(this.m_indexToId.get(index)));
		this.m_indexToId.clear();
		this.m_indexToId.putAll(indexToId);
	}
	
	public void addElement(Object ele) {
		int index = this.m_elements.size();
		this.m_elements.add(ele);
		if(ele instanceof HAPExecutableExpression) {
			String expId = index+"";
			this.m_expressions.put(expId, (HAPExecutableExpression)ele);
			this.m_indexToId.put(index, expId);
		}
	}
	
	public List<Object> getElements(){  return this.m_elements;   }

	public String getIdByIndex(int index) {   return this.m_indexToId.get(index);   }
	
	public HAPDefinitionScriptExpression getDefinition(){  return this.m_definition;  } 

	public Map<String, HAPExecutableExpression> getExpressions(){   return this.m_expressions;    }
	
	public boolean isConstant(){  return this.m_isConstant;  }
	public Object getValue(){  return this.m_value;  }
	public void setValue(Object value){  
		this.m_value = value;
		this.m_isConstant = true;
	}

	public boolean isDataExpression() {		return this.m_definition.isDataExpression();	}
	
	public Set<String> getVariableNames(){ 
		Set<String> out = new HashSet<String>();
		for(Object ele : this.m_elements){
			if(ele instanceof HAPExecutableExpression){
				HAPExecutableExpression expExe = (HAPExecutableExpression)ele;
				out.addAll(HAPOperandUtility.discoverVariables(expExe.getOperand()));
			}
			else if(ele instanceof HAPScriptInScriptExpression){
				HAPScriptInScriptExpression scriptSegment = (HAPScriptInScriptExpression)ele;
				out.addAll(scriptSegment.getVariableNames());
			}
		}
		return out;
	}

	public Set<String> getDataVariableNames(){
		Set<String> out = new HashSet<String>();
		for(Object ele : this.m_elements){
			if(ele instanceof HAPExecutableExpression){
				HAPExecutableExpression expExe = (HAPExecutableExpression)ele;
				out.addAll(HAPOperandUtility.discoverVariables(expExe.getOperand()));
			}
		}
		return out;
	}
	
	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		return HAPResourceDataFactory.createJSValueResourceData(HAPRuntimeJSScriptUtility.buildScriptExpressionJSFunction(this));
	}

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		for(HAPExecutableExpression exp : this.getExpressions().values()) {
			out.addAll(exp.getResourceDependency(runtimeInfo));
		}
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(DEFINITION, this.m_definition.getDefinition());
		jsonMap.put(VARIABLENAMES, HAPJsonUtility.buildJson(this.getVariableNames(), HAPSerializationFormat.JSON));
		jsonMap.put(EXPRESSIONS, HAPJsonUtility.buildJson(m_expressions, HAPSerializationFormat.JSON));
	}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildFullJsonMap(jsonMap, typeJsonMap);
	}

}
