package com.nosliw.data.core.service.resource;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.service.interfacee.HAPIdServcieInterface;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPResourceIdServiceInterface  extends HAPResourceIdSimple{

	private HAPIdServcieInterface m_serviceInterfaceId; 
	
	public HAPResourceIdServiceInterface(){	super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICEINTERFACE);	}

	public HAPResourceIdServiceInterface(HAPResourceIdSimple resourceId){
		this();
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdServiceInterface(String idLiterate) {
		this();
		init(idLiterate, null);
	}

	public HAPResourceIdServiceInterface(HAPIdServcieInterface serviceInterfaceId){
		this();
		init(null, null);
		this.m_serviceInterfaceId = serviceInterfaceId;
		this.m_id = HAPSerializeManager.getInstance().toStringValue(serviceInterfaceId, HAPSerializationFormat.LITERATE); 
	}

	@Override
	protected void setId(String id){
		super.setId(id);
		this.m_serviceInterfaceId = new HAPIdServcieInterface(id);
	}

	public HAPIdServcieInterface getServiceInterfaceId(){  return this.m_serviceInterfaceId;	}
	
	@Override
	public HAPResourceIdServiceInterface clone(){
		HAPResourceIdServiceInterface out = new HAPResourceIdServiceInterface();
		out.cloneFrom(this);
		return out;
	}

}
