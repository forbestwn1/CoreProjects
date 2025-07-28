package com.nosliw.core.data;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPResourceIdJSHelper extends HAPResourceIdSimple{

	private HAPJSHelperId m_helperId; 
	
	public HAPResourceIdJSHelper(){    super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_JSHELPER, "1.0.0");      }

	public HAPResourceIdJSHelper(HAPResourceIdSimple resourceId){
		this();
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdJSHelper(String idLiterate) {
		this();
		init(idLiterate, null);
	}

	public HAPResourceIdJSHelper(HAPJSHelperId helperId){
		this();
		init(null, null);
		this.setHelperId(helperId);
	}

	@Override
	public void setId(String id){
		super.setId(id);
		this.m_helperId = new HAPJSHelperId(id);
	}

	public HAPJSHelperId getHelper(){  return this.m_helperId;	}
	protected void setHelperId(HAPJSHelperId helperId){
		this.m_helperId = helperId;
		this.m_id = HAPManagerSerialize.getInstance().toStringValue(helperId, HAPSerializationFormat.LITERATE); 
	}
	
	@Override
	public HAPResourceIdJSHelper clone(){
		HAPResourceIdJSHelper out = new HAPResourceIdJSHelper();
		out.cloneFrom(this);
		return out;
	}
}
