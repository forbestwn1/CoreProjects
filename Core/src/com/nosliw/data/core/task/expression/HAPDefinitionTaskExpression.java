package com.nosliw.data.core.task.expression;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.expression.HAPExpressionUtility;
import com.nosliw.data.core.task.HAPDefinitionTask;

public class HAPDefinitionTaskExpression extends HAPDefinitionTask{

	private HAPManagerTaskExpression m_expressionTaskManager;
	
	@HAPAttribute
	public static String STEPS = "steps";

	//all the information for references to other task, for instance, variable mapping
	@HAPAttribute
	public static String REFERENCES = "references";

	private Map<String, HAPReferenceInfo> m_referencesInfo;
	
	public HAPDefinitionTaskExpression(HAPManagerTaskExpression expressionTaskManager){
		this.m_referencesInfo = new LinkedHashMap<String, HAPReferenceInfo>();
		this.m_expressionTaskManager = expressionTaskManager;
	}
	
	@Override
	public String getType(){   return HAPConstant.DATATASK_TYPE_EXPRESSION;   };
	
	//steps within task
	public HAPDefinitionStep[] getSteps(){  return this.getChildren().toArray(new HAPDefinitionStep[0]);  }
	
	//references definition
	public Map<String, HAPReferenceInfo> getReferences(){  return this.m_referencesInfo;   }

	//process preference info in definition to add reference name to mapped variable name
	private void processReferenceInfos(){
		if(m_referencesInfo!=null){
			for(String ref : m_referencesInfo.keySet()){
				HAPReferenceInfo refInfo = m_referencesInfo.get(ref);
				Map<String, String> newVarMapping = new LinkedHashMap<String, String>();
				Map<String, String> varMapping = refInfo.getVariablesMap();
				for(String varName : varMapping.keySet()){
					newVarMapping.put(varName, HAPExpressionUtility.buildFullVariableName(ref, varMapping.get(varName)));
				}
				refInfo.setVariableMap(newVarMapping);
			}
		}
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			super.buildObjectByJson(json);
			JSONObject jsonObj = (JSONObject)json;

			{
				JSONObject refsObj = jsonObj.optJSONObject(REFERENCES);
				if(refsObj!=null){
					Iterator<String> its = refsObj.keys();
					while(its.hasNext()){
						String name = its.next();
						JSONObject refInfoObj = refsObj.optJSONObject(name);
						HAPReferenceInfo refInfo = new HAPReferenceInfo();
						refInfo.buildObject(refInfoObj, HAPSerializationFormat.JSON);
					}
				}
			}
			
			{
				JSONArray stepsArray = jsonObj.optJSONArray(STEPS);
				for(int i=0; i<stepsArray.length(); i++){
					this.addChild(HAPExpressionTaskUtility.buildExpressionStep(stepsArray.get(i), this.m_expressionTaskManager));
				}
			}
			
//			processReferenceInfos();
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
		jsonMap.put(REFERENCES, HAPJsonUtility.buildJson(this.m_referencesInfo, HAPSerializationFormat.JSON));
	}
	
}
