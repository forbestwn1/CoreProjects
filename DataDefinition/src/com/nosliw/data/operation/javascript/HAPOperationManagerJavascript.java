package com.nosliw.data.operation.javascript;

import com.nosliw.data.HAPDataOperationInfo;
import com.nosliw.data.HAPDataOperationResource;
import com.nosliw.data.HAPExpression;
import com.nosliw.data.HAPOperationManager;

public class HAPOperationManagerJavascript implements HAPOperationManager{

	@Override
	public String getLanguage() {
		return null;
	}

	@Override
	public HAPDataOperationResource getDataOperationResource(HAPDataOperationInfo dataOpInfo) {
		return null;
	}

	@Override
	public HAPDataOperationResource getExpressionExecuteResource(HAPExpression expression) {
		return null;
	}

	@Override
	public HAPDataOperationResource prepareResources(HAPDataOperationResource resources) {
		return null;
	}

}
