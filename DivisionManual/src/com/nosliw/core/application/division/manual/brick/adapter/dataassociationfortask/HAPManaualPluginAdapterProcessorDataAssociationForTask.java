package com.nosliw.core.application.division.manual.brick.adapter.dataassociationfortask;

import java.util.Map;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBrickAdapter;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.adapter.dataassociationfortask.HAPAdapterDataAssociationForTask;
import com.nosliw.core.application.common.dataassociation.HAPDataAssociation;
import com.nosliw.core.application.common.dataassociation.HAPDataAssociationForTask;
import com.nosliw.core.application.division.manual.HAPManualBrickAdapter;
import com.nosliw.core.application.division.manual.HAPManualContextProcessAdapter;
import com.nosliw.core.application.division.manual.HAPPluginProcessorAdapterImp;
import com.nosliw.core.application.division.manual.common.dataassociation.HAPManualDataAssociation;
import com.nosliw.core.application.division.manual.common.dataassociation.HAPManualDataAssociationForTask;
import com.nosliw.core.application.division.manual.common.dataassociation.HAPManualProcessorDataAssociation;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManaualPluginAdapterProcessorDataAssociationForTask extends HAPPluginProcessorAdapterImp{

	private HAPRuntimeEnvironment m_runtimeEnv;
	
	public HAPManaualPluginAdapterProcessorDataAssociationForTask(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.DATAASSOCIATIONFORTASK_100);
		this.m_runtimeEnv = runtimeEnv;
	}

	@Override
	public void process(HAPBrickAdapter adapterExe, HAPManualBrickAdapter adapterDef,	HAPManualContextProcessAdapter processContext) {
		HAPManualAdapterDataAssociationForTask daAdapterDef = (HAPManualAdapterDataAssociationForTask)adapterDef;
		HAPAdapterDataAssociationForTask daAdapterExe = (HAPAdapterDataAssociationForTask)adapterExe;
		
		HAPManualDataAssociationForTask daForTaskDef = daAdapterDef.getDataAssociation();
		
		HAPDataAssociationForTask daForTaskExe = new HAPDataAssociationForTask(); 
		
		HAPPath baseBlockPath = processContext.getRootPathForBaseBrick();
		HAPPath secondBlockPath = baseBlockPath.trimLast().trimLast();
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
