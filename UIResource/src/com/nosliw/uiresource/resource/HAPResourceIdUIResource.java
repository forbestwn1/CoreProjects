package com.nosliw.uiresource.resource;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPResourceIdUIResource  extends HAPResourceIdSimple{

	private HAPUIResourceId m_uiResourceId; 
	
	public HAPResourceIdUIResource(){  super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIRESOURCE);  }

	public HAPResourceIdUIResource(HAPResourceIdSimple resourceId){
		this();
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdUIResource(String idLiterate) {
		this();
		init(idLiterate, null);
	}

	public HAPResourceIdUIResource(HAPUIResourceId uiResourceId){
		this();
		init(null, null);
		this.m_uiResourceId = uiResourceId;
		this.m_id = HAPManagerSerialize.getInstance().toStringValue(uiResourceId, HAPSerializationFormat.LITERATE); 
	}

	@Override
	protected void setId(String id){
		super.setId(id);
		this.m_uiResourceId = new HAPUIResourceId(id);
	}

	public HAPUIResourceId getUIResourceId(){  return this.m_uiResourceId;	}
	
	@Override
	public HAPResourceIdUIResource clone(){
		HAPResourceIdUIResource out = new HAPResourceIdUIResource();
		out.cloneFrom(this);
		return out;
	}
}
