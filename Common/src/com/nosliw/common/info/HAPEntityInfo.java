package com.nosliw.common.info;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializable;

@HAPEntityWithAttribute
public interface HAPEntityInfo extends HAPSerializable{

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String NAME = "name";
	
	@HAPAttribute
	public static String STATUS = "status";
	
	@HAPAttribute
	public static String DISPLAYNAME = "displayName";
	
	@HAPAttribute
	public static String DESCRIPTION = "description";
	
	@HAPAttribute
	public static String INFO = "info";
	
	String getId();
	void setId(String id);
	
	String getName();
	void setName(String name);

	String getStatus();
	void setStatus(String status);

	String getDisplayName();
	void setDisplayName(String name);
	
	String getDescription();
	void setDescription(String description);

	HAPInfo getInfo();
	void setInfo(HAPInfo info);
	
	void cloneToEntityInfo(HAPEntityInfo entityInfo);

	void buildEntityInfoByJson(Object json);
	
	HAPEntityInfo cloneEntityInfo();
}
