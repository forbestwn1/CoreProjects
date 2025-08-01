package com.nosliw.core.application.entity.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.displayresource.HAPDisplayResourceNode;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.common.interactive.HAPInteractiveTask;

@HAPEntityWithAttribute
public class HAPServiceProfile extends HAPEntityInfoImp{

	@HAPAttribute
	public static String INTERFACE = "interface";

	@HAPAttribute
	public static String TAG = "tag";

	@HAPAttribute
	public static String DISPLAY = "display";

	
	private HAPInteractiveTask m_interface;
	
	private List<String> m_tags;
	
	private HAPDisplayResourceNode m_displayResource;
	
	public HAPServiceProfile() {
		this.m_tags = new ArrayList<String>();
	}
	
	public HAPInteractiveTask getInterface() {    return this.m_interface;      }
	public void setInterface(HAPInteractiveTask taskInterface) {   this.m_interface = taskInterface;    }
	
	public List<String> getTags(){   return this.m_tags;  }
	public void setTags(List<String> tags) {
		if(tags!=null) {
			this.m_tags.addAll(tags);
		}      
	}

	public HAPDisplayResourceNode getDisplayResource() {    return this.m_displayResource;      }
	public void setDisplayResource(HAPDisplayResourceNode displayResource) {     this.m_displayResource = displayResource;      }
	
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(INTERFACE, HAPManagerSerialize.getInstance().toStringValue(this.m_interface, HAPSerializationFormat.JSON));
		jsonMap.put(TAG, HAPManagerSerialize.getInstance().toStringValue(this.m_tags, HAPSerializationFormat.JSON));
		jsonMap.put(DISPLAY, HAPManagerSerialize.getInstance().toStringValue(this.m_displayResource, HAPSerializationFormat.JSON));
	}
	
}
