package com.nosliw.core.application.brick.dataexpression.library;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.application.common.dataexpression.HAPExpressionData;
import com.nosliw.core.application.common.interactive.HAPInteractiveExpression;
import com.nosliw.core.application.common.interactive.HAPWithInteractiveExpression;
import com.nosliw.core.data.matcher.HAPMatchers;
import com.nosliw.core.resource.HAPManagerResource;
import com.nosliw.core.resource.HAPResourceDependency;
import com.nosliw.core.runtime.HAPRuntimeInfo;
import com.nosliw.core.xxx.application1.HAPExecutableImp;

@HAPEntityWithAttribute
public class HAPElementInLibraryDataExpression extends HAPExecutableImp implements HAPWithInteractiveExpression{

	@HAPAttribute
	public static String EXPRESSION = "expression";
	
	@HAPAttribute
	public static String RESULTMATCHERS = "resultMatchers";

	private HAPExpressionData m_dataExpression;
	
	private HAPMatchers m_resultMatchers;

	private HAPInteractiveExpression m_interactive;
	
	public HAPElementInLibraryDataExpression() {
	}
	
	public HAPExpressionData getExpression() {	return m_dataExpression;	}
	public void setExpression(HAPExpressionData dataExpression) {	this.m_dataExpression = dataExpression;	}

	public HAPMatchers getResultMatchers() {		return this.m_resultMatchers;	}
	public void setResultMatchers(HAPMatchers matchers) {    this.m_resultMatchers = matchers;    }

	@Override
	public HAPInteractiveExpression getExpressionInterface() {    return this.m_interactive;      }
	public void setExpressionInteractive(HAPInteractiveExpression interactive) {    this.m_interactive = interactive;     }
	
	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJSJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(EXPRESSIONINTERFACE, HAPManagerSerialize.getInstance().toStringValue(this.m_interactive, HAPSerializationFormat.JSON));
		jsonMap.put(EXPRESSION, HAPManagerSerialize.getInstance().toStringValue(this.getExpression(), HAPSerializationFormat.JAVASCRIPT));
		jsonMap.put(RESULTMATCHERS, HAPUtilityJson.buildJson(this.getResultMatchers(), HAPSerializationFormat.JSON));
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
