package com.nosliw.uiresource.definition;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.common.utils.HAPJsonTypeAsItIs;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.uiresource.expression.HAPScriptExpression;

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
	private String m_scriptFunction;

	public HAPEmbededScriptExpression(String uiId, HAPScriptExpression scriptExpression){
		this.m_uiId = uiId;
		this.m_elements = new ArrayList<Object>();
		this.m_elements.add(scriptExpression);
		this.init();
	}
	
	public HAPEmbededScriptExpression(String uiId, List<Object> elements){
		this.m_uiId = uiId;
		this.m_elements = elements;
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
		for(Object ele : this.m_elements){
			if(ele instanceof String){
				funScript.append(ele);
			}
			else if(ele instanceof HAPScriptExpression){
				HAPScriptExpression scriptExpression = (HAPScriptExpression)ele;
				funScript.append(scriptExpressionDataParmName+"[\""+scriptExpression.getId()+"\"]");
			}
		}
		
		InputStream javaTemplateStream = HAPFileUtility.getInputStreamOnClassPath(HAPEmbededScriptExpressionInAttribute.class, "EmbededScriptExpressionFunction.temp");
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put("functionScript", funScript.toString());
		templateParms.put("scriptExpressionData", scriptExpressionDataParmName);
		this.m_scriptFunction = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
	}
	
	public List<HAPScriptExpression> getScriptExpressions(){		return new ArrayList(this.m_scriptExpressions.values());	}
	public String getUIId(){   return this.m_uiId;   }
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(UIID, this.m_uiId);
		jsonMap.put(SCRIPTFUNCTION, m_scriptFunction);
		typeJsonMap.put(SCRIPTFUNCTION, HAPJsonTypeAsItIs.class);
		jsonMap.put(SCRIPTEXPRESSIONS, HAPJsonUtility.buildJson(m_scriptExpressions, HAPSerializationFormat.JSON));
	}
}
