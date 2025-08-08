package com.nosliw.core.application.brick.test.complex.script;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.valueport.HAPReferenceElement;
import com.nosliw.core.application.valueport.HAPResultReferenceResolve;
import com.nosliw.core.resource.HAPResourceId;

@HAPEntityWithAttribute
public interface HAPBlockTestComplexScript extends HAPBrick{

	@HAPAttribute
	public static String SCRIPT = "script";
	
	@HAPAttribute
	public static String PARM = "parm";

	@HAPAttribute
	public static String VARIABLE = "variable";
	
	@HAPAttribute
	public static String ATTACHMENT = "attachment";
	
	@HAPAttribute
	public static String UNKNOWNVARIABLE = "unknownVariable";
	
	@HAPAttribute
	public static String VARIABLEEXTENDED = "variableExtended";
	
	@HAPAttribute
	public static String TASKTRIGGUER = "taskTrigguer";

	HAPResourceId getScrip();
	Map<String, Object> getParms();
	List<HAPResultReferenceResolve> getVariables();
	List<HAPReferenceElement> getUnknownVariables();
	List<HAPExecutableVariableExpected> getExtendedVariables();
	List<HAPInfoAttachmentResolve> getAttachments();

	List<HAPTestTaskTrigguer> getTaskTrigguers();
	
//	@Override
//	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPManagerResource resourceManager) {
//		HAPResourceDependency scriptResource = new HAPResourceDependency(HAPFactoryResourceId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT, this.getScriptName()));
//		dependency.add(scriptResource);
//	}

}
