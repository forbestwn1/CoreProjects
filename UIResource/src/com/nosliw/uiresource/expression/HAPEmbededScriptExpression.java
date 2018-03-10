package com.nosliw.uiresource.expression;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPScript;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.runtime.HAPExecuteExpression;
import com.nosliw.uiresource.definition.HAPEmbededScriptExpressionInAttribute;

/**
 * This class represent all string value that contains script expressions 
 */
@HAPEntityWithAttribute
public class HAPEmbededScriptExpression extends HAPSerializableImp{

	@HAPAttribute
	public static final String UIID = "uiId";
	
	@HAPAttribute
	public static final String SCRIPTEXPRESSIONS = "scriptExpressions";
	
	@HAPAttribute
	public static final String SCRIPTFUNCTION = "scriptFunction";
	
	private String m_uiId;
	
	//a list of elements
	//   string 
	//   script expression
	private List<Object> m_elements;
	
	//all script expressions
	private Map<String, HAPScriptExpression> m_scriptExpressions;
	
	//javascript function to execute script expression 
	private HAPScript m_scriptFunction;

	private HAPExpressionSuiteManager m_expressionManager;
	
	public HAPEmbededScriptExpression(String uiId, HAPScriptExpression scriptExpression, HAPExpressionSuiteManager expressionManager){
		this.m_expressionManager = expressionManager;
		this.m_uiId = uiId;
		this.m_elements = new ArrayList<Object>();
		this.m_elements.add(scriptExpression);
		this.init();
	}
	
	public HAPEmbededScriptExpression(String uiId, List<Object> elements, HAPExpressionSuiteManager expressionManager){
		this.m_expressionManager = expressionManager;
		this.m_uiId = uiId;
		this.m_elements = elements;
		this.init();
	}

	public HAPEmbededScriptExpression(String uiId, String content, HAPExpressionSuiteManager expressionManager){
		this.m_expressionManager = expressionManager;
		this.m_uiId = uiId;
		this.m_elements = HAPScriptExpressionUtility.discoverUIExpressionInText(content, this.m_expressionManager);
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

		//build javascript function to execute the script
		String scriptExpressionDataParmName = "scriptExpressionData"; 
		StringBuffer funScript = new StringBuffer();
		Map<String, HAPExpression> expressions = new LinkedHashMap<String, HAPExpression>();
		int i = 0;
		for(Object ele : this.m_elements){
			if(i>0)  funScript.append("+");
			if(ele instanceof String){
				funScript.append("\""+ele+"\"");
			}
			else if(ele instanceof HAPScriptExpression){
				HAPScriptExpression scriptExpression = (HAPScriptExpression)ele;
				funScript.append(scriptExpressionDataParmName+"[\""+scriptExpression.getId()+"\"]");
			}
			i++;
		}
		
		InputStream javaTemplateStream = HAPFileUtility.getInputStreamOnClassPath(HAPEmbededScriptExpressionInAttribute.class, "EmbededScriptExpressionFunction.temp");
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put("functionScript", funScript.toString());
		templateParms.put("scriptExpressionData", scriptExpressionDataParmName);
		this.m_scriptFunction = new HAPScript(HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms));
	}
	
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
	
	public List<HAPScriptExpression> getScriptExpressions(){		return new ArrayList(this.m_scriptExpressions.values());	}
	public List<HAPExecuteExpression> getExpressions(){
		List<HAPExecuteExpression> out = new ArrayList<HAPExecuteExpression>();
		for(HAPScriptExpression scriptExpression : this.getScriptExpressions()){
			out.addAll(scriptExpression.getExpressions().values());
		}
		return out;
	}
	public String getUIId(){   return this.m_uiId;   }
	public HAPScript getScriptFunction(){   return this.m_scriptFunction;  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(UIID, this.m_uiId);
		jsonMap.put(SCRIPTEXPRESSIONS, HAPJsonUtility.buildJson(m_scriptExpressions, HAPSerializationFormat.JSON));
	}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(UIID, this.m_uiId);
		jsonMap.put(SCRIPTEXPRESSIONS, HAPJsonUtility.buildJson(m_scriptExpressions, HAPSerializationFormat.JSON_FULL));
		jsonMap.put(SCRIPTFUNCTION, m_scriptFunction.toStringValue(HAPSerializationFormat.JSON_FULL));
		typeJsonMap.put(SCRIPTFUNCTION, m_scriptFunction.getClass());
	}
}
