package com.nosliw.core.application.division.manual.brick.adapter.dataassociation;

import java.util.List;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.adapter.dataassociation.HAPAdapterDataAssociation;
import com.nosliw.core.application.common.dataassociation.HAPDataAssociationMapping;
import com.nosliw.core.application.common.dataassociation.HAPTunnel;
import com.nosliw.core.application.division.manual.HAPManualContextProcessAdapter;
import com.nosliw.core.application.division.manual.HAPPluginProcessorAdapterImp;
import com.nosliw.core.application.division.manual.common.dataassociation.HAPManualDataAssociation;
import com.nosliw.core.application.division.manual.common.dataassociation.HAPManualProcessorDataAssociation;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickAdapter;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickAdapter;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManaualPluginAdapterProcessorDataAssociation extends HAPPluginProcessorAdapterImp{

	private HAPRuntimeEnvironment m_runtimeEnv;
	
	public HAPManaualPluginAdapterProcessorDataAssociation(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.DATAASSOCIATION_100);
		this.m_runtimeEnv = runtimeEnv;
	}

	@Override
	public void process(HAPManualBrickAdapter adapterExe, HAPManualDefinitionBrickAdapter adapterDef,	HAPManualContextProcessAdapter processContext) {
		HAPManualAdapterDataAssociation daAdapterDef = (HAPManualAdapterDataAssociation)adapterDef;
		HAPAdapterDataAssociation daAdapterExe = (HAPAdapterDataAssociation)adapterExe;
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
