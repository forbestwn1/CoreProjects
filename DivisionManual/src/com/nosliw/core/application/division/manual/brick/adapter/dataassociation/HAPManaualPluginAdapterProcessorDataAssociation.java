package com.nosliw.core.application.division.manual.brick.adapter.dataassociation;

import java.util.List;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.common.dataassociation.HAPDataAssociationMapping;
import com.nosliw.core.application.common.dataassociation.HAPTunnel;
import com.nosliw.core.application.common.dataassociation.definition.HAPDefinitionDataAssociation;
import com.nosliw.core.application.division.manual.common.dataassociation.HAPManualProcessorDataAssociation;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualContextProcessAdapter;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorAdapter;

public class HAPManaualPluginAdapterProcessorDataAssociation extends HAPManualPluginProcessorAdapter{

	public HAPManaualPluginAdapterProcessorDataAssociation(HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.DATAASSOCIATION_100, HAPManualAdapterDataAssociation.class, manualBrickMan);
	}

	@Override
	public void process(HAPManualBrick adapterExe, HAPManualDefinitionBrick adapterDef,	HAPManualContextProcessAdapter processContext) {
		HAPManualDefinitionAdapterDataAssociation daAdapterDef = (HAPManualDefinitionAdapterDataAssociation)adapterDef;
		HAPManualAdapterDataAssociation daAdapterExe = (HAPManualAdapterDataAssociation)adapterExe;
		HAPDataAssociationMapping daExe = new HAPDataAssociationMapping();
		daAdapterExe.setDataAssciation(daExe);
		List<HAPDefinitionDataAssociation> das = daAdapterDef.getDataAssociation();
		for(HAPDefinitionDataAssociation da : das) {
			HAPPath baseBlockPath = processContext.getRootPathForBaseBrick();
			HAPPath secondBlockPath = this.getSecondBlockPath(processContext);
			HAPDataAssociationMapping daMappingExe = (HAPDataAssociationMapping)HAPManualProcessorDataAssociation.processDataAssociation(da, baseBlockPath, secondBlockPath, processContext.getCurrentBundle(), processContext.getRootBrickName());
			for(HAPTunnel tunnel : daMappingExe.getTunnels()) {
				daExe.addTunnel(tunnel);
			}
		}
	}

}
