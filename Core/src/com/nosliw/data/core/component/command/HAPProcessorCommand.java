package com.nosliw.data.core.component.command;

import java.util.Map;

import com.nosliw.data.core.domain.entity.adapter.dataassociation.HAPDefinitionGroupDataAssociationForTask;
import com.nosliw.data.core.domain.entity.adapter.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.domain.entity.adapter.dataassociation.HAPExecutableGroupDataAssociationForTask;
import com.nosliw.data.core.domain.entity.adapter.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.interactive.HAPUtilityInteractive;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;

public class HAPProcessorCommand {

	public static HAPExecutableCommand process(HAPDefinitionCommand commandDef, HAPValueStructure parentStructure, HAPRuntimeEnvironment runtimeEnv) {
		
		HAPExecutableCommand out = new HAPExecutableCommand();
		commandDef.cloneToEntityInfo(out);
		out.setTaskName(commandDef.getTaskName());
		
		HAPContainerStructure parentStructures = HAPContainerStructure.createDefault(parentStructure);
		
		HAPDefinitionCommand solidatedCommandDef = commandDef.cloneCommandDefinition();
		HAPUtilityInteractive.solidateRelative(solidatedCommandDef, parentStructures, null, true, null);
		out.setCommand(solidatedCommandDef);

		//build data association
		HAPExecutableGroupDataAssociationForTask dataAssociationGroupExe = out.getDataAssociations();

		HAPDefinitionGroupDataAssociationForTask dataAssociationGroupDef = HAPUtilityInteractive.buildDataAssociation(solidatedCommandDef);
		
		//in data association
		HAPExecutableDataAssociation inDataAssociation = HAPProcessorDataAssociation.processDataAssociation(HAPContainerStructure.createDefault(HAPUtilityInteractive.buildInValueStructureFromWithInteractive(solidatedCommandDef)), dataAssociationGroupDef.getInDataAssociation(), parentStructures, null, runtimeEnv);
		dataAssociationGroupExe.setInDataAssociation(inDataAssociation);
		
		Map<String, HAPValueStructureDefinitionFlat> resultStructures = HAPUtilityInteractive.buildOutValueStructureFromWithInteractive(solidatedCommandDef);
		for(String resultName : solidatedCommandDef.getResults().keySet()) {
			HAPExecutableDataAssociation outDataAssociation = HAPProcessorDataAssociation.processDataAssociation(parentStructures, dataAssociationGroupDef.getOutDataAssociations().get(resultName), HAPContainerStructure.createDefault(resultStructures.get(resultName)), null, runtimeEnv);
			dataAssociationGroupExe.addOutDataAssociation(resultName, outDataAssociation);
		}
		
		return out;
	}
	
}
