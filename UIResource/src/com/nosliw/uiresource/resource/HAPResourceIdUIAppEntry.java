package com.nosliw.uiresource.resource;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPResourceIdUIAppEntry  extends HAPResourceIdSimple{

	private HAPUIAppEntryId m_uiAppEntryId; 
	
	public HAPResourceIdUIAppEntry(){   super(HAPConstant.RUNTIME_RESOURCE_TYPE_UIAPPENTRY);      }

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
		this.m_id = HAPSerializeManager.getInstance().toStringValue(uiAppEntryId, HAPSerializationFormat.LITERATE); 
	}

	@Override
	protected void setId(String id){
		super.setId(id);
		this.m_uiAppEntryId = HAPUIAppEntryId.buildUIAppEntryId(id);
	}

	public HAPUIAppEntryId getUIAppEntryId(){	return this.m_uiAppEntryId;	}
	
	@Override
	public HAPResourceIdUIAppEntry clone(){
		HAPResourceIdUIAppEntry out = new HAPResourceIdUIAppEntry();
		out.cloneFrom(this);
		return out;
	}
}
