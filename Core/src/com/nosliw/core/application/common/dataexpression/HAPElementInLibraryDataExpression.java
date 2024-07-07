package com.nosliw.core.application.common.dataexpression;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.application.common.interactive.HAPInteractiveExpression;
import com.nosliw.core.application.common.interactive.HAPWithInteractive;
import com.nosliw.core.application.common.valueport.HAPGroupValuePorts;
import com.nosliw.core.application.common.valueport.HAPWithValuePortGroup;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.resource.HAPManagerResource;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.runtime.HAPExecutableImpEntityInfo;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPElementInLibraryDataExpression extends HAPExecutableImpEntityInfo implements HAPWithInteractive, HAPWithValuePortGroup{

	@HAPAttribute
	public static String EXPRESSION = "expression";
	
	@HAPAttribute
	public static String RESULTMATCHERS = "resultMatchers";

	private HAPDataExpression m_dataExpression;
	
	private HAPMatchers m_resultMatchers;

	private HAPInteractiveExpression m_interactive;
	
	public HAPElementInLibraryDataExpression() {
	}
	
	public HAPDataExpression getExpression() {	return m_dataExpression;	}
	public void setExpression(HAPDataExpression dataExpression) {	this.m_dataExpression = dataExpression;	}

	public HAPMatchers getResultMatchers() {		return this.m_resultMatchers;	}
	public void setResultMatchers(HAPMatchers matchers) {    this.m_resultMatchers = matchers;    }

	public HAPInteractiveExpression getInteractive() {    return this.m_interactive;      }
	public void setInteractive(HAPInteractiveExpression interactive) {    this.m_interactive = interactive;     }
	
	@Override
	public HAPGroupValuePorts getExternalValuePortGroup() {   return this.m_interactive.getExternalValuePortGroup();  }
	
	@Override
	public HAPGroupValuePorts getInternalValuePortGroup() {   return this.m_interactive.getInternalValuePortGroup();  }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(INTERACTIVE, HAPManagerSerialize.getInstance().toStringValue(this.m_interactive, HAPSerializationFormat.JSON));
		jsonMap.put(EXPRESSION, HAPManagerSerialize.getInstance().toStringValue(this.getExpression(), HAPSerializationFormat.JSON));
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
