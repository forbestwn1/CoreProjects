package com.nosliw.data.core.component.attachment;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoWritable;

public interface HAPAttachment extends HAPEntityInfoWritable{

	@HAPAttribute
	public static String VALUETYPE = "valueType";
	
	@HAPAttribute
	public static String ADAPTOR = "adaptor";
	
	String getType();

	String getValueType();
	void setValueType(String valueType);
	
	Object getAdaptor();
	
	HAPAttachment cloneAttachment();
}
