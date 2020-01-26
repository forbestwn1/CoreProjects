package com.nosliw.data.core.process.resource;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.process.HAPActivityPluginId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPResourceIdActivityPlugin  extends HAPResourceIdSimple{

	private HAPActivityPluginId m_activityPluginId; 
	
	public HAPResourceIdActivityPlugin(){    super(HAPConstant.RUNTIME_RESOURCE_TYPE_ACTIVITYPLUGIN);    }

	public HAPResourceIdActivityPlugin(HAPResourceIdSimple resourceId){
		this();
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdActivityPlugin(String idLiterate) {
		this();
		init(idLiterate, null);
	}

	public HAPResourceIdActivityPlugin(HAPActivityPluginId activityPluginId){
		this();
		init(null, null);
		this.m_activityPluginId = activityPluginId;
		this.m_id = HAPSerializeManager.getInstance().toStringValue(activityPluginId, HAPSerializationFormat.LITERATE); 
	}

	@Override
	protected void setId(String id){
		super.setId(id);
		this.m_activityPluginId = new HAPActivityPluginId(id);
	}

	public HAPActivityPluginId getActivityPlugId(){  return this.m_activityPluginId;	}
	
	@Override
	public HAPResourceIdActivityPlugin clone(){
		HAPResourceIdActivityPlugin out = new HAPResourceIdActivityPlugin();
		out.cloneFrom(this);
		return out;
	}

}
