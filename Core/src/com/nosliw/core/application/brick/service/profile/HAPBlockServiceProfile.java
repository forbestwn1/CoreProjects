package com.nosliw.core.application.brick.service.profile;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.brick.service.interfacee.HAPBlockServiceInterface;
import com.nosliw.core.application.common.entityinfo.HAPBrickWithEntityInfoSimple;

//contains all information related with service definition
@HAPEntityWithAttribute
public class HAPBlockServiceProfile extends HAPBrickWithEntityInfoSimple{

	public static final String CHILD_INTERFACE = "interface";
	
	@HAPAttribute
	public static String TAG = "tag";

	@HAPAttribute
	public static String INTERFACE = "interface";

	public HAPBlockServiceProfile(){
	} 
	
	@Override
	public void init() {
		this.setAttributeValueWithValue(TAG, new ArrayList<String>());
	}
	
	public void setInterface(HAPEntityOrReference brickOrRef) {		this.setAttributeValueWithBrick(INTERFACE, brickOrRef);	}
	public HAPEntityOrReference getServiceInterface() {   return this.getAttributeValueOfBrick(INTERFACE);   }
	
	public List<String> getTags(){   return (List<String>)this.getAttributeValueOfValue(TAG);    }
	
//	public void process(HAPRuntimeEnvironment runtimeEnv) {
//		this.m_staticInfo.process(runtimeEnv);
//		this.m_isProcessed = true;
//	}
	
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
//    	else if(DISPLAY.equals(attrName)) {
//    		JSONObject displayResourceObj = ((JSONObject)obj).optJSONObject(DISPLAY);
//    		if(displayResourceObj!=null) {
//    			this.getDisplayResource().buildObject(displayResourceObj, HAPSerializationFormat.JSON);
//    		}
//    	}
    	return out;
    }

    @Override
	protected HAPIdBrickType getAttributeBrickType(String attrName) {
    	if(INTERFACE.equals(attrName)) {
    		return HAPEnumBrickType.INTERACTIVEINTERFACE_100;
    	}
    	return null;     
    }
    
	protected boolean buildBrickFormatJson1(JSONObject jsonObj, HAPManagerApplicationBrick brickMan) {
		try{
			JSONObject objJson = (JSONObject)json;
			super.buildObjectByJson(objJson);
			
			Object interfaceObj = objJson.get(INTERFACE);
			if(interfaceObj instanceof String) {
//				this.m_interface = HAPFactoryResourceId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICEINTERFACE, interfaceObj);
			}
			else {
				HAPBlockServiceInterface serviceInterfaceInfo = new HAPBlockServiceInterface();
				serviceInterfaceInfo.buildObject(objJson, HAPSerializationFormat.JSON);
				this.m_interface = serviceInterfaceInfo;
			}

			this.m_tags.clear();
			JSONArray tagArray = objJson.optJSONArray(TAG);
			if(tagArray!=null) {
				for(int i=0; i<tagArray.length(); i++) {
					this.m_tags.add(tagArray.getString(i));
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;  
	}
}
