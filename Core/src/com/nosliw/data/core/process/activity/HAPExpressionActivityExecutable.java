package com.nosliw.data.core.process.activity;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.process.HAPActivityPluginId;
import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPExecutableActivityNormal;
import com.nosliw.data.core.process.resource.HAPResourceIdActivityPlugin;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.expression.HAPProcessContextScriptExpression;
import com.nosliw.data.core.script.expression.HAPScriptExpression;

public class HAPExpressionActivityExecutable extends HAPExecutableActivityNormal{


	private HAPProcessContextScriptExpression m_expressionProcessContext;

	
	private HAPScriptExpression m_scriptExpression;

	public HAPExpressionActivityExecutable(String id, HAPDefinitionActivity activityDef) {
		super(id, activityDef);
		this.m_expressionProcessContext = new HAPProcessContextScriptExpression();
	}

	public HAPProcessContextScriptExpression getScriptExpressionProcessContext() {
		return this.m_expressionProcessContext;
	}
	
	public void setScriptExpression(HAPScriptExpression scriptExpression) {    this.m_scriptExpression = scriptExpression;    }
	public HAPScriptExpression getScriptExpression() {   return this.m_scriptExpression;  }
	
	public HAPExpressionActivityDefinition getExpressionActivityDefinition() {   return (HAPExpressionActivityDefinition)this.getActivityDefinition();   }

	
	@Override
	public List<HAPResourceDependent> getResourceDependency(HAPRuntimeInfo runtimeInfo) {
		List<HAPResourceDependent> out = new ArrayList<HAPResourceDependent>();
		
		out.add(new HAPResourceDependent(new HAPResourceIdActivityPlugin(new HAPActivityPluginId(HAPConstant.ACTIVITY_TYPE_EXPRESSION))));
		
		out.addAll(this.m_scriptExpression.getResourceDependency(runtimeInfo));
		return out;
	} 

}
