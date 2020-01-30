package com.nosliw.uiresource.resource;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPResourceIdSupplement;

public class HAPResourceIdUIApp  extends HAPResourceIdSimple{

	private HAPUIAppId m_uiAppId; 
	
	public HAPResourceIdUIApp(){  super(HAPConstant.RUNTIME_RESOURCE_TYPE_UIMODULE);    }

	public HAPResourceIdUIApp(HAPResourceIdSimple resourceId){
		this();
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdUIApp(String idLiterate) {
		this();
		init(idLiterate, null);
	}

	public HAPResourceIdUIApp(HAPUIAppId uiAppId){
		this();
		init(null, null);
		this.m_uiAppId = uiAppId;
		this.m_id = HAPSerializeManager.getInstance().toStringValue(uiAppId, HAPSerializationFormat.LITERATE); 
	}
	
	public HAPResourceIdUIApp(String id, HAPResourceIdSupplement supplement){
		init(id, supplement);
	}

	@Override
	protected void setId(String id){
		super.setId(id);
		this.m_uiAppId = new HAPUIAppId(id);
	}

	public HAPUIAppId getUIAppId(){  return this.m_uiAppId;	}
	
	@Override
	public HAPResourceIdUIApp clone(){
		HAPResourceIdUIApp out = new HAPResourceIdUIApp();
		out.cloneFrom(this);
		return out;
	}

}
