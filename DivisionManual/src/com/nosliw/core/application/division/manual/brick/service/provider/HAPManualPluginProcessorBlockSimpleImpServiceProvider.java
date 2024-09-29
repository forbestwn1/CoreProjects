package com.nosliw.core.application.division.manual.brick.service.provider;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.HAPIdBrick;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.HAPUtilityBrickId;
import com.nosliw.core.application.brick.interactive.interfacee.task.HAPBlockInteractiveInterfaceTask;
import com.nosliw.core.application.brick.service.profile.HAPBlockServiceProfile;
import com.nosliw.core.application.brick.service.provider.HAPKeyService;
import com.nosliw.core.application.common.interactive.HAPUtilityInteractiveValuePort;
import com.nosliw.core.application.common.valueport.HAPGroupValuePorts;
import com.nosliw.core.application.division.manual.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPManualPluginProcessorBlockComplex;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionUtilityBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPUtilityResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginProcessorBlockSimpleImpServiceProvider extends HAPManualPluginProcessorBlockComplex{

	public HAPManualPluginProcessorBlockSimpleImpServiceProvider(HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.SERVICEPROVIDER_100, HAPManualBlockSimpleServiceProvider.class, runtimeEnv, manualBrickMan);
	}

	@Override
	public void processInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualDefinitionBlockSimpleServiceProvider serviceProviderDef = (HAPManualDefinitionBlockSimpleServiceProvider)blockPair.getLeft();
		HAPManualBlockSimpleServiceProvider serviceProviderExe = (HAPManualBlockSimpleServiceProvider)blockPair.getRight();
	
		HAPKeyService serviceKey = serviceProviderDef.getServiceKey();

		//service key
		serviceProviderExe.setServiceKey(serviceKey);

		//service interactive interface
		HAPManagerApplicationBrick brickMan = this.getRuntimeEnvironment().getBrickManager();
		HAPResourceIdSimple serviceProfileResourceId = HAPUtilityBrickId.fromBrickId2ResourceId(new HAPIdBrick(HAPEnumBrickType.SERVICEPROFILE_100, null, serviceKey.getServiceId()));
		HAPBlockServiceProfile serviceProfileBlock = (HAPBlockServiceProfile)HAPUtilityBrick.getBrickByResource(HAPUtilityResourceId.normalizeResourceId(serviceProfileResourceId), brickMan);
//		serviceProviderExe.setTaskInterface(serviceProfileBlock.getServiceInterface());
		
//		HAPBlockInteractiveInterfaceTask taskInterfaceBlock = (HAPBlockInteractiveInterfaceTask)HAPUtilityBrick.getBrick(serviceProfileBlock.getServiceInterface(), brickMan);
//		HAPInteractiveTask taskInteractive = taskInterfaceBlock.getValue();
//		serviceProviderExe.setTaskInterface(taskInteractive);
	}
	
	@Override
	public void processOtherValuePortBuild(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		HAPBundle bundle = processContext.getCurrentBundle();
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = HAPManualDefinitionUtilityBrick.getBrickPair(pathFromRoot, bundle);
		HAPManualDefinitionBlockSimpleServiceProvider definitionBlock = (HAPManualDefinitionBlockSimpleServiceProvider)blockPair.getLeft();
		HAPManualBlockSimpleServiceProvider serviceProviderExe = (HAPManualBlockSimpleServiceProvider)blockPair.getRight();
		HAPManagerApplicationBrick brickMan = this.getRuntimeEnvironment().getBrickManager();

//		HAPBlockInteractiveInterfaceTask taskInterfaceBlock = (HAPBlockInteractiveInterfaceTask)HAPUtilityBrick.getBrick(serviceProviderExe.getTaskInterface(), brickMan);

		HAPResourceIdSimple serviceProfileResourceId = HAPUtilityBrickId.fromBrickId2ResourceId(new HAPIdBrick(HAPEnumBrickType.SERVICEPROFILE_100, null, serviceProviderExe.getServiceKey().getServiceId()));
		HAPBlockServiceProfile serviceProfileBlock = (HAPBlockServiceProfile)HAPUtilityBrick.getBrickByResource(HAPUtilityResourceId.normalizeResourceId(serviceProfileResourceId), brickMan);
		HAPBlockInteractiveInterfaceTask taskInterfaceBlock = (HAPBlockInteractiveInterfaceTask)HAPUtilityBrick.getBrick(serviceProfileBlock.getTaskInterface(), brickMan);
		
		Pair<HAPGroupValuePorts, HAPGroupValuePorts> valuePortGroupPair = HAPUtilityInteractiveValuePort.buildValuePortGroupForInteractiveTask(taskInterfaceBlock.getValue(), processContext.getCurrentBundle().getValueStructureDomain());
		serviceProviderExe.addOtherInternalValuePortGroup(valuePortGroupPair.getLeft());
		serviceProviderExe.addOtherExternalValuePortGroup(valuePortGroupPair.getRight());
	}
}
