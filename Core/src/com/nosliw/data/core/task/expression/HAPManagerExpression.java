package com.nosliw.data.core.task.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.task.HAPDefinitionTask;

public class HAPManagerExpression {

	private static Map<String, Class<? extends HAPDefinitionStep>> m_stepDefinitionClasses = new LinkedHashMap<String, Class<? extends HAPDefinitionStep>>();
	private static Map<String, HAPProcessorStep> m_stepProcessors = new LinkedHashMap<String, HAPProcessorStep>();
	
	static{
		HAPManagerExpression.registerStep(HAPDefinitionStepExpression.class, null);
	}
	
	public static void registerStep(Class<? extends HAPDefinitionStep> stepClass, HAPProcessorStep stepProcessor){
		try {
			HAPDefinitionStep step = stepClass.newInstance();
			m_stepDefinitionClasses.put(step.getType(), stepClass);
			m_stepProcessors.put(step.getType(), stepProcessor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static HAPDefinitionStep buildExpressionStep(Object obj){
		HAPDefinitionStep step = null;
		try{
			JSONObject jsonObj = (JSONObject)obj;
			String type = jsonObj.optString(HAPDefinitionTask.TYPE);
			if(HAPBasicUtility.isStringEmpty(type))  type = HAPConstant.EXPRESSIONTASK_STEPTYPE_EXPRESSION;
			step = m_stepDefinitionClasses.get(type).newInstance();
			step.buildObject(obj, HAPSerializationFormat.JSON);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return step;
	}
	
	public static HAPExecutableStep processStep(HAPDefinitionStep stepDef, Map<String, HAPData> constants, HAPProcessContext context) {
		return m_stepProcessors.get(stepDef.getType()).process(stepDef, constants, context);
	}
	
}
