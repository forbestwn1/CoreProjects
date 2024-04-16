package com.nosliw.core.application.brick.service.interfacee;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.displayresource.HAPDisplayResourceNode;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.brick.interactive.interfacee.HAPBrickInteractiveInterface;
import com.nosliw.core.application.common.entityinfo.HAPBrickWithEntityInfoSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

//static information for a service. readable, query for service
//information needed during configuration time
@HAPEntityWithAttribute
public class HAPBrickServiceInterface extends HAPBrickWithEntityInfoSimple{

	@HAPAttribute
	public static String INTERFACE = "interface";

	@HAPAttribute
	public static String TAG = "tag";

	@HAPAttribute
	public static String DISPLAY = "display";

	public HAPBrickServiceInterface() {
		this.setAttributeValueWithValue(TAG, new ArrayList<String>());
		this.setAttributeValueWithValue(DISPLAY, new HAPDisplayResourceNode());
	}

	public HAPBrickInteractiveInterface getActiveInterface() {  return (HAPBrickInteractiveInterface)this.getAttributeBrick(INTERFACE);  } 
	
	public List<String> getTags(){   return (List<String>)this.getAttributeValue(TAG);    }
	
	public HAPDisplayResourceNode getDisplayResource() {   return (HAPDisplayResourceNode)this.getAttributeValue(DISPLAY);     }
	
	public void process(HAPRuntimeEnvironment runtimeEnv) {  this.getActiveInterface().process(runtimeEnv);	}
	
    @Override
	protected boolean buildAttributeValueFormatJson(String attrName, Object obj) {
    	super.buildAttributeValueFormatJson(attrName, obj);
    	if(TAG.equals(attrName)) {
    		this.getTags().clear();
    		JSONArray tagArray = (JSONArray)obj;
    		if(tagArray!=null) {
    			for(int i=0; i<tagArray.length(); i++) {
    				this.getTags().add(tagArray.getString(i));
    			}
    		}
    	}
    	else if(DISPLAY.equals(attrName)) {
    		JSONObject displayResourceObj = ((JSONObject)obj).optJSONObject(DISPLAY);
    		if(displayResourceObj!=null) {
    			this.getDisplayResource().buildObject(displayResourceObj, HAPSerializationFormat.JSON);
    		}
    	}
    	return true;
    }
}
