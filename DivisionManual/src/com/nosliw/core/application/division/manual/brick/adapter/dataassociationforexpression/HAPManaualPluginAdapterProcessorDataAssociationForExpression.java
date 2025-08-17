package com.nosliw.core.application.division.manual.brick.adapter.dataassociationforexpression;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.common.dataassociation.HAPDataAssociation;
import com.nosliw.core.application.common.dataassociation.HAPDataAssociationForExpression;
import com.nosliw.core.application.common.dataassociation.definition.HAPDefinitionDataAssociationForExpression;
import com.nosliw.core.application.common.dataassociation.definition.HAPDefinitionProcessorDataAssociation;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualContextProcessAdapter;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorAdapter;

public class HAPManaualPluginAdapterProcessorDataAssociationForExpression extends HAPManualPluginProcessorAdapter{

	public HAPManaualPluginAdapterProcessorDataAssociationForExpression() {
		super(HAPEnumBrickType.DATAASSOCIATIONFOREXPRESSION_100, HAPManualAdapterDataAssociationForExpression.class);
	}

	@Override
	public void process(HAPManualBrick adapterExe, HAPManualDefinitionBrick adapterDef,	HAPManualContextProcessAdapter processContext) {
		HAPManualDefinitionAdapterDataAssociationForExpression daAdapterDef = (HAPManualDefinitionAdapterDataAssociationForExpression)adapterDef;
		HAPManualAdapterDataAssociationForExpression daAdapterExe = (HAPManualAdapterDataAssociationForExpression)adapterExe;
		
		HAPDefinitionDataAssociationForExpression daForExpressionDef = daAdapterDef.getDataAssociation();
		
		HAPDataAssociationForExpression daForExpressionExe = new HAPDataAssociationForExpression(); 
		
		HAPPath baseBlockPath = processContext.getRootPathForBaseBrick();
		HAPPath secondBlockPath = this.getSecondBlockPath(processContext);
		HAPDataAssociation daForRequest = HAPDefinitionProcessorDataAssociation.processDataAssociation(daForExpressionDef.getInDataAssociation(), baseBlockPath, secondBlockPath, processContext.getCurrentBundle(), processContext.getRootBrickName(), processContext.getDataTypeHelper(), processContext.getResourceManager(), processContext.getRuntimeInfo());
		daForExpressionExe.setInDataAssociation(daForRequest);

		HAPDataAssociation daForResponse = HAPDefinitionProcessorDataAssociation.processDataAssociation(daForExpressionDef.getOutDataAssociation(), baseBlockPath, secondBlockPath, processContext.getCurrentBundle(), processContext.getRootBrickName(), processContext.getDataTypeHelper(), processContext.getResourceManager(), processContext.getRuntimeInfo());
		daForExpressionExe.setOutDataAssociation(daForResponse);
		
		daAdapterExe.setDataAssciation(daForExpressionExe);
	}

}
