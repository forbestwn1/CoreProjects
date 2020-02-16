package com.nosliw.data.core.process;

import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPEntityInfoWritable;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.component.HAPAttachment;
import com.nosliw.data.core.component.HAPAttachmentContainer;
import com.nosliw.data.core.component.HAPChildrenComponentIdContainer;
import com.nosliw.data.core.component.HAPComponent;
import com.nosliw.data.core.component.HAPComponentUtility;
import com.nosliw.data.core.component.HAPHandlerEvent;
import com.nosliw.data.core.component.HAPHandlerLifecycle;
import com.nosliw.data.core.component.HAPWithAttachment;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.script.context.HAPContextGroup;

public class HAPDefinitionProcessWrapper  extends HAPSerializableImp implements HAPComponent{

	@HAPAttribute
	public static String SUITE = "suite";

	@HAPAttribute
	public static String PROCESS = "process";

	@HAPAttribute
	public static String PROCESSNAME = "processName";

	private HAPResourceId m_resourceId;

	private HAPDefinitionProcessSuite m_suite;
	
	private String m_processName;
	
	private HAPDefinitionProcessSuiteElementEntity m_process;

	public HAPDefinitionProcessWrapper(HAPDefinitionProcessSuite suite, String process) {
		this.m_suite = suite;
		this.m_processName = process;
		this.m_process = this.m_suite.getProcessElement(this.m_processName);
		HAPComponentUtility.mergeWithParentAttachment(this.m_process, this.m_suite.getAttachmentContainer());
	}
	
	@Override
	public String getResourceType() {   return HAPConstant.RUNTIME_RESOURCE_TYPE_PROCESS;  }
	
	public HAPDefinitionProcessSuiteElementEntity getProcess() {    return this.m_process;   }
	
	public HAPDefinitionProcessSuite getSuite() {   return this.m_suite;  }

	@Override
	public HAPChildrenComponentIdContainer getChildrenComponentId() {
		return this.m_process.getChildrenComponentId();
	}
	
	@Override
	public HAPInfo getInfo() {		return this.m_process.getInfo();	}

	@Override
	public void setResourceId(HAPResourceId resourceId) {  this.m_resourceId = resourceId; }

	@Override
	public HAPResourceId getResourceId() {  return this.m_resourceId;  }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PROCESS, this.m_processName);
		jsonMap.put(SUITE, HAPJsonUtility.buildJson(this.m_suite, HAPSerializationFormat.JSON));
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cloneToEntityInfo(HAPEntityInfoWritable entityInfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buildEntityInfoByJson(Object json) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HAPEntityInfo clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPAttachmentContainer getAttachmentContainer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, HAPAttachment> getAttachmentsByType(String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void mergeBy(HAPWithAttachment parent, String mode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<HAPHandlerLifecycle> getLifecycleAction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addLifecycleAction(HAPHandlerLifecycle lifecycleAction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<HAPHandlerEvent> getEventHandlers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addEventHandler(HAPHandlerEvent eventHandler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HAPContextGroup getContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setContext(HAPContextGroup context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPDefinitionProcessWrapper getProcess(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
