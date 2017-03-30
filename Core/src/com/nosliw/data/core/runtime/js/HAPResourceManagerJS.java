package com.nosliw.data.core.runtime.js;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPOperationId;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceManager;

public abstract class HAPResourceManagerJS  implements HAPResourceManager{

	public HAPResourceId buildResourceIdObject(String literate){
		String[] segs = HAPNamingConversionUtility.parseDetails(literate);
		String type = segs[0];
		String id = segs[1];
		String alias = null;
		if(segs.length>=3)   alias = segs[2];
		
		HAPResourceId out = null;
		switch(type){
		case HAPConstant.DATAOPERATION_RESOURCE_TYPE_DATATYPEOPERATION:
			out = new HAPResourceIdOperation(id, alias);
			break;
		case HAPConstant.DATAOPERATION_RESOURCE_TYPE_DATATYPE:
			out = new HAPResourceIdDataType(id, alias);
			break;
		case HAPConstant.DATAOPERATION_RESOURCE_TYPE_LIBRARY:
			out = new HAPResourceIdLibrary(id, alias);
			break;
		case HAPConstant.DATAOPERATION_RESOURCE_TYPE_HELPER:
			out = new HAPResourceId(HAPConstant.DATAOPERATION_RESOURCE_TYPE_HELPER, id, alias);
			break;
		}
		return out;
	}
	
	public String buildResourceIdLiterate(HAPResourceId resourceId){
		return resourceId.toStringValue(HAPSerializationFormat.LITERATE);
	}
	
	public HAPResourceId buildResourceIdFromIdData(Object resourceIdData, String alias){
		HAPResourceId out = null;
		if(resourceIdData instanceof HAPOperationId){
        	out = new HAPResourceIdOperation((HAPOperationId)resourceIdData, alias);
		}
		return out;
	}
}
