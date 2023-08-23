package com.nosliw.data.core.script.expression1.resource;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.script.expression1.HAPIdScriptGroup;

public class HAPResourceIdScriptGroup  extends HAPResourceIdSimple{

	private HAPIdScriptGroup m_scriptId; 
	
	public HAPResourceIdScriptGroup(){  super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSION);    }

	public HAPResourceIdScriptGroup(HAPResourceIdSimple resourceId){
		this();
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdScriptGroup(String idLiterate) {
		this();
		init(idLiterate, null);
	}

	public HAPResourceIdScriptGroup(HAPIdScriptGroup scriptId){
		this();
		init(null, null);
		this.m_scriptId = scriptId;
		this.m_id = HAPSerializeManager.getInstance().toStringValue(scriptId, HAPSerializationFormat.LITERATE); 
	}

	@Override
	protected void setId(String id){
		super.setId(id);
		this.m_scriptId = new HAPIdScriptGroup(id);
	}

	public HAPIdScriptGroup getUIModuleId(){  return this.m_scriptId;	}
	
	@Override
	public HAPResourceIdScriptGroup clone(){
		HAPResourceIdScriptGroup out = new HAPResourceIdScriptGroup();
		out.cloneFrom(this);
		return out;
	}

}
