package com.nosliw.test.domain;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.entity.HAPEntityPackage;
import com.nosliw.data.core.entity.HAPIdEntity;
import com.nosliw.data.core.entity.HAPIdEntityType;
import com.nosliw.data.core.entity.HAPUtilityEntity;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPTestDomain {

	public static void main(String[] args) {
		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();
//		HAPDomainEntityDefinitionGlobal globalDomain = new HAPDomainEntityDefinitionGlobal(new HAPGeneratorId(), runtimeEnvironment.getDomainEntityManager(), runtimeEnvironment.getResourceDefinitionManager());
		
		HAPResourceIdSimple resourceId = createResourceId(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_1, "basic"); 
//		HAPResourceIdSimple resourceId = new HAPResourceIdSimple(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_1, "test.adapter.dataassociation");
//		HAPResourceIdSimple resourceId = new HAPResourceIdSimple(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_1, "test.entity.service");
//		HAPResourceIdSimple resourceId = new HAPResourceIdSimple(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_1, "test.basic.reference");

//		HAPResourceIdSimple resourceId = new HAPResourceIdSimple(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONGROUP, "test1");
//		HAPResourceIdSimple resourceId = new HAPResourceIdSimple(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONGROUP, "test1");
		
//		HAPResourceIdSimple resourceId = new HAPResourceIdSimple(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_1, "test.entity.valuestructure.reference");

//		HAPResourceIdSimple resourceId = new HAPResourceIdSimple(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_1, "test.entity.attachment");
		

//		HAPResourceIdSimple resourceId = new HAPResourceIdSimple(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_1, "test.basic.variable.extension");

//		HAPResourceIdSimple resourceId = new HAPResourceIdSimple(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_1, "test.entity.task");
//		HAPResourceIdSimple resourceId = new HAPResourceIdSimple(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_1, "test.entity.scripttaskgroup");

//		HAPResourceIdSimple resourceId = new HAPResourceIdSimple(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTTASKGROUP, "test1");

		//definition
//		HAPResourceDefinition resourceDef = runtimeEnvironment.getResourceDefinitionManager().getResourceDefinition(resourceId, globalDomain);
//		String expandedJsonStr = HAPUtilityDomain.getEntityExpandedJsonString(resourceDef.getEntityId(), globalDomain);
//		System.out.println(HAPJsonUtility.formatJson(expandedJsonStr));

		//process
		HAPEntityPackage executablePackage = runtimeEnvironment.getEntityManager().getPackage(resourceId);		
//		System.out.println();
//		System.out.println();
//		System.out.println("*******************************************************************************");
//		System.out.println();
//		System.out.println();
//		System.out.println(HAPJsonUtility.formatJson(executableResult.getDomainContext().getAttachmentDomain().toString()));

//		System.out.println();
//		System.out.println();
//		System.out.println("*******************************************************************************");
//		System.out.println();
//		System.out.println();
//		System.out.println(HAPJsonUtility.formatJson(executableResult.getDomainContext().getExecutableDomain().toString()));
	}
	
	private static HAPResourceIdSimple createResourceId(String resourceType, String id) {
		HAPIdEntity entityId = new HAPIdEntity(new HAPIdEntityType(resourceType, "1.0.0"), HAPConstantShared.ENTITY_DIVISION_MANUAL, id);
		return HAPUtilityEntity.fromEntityId2ResourceId(entityId);
	}
}
