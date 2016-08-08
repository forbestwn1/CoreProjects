package com.nosliw.common.test;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.configure.HAPConfigureImp;
import com.nosliw.common.configure.HAPConfigureManager;

/*
 * test enviroment data
 * it is created at test suite level
 * 		every test case can clone it and add variable to it in before method
 * 		child test suite will get the test env from parent test suite if it does not have its own
 */
public class HAPTestEnv {

	static private final String BASE_OBJECT_NAME = "this"; 
	
	private HAPConfigureImp m_configures;
	
	private Map<String, Object> m_objects;
	
	public HAPTestEnv(HAPConfigureImp configures){
		if(configures!=null){
			this.m_configures = configures;
		}
		else{
			this.m_configures = HAPConfigureManager.getInstance().createConfigure();
		}
		this.m_objects = new LinkedHashMap<String, Object>();
	}

	public HAPTestEnv(){
		this(null);
	}

	public HAPTestEnv softMerge(HAPTestEnv testEnv){
		this.m_configures = this.m_configures.merge(testEnv.m_configures, false, false);
		for(String name : testEnv.m_objects.keySet()){
			if(this.m_objects.get(name)==null){
				this.m_objects.put(name, testEnv.m_objects.get(name));
			}
		}
		return this;
	}
	
	public Object getBaseObject(){  return this.m_objects.get(BASE_OBJECT_NAME); }
	public void setBaseObject(Object obj){  this.m_objects.put(BASE_OBJECT_NAME, obj); }
	
	public HAPConfigureImp getConfigure(){ return this.m_configures; }
	public Map<String, Object> getValues(){  return this.m_objects; }
	
}
