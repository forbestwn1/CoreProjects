package com.nosliw.uiresource.resource;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPResourceIdUIModule  extends HAPResourceId{

	private HAPUIModuleId m_uiModuleId; 
	
	public HAPResourceIdUIModule(){}

	public HAPResourceIdUIModule(HAPResourceId resourceId){
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdUIModule(String idLiterate) {
		init(HAPConstant.RUNTIME_RESOURCE_TYPE_UIMODULE, idLiterate, null);
	}

	public HAPResourceIdUIModule(HAPUIModuleId uiModuleId){
		init(HAPConstant.RUNTIME_RESOURCE_TYPE_UIMODULE, null, null);
		this.m_uiModuleId = uiModuleId;
		this.m_id = HAPSerializeManager.getInstance().toStringValue(uiModuleId, HAPSerializationFormat.LITERATE); 
	}

	@Override
	protected void setId(String id){
		super.setId(id);
		this.m_uiModuleId = new HAPUIModuleId(id);
	}

	public HAPUIModuleId getUIResourceId(){  return this.m_uiModuleId;	}
	
	@Override
	public HAPResourceIdUIModule clone(){
		HAPResourceIdUIModule out = new HAPResourceIdUIModule();
		out.cloneFrom(this);
		return out;
	}

}
