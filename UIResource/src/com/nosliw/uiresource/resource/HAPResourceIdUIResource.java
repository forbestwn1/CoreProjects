package com.nosliw.uiresource.resource;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPResourceIdUIResource  extends HAPResourceId{

	private HAPUIResourceId m_uiResourceId; 
	
	public HAPResourceIdUIResource(){}

	public HAPResourceIdUIResource(HAPResourceId resourceId){
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdUIResource(String idLiterate) {
		init(HAPConstant.RUNTIME_RESOURCE_TYPE_UIRESOURCE, idLiterate, null);
	}

	public HAPResourceIdUIResource(HAPUIResourceId uiResourceId){
		init(HAPConstant.RUNTIME_RESOURCE_TYPE_UIRESOURCE, null, null);
		this.m_uiResourceId = uiResourceId;
		this.m_id = HAPSerializeManager.getInstance().toStringValue(uiResourceId, HAPSerializationFormat.LITERATE); 
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
