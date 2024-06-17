package com.nosliw.uiresource.resource;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPSupplementResourceId;

public class HAPResourceIdUIApp  extends HAPResourceIdSimple{

	private HAPUIAppId m_uiAppId; 
	
	public HAPResourceIdUIApp(){  super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIAPP);    }

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
		this.m_id = HAPManagerSerialize.getInstance().toStringValue(uiAppId, HAPSerializationFormat.LITERATE); 
	}
	
	public HAPResourceIdUIApp(String id, HAPSupplementResourceId supplement){
		this();
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
