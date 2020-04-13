package com.nosliw.common.info;

public interface HAPEntityInfoWritable extends HAPEntityInfo{

	void setId(String id);
	
	void setName(String name);
	
	void setDescription(String description);
	
	void setInfo(HAPInfo info);
	
}
