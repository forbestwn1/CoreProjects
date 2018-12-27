package com.nosliw.data.core.script.expression;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.data.core.runtime.HAPExecutableExpression;

/**
 *  EmbededScriptExpression: a string which contains script expression
 */
@HAPEntityWithAttribute
public class HAPEmbededScriptExpression extends HAPSerializableImp{

	@HAPAttribute
	public static final String SCRIPTEXPRESSIONS = "scriptExpressions";
	
	private HAPDefinitionEmbededScriptExpression m_definition;
	
	private List<Object> m_elements;
	
	//all script expressions by id
	private Map<String, HAPScriptExpression> m_scriptExpressions;
	
	//from index in elements to script expression id
	private Map<Integer, String> m_indexToId;
	
	public HAPEmbededScriptExpression(HAPDefinitionEmbededScriptExpression definition) {
		this.m_elements = new ArrayList<Object>();
		this.m_indexToId = new LinkedHashMap<Integer, String>();
		this.m_scriptExpressions = new LinkedHashMap<String, HAPScriptExpression>();
		this.m_definition = definition;
	}
	
	public HAPDefinitionEmbededScriptExpression getDefinition() {   return this.m_definition;    }
	
	public void addElement(Object element) {
		int index = this.m_elements.size();
		this.m_elements.add(element);
		if(element instanceof HAPScriptExpression) {
			HAPScriptExpression scriptExp = (HAPScriptExpression)element;
			String scriptExpressionId = index + "";
			//update expression id in script expression 
			scriptExp.updateExpressionId(new HAPUpdateName() {
				@Override
				public String getUpdatedName(String name) {
					return scriptExpressionId+"_"+name;
				}});
			this.m_scriptExpressions.put(scriptExpressionId, scriptExp);
			this.m_indexToId.put(index, scriptExpressionId);
		}
	}
	
	public List<Object> getElements(){  return this.m_elements;  }
	
	public boolean isConstant(){
		boolean out = true;
		for(Object ele : this.m_definition.getElements()){
			if(ele instanceof HAPScriptExpression){
				if(!((HAPScriptExpression)ele).isConstant()){
					out = false;
				}
			}
		}
		return out;
	}
	
	public boolean isString() {  return this.m_definition.isString();  }
	
//	public void updateWithConstantsValue(Map<String, Object> constantsValue) {
//		for(Object ele : this.m_elements){
//			if(ele instanceof HAPScriptExpression){
//				((HAPScriptExpression)ele).updateWithConstantsValue(constantsValue);
//			}
//		}
//	}

	
	public String getValue(){
		if(this.isConstant()){
			StringBuffer out = new StringBuffer();
			for(Object ele : this.m_elements){
				if(ele instanceof HAPScriptExpression){
					HAPScriptExpression scriptExpression = (HAPScriptExpression)ele; 
					if(!scriptExpression.isConstant()){
						out.append(scriptExpression.getValue().toString());
					}
					else if(ele instanceof String){
						out.append(ele.toString());
					}
				}
			}
			return out.toString();
		}
		else{
			return null;
		}
	}
	
	public List<HAPScriptExpression> getScriptExpressionsList(){		return new ArrayList(this.m_scriptExpressions.values());	}
	
	public String getScriptExpressionIdByIndex(int index) {		return this.m_indexToId.get(index);	}
	
	public Map<String, HAPScriptExpression> getScriptExpressions(){  return this.m_scriptExpressions;  }
	public Map<String, HAPExecutableExpression> getExpressions(){
		Map<String, HAPExecutableExpression> out = new LinkedHashMap<String, HAPExecutableExpression>();
		for(HAPScriptExpression scriptExpression : this.m_scriptExpressions.values()){
			out.putAll(scriptExpression.getExpressions());
		}
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(SCRIPTEXPRESSIONS, HAPJsonUtility.buildJson(m_scriptExpressions, HAPSerializationFormat.JSON));
	}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(SCRIPTEXPRESSIONS, HAPJsonUtility.buildJson(m_scriptExpressions, HAPSerializationFormat.JSON_FULL));
	}
}
