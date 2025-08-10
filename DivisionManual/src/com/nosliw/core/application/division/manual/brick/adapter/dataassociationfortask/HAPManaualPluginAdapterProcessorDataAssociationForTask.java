package com.nosliw.core.application.division.manual.brick.adapter.dataassociationfortask;

import java.util.Map;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.common.dataassociation.HAPDataAssociation;
import com.nosliw.core.application.common.dataassociation.HAPDataAssociationForTask;
import com.nosliw.core.application.common.dataassociation.definition.HAPDefinitionDataAssociation;
import com.nosliw.core.application.common.dataassociation.definition.HAPDefinitionDataAssociationForTask;
import com.nosliw.core.application.common.dataassociation.definition.HAPDefinitionProcessorDataAssociation;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualContextProcessAdapter;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorAdapter;

public class HAPManaualPluginAdapterProcessorDataAssociationForTask extends HAPManualPluginProcessorAdapter{

	public HAPManaualPluginAdapterProcessorDataAssociationForTask(HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.DATAASSOCIATIONFORTASK_100, HAPManualAdapterDataAssociationForTask.class, manualBrickMan);
	}

	@Override
	public void process(HAPManualBrick adapterExe, HAPManualDefinitionBrick adapterDef,	HAPManualContextProcessAdapter processContext) {
		HAPManualDefinitionAdapterDataAssociationForTask daAdapterDef = (HAPManualDefinitionAdapterDataAssociationForTask)adapterDef;
		HAPManualAdapterDataAssociationForTask daAdapterExe = (HAPManualAdapterDataAssociationForTask)adapterExe;
		
		HAPDefinitionDataAssociationForTask daForTaskDef = daAdapterDef.getDataAssociation();
		
		HAPDataAssociationForTask daForTaskExe = new HAPDataAssociationForTask(); 
		
		HAPPath baseBlockPath = processContext.getRootPathForBaseBrick();
		HAPPath secondBlockPath = this.getSecondBlockPath(processContext);
		
		HAPDefinitionDataAssociation inDA = daForTaskDef.getInDataAssociation();
		if(inDA!=null) {
			HAPDataAssociation daForRequest = HAPDefinitionProcessorDataAssociation.processDataAssociation(inDA, baseBlockPath, secondBlockPath, processContext.getCurrentBundle(), processContext.getRootBrickName(), processContext.getDataTypeHelper(), processContext.getResourceManager(), processContext.getRuntimeInfo());
			daForTaskExe.setInDataAssociation(daForRequest);
		}
		
		Map<String, HAPDefinitionDataAssociation> outDaDefs = daForTaskDef.getOutDataAssociations();
		for(Object key : outDaDefs.keySet()) {
			HAPDataAssociation daForResponse = HAPDefinitionProcessorDataAssociation.processDataAssociation(daForTaskDef.getOutDataAssociations().get(key), baseBlockPath, secondBlockPath, processContext.getCurrentBundle(), processContext.getRootBrickName(), processContext.getDataTypeHelper(), processContext.getResourceManager(), processContext.getRuntimeInfo());
			daForTaskExe.addOutDataAssociation((String)key, daForResponse);
		}
		
		daAdapterExe.setDataAssciation(daForTaskExe);
	}

}
