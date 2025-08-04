package com.nosliw.core.application.division.manual.brick.service.provider;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPIdBrick;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.HAPUtilityBrickId;
import com.nosliw.core.application.division.manual.common.task.HAPManualUtilityTask;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorBlockComplex;
import com.nosliw.core.resource.HAPResourceIdSimple;
import com.nosliw.core.resource.HAPUtilityResourceId;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.core.xxx.application1.brick.interactive.interfacee.task.HAPBlockInteractiveInterfaceTask;
import com.nosliw.core.xxx.application1.brick.service.profile.HAPBlockServiceProfile;
import com.nosliw.core.xxx.application1.brick.service.provider.HAPKeyService;
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
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualDefinitionBlockSimpleServiceProvider definitionBlock = (HAPManualDefinitionBlockSimpleServiceProvider)blockPair.getLeft();
		HAPManualBlockSimpleServiceProvider serviceProviderExe = (HAPManualBlockSimpleServiceProvider)blockPair.getRight();
		HAPManagerApplicationBrick brickMan = this.getRuntimeEnvironment().getBrickManager();

//		HAPBlockInteractiveInterfaceTask taskInterfaceBlock = (HAPBlockInteractiveInterfaceTask)HAPUtilityBrick.getBrick(serviceProviderExe.getTaskInterface(), brickMan);

		HAPResourceIdSimple serviceProfileResourceId = HAPUtilityBrickId.fromBrickId2ResourceId(new HAPIdBrick(HAPEnumBrickType.SERVICEPROFILE_100, null, serviceProviderExe.getServiceKey().getServiceId()));
		HAPBlockServiceProfile serviceProfileBlock = (HAPBlockServiceProfile)HAPUtilityBrick.getBrickByResource(HAPUtilityResourceId.normalizeResourceId(serviceProfileResourceId), brickMan);
		HAPBlockInteractiveInterfaceTask taskInterfaceBlock = (HAPBlockInteractiveInterfaceTask)HAPUtilityBrick.getBrick(serviceProfileBlock.getTaskInterface(), brickMan);
		
		HAPManualUtilityTask.buildValuePortGroupForInteractiveTask(serviceProviderExe, taskInterfaceBlock.getValue(), processContext.getCurrentBundle().getValueStructureDomain());
	}
}
