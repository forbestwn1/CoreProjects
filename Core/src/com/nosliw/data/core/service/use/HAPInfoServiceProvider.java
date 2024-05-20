package com.nosliw.data.core.service.use;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.core.application.brick.service.profile.HAPBlockServiceProfile;
import com.nosliw.core.application.division.manual.brick.adapter.dataassociationfortask.HAPDefinitionGroupDataAssociationForTask;

//store all the info related with service provider attachment
public class HAPInfoServiceProvider extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String DATAMAPPING = "dataMapping";

	//service definition of provider
	private HAPBlockServiceProfile m_serviceDef;
	
	//data mapping to adaptor service provider to service use
	private HAPDefinitionGroupDataAssociationForTask m_dataMapping;
	
	public HAPInfoServiceProvider(HAPEntityInfo entityInfo, HAPBlockServiceProfile serviceDef, HAPDefinitionGroupDataAssociationForTask dataMapping) {
		entityInfo.cloneToEntityInfo(this);
		this.m_serviceDef = serviceDef;
		this.m_dataMapping = dataMapping;
	}
	
	public HAPBlockServiceProfile getServiceDefinition() {    return this.m_serviceDef;  }
	
	public HAPDefinitionGroupDataAssociationForTask getDataMapping() {   return this.m_dataMapping;    }
	
}
