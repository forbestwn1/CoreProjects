package com.nosliw.uiresource.resource;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.uiresource.page.tag.HAPUITagId;

public class HAPResourceIdUITag  extends HAPResourceId{

	private HAPUITagId m_uiTagId; 
	
	public HAPResourceIdUITag(){}

	public HAPResourceIdUITag(HAPResourceId resourceId){
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdUITag(String idLiterate) {
		init(HAPConstant.RUNTIME_RESOURCE_TYPE_UITAG, idLiterate, null);
	}

	public HAPResourceIdUITag(HAPUITagId uiTagId){
		init(HAPConstant.RUNTIME_RESOURCE_TYPE_UITAG, null, null);
		this.m_uiTagId = uiTagId;
		this.m_id = HAPSerializeManager.getInstance().toStringValue(uiTagId, HAPSerializationFormat.LITERATE); 
	}

	@Override
	protected void setId(String id){
		super.setId(id);
		this.m_uiTagId = new HAPUITagId(id);
	}

	public HAPUITagId getUITagId(){  return this.m_uiTagId;	}
	
	@Override
	public HAPResourceIdUITag clone(){
		HAPResourceIdUITag out = new HAPResourceIdUITag();
		out.cloneFrom(this);
		return out;
	}

}
