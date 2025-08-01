package com.nosliw.core.application.brick;

import org.springframework.stereotype.Component;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.HAPPluginResourceManagerImpBrick;
import com.nosliw.core.application.HAPUtilityBrickId;
import com.nosliw.core.application.configure.HAPPluginResourceManagerConfigure;
import com.nosliw.core.resource.HAPFactoryResourceTypeId;
import com.nosliw.core.resource.HAPManagerResource;
import com.nosliw.core.resource.HAPProviderResourcePluginImp;

@Component
public class HAPProviderResourcePluginBrick extends HAPProviderResourcePluginImp{

	private HAPManagerResource m_resourceManager;
	private HAPManagerApplicationBrick m_brickManager;
	
	public HAPProviderResourcePluginBrick(HAPManagerResource resourceManager, HAPManagerApplicationBrick brickManager) {
		this.m_resourceManager = resourceManager;
		this.m_brickManager = brickManager;

		
		this.registerPlugin(HAPUtilityBrickId.getResourceTypeIdFromBrickTypeId(HAPEnumBrickType.TEST_COMPLEX_1_100), new HAPPluginResourceManagerImpBrick(this.m_resourceManager, this.m_brickManager));
		this.registerPlugin(HAPUtilityBrickId.getResourceTypeIdFromBrickTypeId(HAPEnumBrickType.TEST_COMPLEX_SCRIPT_100), new HAPPluginResourceManagerImpBrick(this.m_resourceManager, this.m_brickManager));

		this.registerPlugin(HAPUtilityBrickId.getResourceTypeIdFromBrickTypeId(HAPEnumBrickType.TASKWRAPPER_100), new HAPPluginResourceManagerImpBrick(this.m_resourceManager, this.m_brickManager));
		
		this.registerPlugin(HAPUtilityBrickId.getResourceTypeIdFromBrickTypeId(HAPEnumBrickType.DATAEXPRESSIONLIB_100), new HAPPluginResourceManagerImpBrick(this.m_resourceManager, this.m_brickManager));
		this.registerPlugin(HAPUtilityBrickId.getResourceTypeIdFromBrickTypeId(HAPEnumBrickType.DATAEXPRESSIONGROUP_100), new HAPPluginResourceManagerImpBrick(this.m_resourceManager, this.m_brickManager));

		this.registerPlugin(HAPUtilityBrickId.getResourceTypeIdFromBrickTypeId(HAPEnumBrickType.SCRIPTEXPRESSIONGROUP_100), new HAPPluginResourceManagerImpBrick(this.m_resourceManager, this.m_brickManager));
		
		this.registerPlugin(HAPUtilityBrickId.getResourceTypeIdFromBrickTypeId(HAPEnumBrickType.SERVICEPROFILE_100), new HAPPluginResourceManagerImpBrick(this.m_resourceManager, this.m_brickManager));
		this.registerPlugin(HAPUtilityBrickId.getResourceTypeIdFromBrickTypeId(HAPEnumBrickType.SERVICEINTERFACE_100), new HAPPluginResourceManagerImpBrick(this.m_resourceManager, this.m_brickManager));

		
		this.registerPlugin(HAPFactoryResourceTypeId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT), new HAPPluginResourceManagerScript(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT));
		this.registerPlugin(HAPFactoryResourceTypeId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DECORATION_SCRIPT), new HAPPluginResourceManagerImpBrick(this.m_resourceManager, this.m_brickManager));

		
		this.registerPlugin(HAPFactoryResourceTypeId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_CONFIGURE), new HAPPluginResourceManagerConfigure());

		this.registerPlugin(HAPUtilityBrickId.getResourceTypeIdFromBrickTypeId(HAPEnumBrickType.UIPAGE_100), new HAPPluginResourceManagerImpBrick(this.m_resourceManager, this.m_brickManager));

		
	}

}
