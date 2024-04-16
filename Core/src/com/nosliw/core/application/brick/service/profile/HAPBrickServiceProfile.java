package com.nosliw.core.application.brick.service.profile;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.brick.interactive.interfacee.HAPBrickInteractiveInterface;
import com.nosliw.core.application.brick.service.interfacee.HAPBrickServiceInterface;
import com.nosliw.core.application.common.entityinfo.HAPBrickWithEntityInfoSimple;
import com.nosliw.core.application.service.HAPInfoServiceRuntime;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

//contains all information related with service definition
@HAPEntityWithAttribute
public class HAPBrickServiceProfile extends HAPBrickWithEntityInfoSimple{

	public static final String CHILD_INTERFACE = "interface";
	
	@HAPAttribute
	public static String TAG = "tag";

	@HAPAttribute
	public static String INTERFACE = "interface";

	private HAPBrickServiceInterface m_interface;

	private List<String> m_tags;
	
	//information used for configuration, management purpose
	private HAPInfoServiceStatic m_staticInfo;
	
	private boolean m_isProcessed = false;
	
	public HAPBrickServiceProfile(){
		this.setAttributeValueWithValue(TAG, new ArrayList<String>());
	}
	
	public void setInterface(HAPEntityOrReference brickOrRef) {		this.setAttributeValueWithBrick(INTERFACE, brickOrRef);	}
	public HAPBrickInteractiveInterface getServiceInterface() {   return this.getAttributeBrick(INTERFACE);   }
	
	public List<String> getTags(){   return (List<String>)this.getAttributeValue(TAG);    }
	

	public void process(HAPRuntimeEnvironment runtimeEnv) {
		this.m_staticInfo.process(runtimeEnv);
		this.m_isProcessed = true;
	}
	
	public boolean isProcessed() {   return this.m_isProcessed;   }
	
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

	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject objJson = (JSONObject)json;
			
			this.m_staticInfo = new HAPInfoServiceStatic();
			this.m_staticInfo.buildObjectByJson(objJson.getJSONObject(STATIC));
			
			this.m_runtimeInfo = new HAPInfoServiceRuntime();
			this.m_runtimeInfo.buildObjectByJson(objJson.getJSONObject(RUNTIME));
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;  
	}
}
