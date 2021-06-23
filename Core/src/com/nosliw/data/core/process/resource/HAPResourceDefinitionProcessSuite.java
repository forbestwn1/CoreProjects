package com.nosliw.data.core.process.resource;

import java.util.Map;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPContainerChildReferenceResource;
import com.nosliw.data.core.component.HAPElementContainerResourceDefinition;
import com.nosliw.data.core.component.HAPResourceDefinitionContainer;
import com.nosliw.data.core.process.HAPDefinitionProcessSuite;
import com.nosliw.data.core.resource.HAPResourceDefinition;

//suite that contain multiple process
@HAPEntityWithAttribute
public class HAPResourceDefinitionProcessSuite extends HAPResourceDefinitionContainer<HAPElementContainerResourceDefinition> implements HAPDefinitionProcessSuite{

	public HAPResourceDefinitionProcessSuite() {
	}

	@Override
	public String getResourceType() {   return HAPConstantShared.RUNTIME_RESOURCE_TYPE_PROCESSSUITE;  }

	@Override
	public HAPResourceDefinition getElementResourceDefinition(String eleName) {	return new HAPResourceDefinitionProcess(this, eleName);	}

	@Override
	public HAPContainerChildReferenceResource getChildrenReferencedResource() {
		return null;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
	}
	
	public HAPResourceDefinitionProcessSuite cloneProcessSuiteDefinition() {
		HAPResourceDefinitionProcessSuite out = new HAPResourceDefinitionProcessSuite();
		this.cloneToResourceDefinitionContainer(out);
		return out;
	}

	@Override
	public HAPResourceDefinitionContainer cloneResourceDefinitionContainer() {	return this.cloneProcessSuiteDefinition();	}

}
