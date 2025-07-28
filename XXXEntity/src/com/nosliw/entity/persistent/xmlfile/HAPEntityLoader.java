package com.nosliw.entity.persistent.xmlfile;

import com.nosliw.entity.data.HAPEntityWraper;
import com.nosliw.entity.dataaccess.HAPEntityDataAccess;

//entity loader works as a gate between the memory and the physical persistent

public interface HAPEntityLoader extends HAPEntityDataAccess{
	
	public void init();
	public void clear();
	public void load();
	
	public String getName();

	public void removeEntityFromPersist(HAPEntityWraper entityWraper);
	public void persistEntity(HAPEntityWraper entityWraper);
	
	
	public HAPEntityWraper parseString(String content, String format);
	
	public void afterLoad();
}
