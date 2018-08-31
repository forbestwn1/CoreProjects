package com.nosliw.data.core.script.expressionscript;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.HAPExecuteExpression;
import com.nosliw.data.core.script.context.HAPContext;

/**
 * This class represent all string value that contains script expressions 
 */
@HAPEntityWithAttribute
public class HAPEmbededScriptExpression extends HAPSerializableImp{

	@HAPAttribute
	public static final String SCRIPTEXPRESSIONS = "scriptExpressions";
	
	//a list of elements
	//   string 
	//   script expression
	private List<Object> m_elements;
	
	//all script expressions
	private Map<String, HAPScriptExpression> m_scriptExpressions;
	
	public HAPEmbededScriptExpression(HAPScriptExpression scriptExpression){
		this.m_elements = new ArrayList<Object>();
		this.m_elements.add(scriptExpression);
		this.init();
	}
	
	public HAPEmbededScriptExpression(List<Object> elements){
		this.m_elements = elements;
		this.init();
	}

	public HAPEmbededScriptExpression(String content){
		this.m_elements = HAPScriptExpressionUtility.discoverEmbededScriptExpression(content);
		this.init();
	}
	
	private void init(){
		this.m_scriptExpressions = new LinkedHashMap<String, HAPScriptExpression>();
		for(Object element : this.m_elements){
			if(element instanceof HAPScriptExpression){
				HAPScriptExpression scriptExp = (HAPScriptExpression)element;
				this.m_scriptExpressions.put(scriptExp.getId(), scriptExp);
			}
		}
	}
	
	public List<Object> getElements(){  return this.m_elements;  }
	
	public boolean isConstant(){
		boolean out = true;
		for(Object ele : this.m_elements){
			if(ele instanceof HAPScriptExpression){
				if(!((HAPScriptExpression)ele).isConstant()){
					out = false;
				}
			}
		}
		return out;
	}
	
	public void updateWithConstantsValue(Map<String, Object> constantsValue) {
		for(Object ele : this.m_elements){
			if(ele instanceof HAPScriptExpression){
				((HAPScriptExpression)ele).updateWithConstantsValue(constantsValue);
			}
		}
	}

	
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
	public Map<String, HAPScriptExpression> getScriptExpressions(){  return this.m_scriptExpressions;  }
	public List<HAPExecuteExpression> getExpressions(){
		List<HAPExecuteExpression> out = new ArrayList<HAPExecuteExpression>();
		for(HAPScriptExpression scriptExpression : this.getScriptExpressionsList()){
			out.addAll(scriptExpression.getExpressions().values());
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
