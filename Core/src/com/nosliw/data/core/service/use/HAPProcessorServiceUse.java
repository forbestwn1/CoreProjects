package com.nosliw.data.core.service.use;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.criteria.HAPCriteriaUtility;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaId;
import com.nosliw.data.core.criteria.HAPVariableInfo;
import com.nosliw.data.core.expression.HAPMatchers;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefinitionElement;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafConstant;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafData;
import com.nosliw.data.core.script.context.HAPContextDefinitionRootId;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPDefinitionDataAssociationGroup;
import com.nosliw.data.core.script.context.HAPExecutableDataAssociationGroup;
import com.nosliw.data.core.script.context.HAPProcessorDataAssociation;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.data.core.service.interfacee.HAPServiceInterface;
import com.nosliw.data.core.service.interfacee.HAPServiceOutput;

public class HAPProcessorServiceUse {

	public HAPExecutableServiceUse process(
			HAPDefinitionServiceUse definition,
			HAPServiceInterface providerInterface,
			HAPContextGroup globalContext, 
			HAPConfigureContextProcessor configure, 
			HAPRequirementContextProcessor contextProcessRequirement) {
		HAPExecutableServiceUse out = new HAPExecutableServiceUse(definition);
		
		HAPDefinitionMappingService serviceMapping = definition.getServiceMapping();
		
		//process parm mapping
		HAPExecutableDataAssociationGroup processedParms = HAPProcessorDataAssociation.processDataAssociation(globalContext, serviceMapping.getParms(), contextProcessRequirement);
		out.setParmMapping(processedParms);
		
		//process parm mapping
		for(String providerParmName : providerInterface.getParms().keySet()) {
			HAPDataTypeCriteria providerParmCriteria = providerInterface.getParm(providerParmName).getCriteria();
			HAPContextDefinitionElement useEleDef = processedParms.getContext().getContext().getElement(providerParmName).getDefinition().getSolidContextDefinitionElement();
			HAPDataTypeCriteria userParmCriteria = null;
			switch(useEleDef.getType()) {
			case HAPConstant.CONTEXT_ELEMENTTYPE_DATA:
				userParmCriteria = ((HAPContextDefinitionLeafData)useEleDef).getCriteria().getCriteria();
				break;
			case HAPConstant.CONTEXT_ELEMENTTYPE_CONSTANT:
				userParmCriteria = new HAPDataTypeCriteriaId(((HAPContextDefinitionLeafConstant)useEleDef).getDataValue().getDataTypeId(), null);
				break;
			}
			HAPMatchers matchers = HAPCriteriaUtility.isMatchable(userParmCriteria, providerParmCriteria, contextProcessRequirement.dataTypeHelper);
			out.setParmMatchers(providerParmName, matchers);
		}
		
		//process result mapping
		Map<String, HAPDefinitionDataAssociationGroup> resultMapping = definition.getResultMapping();
		for(String result :resultMapping.keySet()) {
			HAPDefinitionDataAssociationGroup mapping = resultMapping.get(result);
			
			HAPContext outputContext = new HAPContext();
			Map<String, HAPServiceOutput> serviceOutput = providerInterface.getResults().get(result).getOutput();
			for(String outParm : serviceOutput.keySet()) {
				outputContext.addElement(outParm, new HAPContextDefinitionLeafData(HAPVariableInfo.buildVariableInfo(serviceOutput.get(outParm).getCriteria())));
			}
			HAPExecutableDataAssociationGroup processedResultMapping = HAPProcessorDataAssociation.processDataAssociation(outputContext, mapping, contextProcessRequirement);
			out.addResultMapping(result, processedResultMapping);
			
			//matchers
			HAPContext resultContext = processedResultMapping.getContext().getContext();
			for(String name :resultContext.getElementNames()) {
				Map<String, HAPMatchers> matchers = HAPUtilityContext.mergeContextRoot(resultContext.getElement(name), globalContext.getElement(new HAPContextDefinitionRootId(name)), false, contextProcessRequirement);
				out.addResultMatchers(result, name, matchers);		
			}
		}
		
		return out;
	}
	
}
