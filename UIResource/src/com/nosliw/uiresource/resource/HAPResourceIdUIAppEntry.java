package com.nosliw.uiresource.resource;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPResourceIdUIAppEntry  extends HAPResourceId{

	private HAPUIAppEntryId m_uiAppEntryId; 
	
	public HAPResourceIdUIAppEntry(){}

	public HAPResourceIdUIAppEntry(HAPResourceId resourceId){
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdUIAppEntry(String idLiterate) {
		super(HAPConstant.RUNTIME_RESOURCE_TYPE_UIAPPENTRY, idLiterate);
	}

	public HAPResourceIdUIAppEntry(HAPUIAppEntryId uiAppEntryId){
		super(HAPConstant.RUNTIME_RESOURCE_TYPE_UIAPPENTRY, null);
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
