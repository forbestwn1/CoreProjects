package com.nosliw.core.application.division.manual.brick.adapter.dataassociation;

import java.util.List;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.common.dataassociation.HAPDataAssociationMapping;
import com.nosliw.core.application.common.dataassociation.HAPTunnel;
import com.nosliw.core.application.division.manual.HAPManualContextProcessAdapter;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPPluginProcessorAdapter;
import com.nosliw.core.application.division.manual.common.dataassociation.HAPManualDataAssociation;
import com.nosliw.core.application.division.manual.common.dataassociation.HAPManualProcessorDataAssociation;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickAdapter;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManaualPluginAdapterProcessorDataAssociation extends HAPPluginProcessorAdapter{

	private HAPRuntimeEnvironment m_runtimeEnv;
	
	public HAPManaualPluginAdapterProcessorDataAssociation(HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.DATAASSOCIATION_100, HAPManualAdapterDataAssociation.class, runtimeEnv, manualBrickMan);
		this.m_runtimeEnv = runtimeEnv;
	}

	@Override
	public void process(HAPManualBrick adapterExe, HAPManualDefinitionBrickAdapter adapterDef,	HAPManualContextProcessAdapter processContext) {
		HAPManualDefinitionAdapterDataAssociation daAdapterDef = (HAPManualDefinitionAdapterDataAssociation)adapterDef;
		HAPManualAdapterDataAssociation daAdapterExe = (HAPManualAdapterDataAssociation)adapterExe;
		HAPDataAssociationMapping daExe = new HAPDataAssociationMapping();
		daAdapterExe.setDataAssciation(daExe);
		List<HAPManualDataAssociation> das = daAdapterDef.getDataAssociation();
		for(HAPManualDataAssociation da : das) {
			HAPPath baseBlockPath = processContext.getRootPathForBaseBrick();
			HAPPath secondBlockPath = baseBlockPath.trimLast();
			HAPDataAssociationMapping daMappingExe = (HAPDataAssociationMapping)HAPManualProcessorDataAssociation.processDataAssociation(da, baseBlockPath, secondBlockPath, processContext.getCurrentBundle(), this.m_runtimeEnv);
			for(HAPTunnel tunnel : daMappingExe.getTunnels()) {
				daExe.addTunnel(tunnel);
			}
		}
	}

}
