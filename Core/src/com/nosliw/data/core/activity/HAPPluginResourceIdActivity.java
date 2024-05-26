package com.nosliw.data.core.activity;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.process1.HAPActivityPluginId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPPluginResourceIdActivity  extends HAPResourceIdSimple{

	private HAPActivityPluginId m_activityPluginId; 
	
	public HAPPluginResourceIdActivity(){    super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_ACTIVITYPLUGIN);    }

	public HAPPluginResourceIdActivity(HAPResourceIdSimple resourceId){
		this();
		this.cloneFrom(resourceId);
	}
	
	public HAPPluginResourceIdActivity(String idLiterate) {
		this();
		init(idLiterate, null);
	}

	public HAPPluginResourceIdActivity(HAPActivityPluginId activityPluginId){
		this();
		init(null, null);
		this.m_activityPluginId = activityPluginId;
		this.m_id = HAPSerializeManager.getInstance().toStringValue(activityPluginId, HAPSerializationFormat.LITERATE); 
	}

	@Override
	public void setId(String id){
		super.setId(id);
		this.m_activityPluginId = new HAPActivityPluginId(id);
	}

	public HAPActivityPluginId getActivityPlugId(){  return this.m_activityPluginId;	}
	
	@Override
	public HAPPluginResourceIdActivity clone(){
		HAPPluginResourceIdActivity out = new HAPPluginResourceIdActivity();
		out.cloneFrom(this);
		return out;
	}

}
