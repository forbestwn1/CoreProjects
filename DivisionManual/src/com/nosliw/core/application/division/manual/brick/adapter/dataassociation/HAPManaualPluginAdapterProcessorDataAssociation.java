package com.nosliw.core.application.division.manual.brick.adapter.dataassociation;

import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBrickAdapter;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.adapter.dataassociation.HAPAdapterDataAssciation;
import com.nosliw.core.application.brick.adapter.dataassociation.HAPDataAssociationMapping;
import com.nosliw.core.application.brick.adapter.dataassociation.HAPTunnel;
import com.nosliw.core.application.division.manual.HAPManualBrickAdapter;
import com.nosliw.core.application.division.manual.HAPManualContextProcessAdapter;
import com.nosliw.core.application.division.manual.HAPPluginProcessorAdapterImp;
import com.nosliw.core.application.division.manual.common.dataassociation.HAPManualDataAssociation;
import com.nosliw.core.application.division.manual.common.dataassociation.mapping.HAPManualDataAssociationMapping;
import com.nosliw.core.application.division.manual.common.dataassociation.mapping.HAPProcessorDataAssociationMapping;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManaualPluginAdapterProcessorDataAssociation extends HAPPluginProcessorAdapterImp{

	private HAPRuntimeEnvironment m_runtimeEnv;
	
	public HAPManaualPluginAdapterProcessorDataAssociation(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.DATAASSOCIATION_100);
		this.m_runtimeEnv = runtimeEnv;
	}

	@Override
	public void process(HAPBrickAdapter adapterExe, HAPManualBrickAdapter adapterDef,	HAPManualContextProcessAdapter processContext) {
		HAPManualAdapterDataAssciation daAdapterDef = (HAPManualAdapterDataAssciation)adapterDef;
		HAPAdapterDataAssciation daAdapterExe = (HAPAdapterDataAssciation)adapterExe;
		HAPDataAssociationMapping daExe = new HAPDataAssociationMapping();
		daAdapterExe.setDataAssciation(daExe);
		List<HAPManualDataAssociation> das = daAdapterDef.getDataAssociation();
		for(HAPManualDataAssociation da : das) {
			String daType = da.getType();
			if(daType.equals(HAPConstantShared.DATAASSOCIATION_TYPE_MAPPING)) {
				HAPDataAssociationMapping daMappingExe = HAPProcessorDataAssociationMapping.processValueMapping((HAPManualDataAssociationMapping)da, processContext.getRootPathForBaseBrick(), processContext.getCurrentBundle(), this.m_runtimeEnv);
				for(HAPTunnel tunnel : daMappingExe.getTunnels()) {
					daExe.addTunnel(tunnel);
				}
			}
		}
	}

}
