package com.nosliw.data.core.task.expression;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.expression.HAPDefinitionExpression;

public class HAPDefinitionStepLoop extends HAPDefinitionStep{

	@HAPAttribute
	public static String CONTAINER = "container";

	@HAPAttribute
	public static String ELEMENTVARIABLE = "elementVariable";

	@HAPAttribute
	public static String EXECUTETASK = "executeTask";
	
	@HAPAttribute 
	public static String OUTPUTVARIABLE = "outputVariable";
	
	@HAPAttribute
	public static String BREAK = "break";
	
	private HAPDefinitionExpression m_container;
	
	private String m_elementVariable;
	
	private String m_executeTask;
	
	private String m_outputVariable;
	
	private HAPDefinitionExpression m_break;
	
	@Override
	public String getType() {  return HAPConstant.EXPRESSIONTASK_STEPTYPE_LOOP;	}

	public HAPDefinitionExpression getContainer() {  return this.m_container;   }
	
	public String getElementVariable() {  return this.m_elementVariable;   }
	
	public String getExecuteTask() {   return this.m_executeTask;   }
	
	public String getOutputVariable() {   return this.m_outputVariable;  }

	public HAPDefinitionExpression getBreak() {  return this.m_break;   }

	@Override
	public Set<String> getVariableNames() {
		Set<String> out = new HashSet<String>();
		out.addAll(this.m_container.getVariableNames());
		out.add(this.m_elementVariable);
		return out;
	}

	@Override
	public Set<String> getReferenceNames() {
		Set<String> out = new HashSet<String>();
		out.addAll(this.m_container.getReferenceNames());
		return out;
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			super.buildObjectByJson(json);
			JSONObject jsonObj = (JSONObject)json;

			this.m_elementVariable = jsonObj.getString(ELEMENTVARIABLE);
			this.m_container = new HAPDefinitionExpression(jsonObj.getString(CONTAINER));
			this.m_executeTask = jsonObj.getString(EXECUTETASK);
			this.m_outputVariable = jsonObj.getString(OUTPUTVARIABLE);
			if(HAPBasicUtility.isStringNotEmpty(jsonObj.optString(BREAK)))	this.m_break = new HAPDefinitionExpression(jsonObj.optString(BREAK));
			return true;  
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
	}
}
