package com.nosliw.data.core.process.resource;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.runtime.HAPResourceId;

public class HAPResourceIdActivityPlugin  extends HAPResourceId{

	private HAPActivityPluginId m_activityPluginId; 
	
	public HAPResourceIdActivityPlugin(){}

	public HAPResourceIdActivityPlugin(HAPResourceId resourceId){
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdActivityPlugin(String idLiterate) {
		super(HAPConstant.RUNTIME_RESOURCE_TYPE_ACTIVITYPLUGIN, idLiterate);
	}

	public HAPResourceIdActivityPlugin(HAPActivityPluginId activityPluginId){
		super(HAPConstant.RUNTIME_RESOURCE_TYPE_ACTIVITYPLUGIN, null);
		this.m_activityPluginId = activityPluginId;
		this.m_id = HAPSerializeManager.getInstance().toStringValue(activityPluginId, HAPSerializationFormat.LITERATE); 
	}

	@Override
	protected void setId(String id){
		super.setId(id);
		this.m_activityPluginId = new HAPActivityPluginId(id);
	}

	public HAPActivityPluginId getProcessId(){  return this.m_activityPluginId;	}
	
	public HAPResourceIdActivityPlugin clone(){
		HAPResourceIdActivityPlugin out = new HAPResourceIdActivityPlugin();
		out.cloneFrom(this);
		return out;
	}

}
