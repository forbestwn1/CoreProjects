package com.nosliw.data.operation.java;

import com.nosliw.data.core.HAPOperation;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.resource.HAPResourceManager;
import com.nosliw.data.core.resource.HAPResources;

public class HAPOperationManagerJava implements HAPResourceManager{

	@Override
	public String getRuntimeInfo() {
		return null;
	}

	@Override
	public HAPResources getDataOperationResource(HAPOperation dataOpInfo) {
		return null;
	}

	@Override
	public HAPResources getExpressionExecuteResource(HAPExpression expression) {
		return null;
	}

	@Override
	public HAPResources prepareResources(HAPResources resources) {
		return null;
	}

}
