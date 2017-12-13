package com.nosliw.data.library.entity.v100;

import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.basic.string.HAPStringData;
import com.nosliw.data1.HAPDataOperation;
import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.data1.HAPOperationContext;
import com.nosliw.data1.HAPOperationInfoAnnotation;

public class HAPEntityOperation extends HAPDataOperation{

	public HAPEntityOperation(HAPDataTypeManager man, HAPDataType dataType) {
		super(man, dataType);
	}

	@HAPOperationInfoAnnotation(in = { "entity:simple", "string:simple", "any" }, out = "entity:simple", description = "Set Entity attribute")
	public HAPData setAttribute(HAPData[] parms, HAPOperationContext opContext){
		HAPEntityData entityData = (HAPEntityData)parms[0];
		HAPStringData nameData = (HAPStringData)parms[1];
		HAPData attrData = parms[2];
		
		entityData.setAttribute(nameData.getValue(), attrData);
		return entityData;
	}

	@HAPOperationInfoAnnotation(in = { "entity:simple", "string:simple" }, out = "any", description = "Set Entity attribute")
	public HAPData getAttribute(HAPData[] parms, HAPOperationContext opContext){
		HAPEntityData entityData = (HAPEntityData)parms[0];
		HAPStringData nameData = (HAPStringData)parms[1];
		return entityData.getAttribute(nameData.getValue());
	}
	
	@HAPOperationInfoAnnotation(in = {  }, out = "entity:simple", description = "New Entity")
	public HAPData newEntity(HAPData[] parms, HAPOperationContext opContext){
		HAPEntity entity = (HAPEntity)this.getDataType();
		return entity.newData();
	}
	
}
