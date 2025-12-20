package com.nosliw.core.application.entity.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.displayresource.HAPDisplayResourceNode;
import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPWrapperValueOfReferenceResource;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.brick.service.profile.HAPBlockServiceProfile;
import com.nosliw.core.application.common.brick.HAPBrickImpWithEntityInfo;
import com.nosliw.core.application.common.interactive.HAPWithBlockInteractiveTask;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;
import com.nosliw.core.application.valueport.HAPContainerValuePorts;
import com.nosliw.core.resource.HAPFactoryResourceId;
import com.nosliw.core.resource.HAPResourceId;

//contains all information related with service definition
@HAPEntityWithAttribute
public class HAPBlockServiceProfileImp extends HAPBrickImpWithEntityInfo implements HAPBlockServiceProfile{

	public HAPBlockServiceProfileImp(){
		this.setBrickType(HAPEnumBrickType.SERVICEPROFILE_100);
	} 
	
	@Override
	public void setTaskInterface(HAPEntityOrReference brickOrRef) {		this.setAttributeValueWithBrick(HAPWithBlockInteractiveTask.TASKINTERFACE, brickOrRef);	}
	@Override
	public HAPEntityOrReference getTaskInterface() {   return this.getAttributeValueOfBrickLocal(HAPWithBlockInteractiveTask.TASKINTERFACE);   }
	
	@Override
	public List<String> getTags(){   return (List<String>)this.getAttributeValueOfValue(TAG);    }
	public void setTags(List<String> tags) {    this.setAttributeValueWithValue(TAG, tags);       }
	
	@Override
	public HAPDisplayResourceNode getDisplayResource() {    return (HAPDisplayResourceNode)this.getAttributeValueOfValue(DISPLAY);  }
	public void setDisplayResource(HAPDisplayResourceNode displayResource) {    this.setAttributeValueWithValue(DISPLAY, displayResource);      }
 
	public static HAPBlockServiceProfileImp parse(JSONObject jsonObj, HAPManagerDataRule dataRuleMan){
		HAPBlockServiceProfileImp out = new HAPBlockServiceProfileImp();
		out.buildEntityInfoByJson(jsonObj);

		Object displayObj = jsonObj.opt(DISPLAY);
		if(displayObj!=null) {
			HAPDisplayResourceNode displayResource = new HAPDisplayResourceNode();
			displayResource.buildObject(displayObj, HAPSerializationFormat.JSON);
			out.setDisplayResource(displayResource);
		}
		
		Object resourceObj = jsonObj.opt(HAPWrapperValueOfReferenceResource.RESOURCEID);
		if(resourceObj!=null) {
			HAPResourceId resourceId = HAPFactoryResourceId.tryNewInstance(HAPEnumBrickType.INTERACTIVETASKINTERFACE_100.getBrickType(), HAPEnumBrickType.INTERACTIVETASKINTERFACE_100.getVersion(), resourceObj);
			out.setTaskInterface(resourceId);
		}
		else {
			out.setTaskInterface(HAPUtilityServiceParse.parseTaskInterfaceInterfaceBlock(jsonObj, dataRuleMan));
		}
		
		List<String> tags = new ArrayList<String>();
		JSONArray tagArray = jsonObj.optJSONArray(TAG);
		if(tagArray!=null) {
			for(int i=0; i<tagArray.length(); i++) {
				tags.add(tagArray.getString(i));
			}
		}
		out.setTags(tags);
		
		return out;  
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		
		this.buildEntityInfoByJson(jsonObj);

		Object displayObj = jsonObj.opt(DISPLAY);
		if(displayObj!=null) {
			HAPDisplayResourceNode displayResource = new HAPDisplayResourceNode();
			displayResource.buildObject(displayObj, HAPSerializationFormat.JSON);
			this.setDisplayResource(displayResource);
		}
		
		Object resourceObj = jsonObj.opt(HAPWrapperValueOfReferenceResource.RESOURCEID);
		if(resourceObj!=null) {
			HAPResourceId resourceId = HAPFactoryResourceId.tryNewInstance(HAPEnumBrickType.INTERACTIVETASKINTERFACE_100.getBrickType(), HAPEnumBrickType.INTERACTIVETASKINTERFACE_100.getVersion(), resourceObj);
			this.setTaskInterface(resourceId);
		}
		else {
			this.setTaskInterface(HAPUtilityServiceParse.parseTaskInterfaceInterfaceBlock(jsonObj));
		}
		
		List<String> tags = new ArrayList<String>();
		JSONArray tagArray = jsonObj.optJSONArray(TAG);
		if(tagArray!=null) {
			for(int i=0; i<tagArray.length(); i++) {
				tags.add(tagArray.getString(i));
			}
		}
		this.setTags(tags);
		
		return true;  
	}

	@Override
	public HAPContainerValuePorts getInternalValuePorts() {  return null;   }

	@Override
	public HAPContainerValuePorts getExternalValuePorts() {   return null;   }

}
