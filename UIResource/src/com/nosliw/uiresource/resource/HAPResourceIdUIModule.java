package com.nosliw.uiresource.resource;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPResourceIdUIModule  extends HAPResourceIdSimple{

	private HAPUIModuleId m_uiModuleId; 
	
	public HAPResourceIdUIModule(){  super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIMODULE);    }

	public HAPResourceIdUIModule(HAPResourceIdSimple resourceId){
		this();
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdUIModule(String idLiterate) {
		this();
		init(idLiterate, null);
	}

	public HAPResourceIdUIModule(HAPUIModuleId uiModuleId){
		this();
		init(null, null);
		this.m_uiModuleId = uiModuleId;
		this.m_id = HAPSerializeManager.getInstance().toStringValue(uiModuleId, HAPSerializationFormat.LITERATE); 
	}

	@Override
	protected void setId(String id){
		super.setId(id);
		this.m_uiModuleId = new HAPUIModuleId(id);
	}

	public HAPUIModuleId getUIModuleId(){  return this.m_uiModuleId;	}
	
	@Override
	public HAPResourceIdUIModule clone(){
		HAPResourceIdUIModule out = new HAPResourceIdUIModule();
		out.cloneFrom(this);
		return out;
	}

}
