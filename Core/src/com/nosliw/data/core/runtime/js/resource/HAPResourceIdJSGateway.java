package com.nosliw.data.core.runtime.js.resource;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPResourceIdJSGateway extends HAPResourceIdSimple{

	private HAPJSGatewayId m_gatewayId; 
	
	public HAPResourceIdJSGateway(){    super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_JSGATEWAY, "1.0.0");     }

	public HAPResourceIdJSGateway(HAPResourceIdSimple resourceId){
		this();
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdJSGateway(String idLiterate) {
		this();
		init(idLiterate, null);
	}

	public HAPResourceIdJSGateway(HAPJSGatewayId gatewayId){
		this();
		init(null, null);
		this.setGatewayId(gatewayId);
	}

	@Override
	public void setId(String id){
		super.setId(id);
		this.m_gatewayId = new HAPJSGatewayId(id);
	}

	public HAPJSGatewayId getGatewayId(){  return this.m_gatewayId;	}
	protected void setGatewayId(HAPJSGatewayId gatewayId){
		this.m_gatewayId = gatewayId;
		this.m_id = HAPSerializeManager.getInstance().toStringValue(gatewayId, HAPSerializationFormat.LITERATE); 
	}
	
	@Override
	public HAPResourceIdJSGateway clone(){
		HAPResourceIdJSGateway out = new HAPResourceIdJSGateway();
		out.cloneFrom(this);
		return out;
	}

}
