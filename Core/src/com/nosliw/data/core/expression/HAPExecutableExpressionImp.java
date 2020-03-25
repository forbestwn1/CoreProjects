package com.nosliw.data.core.expression;

import java.util.List;
import java.util.Map;

import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.operand.HAPOperandTask;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.resource.HAPResourceData;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceInfo;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

//entity that can is runnable within runtime environment
public abstract class HAPExecutableExpressionImp extends HAPExecutableImp implements HAPExecutableExpression{

//	List<HAPResourceDependency> m_resources;
	
	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		super.buildResourceDependency(dependency, runtimeInfo, resourceManager);
		//get converter resource id from var converter in expression 
		Map<String, HAPMatchers> matchers = this.getVariableMatchers();
		if(matchers!=null){
			for(String varName : matchers.keySet()){
				dependency.addAll(matchers.get(varName).getResourceDependency(runtimeInfo, resourceManager));
			}
		}
		
		HAPOperandUtility.processAllOperand(this.getOperand(), dependency, new HAPOperandTask(){
			@Override
			public boolean processOperand(HAPOperandWrapper operand, Object data) {
				List<HAPResourceDependency> dependency = (List<HAPResourceDependency>)data;
				List<HAPResourceId> operandResourceIds = (List)operand.getOperand().getResources();
				List<HAPResourceInfo> resourceInfos = resourceManager.discoverResources(operandResourceIds, runtimeInfo);
				for(HAPResourceInfo resourceInfo : resourceInfos) {
					dependency.addAll(resourceInfo.getDependency());
				}

				return true;
			}
		});
//		return new ArrayList(result);

		
		
//		List<HAPResourceIdSimple> expressionDependency = HAPUtilityExpressionResource.discoverResources(this);
//		for(HAPResourceIdSimple id : expressionDependency) {
//			dependency.add(new HAPResourceDependency(id));
//		}
	}
//	@Override
//	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo) {
//		if(this.m_resources==null) {
//			List<HAPResourceIdSimple> expressionDependency = HAPUtilityExpressionResource.discoverResources(this);
//			this.m_resources = new ArrayList<HAPResourceDependency>();
//			for(HAPResourceIdSimple id : expressionDependency) {
//				this.m_resources.add(new HAPResourceDependency(id));
//			}
//		}
//		return this.m_resources;
//	}

	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		// TODO Auto-generated method stub
		return null;
	}
}
