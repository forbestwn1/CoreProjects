package com.nosliw.data.core.runtime.js.resource;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPResourceIdJSHelper extends HAPResourceId{

	private HAPJSHelperId m_helperId; 
	
	public HAPResourceIdJSHelper(){}

	public HAPResourceIdJSHelper(HAPResourceId resourceId){
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdJSHelper(String idLiterate) {
		init(HAPConstant.RUNTIME_RESOURCE_TYPE_JSHELPER, idLiterate, null);
	}

	public HAPResourceIdJSHelper(HAPJSHelperId helperId){
		init(HAPConstant.RUNTIME_RESOURCE_TYPE_JSHELPER, null, null);
		this.setHelperId(helperId);
	}

	@Override
	protected void setId(String id){
		super.setId(id);
		this.m_helperId = new HAPJSHelperId(id);
	}

	public HAPJSHelperId getHelper(){  return this.m_helperId;	}
	protected void setHelperId(HAPJSHelperId helperId){
		this.m_helperId = helperId;
		this.m_id = HAPSerializeManager.getInstance().toStringValue(helperId, HAPSerializationFormat.LITERATE); 
	}
	
	@Override
	public HAPResourceIdJSHelper clone(){
		HAPResourceIdJSHelper out = new HAPResourceIdJSHelper();
		out.cloneFrom(this);
		return out;
	}
}
