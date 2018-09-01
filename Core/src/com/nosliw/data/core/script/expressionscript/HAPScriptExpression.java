package com.nosliw.data.core.script.expressionscript;

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
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.runtime.HAPExecuteExpression;
import com.nosliw.data.core.script.context.HAPContext;

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
	public static final String ID = "id";
	
	@HAPAttribute
	public static final String SCRIPTFUNCTION = "scriptFunction";

	@HAPAttribute
	public static final String EXPRESSIONS = "expressions";
	
//	@HAPAttribute
//	public static final String VARIABLENAMES = "variableNames";
	
	//id of script expression
	private String m_id;
	
	//definition literate
	private String m_definition;

	//after parsing content, we got a list of elements in ui expression:
	//     	js expression:  HAPScriptExpressionScriptSegment
	//		data expression : HAPExpressionDefinition
	private List<Object> m_elements;

	//expressions used in script expression
	//element index ---- processed expression
	private Map<String, HAPExecuteExpression> m_expressions;

	//when script expression does not contain any variable
	//it means that the script expression can be executed and get result during expression processing stage
	//then script expression turn to constant instead
	private boolean m_isConstant;
	private Object m_value;
	
	public HAPScriptExpression(String id, String content){
		this.m_id = id;
		this.m_elements = new ArrayList<Object>();
		this.m_expressions = new LinkedHashMap<String, HAPExecuteExpression>();
		this.m_definition = content;
		this.parseDefinition();
		this.m_isConstant = false;
	}

	public String getId(){   return this.m_id;  }
	
	public List<Object> getElements(){  return this.m_elements;   }
	
	public String getDefinition(){  return this.m_definition;  } 

	public Map<String, HAPExecuteExpression> getExpressions(){   return this.m_expressions;    }
	
	public Set<String> getVariableNames(){ 
		Set<String> out = new HashSet<String>();
		for(Object ele : this.m_elements){
			if(ele instanceof HAPDefinitionExpression){
				HAPDefinitionExpression expDef = (HAPDefinitionExpression)ele;
				out.addAll(HAPOperandUtility.discoverVariables(expDef.getOperand()));
			}
			else if(ele instanceof HAPScriptExpressionScriptSegment){
				HAPScriptExpressionScriptSegment scriptSegment = (HAPScriptExpressionScriptSegment)ele;
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
			if(ele instanceof HAPDefinitionExpression){
				HAPDefinitionExpression expDef = (HAPDefinitionExpression)ele;
				HAPOperandUtility.updateConstantData(expDef.getOperand(), HAPScriptExpressionUtility.getConstantData(constantsValue));
			}
			else if(ele instanceof HAPScriptExpressionScriptSegment){
				HAPScriptExpressionScriptSegment scriptSegment = (HAPScriptExpressionScriptSegment)ele;
				scriptSegment.updateConstantValue(constantsValue);
			}
		}
	}
	
	//process all expression definitions in script expression
	public void processExpressions(HAPContextScriptExpressionProcess expressionContext, Map<String, String> configure, HAPExpressionSuiteManager expressionManager){
		//preprocess attributes operand in expressions, some attributes operand can be combine into one variable operand
		for(HAPDefinitionExpression expDef : this.getExpressionDefinitions()){
			HAPScriptExpressionUtility.processAttributeOperandInExpression(expDef, expressionContext.getVariables());
		}

		this.m_expressions = new LinkedHashMap<String, HAPExecuteExpression>();
		for(int i=0; i<this.m_elements.size(); i++) {
			Object element = this.m_elements.get(i);
			if(element instanceof HAPDefinitionExpression){
				HAPProcessContext context = new HAPProcessContext(); 
				m_expressions.put(i+"", expressionManager.compileExpression(null, (HAPDefinitionExpression)element, expressionContext.getExpressionDefinitionSuite(), null, configure, context));
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
				HAPDefinitionExpression expressionDefinition = new HAPDefinitionExpression(expressionStr);
				this.m_elements.add(expressionDefinition);
			}
			i++;
		}
	}
	
	public List<HAPDefinitionExpression> getExpressionDefinitions(){
		List<HAPDefinitionExpression> out = new ArrayList<HAPDefinitionExpression>();
		for(Object element : this.m_elements){
			if(element instanceof HAPDefinitionExpression){
				out.add((HAPDefinitionExpression)element);
			}
		}
		return out;
	}

	public List<HAPScriptExpressionScriptSegment> getScriptSegments(){
		List<HAPScriptExpressionScriptSegment> out = new ArrayList<HAPScriptExpressionScriptSegment>();
		for(Object element : this.m_elements){
			if(element instanceof HAPScriptExpressionScriptSegment){
				out.add((HAPScriptExpressionScriptSegment)element);
			}
		}
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ID, this.m_id);
		jsonMap.put(DEFINITION, this.m_definition);
//		jsonMap.put(VARIABLENAMES, HAPJsonUtility.buildJson(this.m_variableNames, HAPSerializationFormat.JSON));
		jsonMap.put(EXPRESSIONS, HAPJsonUtility.buildJson(m_expressions, HAPSerializationFormat.JSON));
	}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildFullJsonMap(jsonMap, typeJsonMap);
	}
}
