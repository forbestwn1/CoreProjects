package com.nosliw.uiresource.resource;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.uiresource.page.tag.HAPUITagId;

public class HAPResourceIdUITag  extends HAPResourceIdSimple{

	private HAPUITagId m_uiTagId; 
	
	public HAPResourceIdUITag(){	super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UICUSTOMERTAG);	}

	public HAPResourceIdUITag(HAPResourceIdSimple resourceId){
		this();
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdUITag(String idLiterate) {
		this();
		init(idLiterate, null);
	}

	public HAPResourceIdUITag(HAPUITagId uiTagId){
		this();
		init(null, null);
		this.m_uiTagId = uiTagId;
		this.m_id = HAPManagerSerialize.getInstance().toStringValue(uiTagId, HAPSerializationFormat.LITERATE); 
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
