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
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.runtime.HAPExecutableExpression;

/**
 * Represent script expression
 * Script expression is a mix of data expression and javascript expression
 * The result of script expression is javascript entity (string, boolean number, or object)
 * It is used in:
 * 		part of html text
 * 		tag attribute
 * 		constant definition
 */
@HAPEntityWithAttribute
public class HAPScriptExpression extends HAPSerializableImp{

	public static final String EXPRESSION_TOKEN_OPEN = "#|";
	public static final String EXPRESSION_TOKEN_CLOSE = "|#";

	@HAPAttribute
	public static final String DEFINITION = "definition";

	@HAPAttribute
	public static final String SCRIPTFUNCTION = "scriptFunction";

	@HAPAttribute
	public static final String EXPRESSIONS = "expressions";
	
	@HAPAttribute
	public static final String VARIABLENAMES = "variableNames";
	
	//definition literate
	private String m_definition;

	//after parsing content, we got a list of elements in ui expression:
	//     	js expression:  HAPScriptExpressionScriptSegment
	//		data expression : HAPExpressionDefinition
	private List<Object> m_elements;

	//expressions used in script expression
	//element index ---- processed expression
	private Map<String, HAPExecutableExpression> m_expressions;

	//when script expression does not contain any variable
	//it means that the script expression can be executed and get result during expression processing stage
	//then script expression turn to constant instead
	private boolean m_isConstant;
	private Object m_value;
	
	public HAPScriptExpression(String content){
		this.m_elements = new ArrayList<Object>();
		this.m_expressions = new LinkedHashMap<String, HAPExecutableExpression>();
		this.m_definition = content;
		this.parseDefinition();
		this.m_isConstant = false;
	}

	public boolean isDataExpression() {
		if(this.m_elements.size()==1 && this.m_elements.get(0) instanceof HAPExpressionInScriptExpression) return true;
		return false;
	}
	
	public void updateExpressionId(HAPUpdateName update) {
		for(Object ele : this.m_elements){
			if(ele instanceof HAPExpressionInScriptExpression){
				HAPExpressionInScriptExpression expDef = (HAPExpressionInScriptExpression)ele;
				expDef.setId(update.getUpdatedName(expDef.getId()));
			}
		}

		Map<String, HAPExecutableExpression> expressions = new LinkedHashMap<String, HAPExecutableExpression>();
		for(String id : this.m_expressions.keySet()) {
			expressions.put(update.getUpdatedName(id), this.m_expressions.get(id));
		}
		this.m_expressions.clear();
		this.m_expressions.putAll(expressions);
	}
	
	public List<Object> getElements(){  return this.m_elements;   }
	
	public String getDefinition(){  return this.m_definition;  } 

	public Map<String, HAPExecutableExpression> getExpressions(){   return this.m_expressions;    }
	
	public Set<String> getVariableNames(){ 
		Set<String> out = new HashSet<String>();
		for(Object ele : this.m_elements){
			if(ele instanceof HAPExpressionInScriptExpression){
				HAPExpressionInScriptExpression expDef = (HAPExpressionInScriptExpression)ele;
				out.addAll(HAPOperandUtility.discoverVariables(expDef.getOperand()));
			}
			else if(ele instanceof HAPScriptInScriptExpression){
				HAPScriptInScriptExpression scriptSegment = (HAPScriptInScriptExpression)ele;
				out.addAll(scriptSegment.getVariableNames());
			}
		}
		return out;
	}

	public boolean isConstant(){  return this.m_isConstant;  }
	public Object getValue(){  return this.m_value;  }
	public void setValue(Object value){  
		this.m_value = value;
		this.m_isConstant = true;
	}

