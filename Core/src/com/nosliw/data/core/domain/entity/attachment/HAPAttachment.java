package com.nosliw.data.core.domain.entity.attachment;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritable;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

@HAPEntityWithAttribute
public interface HAPAttachment extends HAPEntityInfoWritable{

	@HAPAttribute
	public static String VALUETYPE = "valueType";
	
	@HAPAttribute
	public static String RAWVALUE = "RAWVALUE";
	
	@HAPAttribute
	public static String ENTITYID = "entityId";
	
	String getValueType();
	
	Object getRawValue();
	
	HAPIdEntityInDomain getEntityId();
	
	HAPAttachment cloneAttachment();
}
