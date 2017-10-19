package com.nosliw.uiresource.resource;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.runtime.HAPResourceId;

public class HAPResourceIdUIResource  extends HAPResourceId{

	private HAPUIResourceId m_uiResourceId; 
	
	public HAPResourceIdUIResource(){}

	public HAPResourceIdUIResource(HAPResourceId resourceId){
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdUIResource(String idLiterate) {
		super(HAPConstant.RUNTIME_RESOURCE_TYPE_UIRESOURCE, idLiterate);
	}

	public HAPResourceIdUIResource(HAPUIResourceId uiResourceId){
		super(HAPConstant.RUNTIME_RESOURCE_TYPE_UIRESOURCE, null);
		this.setGatewayId(uiResourceId);
	}

	@Override
	protected void setId(String id){
		super.setId(id);
		this.m_uiResourceId = new HAPUIResourceId(id);
	}

	public HAPUIResourceId getUIResourceId(){  return this.m_uiResourceId;	}
	protected void setGatewayId(HAPUIResourceId uiResourceId){
		this.m_uiResourceId = uiResourceId;
		this.m_id = HAPSerializeManager.getInstance().toStringValue(uiResourceId, HAPSerializationFormat.LITERATE); 
	}
	
	public HAPResourceIdUIResource clone(){
		HAPResourceIdUIResource out = new HAPResourceIdUIResource();
		out.cloneFrom(this);
		return out;
	}

}
