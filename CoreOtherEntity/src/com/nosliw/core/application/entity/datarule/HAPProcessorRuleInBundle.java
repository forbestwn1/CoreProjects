package com.nosliw.core.application.entity.datarule;

import java.util.HashSet;
import java.util.Map;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPDomainValueStructure;
import com.nosliw.core.application.HAPWrapperBrickRoot;
import com.nosliw.core.application.brick.container.HAPBrickContainerImp;
import com.nosliw.core.application.brick.wrappertask.HAPBlockTaskWrapperImp;
import com.nosliw.core.application.common.datadefinition.HAPDefinitionDataRule;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafData;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafRelativeForValue;
import com.nosliw.core.application.common.structure.HAPRootInStructure;
import com.nosliw.core.application.common.structure.HAPStructure;
import com.nosliw.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.core.data.criteria.HAPUtilityCriteria;

public class HAPProcessorRuleInBundle {

	public static void process(HAPBundle bundle, HAPManagerDataRule dataRuleManager) {
		
		//build new branch to host data rule tasks
		String validationTaskBranchName = "validationRuleTasks";
		
		HAPBrickContainerImp containerBrick = new HAPBrickContainerImp(); 
		
		HAPDomainValueStructure valueStructureDomain = bundle.getValueStructureDomain();
		Map<String, HAPStructure> structures = valueStructureDomain.getValueStructureDefinitions();
		for(HAPStructure structure : new HashSet<HAPStructure>(structures.values())) {
			Map<String, HAPRootInStructure> roots = structure.getRoots();
			for(String rootName : roots.keySet()) {
				HAPElementStructure ele = roots.get(rootName).getDefinition();

				String eleType = ele.getType();
                if(eleType.equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA)) {
                	HAPElementStructureLeafData dataEle = (HAPElementStructureLeafData)ele;
                	buildRuildForDataElement(dataEle, containerBrick, validationTaskBranchName, valueStructureDomain, dataRuleManager);
                }
                if(eleType.equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_VALUE)) {
                	HAPElementStructureLeafRelativeForValue forValueElement = (HAPElementStructureLeafRelativeForValue)ele;
                	HAPElementStructureLeafData dataEle = forValueElement.getDefinition();
                	buildRuildForDataElement(dataEle, containerBrick, validationTaskBranchName, valueStructureDomain, dataRuleManager);
                }
			}
		}
		HAPWrapperBrickRoot validationRuleTaskBranchRoot = new HAPWrapperBrickRoot(containerBrick);
		validationRuleTaskBranchRoot.setName(validationTaskBranchName);
		bundle.addRootBrickWrapper(validationRuleTaskBranchRoot);
	}
	
	private static void buildRuildForDataElement(HAPElementStructureLeafData dataEle, HAPBrickContainerImp containerBrick, String validationTaskBranchName, HAPDomainValueStructure valueStructureDomain, HAPManagerDataRule dataRuleManager) {
    	for(HAPDefinitionDataRule ruleDef : dataEle.getDataDefinition().getRules()) {
    		HAPDataRule rule = ruleDef.getDataRule();
    		HAPDataTypeCriteria ruleCriteria = HAPUtilityCriteria.getChildCriteriaByPath(dataEle.getCriteria(), ruleDef.getPath());
    		rule.setDataCriteria(ruleCriteria);
    		
    		HAPBlockTaskWrapperImp taskWrapperBrick = new HAPBlockTaskWrapperImp(); 
    		String attrName = containerBrick.addElementWithBrickOrReference(taskWrapperBrick);
    		HAPDataRuleImplementationLocal dataRuleImp = new HAPDataRuleImplementationLocal(validationTaskBranchName + "."+attrName+".taskWrapper");
    		rule.setImplementation(dataRuleImp);

    		HAPEntityOrReference ruleTaskBrickOrRef = dataRuleManager.transformDataRule(rule, valueStructureDomain);
    		taskWrapperBrick.setTask(ruleTaskBrickOrRef);
    	}
	}
	
	
}