	//update script constant information 
	public void updateWithConstantsValue(Map<String, Object> constantsValue) {
		for(Object ele : this.m_elements){
			if(ele instanceof HAPExpressionInScriptExpression){
				HAPExpressionInScriptExpression expDef = (HAPExpressionInScriptExpression)ele;
				HAPOperandUtility.updateConstantData(expDef.getOperand(), HAPUtilityScriptExpression.getConstantData(constantsValue));
			}
			else if(ele instanceof HAPScriptInScriptExpression){
				HAPScriptInScriptExpression scriptSegment = (HAPScriptInScriptExpression)ele;
				scriptSegment.updateConstantValue(constantsValue);
			}
		}
	}
	
	//process all expression definitions in script expression
	public void processExpressions(HAPContextScriptExpressionProcess expressionContext, Map<String, String> configure, HAPExpressionSuiteManager expressionManager){
		//preprocess attributes operand in expressions, some attributes operand can be combine into one variable operand
		for(HAPExpressionInScriptExpression expDef : this.getExpressionDefinitions()){
			HAPUtilityScriptExpression.processAttributeOperandInExpression(expDef, expressionContext.getVariables());
		}

		this.m_expressions = new LinkedHashMap<String, HAPExecutableExpression>();
		for(int i=0; i<this.m_elements.size(); i++) {
			Object element = this.m_elements.get(i);
			if(element instanceof HAPExpressionInScriptExpression){
				HAPExpressionInScriptExpression expEle = (HAPExpressionInScriptExpression)element;
				HAPProcessContext context = new HAPProcessContext();
				m_expressions.put(expEle.getId(), expressionManager.compileExpression(expEle, expressionContext.getExpressionDefinitionSuite(), null, configure, context));
			}
		}
	}
	 
	//parse definition to get segments
	private void parseDefinition(){
		String content = this.m_definition;
		int i = 0;
		while(HAPBasicUtility.isStringNotEmpty(content)){
			int index = content.indexOf(EXPRESSION_TOKEN_OPEN);
			if(index==-1){
				//no expression
				this.m_elements.add(new HAPScriptInScriptExpression(content));
				content = null;
			}
			else if(index!=0){
				//start with text
				HAPScriptInScriptExpression scriptSegment = new HAPScriptInScriptExpression(content.substring(0, index));
				this.m_elements.add(scriptSegment);
				content = content.substring(index);
			}
			else{
				//start with expression
				int expEnd = content.indexOf(EXPRESSION_TOKEN_CLOSE);
				int expStart = index + EXPRESSION_TOKEN_OPEN.length();
				//get expression element
				String expressionStr = content.substring(expStart, expEnd);
				content = content.substring(expEnd + EXPRESSION_TOKEN_CLOSE.length());
				//build expression definition
				HAPExpressionInScriptExpression expressionDefinition = new HAPExpressionInScriptExpression(expressionStr, i+"");
				this.m_elements.add(expressionDefinition);
			}
			i++;
		}
	}
	
	public List<HAPExpressionInScriptExpression> getExpressionDefinitions(){
		List<HAPExpressionInScriptExpression> out = new ArrayList<HAPExpressionInScriptExpression>();
		for(Object element : this.m_elements){
			if(element instanceof HAPExpressionInScriptExpression){
				out.add((HAPExpressionInScriptExpression)element);
			}
		}
		return out;
	}

	public List<HAPScriptInScriptExpression> getScriptSegments(){
		List<HAPScriptInScriptExpression> out = new ArrayList<HAPScriptInScriptExpression>();
		for(Object element : this.m_elements){
			if(element instanceof HAPScriptInScriptExpression){
				out.add((HAPScriptInScriptExpression)element);
			}
		}
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(DEFINITION, this.m_definition);
		jsonMap.put(VARIABLENAMES, HAPJsonUtility.buildJson(this.getVariableNames(), HAPSerializationFormat.JSON));
		jsonMap.put(EXPRESSIONS, HAPJsonUtility.buildJson(m_expressions, HAPSerializationFormat.JSON));
	}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildFullJsonMap(jsonMap, typeJsonMap);
	}
}
