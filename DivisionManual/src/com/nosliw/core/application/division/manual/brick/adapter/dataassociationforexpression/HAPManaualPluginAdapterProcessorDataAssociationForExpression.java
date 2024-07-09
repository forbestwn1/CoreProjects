package com.nosliw.core.application.division.manual.brick.adapter.dataassociationforexpression;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.common.dataassociation.HAPDataAssociation;
import com.nosliw.core.application.common.dataassociation.HAPDataAssociationForExpression;
import com.nosliw.core.application.division.manual.HAPManualContextProcessAdapter;
import com.nosliw.core.application.division.manual.HAPPluginProcessorAdapter;
import com.nosliw.core.application.division.manual.common.dataassociation.HAPManualDataAssociationForExpression;
import com.nosliw.core.application.division.manual.common.dataassociation.HAPManualProcessorDataAssociation;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickAdapter;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickAdapter;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManaualPluginAdapterProcessorDataAssociationForExpression extends HAPPluginProcessorAdapter{

	private HAPRuntimeEnvironment m_runtimeEnv;
	
	public HAPManaualPluginAdapterProcessorDataAssociationForExpression(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.DATAASSOCIATIONFOREXPRESSION_100, HAPManualAdapterDataAssociationForExpression.class, runtimeEnv);
		this.m_runtimeEnv = runtimeEnv;
	}

	@Override
	public void process(HAPManualBrickAdapter adapterExe, HAPManualDefinitionBrickAdapter adapterDef,	HAPManualContextProcessAdapter processContext) {
		HAPManualDefinitionAdapterDataAssociationForExpression daAdapterDef = (HAPManualDefinitionAdapterDataAssociationForExpression)adapterDef;
		HAPManualAdapterDataAssociationForExpression daAdapterExe = (HAPManualAdapterDataAssociationForExpression)adapterExe;
		
		HAPManualDataAssociationForExpression daForExpressionDef = daAdapterDef.getDataAssociation();
		
		HAPDataAssociationForExpression daForExpressionExe = new HAPDataAssociationForExpression(); 
		
		HAPPath baseBlockPath = processContext.getRootPathForBaseBrick();
		HAPPath secondBlockPath = baseBlockPath.trimLast().trimLast();
		HAPDataAssociation daForRequest = HAPManualProcessorDataAssociation.processDataAssociation(daForExpressionDef.getInDataAssociation(), baseBlockPath, secondBlockPath, processContext.getCurrentBundle(), this.m_runtimeEnv);
		daForExpressionExe.setInDataAssociation(daForRequest);

		HAPDataAssociation daForResponse = HAPManualProcessorDataAssociation.processDataAssociation(daForExpressionDef.getOutDataAssociation(), baseBlockPath, secondBlockPath, processContext.getCurrentBundle(), this.m_runtimeEnv);
		daForExpressionExe.setOutDataAssociation(daForResponse);
		
		daAdapterExe.setDataAssciation(daForExpressionExe);
	}

}
