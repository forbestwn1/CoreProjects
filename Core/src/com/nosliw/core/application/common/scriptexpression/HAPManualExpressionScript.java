package com.nosliw.core.application.common.scriptexpression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonTypeAsItIs;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.dataexpression.HAPContainerDataExpression;
import com.nosliw.core.application.common.scriptexpression.serialize.HAPScriptFunctionInfo;
import com.nosliw.core.application.common.scriptexpression.serialize.HAPUtilityScriptForExecuteJSScript;
import com.nosliw.core.application.common.withvariable.HAPWithVariableImp;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;

@HAPEntityWithAttribute
public class HAPManualExpressionScript extends HAPWithVariableImp implements HAPExpressionScript{

	private String m_type;
	
	private List<HAPManualSegmentScriptExpression> m_segments;
	
	private Set<String> m_varKeys = new HashSet<String>();

	private HAPContainerDataExpression m_dataExpressionContainer;
	
	public HAPManualExpressionScript(String type) {
		this.m_segments = new ArrayList<HAPManualSegmentScriptExpression>();
		this.m_type = type;
		this.m_dataExpressionContainer = new HAPContainerDataExpression(); 
	}
	
	@Override
	public String getType() {     return this.m_type;      }

	@Override
	public String getWithVariableEntityType() {		return HAPConstantShared.WITHVARIABLE_ENTITYTYPE_SCRIPTEXPRESSION;	}

	@Override
	public HAPContainerDataExpression getDataExpressionContainer() {   return this.m_dataExpressionContainer;  }
	
	public void addSegment(HAPManualSegmentScriptExpression segment) {	this.m_segments.add(segment);	}
	public List<HAPManualSegmentScriptExpression> getSegments(){    return this.m_segments;     }
	
	public Set<String> getVariableKeys(){   return this.m_varKeys;    }
	public void addVariableKey(String key) {   this.m_varKeys.add(key);    }

//	@Override
//	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
//		super.buildJsonMap(jsonMap, typeJsonMap);
//		jsonMap.put(TYPE, this.getType());
//		jsonMap.put(VARIABLEKEYS, HAPUtilityJson.buildJson(this.m_varKeys, HAPSerializationFormat.JSON));
//		jsonMap.put(DATAEXPRESSIONIDS, HAPUtilityJson.buildJson(this.m_dataExpressionId, HAPSerializationFormat.JSON));
//		
//		List<String> segmentArrayStr = new ArrayList<String>();
//		for(HAPManualSegmentScriptExpression segment : this.m_segments) {
//			segmentArrayStr.add(segment.toStringValue(HAPSerializationFormat.JSON));
//		}
//		jsonMap.put(SEGMENT, HAPUtilityJson.buildArrayJson(segmentArrayStr.toArray(new String[0])));
//	}
	
	@Override
	public void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJSJsonMap(jsonMap, typeJsonMap);
		HAPScriptFunctionInfo scriptFunctionInfo = HAPUtilityScriptForExecuteJSScript.buildExpressionFunctionInfo(this);
		
		String functionParmValue = "{}";
		List<HAPJSScriptInfo> childrenFun = scriptFunctionInfo.getChildren();
		if(!childrenFun.isEmpty()) {
			Map<String, String> funScriptMap = new LinkedHashMap<String, String>();
			Map<String, Class<?>> funScriptTypeMap = new LinkedHashMap<String, Class<?>>();
			for(HAPJSScriptInfo childFun : childrenFun) {
				funScriptMap.put(childFun.getName(), childFun.getScript());
				funScriptTypeMap.put(childFun.getName(), HAPJsonTypeAsItIs.class);
			}
			functionParmValue = HAPUtilityJson.buildMapJson(funScriptMap, funScriptTypeMap);
		}
		jsonMap.put(SUPPORTFUNCTION, functionParmValue);
		typeJsonMap.put(SUPPORTFUNCTION, HAPJsonTypeScript.class);

		jsonMap.put(SCRIPTFUNCTION, new HAPJsonTypeScript(scriptFunctionInfo.getMainScript().getScript()).toStringValue(HAPSerializationFormat.JSON_FULL));
		typeJsonMap.put(SCRIPTFUNCTION, HAPJsonTypeScript.class);
		
		jsonMap.put(DATAEXPRESSION, this.m_dataExpressionContainer.toStringValue(HAPSerializationFormat.JAVASCRIPT));
	}

//	@Override
//	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPManagerResource resourceManager) {
//		
//	}

}
