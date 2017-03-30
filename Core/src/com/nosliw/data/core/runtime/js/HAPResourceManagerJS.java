package com.nosliw.data.core.runtime.js;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceManager;

public abstract class HAPResourceManagerJS  implements HAPResourceManager{

	public HAPResourceId buildResourceId(String literate){
		String[] segs = HAPNamingConversionUtility.parseDetails(literate);
		String type = segs[0];
		String id = segs[1];
		
		HAPResourceId out = null;
		switch(type){
		case HAPConstant.DATAOPERATION_RESOURCE_TYPE_DATATYPEOPERATION:
			out = new HAPResourceIdOperation(id);
			break;
		case HAPConstant.DATAOPERATION_RESOURCE_TYPE_DATATYPE:
			out = new HAPResourceIdDataType(id);
			break;
		case HAPConstant.DATAOPERATION_RESOURCE_TYPE_LIBRARY:
			out = new HAPResourceIdLibrary(id);
			break;
		case HAPConstant.DATAOPERATION_RESOURCE_TYPE_HELPER:
			out = new HAPResourceId(HAPConstant.DATAOPERATION_RESOURCE_TYPE_HELPER, id);
			break;
		}
		return out;
	}
}
