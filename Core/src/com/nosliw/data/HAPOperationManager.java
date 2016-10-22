package com.nosliw.data;

public interface HAPOperationManager {

	public String getLanguage();
	
	public HAPDataOperationResource getDataOperationResource(HAPDataOperationInfo dataOpInfo);

	public HAPDataOperationResource getExpressionExecuteResource(HAPExpression expression);

	public HAPDataOperationResource prepareResources(HAPDataOperationResource resources);

}
