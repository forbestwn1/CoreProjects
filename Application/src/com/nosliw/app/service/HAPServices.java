package com.nosliw.app.service;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.datatype.HAPDataTypeInfo;

/*
 * this interface define all the service/method for UI
 * return type for all the method are service data
 * therefore, all the method may fail for some reason
 */
public interface HAPServices extends HAPEntityService{
	
	//*********************************  method about entity definition  ***************************
	public HAPServiceData getAllEntityDefinitions();
	public HAPServiceData getEntityDefinitionsByGroup(String group);
	public HAPServiceData getEntityDefinitionByName(String name);
	
	//*********************************  data type definition  ***************************
	public HAPServiceData getRelatedDataType(HAPDataTypeInfo info);
	public HAPServiceData getDataTypeOperationScript(HAPDataTypeInfo info);
	public HAPServiceData getAllDataTypes();
	
	//*********************************  ui resource  *******************************
	public HAPServiceData getUIResource(String name);
	public HAPServiceData getAllUIResources();
	
}
