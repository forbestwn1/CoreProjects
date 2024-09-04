package com.nosliw.core.application.division.manual.brick.adapter.dataassociationfortask;

import java.util.Map;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.common.dataassociation.HAPDataAssociation;
import com.nosliw.core.application.common.dataassociation.HAPDataAssociationForTask;
import com.nosliw.core.application.division.manual.HAPManualContextProcessAdapter;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPManualPluginProcessorAdapter;
import com.nosliw.core.application.division.manual.common.dataassociation.HAPManualDataAssociation;
import com.nosliw.core.application.division.manual.common.dataassociation.HAPManualDataAssociationForTask;
import com.nosliw.core.application.division.manual.common.dataassociation.HAPManualProcessorDataAssociation;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManaualPluginAdapterProcessorDataAssociationForTask extends HAPManualPluginProcessorAdapter{

	private HAPRuntimeEnvironment m_runtimeEnv;
	
	public HAPManaualPluginAdapterProcessorDataAssociationForTask(HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.DATAASSOCIATIONFORTASK_100, HAPManualAdapterDataAssociationForTask.class, runtimeEnv, manualBrickMan);
		this.m_runtimeEnv = runtimeEnv;
	}

	@Override
	public void process(HAPManualBrick adapterExe, HAPManualDefinitionBrick adapterDef,	HAPManualContextProcessAdapter processContext) {
		HAPManualDefinitionAdapterDataAssociationForTask daAdapterDef = (HAPManualDefinitionAdapterDataAssociationForTask)adapterDef;
		HAPManualAdapterDataAssociationForTask daAdapterExe = (HAPManualAdapterDataAssociationForTask)adapterExe;
		
		HAPManualDataAssociationForTask daForTaskDef = daAdapterDef.getDataAssociation();
		
		HAPDataAssociationForTask daForTaskExe = new HAPDataAssociationForTask(); 
		
		HAPPath baseBlockPath = processContext.getRootPathForBaseBrick();
		HAPPath secondBlockPath = baseBlockPath.trimLast().getLeft().trimLast().getLeft();
		HAPDataAssociation daForRequest = HAPManualProcessorDataAssociation.processDataAssociation(daForTaskDef.getInDataAssociation(), baseBlockPath, secondBlockPath, processContext.getCurrentBundle(), this.m_runtimeEnv);
		daForTaskExe.setInDataAssociation(daForRequest);
		
		Map<String, HAPManualDataAssociation> outDaDefs = daForTaskDef.getOutDataAssociations();
		for(Object key : outDaDefs.keySet()) {
			HAPDataAssociation daForResponse = HAPManualProcessorDataAssociation.processDataAssociation(daForTaskDef.getOutDataAssociations().get(key), baseBlockPath, secondBlockPath, processContext.getCurrentBundle(), this.m_runtimeEnv);
			daForTaskExe.addOutDataAssociation((String)key, daForResponse);
		}
		
		daAdapterExe.setDataAssciation(daForTaskExe);
	}

}
