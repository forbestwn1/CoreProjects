package com.nosliw.uiresource.expression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPJsonTypeAsItIs;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionDefinition;
import com.nosliw.data.core.expression.HAPExpressionDefinitionSuite;
import com.nosliw.data.core.expression.HAPExpressionManager;

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

	//javascript function to execute script expression 
	private String m_scriptFunction;
	
	//expressions used in script expression
	private Map<String, HAPExpression> m_expressions;

	//variables used in script expression
	private Set<String> m_variableNames;
	
	private HAPExpressionManager m_expressionManager;
	
	
	public HAPScriptExpression(String content, HAPExpressionManager expressionMan){
		this.m_variableNames = new HashSet<String>();
		this.m_elements = new ArrayList<Object>();
		this.m_expressionManager = expressionMan;
		this.m_definition = content;
		this.parseDefinition();
		this.m_scriptFunction = HAPScriptExpressionUtility.buildScriptExpressionJSFunction(this);
	}
	
	public List<Object> getElements(){  return this.m_elements;   }
	
	public String getScriptFunction(){  return this.m_scriptFunction;  }
	
	public String getDefinition(){  return this.m_definition;  } 

	public Map<String, HAPExpression> getExpressions(){   return this.m_expressions;    }
	
	public Set<String> getVariableNames(){   return this.m_variableNames;   }
	
	//process all expression definitions in script expression
	public void processExpressions(HAPUIResourceExpressionContext expressionContext){
		//preprocess attributes operand in expressions
		for(HAPExpressionDefinition expDef : this.getExpressionDefinitions()){
			HAPUIResourceExpressionUtility.processAttributeOperandInExpression(expDef, expressionContext.getVariables());
		}

		this.m_expressions = new LinkedHashMap<String, HAPExpression>();
		for(HAPExpressionDefinition expDef : this.getExpressionDefinitions()){
			m_expressions.put(expDef.getName(), this.m_expressionManager.processExpression(null, expDef, expressionContext.getExpressionDefinitionSuite(), expressionContext.getVariables()));
		}
	}
	
	//discover all variables in script expression
	public void discoverVarialbes(){
		this.m_variableNames = new HashSet<String>();
		for(Object ele : this.m_elements){
			if(ele instanceof HAPExpressionDefinition){
				HAPExpressionDefinition expDef = (HAPExpressionDefinition)ele;
				this.m_variableNames.addAll(this.m_expressions.get(expDef.getName()).getVariables());
			}
			else if(ele instanceof HAPScriptExpressionScriptSegment){
				HAPScriptExpressionScriptSegment scriptSegment = (HAPScriptExpressionScriptSegment)ele;
				this.m_variableNames.addAll(scriptSegment.getVariableNames());
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
				this.m_elements.add(new HAPScriptExpressionScriptSegment(content));
				content = null;
			}
			else if(index!=0){
				//start with text
				HAPScriptExpressionScriptSegment scriptSegment = new HAPScriptExpressionScriptSegment(content.substring(0, index));
				this.m_elements.add(scriptSegment);
//				this.m_variableNames.addAll(scriptSegment.getVariableNames());
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
				HAPExpressionDefinition expressionDefinition = this.m_expressionManager.newExpressionDefinition(expressionStr, i+"", null, null); 
				this.m_elements.add(expressionDefinition);
			}
			i++;
		}
	}
	
	private List<HAPExpressionDefinition> getExpressionDefinitions(){
		List<HAPExpressionDefinition> out = new ArrayList<HAPExpressionDefinition>();
		for(Object element : this.m_elements){
			if(element instanceof HAPExpressionDefinition){
				out.add((HAPExpressionDefinition)element);
			}
		}
		return out;
	}
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(SCRIPTFUNCTION, m_scriptFunction);
		typeJsonMap.put(SCRIPTFUNCTION, HAPJsonTypeAsItIs.class);
		jsonMap.put(DEFINITION, this.m_definition);
		jsonMap.put(VARIABLENAMES, HAPJsonUtility.buildJson(this.m_variableNames, HAPSerializationFormat.JSON));
		jsonMap.put(EXPRESSIONS, HAPJsonUtility.buildJson(m_expressions, HAPSerializationFormat.JSON));
	}
}
