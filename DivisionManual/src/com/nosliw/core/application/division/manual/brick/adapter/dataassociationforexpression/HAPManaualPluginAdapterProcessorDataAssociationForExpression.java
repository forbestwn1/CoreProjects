package com.nosliw.core.application.division.manual.brick.adapter.dataassociationforexpression;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.common.dataassociation.HAPDataAssociation;
import com.nosliw.core.application.common.dataassociation.HAPDataAssociationForExpression;
import com.nosliw.core.application.division.m.HAPManualBrick;
import com.nosliw.core.application.division.manua.HAPManualContextProcessAdapter;
import com.nosliw.core.application.division.manua.HAPManualManagerBrick;
import com.nosliw.core.application.division.manua.HAPManualPluginProcessorAdapter;
import com.nosliw.core.application.division.manual.common.dataassociation.HAPManualDataAssociationForExpression;
import com.nosliw.core.application.division.manual.common.dataassociation.HAPManualProcessorDataAssociation;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManaualPluginAdapterProcessorDataAssociationForExpression extends HAPManualPluginProcessorAdapter{

	private HAPRuntimeEnvironment m_runtimeEnv;
	
	public HAPManaualPluginAdapterProcessorDataAssociationForExpression(HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.DATAASSOCIATIONFOREXPRESSION_100, HAPManualAdapterDataAssociationForExpression.class, runtimeEnv, manualBrickMan);
		this.m_runtimeEnv = runtimeEnv;
	}

	@Override
	public void process(HAPManualBrick adapterExe, HAPManualDefinitionBrick adapterDef,	HAPManualContextProcessAdapter processContext) {
		HAPManualDefinitionAdapterDataAssociationForExpression daAdapterDef = (HAPManualDefinitionAdapterDataAssociationForExpression)adapterDef;
		HAPManualAdapterDataAssociationForExpression daAdapterExe = (HAPManualAdapterDataAssociationForExpression)adapterExe;
		
		HAPManualDataAssociationForExpression daForExpressionDef = daAdapterDef.getDataAssociation();
		
		HAPDataAssociationForExpression daForExpressionExe = new HAPDataAssociationForExpression(); 
		
		HAPPath baseBlockPath = processContext.getRootPathForBaseBrick();
		HAPPath secondBlockPath = this.getSecondBlockPath(processContext);
		HAPDataAssociation daForRequest = HAPManualProcessorDataAssociation.processDataAssociation(daForExpressionDef.getInDataAssociation(), baseBlockPath, secondBlockPath, processContext.getCurrentBundle(), processContext.getRootBrickName(), this.m_runtimeEnv);
		daForExpressionExe.setInDataAssociation(daForRequest);

		HAPDataAssociation daForResponse = HAPManualProcessorDataAssociation.processDataAssociation(daForExpressionDef.getOutDataAssociation(), baseBlockPath, secondBlockPath, processContext.getCurrentBundle(), processContext.getRootBrickName(), this.m_runtimeEnv);
		daForExpressionExe.setOutDataAssociation(daForResponse);
		
		daAdapterExe.setDataAssciation(daForExpressionExe);
	}

}
