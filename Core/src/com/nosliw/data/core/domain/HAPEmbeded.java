package com.nosliw.data.core.domain;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

@HAPEntityWithAttribute
public abstract class HAPEmbeded extends HAPSerializableImp{

	@HAPAttribute
	public static String ENTITY = "entity";

	@HAPAttribute
	public static String ADAPTER = "adapter";

	private Object m_entity;
	
	private Object m_adapter;
	
	public HAPEmbeded() {}
	
	public HAPEmbeded(Object entity) {
		this.m_entity = entity;
	}
	
	public Object getEntity() {   return this.m_entity;   }
	public void setEntity(Object entity) {    this.m_entity = entity;    }
	
	public Object getAdapter() {	return m_adapter;	}
	public void setAdapter(Object adapter) {	this.m_adapter = adapter;	}

	public abstract HAPEmbeded cloneEmbeded();

}
