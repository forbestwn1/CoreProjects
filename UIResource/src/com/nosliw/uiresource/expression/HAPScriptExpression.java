package com.nosliw.uiresource.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPJsonTypeAsItIs;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionDefinition;
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
	public static final String UIID = "uiId";
	
	@HAPAttribute
	public static final String CONTENT = "content";
	
	@HAPAttribute
	public static final String SCRIPTFUNCTION = "scriptFunction";

	@HAPAttribute
	public static final String EXPRESSIONS = "expressions";
	
	@HAPAttribute
	public static final String VARIABLENAMES = "variableNames";
	
	private String m_uiId;
	
	private String m_content;

	private HAPExpressionManager m_expressionManager;
	
	private String m_scriptFunction;
	
	//expressions used in script expression
	private Map<String, HAPExpression> m_expressions;
	
	private Set<String> m_variableNames;
	
	//store all elements in ui expression:
	//     	js expression:  HAPScriptExpressionScriptSegment
	//		data expression : HAPExpressionDefinition
	private List<Object> m_elements;
	
	public HAPScriptExpression(String uiId, String content, HAPExpressionManager expressionMan){
		this.m_expressionManager = expressionMan;
		this.m_uiId = uiId;
		this.m_content = content;
		this.m_elements = new ArrayList<Object>();
		this.parseContent();
		this.m_scriptFunction = HAPScriptExpressionUtility.buildScriptExpressionJSFunction(this);
	}
	
	public String getId(){  return this.m_uiId;  }
	
	public List<Object> getElements(){  return this.m_elements;   }
	
	public String getScriptFunction(){  return this.m_scriptFunction;  }
	
	public String getContent(){  return this.m_content;  } 
	
	public List<HAPExpressionDefinition> getExpressionDefinitions(){
		List<HAPExpressionDefinition> out = new ArrayList<HAPExpressionDefinition>();
		for(Object element : this.m_elements){
			if(element instanceof HAPExpressionDefinition){
				out.add((HAPExpressionDefinition)element);
			}
		}
		return out;
	}
	
	private void parseContent(){
		String content = this.m_content;
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
				this.m_elements.add(new HAPScriptExpressionScriptSegment(content.substring(0, index)));
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
				HAPExpressionDefinition expressionDefinition = this.m_expressionManager.newExpressionDefinition(expressionStr, this.m_uiId+"_"+i, null, null); 
				this.m_elements.add(expressionDefinition);
			}
			i++;
		}
	}
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(UIID, this.m_uiId);
		jsonMap.put(SCRIPTFUNCTION, m_scriptFunction);
		typeJsonMap.put(SCRIPTFUNCTION, HAPJsonTypeAsItIs.class);
		jsonMap.put(CONTENT, this.m_content);
	}
}
