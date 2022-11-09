package com.nosliw.data.core.script.expression;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonTypeAsItIs;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.domain.entity.expression.HAPExecutableExpressionGroup;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutableImpEntityInfo;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;
import com.nosliw.data.core.runtime.js.util.script.expressionscrip.HAPScriptFunctionInfo;
import com.nosliw.data.core.runtime.js.util.script.expressionscrip.HAPUtilityScriptForExecuteJSScript;
import com.nosliw.data.core.valuestructure.HAPVariableInfoInStructure;

@HAPEntityWithAttribute
public class HAPExecutableScriptEntity extends HAPExecutableImpEntityInfo implements HAPExecutableScriptWithSegment{

	@HAPAttribute
	public static final String SCRIPTFUNCTION = "scriptFunction";

	@HAPAttribute
	public static final String SUPPORTFUNCTION = "supportFunction";

	@HAPAttribute
	public static final String VARIABLESINFO = "variablesInfo";

	@HAPAttribute
	public static final String VARIABLES = "variables";

	@HAPAttribute
	public static final String EXPRESSIONREF = "expressionRef";

	private HAPExecutableScriptWithSegment m_script;
	
	private String m_scriptType;
	
	//when script expression does not contain any variable
	//it means that the script expression can be executed and get result during expression processing stage
	//then script expression turn to constant instead
	private Boolean m_isConstant;
	private Object m_value;

	public HAPExecutableScriptEntity(String scriptType, String id) {
		this.m_scriptType = scriptType;
		this.m_script = new HAPExecutableScriptWithSegmentImp(id) {
			@Override
			public String getScriptType() { return null; }
		};
		
		this.setId(id);
	}
	
	public boolean isDataExpression() {
		List<HAPExecutableScript> segs = this.m_script.getSegments();
		if(segs.size()>=2)  return false;
		if(segs.size()==1) {
			HAPExecutableScript seg = segs.get(0);
			if(seg.getScriptType().equals(HAPConstantShared.SCRIPT_TYPE_SEG_EXPRESSION))  return true;
		}
		return false;
	}

	public boolean isConstant() {
//		if(this.m_isConstant==null) {
//			for(HAPDefinitionConstant constantDef : this.m_constantDefs.values()) {
//				if(constantDef.getValue()==null)  return this.m_isConstant = false;;
//			}
//			return this.m_isConstant = true;
//		}
//		return this.m_isConstant = false;
		return false;
	}
	
	public void setConstantValue(Object value) {	this.m_value = value;	}
	public Object getConstantValue() {   return this.m_value;    }
	
	@Override
	public String getScriptType() {  return this.m_scriptType;  }

	@Override
	public void addSegment(HAPExecutableScript segment) {    this.m_script.addSegment(segment);   }
	@Override
	public void addSegments(List<HAPExecutableScript> segments) {   this.m_script.addSegments(segments);    }
	@Override
	public List<HAPExecutableScript> getSegments(){    return this.m_script.getSegments();     }
	@Override
	public HAPVariableInfoInStructure discoverVariablesInfo1(HAPExecutableExpressionGroup expressionGroup) {return this.m_script.discoverVariablesInfo1(expressionGroup);}
	@Override
	public Set<String> discoverVariables(HAPExecutableExpressionGroup expressionGroup) {  return this.m_script.discoverVariables(expressionGroup); }

	@Override
	public Set<HAPDefinitionConstant> discoverConstantsDefinition(HAPExecutableExpressionGroup expressionGroup) {	return this.m_script.discoverConstantsDefinition(expressionGroup);	}
	@Override
	public Set<String> discoverExpressionReference(HAPExecutableExpressionGroup expressionGroup){	return this.m_script.discoverExpressionReference(expressionGroup);	}

	@Override
	public void updateConstant(Map<String, Object> value) {  this.m_script.updateConstant(value);  }
	
	@Override
	public void updateVariableName(HAPUpdateName nameUpdate) {  this.m_script.updateVariableName(nameUpdate);	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		HAPScriptFunctionInfo scriptFunctionInfo = HAPUtilityScriptForExecuteJSScript.buildFunctionInfo(this);
		
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

		jsonMap.put(EXPRESSIONREF, HAPUtilityJson.buildJson(this.discoverExpressionReference(null), HAPSerializationFormat.JSON));
		
		Set<String> vars = this.discoverVariables(null);
		jsonMap.put(VARIABLES, HAPUtilityJson.buildJson(vars, HAPSerializationFormat.JSON));
	}

	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		
	}

}
