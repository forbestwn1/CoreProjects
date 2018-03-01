package com.nosliw.data.core.task;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.task.expression.HAPDefinitionTaskExpression;

public class HAPTaskDefinitionManager {

	private static Map<String, Class<? extends HAPDefinitionTask>> taskClasses = new LinkedHashMap<String, Class<? extends HAPDefinitionTask>>();
	
	static{
		HAPTaskDefinitionManager.register(HAPDefinitionTaskExpression.class);
	}
	
	public static void register(Class<? extends HAPDefinitionTask> stepClass){
		try {
			HAPDefinitionTask step = stepClass.newInstance();
			taskClasses.put(step.getType(), stepClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static HAPDefinitionTask buildTask(Object obj){
		HAPDefinitionTask step = null;
		try{
			JSONObject jsonObj = (JSONObject)obj;
			String type = jsonObj.optString(HAPDefinitionTask.TYPE);
			if(HAPBasicUtility.isStringEmpty(type))  type = HAPConstant.DATATASK_TYPE_EXPRESSION;
			step = taskClasses.get(type).newInstance();
			step.buildObject(obj, HAPSerializationFormat.JSON);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return step;
	}
}
