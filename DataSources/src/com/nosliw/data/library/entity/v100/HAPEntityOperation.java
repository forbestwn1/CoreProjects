package com.nosliw.data.library.entity.v100;

import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataOperation;
import com.nosliw.data.HAPDataType;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.HAPOperationContext;
import com.nosliw.data.HAPOperationInfoAnnotation;
import com.nosliw.data.basic.string.HAPStringData;

public class HAPEntityOperation extends HAPDataOperation{

	public HAPEntityOperation(HAPDataTypeManager man, HAPDataType dataType) {
		super(man, dataType);
	}

	@HAPOperationInfoAnnotation(in = { "entity:simple", "string:simple", "any" }, out = "entity:simple", description = "Set Entity attribute")
	public HAPData setAttribute(HAPData[] parms, HAPOperationContext opContext){
		HAPEntityData entityData = (HAPEntityData)parms[0];
		HAPStringData nameData = (HAPStringData)parms[1];
		
		HAPData out = entityData.getAttribute(nameData.getValue());
		return out;
	}

	@HAPOperationInfoAnnotation(in = { "entity:simple", "string:simple" }, out = "any", description = "Set Entity attribute")
	public HAPData getAttribute(HAPData[] parms, HAPOperationContext opContext){
		HAPEntityData entityData = (HAPEntityData)parms[0];
		HAPStringData nameData = (HAPStringData)parms[1];
		HAPData attrData = parms[2];
		
		HAPData out = entityData.setAttribute(nameData.getValue(), attrData);
		return out;
	}
	
	@HAPOperationInfoAnnotation(in = {  }, out = "entity:simple", description = "New Entity")
	public HAPData newEntity(HAPData[] parms, HAPOperationContext opContext){
		HAPEntity entity = (HAPEntity)this.getDataType();
		return entity.newData();
	}
	
}
