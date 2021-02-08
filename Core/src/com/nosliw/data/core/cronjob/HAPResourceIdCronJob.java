package com.nosliw.data.core.cronjob;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPResourceIdCronJob  extends HAPResourceIdSimple{

	private HAPCronJobId m_cronJobId; 
	
	public HAPResourceIdCronJob(){  super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_CRONJOB);    }

	public HAPResourceIdCronJob(HAPResourceIdSimple resourceId){
		this();
		this.cloneFrom(resourceId);
	}
	
	public HAPResourceIdCronJob(String idLiterate) {
		this();
		init(idLiterate, null);
	}

	public HAPResourceIdCronJob(HAPCronJobId cronJobId){
		this();
		init(null, null);
		this.m_cronJobId = cronJobId;
		this.m_id = HAPSerializeManager.getInstance().toStringValue(cronJobId, HAPSerializationFormat.LITERATE); 
	}

	@Override
	protected void setId(String id){
		super.setId(id);
		this.m_cronJobId = new HAPCronJobId(id);
	}

	public HAPCronJobId getCronJobId(){  return this.m_cronJobId;	}
	
	@Override
	public HAPResourceIdCronJob clone(){
		HAPResourceIdCronJob out = new HAPResourceIdCronJob();
		out.cloneFrom(this);
		return out;
	}
}
