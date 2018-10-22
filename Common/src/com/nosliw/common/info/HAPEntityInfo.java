package com.nosliw.common.info;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializable;

@HAPEntityWithAttribute
public interface HAPEntityInfo extends HAPSerializable{

	@HAPAttribute
	public static String NAME = "name";
	
	@HAPAttribute
	public static String DESCRIPTION = "description";
	
	@HAPAttribute
	public static String INFO = "info";
	
	HAPInfo getInfo();
	
	String getName();

	String getDescription();

	void setName(String name);
	
	void setDescription(String description);
	
	void setInfo(HAPInfo info);
	
	void cloneToEntityInfo(HAPEntityInfo entityInfo);
	
}
