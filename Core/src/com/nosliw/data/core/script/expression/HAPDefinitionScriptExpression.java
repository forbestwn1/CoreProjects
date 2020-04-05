package com.nosliw.data.core.script.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;

//script expression definition
//it is used in embeded
public class HAPDefinitionScriptExpression extends HAPSerializableImp{

	public static final String EXPRESSION_TOKEN_OPEN = "#|";
	public static final String EXPRESSION_TOKEN_CLOSE = "|#";

	@HAPAttribute
	public static String DEFINITION = "definition";
	
	@HAPAttribute
	public static String SEGMENTS = "segments";
	
	//definition literate
	private String m_definition;

	//after parsing content, we got a list of elements in ui expression:
	//     	js expression:  HAPScriptInScriptExpression
	//		data expression : HAPExpressionInScriptExpression
	private List<Object> m_segments;

	public HAPDefinitionScriptExpression(String definition){
		this.m_segments = new ArrayList<Object>();
		this.m_definition = definition;
		this.parseDefinition();
	}
	
	public String getDefinition() {   return this.m_definition;  }

	public List<Object> getSegments(){  return this.m_segments;   }
	
	public boolean isDataExpression() {
		if(this.m_segments.size()==1 && this.m_segments.get(0) instanceof HAPDefinitionDataExpression) return true;
		return false;
	}
	
	public List<HAPDefinitionDataExpression> getExpressionDefinitions(){
		List<HAPDefinitionDataExpression> out = new ArrayList<HAPDefinitionDataExpression>();
		for(Object element : this.m_segments){
			if(element instanceof HAPDefinitionDataExpression){
				out.add((HAPDefinitionDataExpression)element);
			}
		}
		return out;
	}

	public List<HAPScriptInScriptExpression> getScriptSegments(){
		List<HAPScriptInScriptExpression> out = new ArrayList<HAPScriptInScriptExpression>();
		for(Object element : this.m_segments){
			if(element instanceof HAPScriptInScriptExpression){
				out.add((HAPScriptInScriptExpression)element);
			}
		}
		return out;
	}
	
	//parse definition to get segments
	private void parseDefinition(){
		String content = this.m_definition;
		int i = 0;
		while(HAPBasicUtility.isStringNotEmpty(content)){
			int index = content.indexOf(EXPRESSION_TOKEN_OPEN);
			if(index==-1){
				//no expression
				this.m_segments.add(new HAPScriptInScriptExpression(content));
				content = null;
			}
			else if(index!=0){
				//start with text
				HAPScriptInScriptExpression scriptSegment = new HAPScriptInScriptExpression(content.substring(0, index));
				this.m_segments.add(scriptSegment);
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
				HAPDefinitionDataExpression expressionDefinition = new HAPDefinitionDataExpression(expressionStr);
				this.m_segments.add(expressionDefinition);
			}
			i++;
		}
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DEFINITION, this.m_definition);
		jsonMap.put(SEGMENTS, HAPJsonUtility.buildJson(this.m_segments, HAPSerializationFormat.JSON));
	}
	
	public HAPDefinitionScriptExpression cloneScriptExpressionDefinition() {
		HAPDefinitionScriptExpression out = new HAPDefinitionScriptExpression(this.getDefinition());
		return out;
	}
	
}
