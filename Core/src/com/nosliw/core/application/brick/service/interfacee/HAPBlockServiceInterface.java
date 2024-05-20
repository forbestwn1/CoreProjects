package com.nosliw.core.application.brick.service.interfacee;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.displayresource.HAPDisplayResourceNode;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.brick.interactive.interfacee.HAPBlockInteractiveInterface;
import com.nosliw.core.application.common.entityinfo.HAPBrickWithEntityInfoSimple;

//static information for a service. readable, query for service
//information needed during configuration time
@HAPEntityWithAttribute
public class HAPBlockServiceInterface extends HAPBrickWithEntityInfoSimple{

	@HAPAttribute
	public static String INTERFACE = "interface";

	@HAPAttribute
	public static String TAG = "tag";

	@HAPAttribute
	public static String DISPLAY = "display";

	public HAPBlockServiceInterface() {
		this.setAttributeValueWithValue(TAG, new ArrayList<String>());
		this.setAttributeValueWithValue(DISPLAY, new HAPDisplayResourceNode());
	}

	public HAPBlockInteractiveInterface getActiveInterface() {  return (HAPBlockInteractiveInterface)this.getAttributeValueOfBrick(INTERFACE);  } 
	
	public List<String> getTags(){   return (List<String>)this.getAttributeValueOfValue(TAG);    }
	
	public HAPDisplayResourceNode getDisplayResource() {   return (HAPDisplayResourceNode)this.getAttributeValueOfValue(DISPLAY);     }
	
//	public void process(HAPRuntimeEnvironment runtimeEnv) {  this.getActiveInterface().process(runtimeEnv);	}
	
    @Override
	protected HAPIdBrickType getAttributeBrickType(String attrName) {
    	if(INTERFACE.equals(attrName)) {
    		return HAPEnumBrickType.INTERACTIVEINTERFACE_100;
    	}
    	return null;     
    }
	
    @Override
	protected Object buildAttributeValueFormatJson(String attrName, Object obj) {
    	Object out = super.buildAttributeValueFormatJson(attrName, obj);
    	if(TAG.equals(attrName)) {
    		List<String> tags = new ArrayList<String>();
    		JSONArray tagArray = (JSONArray)obj;
    		if(tagArray!=null) {
    			for(int i=0; i<tagArray.length(); i++) {
    				tags.add(tagArray.getString(i));
    			}
    		}
    		out = tags;
    	}
    	else if(DISPLAY.equals(attrName)) {
    		JSONObject displayResourceObj = ((JSONObject)obj).optJSONObject(DISPLAY);
    		if(displayResourceObj!=null) {
    			this.getDisplayResource().buildObject(displayResourceObj, HAPSerializationFormat.JSON);
    		}
    	}
    	return out;
    }
}
