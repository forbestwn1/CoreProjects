package com.nosliw.data;

public interface HAPOperationManager {

	public String getLanguage();
	
	public HAPOperationResource getDataOperationResource(HAPOperationInfo dataOpInfo);

	public HAPOperationResource getExpressionExecuteResource(HAPExpression expression);

	public HAPOperationResource prepareResources(HAPOperationResource resources);

}
