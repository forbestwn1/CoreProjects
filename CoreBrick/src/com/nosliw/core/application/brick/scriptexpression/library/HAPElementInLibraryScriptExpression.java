package com.nosliw.core.application.brick.scriptexpression.library;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.application.common.dataexpression.HAPContainerDataExpression;
import com.nosliw.core.application.common.dataexpression.HAPWithDataExpression;
import com.nosliw.core.application.common.interactive.HAPInteractiveExpression;
import com.nosliw.core.application.common.interactive.HAPWithInteractiveExpression;
import com.nosliw.core.application.common.scriptexpression.HAPExpressionScript;
import com.nosliw.core.application.common.valueport.HAPGroupValuePorts;
import com.nosliw.core.application.common.valueport.HAPWithValuePortGroup;
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.core.application.common.withvariable.HAPWithVariable;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.resource.HAPManagerResource;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.runtime.HAPExecutableImpEntityInfo;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPElementInLibraryScriptExpression extends HAPExecutableImpEntityInfo implements HAPWithInteractiveExpression, HAPWithVariable, HAPWithValuePortGroup, HAPWithDataExpression{

	@HAPAttribute
	public static String EXPRESSION = "expression";
	
	@HAPAttribute
	public static String RESULTMATCHERS = "resultMatchers";

	private HAPExpressionScript m_scriptExpression;
	
	private HAPMatchers m_resultMatchers;

	private HAPInteractiveExpression m_interactive;
	
	private HAPContainerVariableInfo m_variableInfo;
	
	private HAPContainerDataExpression m_dataExpressionGroup;
	
	public HAPElementInLibraryScriptExpression() {
		this.m_variableInfo = new HAPContainerVariableInfo();
		this.m_dataExpressionGroup = new HAPContainerDataExpression();
	}
	
	public HAPExpressionScript getExpression() {	return m_scriptExpression;	}
	public void setExpression(HAPExpressionScript scriptExpression) {	this.m_scriptExpression = scriptExpression;	}

	@Override
	public HAPContainerDataExpression getDataExpressions() {    return this.m_dataExpressionGroup;     }
	
	
	public HAPMatchers getResultMatchers() {		return this.m_resultMatchers;	}
	public void setResultMatchers(HAPMatchers matchers) {    this.m_resultMatchers = matchers;    }

	public HAPInteractiveExpression getInteractive() {    return this.m_interactive;      }
	public void setInteractive(HAPInteractiveExpression interactive) {    this.m_interactive = interactive;     }
	
	@Override
	public HAPContainerVariableInfo getVariablesInfo() {   return this.m_variableInfo;  }
	public void setVariablesInfo(HAPContainerVariableInfo varsInfo) {    this.m_variableInfo = varsInfo;     }
	
	@Override
	public HAPGroupValuePorts getExternalValuePortGroup() {   return this.m_interactive.getExternalValuePortGroup();  }
	
	@Override
	public HAPGroupValuePorts getInternalValuePortGroup() {   return this.m_interactive.getInternalValuePortGroup();  }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(EXPRESSIONINTERFACE, HAPManagerSerialize.getInstance().toStringValue(this.m_interactive, HAPSerializationFormat.JSON));
		jsonMap.put(HAPWithVariable.VARIABLEINFOS, HAPManagerSerialize.getInstance().toStringValue(this.getVariablesInfo(), HAPSerializationFormat.JSON));
		jsonMap.put(EXPRESSION, HAPManagerSerialize.getInstance().toStringValue(this.getExpression(), HAPSerializationFormat.JSON));
		jsonMap.put(RESULTMATCHERS, HAPUtilityJson.buildJson(this.getResultMatchers(), HAPSerializationFormat.JSON));
		jsonMap.put(DATAEXPRESSION, this.m_dataExpressionGroup.toStringValue(HAPSerializationFormat.JSON));
	}
	
	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJSJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(EXPRESSION, HAPManagerSerialize.getInstance().toStringValue(this.getExpression(), HAPSerializationFormat.JAVASCRIPT));
	}
	
	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPManagerResource resourceManager) {
		super.buildResourceDependency(dependency, runtimeInfo, resourceManager);
		//get converter resource id from var converter in expression 
		HAPMatchers matchers = this.getResultMatchers();
		if(matchers!=null){
			dependency.addAll(matchers.getResourceDependency(runtimeInfo, resourceManager));
		}
	}

}
