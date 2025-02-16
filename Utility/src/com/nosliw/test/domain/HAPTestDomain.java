package com.nosliw.test.domain;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPIdBrick;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPUtilityBrickId;
import com.nosliw.core.application.HAPUtilityBundle;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

class A {
	private int i = 10;
	
	public int getI() {return this.i; } 
}

class B extends A {
	private int i = 20;
	@Override
	public int getI() {return this.i; } 
}

public class HAPTestDomain {
	
	public static void main1(String[] args) {
		A a = new B();
		System.out.println(a.getI());
		
		Runnable r = () -> {int kkk = 555;};
		
		Function<String, Integer> stringToInterger = Integer::parseInt;
		stringToInterger.apply("aaa");
		BiPredicate<List<String>, String> container = List::contains;
		
		Predicate<String> check1 = String::isEmpty; 
		Function<List<String>, List<String>> aaa = aa -> {
			return aa;
		};
		
//		Collector a = null;
		
		
	}
	
	
	public static void main(String[] args) {
		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();
//		HAPDomainEntityDefinitionGlobal globalDomain = new HAPDomainEntityDefinitionGlobal(new HAPGeneratorId(), runtimeEnvironment.getDomainEntityManager(), runtimeEnvironment.getResourceDefinitionManager());

//		HAPResourceIdSimple resourceId = createResourceId(HAPConstantShared.RUNTIME_RESOURCE_TYPE_MODULE, "test0");
		
		
//		HAPResourceIdSimple resourceId = createResourceId(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DECORATION_SCRIPT, "decoration_taskgroup");

//		HAPResourceIdSimple resourceId = createResourceId(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_1, "basic");
		HAPResourceIdSimple resourceId = createResourceId(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_1, "flow");

//		HAPResourceIdSimple resourceId = createResourceId(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TASKWRAPPER, "datavalidationmandatory");

//		HAPResourceIdSimple resourceId = createResourceId(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_1, "temp");
		
//		HAPResourceIdSimple resourceId = createResourceId(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_1, "event");
//		HAPResourceIdSimple resourceId = createResourceId(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_1, "datavalidation");
//		HAPResourceIdSimple resourceId = createResourceId(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_1, "task");

//		HAPResourceIdSimple resourceId = createResourceId(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_1, "uitagdebug");
		
//		HAPResourceIdSimple resourceId = createResourceId(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_1, "test.entity.valuestructure.merge");
		
//		HAPResourceIdSimple resourceId = createResourceId(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONLIB, "test1");

//		HAPResourceIdSimple resourceId = createResourceId(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONGROUP, "test0");

//		HAPResourceIdSimple resourceId = createResourceId(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONGROUP, "test1");

//		HAPResourceIdSimple resourceId = createResourceId(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONLIB, "test1");

//		HAPResourceIdSimple resourceId = createResourceId(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIPAGE, "test0");

		
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
		HAPBundle bundle = HAPUtilityBundle.getBrickBundle(resourceId, runtimeEnvironment.getBrickManager());
		
//		HAPApplicationPackage executablePackage = runtimeEnvironment.getBrickManager().getBrickPackage(resourceId);
		
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
	
	private static HAPResourceIdSimple createResourceIdManual(String resourceType, String id) {
		HAPIdBrick entityId = new HAPIdBrick(new HAPIdBrickType(resourceType, "1.0.0"), HAPConstantShared.BRICK_DIVISION_MANUAL, id);
		return HAPUtilityBrickId.fromBrickId2ResourceId(entityId);
	}

	private static HAPResourceIdSimple createResourceId(String resourceType, String id) {
		HAPIdBrick entityId = new HAPIdBrick(new HAPIdBrickType(resourceType, "1.0.0"), null, id);
		return HAPUtilityBrickId.fromBrickId2ResourceId(entityId);
	}
}
