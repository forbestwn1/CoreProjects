package com.nosliw.data.core.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPJsonUtility;

/**
 *  Store all the information related with a resource, including
 *  	Id
 *  	info: configure info, it normally used to control behavior
 *  	dependency: in order to use this resource, dependency has to be avaiable 
 *  	children:  this resource is a container, its children are real resources
 *  Except: 	
 *  	resource data
 */
@HAPEntityWithAttribute
public class HAPResourceInfo extends HAPSerializableImp{

	@HAPAttribute
	public static String ID = "id";
	
	//When we need to load resource according to resource id, sometimes, we need provide more information in order to control how resources are loaded
	@HAPAttribute
	public static String INFO = "info";

	@HAPAttribute
	public static String DEPENDENCY = "dependency";
	
	@HAPAttribute
	public static String CHILDREN = "children";
	
	
	private HAPResourceId m_resourceId;
	private HAPInfo m_info = new HAPInfoImpSimple();;
	private List<HAPResourceDependent> m_dependency = new ArrayList<HAPResourceDependent>();
	private List<HAPResourceDependent> m_children = new ArrayList<HAPResourceDependent>();
	
	public HAPResourceInfo(){	}
	
	public HAPResourceInfo(HAPResourceId resourceId){
		this.m_resourceId = resourceId;
	}
	
	public HAPResourceId getId(){		return this.m_resourceId;	}
	
	public void setInfo(String name, String value){		this.m_info.setValue(name, value);	}
	
	public HAPResourceInfo withInfo(String name, String value){
		this.setInfo(name, value);
		return this;
	}
	
	public HAPInfo getInfo(){		return this.m_info;	}
	
	public String getInfoValue(String name){		return this.m_info.getValue(name);	}
	
	public List<HAPResourceDependent> getDependency(){  return this.m_dependency;  }
	public void addDependency(HAPResourceDependent child){  this.m_dependency.add(child); }
	
	public List<HAPResourceDependent> getChildren(){  return this.m_children;  }
	public void addChild(HAPResourceDependent child){  this.m_children.add(child);  }
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ID, this.m_resourceId.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(CHILDREN, HAPJsonUtility.buildJson(this.getChildren(), HAPSerializationFormat.JSON));
		jsonMap.put(DEPENDENCY, HAPJsonUtility.buildJson(this.getDependency(), HAPSerializationFormat.JSON));
		jsonMap.put(INFO, HAPSerializeManager.getInstance().toStringValue(this.m_info, HAPSerializationFormat.JSON));
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		this.buildFullJsonMap(jsonMap, typeJsonMap);
	}

	@Override
	protected boolean buildObjectByJson(Object json){		return this.buildObjectByFullJson(json);	}

	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_resourceId = HAPResourceHelper.getInstance().buildResourceIdObject(jsonObj.optString(ID));
		
		JSONArray childrenArray = jsonObj.optJSONArray(CHILDREN);
		for(int i=0; i<childrenArray.length(); i++){
			JSONObject jsonChild = childrenArray.optJSONObject(i);
			HAPResourceDependent childResourceId = new HAPResourceDependent();
			childResourceId.buildObject(jsonChild, HAPSerializationFormat.JSON_FULL);
			this.addChild(childResourceId);
		}

		JSONArray dependencysArray = jsonObj.optJSONArray(DEPENDENCY);
		for(int i=0; i<dependencysArray.length(); i++){
			JSONObject jsonDependency = dependencysArray.optJSONObject(i);
			HAPResourceDependent dependencyResourceId = new HAPResourceDependent();
			dependencyResourceId.buildObject(jsonDependency, HAPSerializationFormat.JSON_FULL);
			this.addDependency(dependencyResourceId);
		}
		return true; 
	}
	
	public HAPResourceInfo clone(){
		HAPResourceInfo out = new HAPResourceInfo();
		out.cloneFrom(this);
		return out;
	}
	
	protected void cloneFrom(HAPResourceInfo resourceInfo){
		this.m_resourceId = resourceInfo.getId().clone();
		for(HAPResourceDependent dependency : resourceInfo.getDependency())			this.m_dependency.add(dependency.clone());
		for(HAPResourceDependent child : resourceInfo.getChildren())			this.m_children.add(child.clone());
	}
	
}
