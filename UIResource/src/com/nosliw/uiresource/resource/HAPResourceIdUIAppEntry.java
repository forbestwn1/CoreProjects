package com.nosliw.uiresource.resource;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPResourceIdUIAppEntry  extends HAPResourceIdSimple{

	private HAPUIAppEntryId m_uiAppEntryId; 
	
	public HAPResourceIdUIAppEntry(){   super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIAPPENTRY);      }

	public HAPResourceIdUIAppEntry(HAPResourceIdSimple resourceId){
		this();
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdUIAppEntry(String idLiterate) {
		this();
		init(idLiterate, null);
	}

	public HAPResourceIdUIAppEntry(HAPUIAppEntryId uiAppEntryId){
		this();
		init(null, null);
		this.m_uiAppEntryId = uiAppEntryId;
		this.m_id = HAPManagerSerialize.getInstance().toStringValue(uiAppEntryId, HAPSerializationFormat.LITERATE); 
	}

	@Override
	protected void setId(String id){
		super.setId(id);
		this.m_uiAppEntryId = HAPUIAppEntryId.buildUIAppEntryId(id);
	}

	public HAPUIAppEntryId getUIAppEntryId(){	return this.m_uiAppEntryId;	}

	public HAPResourceIdUIApp getUIAppResourceId() {     
		HAPUIAppEntryId appEntryId = this.getUIAppEntryId();
		HAPResourceIdUIApp appResourceId = new HAPResourceIdUIApp(appEntryId.getAppId(), this.getSupplement());
		return appResourceId; 
	}
	
	@Override
	public HAPResourceIdUIAppEntry clone(){
		HAPResourceIdUIAppEntry out = new HAPResourceIdUIAppEntry();
		out.cloneFrom(this);
		return out;
	}
}
