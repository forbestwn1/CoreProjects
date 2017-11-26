package com.nosliw.common.configure;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPSegmentParser;

/*
 * node for tree structure configuration
 * configuration has two type of value: configure value and variable value
 * 		configure value has tree structure: every configure is identified by path
 * 		variable value has no tree structure, it is identified by single name
 * configure value cannot refered to another configure, 
 * configure value can only refer to variable value	
 * 		in that case, configure search the variable name within its own scope
 * 		if not found, then search the variable name within parent scrope
 * 	when clone a configure from a child configure, the variables in new configure are all the variables from child configure to root parent configure by variable in child override parent
 *  for external party that use this configure, variable within this configure root can also be a configure item
 *  so that when a party get configure value from configure
 *  	it search within configure first
 *  	if not found, then search within variables in root configure 
 */
public class HAPConfigureImp extends HAPConfigureItem implements HAPConfigure{
	//child configure nested
	private Map<String, HAPConfigureImp> m_childConfigures;
	//configure values
	private Map<String, HAPConfigureValueString> m_values;
	
	//all variables within this configur node
	private Map<String, HAPVariableValue> m_variables;
	
	//base configure used to resolve configure
	private HAPConfigureImp m_baseConfigure;
	
	/*
	 * empty constructor
	 */
	HAPConfigureImp(){
		this.m_childConfigures = new LinkedHashMap<String, HAPConfigureImp>();
		this.m_values = new LinkedHashMap<String, HAPConfigureValueString>();
		this.m_variables = new LinkedHashMap<String, HAPVariableValue>();
	}

	public HAPConfigureImp getBaseConfigure(){  return this.m_baseConfigure;  }
	public void setBaseConfigure(HAPConfigureImp configure){  this.m_baseConfigure = configure;  }
	
	@Override
	public HAPConfigureValue getConfigureValue(String attr){  
		return (HAPConfigureValue)this.getConfigureItemByPath(attr, HAPConfigureItem.VALUE);
	}


	/*
	 * this method is for external party to find property from this configure
	 */
	public String getStringValue(String name){
		HAPConfigureValue configureValue = this.getConfigureValue(name);
		if(configureValue!=null)  return configureValue.getStringContent();
		else{
			HAPVariableValue varValue = this.getVariableValue(name);
			if(varValue!=null)  return varValue.getStringContent();
			else  return null;
		}
	}
	
	HAPConfigureItem getConfigureItemByPath(String path, String type){
		HAPConfigureItem out = this;
		if(HAPBasicUtility.isStringNotEmpty(path)){
			int index = path.indexOf(HAPConstant.SEPERATOR_PATH);
			if(index==-1){
				//single
				switch(type){
					case HAPConfigureItem.CONFIGURE:
						out = this.getChildConfigure(path);	
						break;
					case HAPConfigureItem.VALUE:
						out = this.getChildConfigureValue(path);
						break;
					case HAPConfigureItem.VARIABLE:
						out = this.getChildVaraibleValue(path);
				}
			}
			else{
				//multiple path
				HAPConfigureImp subConf = this.getChildConfigure(path.substring(0, index));
				if(subConf==null)  out = null;
				else	out = subConf.getConfigureItemByPath(path.substring(index+1), type);
			}
		}
		return out;
	}
	
	public HAPConfigureImp addConfigureItem(String name, String value){
		String isGlobalVar = HAPConfigureUtility.isGlobalVariable(name);
		if(isGlobalVar==null){
			//normal configure
			this.addConfigureItem(name, value, false);
		}
		else{
			//global variable definition
			this.addConfigureItem(isGlobalVar, value, true);
		}
		return this;
	}

	private void addConfigureItem(String name, String value, boolean isVariable) {
		HAPSegmentParser nameSegs = new HAPSegmentParser(name);
		if(nameSegs.getSegmentSize()>1){
			//if name have path structure, then have configurable nested
			String childName = nameSegs.next();
			HAPConfigureImp childConf = (HAPConfigureImp)this.getChildConfigure(childName);
			if(childConf==null){
				childConf = new HAPConfigureImp();
				this.addChildConfigure(childName, childConf);
			}
			childConf.addConfigureItem(nameSegs.getRestPath(), value);
		}
		else{
			if(isVariable){
				//variable
				this.addVariableValue(name, value);
			}
			else{
				//configure value
				this.addChildConfigureValue(name, value);
			}
		}
	}
	
	public void addVariableValue(String name, String value){
		HAPVariableValue varValue = new HAPVariableValue(value);
		this.addVariableValue(name, varValue);
	}
	
	public void addVariableValue(String name, HAPVariableValue value){
		value.setParent(this);
		this.m_variables.put(name, value);
	}
	
	public void addChildConfigureValue(String name, String value){ 
		this.addChildConfigureValue(name, new HAPConfigureValueString(value)); 
	}
	public void addChildConfigureValue(String name, HAPConfigureValueString value){
		value.setParent(this);
		this.m_values.put(name, value); 
	}

	public void addChildConfigure(String name, HAPConfigureImp configure){
		configure.setParent(this);
		this.m_childConfigures.put(name, configure); 
	}
	
	/*
	 * resolve all configureation
	 */
	public void resolve(){
		for(String name : this.m_variables.keySet()){
			HAPResolvableConfigureItem item = this.m_variables.get(name);
			HAPConfigureUtility.resolveConfigureItem(item, true);
		}
		for(String name : this.m_values.keySet()){
			HAPResolvableConfigureItem item = this.m_values.get(name);
			HAPConfigureUtility.resolveConfigureItem(item, true);
		}
		for(String name : this.m_childConfigures.keySet()){
			HAPConfigureImp childConfigure = this.m_childConfigures.get(name);
			childConfigure.resolve();
		}
	}
	
	/*
	 * get variable value according to its name
	 * if value does not exits in current configure scope, try to search in parent scope
	 */
	public HAPVariableValue getVariableValue(String name){
		HAPVariableValue out = this.m_variables.get(name);
		if(out==null){
			HAPConfigureImp parent = this.getParent();
			if(parent!=null){
				//if not find in current scope, try to find in parent
				out = parent.getVariableValue(name);
			}
		}
		return out;
	}


	public HAPConfigureImp getChildConfigure(String childName){return this.m_childConfigures.get(childName);}
	public HAPConfigureValueString getChildConfigureValue(String childName){ return this.m_values.get(childName);  }
	public Map<String, HAPVariableValue> getChildVariables(){ return this.m_variables; }
	public HAPVariableValue getChildVaraibleValue(String childName){ return this.m_variables.get(childName);}
	public Map<String, HAPConfigureImp> getChildConfigurables(){return this.m_childConfigures;}
	public Map<String, HAPConfigureValueString> getChildConfigureValues(){ return this.m_values;  }
	public Set<String> getConfigureNames(){  return this.m_values.keySet(); }
	
	/*
	 * sort out variable values for this configureation base
	 */
	private void mergeVariables(Map<String, HAPVariableValue> vars){
		HAPConfigureImp parent = this.getParent();
		if(parent!=null){
			parent.mergeVariables(vars);
		}
		for(String name : this.m_variables.keySet()){
			vars.put(name, (HAPVariableValue)this.m_variables.get(name).clone());
		}
	}
	
	/*
	 * create a new configure with same configure, value and merged variable
	 */
	public HAPConfigureImp cloneConfigure(){
		HAPConfigureImp out = new HAPConfigureImp();
		
		Map<String, HAPVariableValue> vars = new LinkedHashMap<String, HAPVariableValue>();
		this.mergeVariables(vars);
		for(String name : vars.keySet()){
			out.addVariableValue(name, (HAPVariableValue)vars.get(name));
		}
		
		//clone configure
		for(String name : this.getChildConfigurables().keySet()){
			out.addChildConfigure(name, (HAPConfigureImp)this.getChildConfigure(name).clone());
		}
		
		//clone configure values
		for(String name : this.getChildConfigureValues().keySet()){
			out.addChildConfigureValue(name, (HAPConfigureValueString)this.getChildConfigureValue(name).clone());
		}
		return out;
	}
	
	public HAPConfigureItem clone(){
		HAPConfigureImp out = new HAPConfigureImp();
		out.setParent(this);getParent();
		for(String name : this.m_variables.keySet()){
			out.addVariableValue(name, this.m_variables.get(name).clone());
		}
		
		//clone configure
		for(String name : this.getChildConfigurables().keySet()){
			out.addChildConfigure(name, (HAPConfigureImp)this.getChildConfigurables().get(name).clone());
		}
		
		//clone configure values
		for(String name : this.getChildConfigureValues().keySet()){
			out.addChildConfigureValue(name, (HAPConfigureValueString)this.getChildConfigureValues().get(name).clone());
		}
		return out;
	}
	
	@Override
	public HAPConfigure cloneChildConfigure(String path) {
		HAPConfigureImp configure = (HAPConfigureImp)this.getConfigureItemByPath(path, HAPConfigureItem.CONFIGURE);
		if(configure==null)  return null;
		else{
			return configure.cloneConfigure();
		}
	}
	
	@Override
	public String toString(){ return HAPJsonUtility.formatJson(this.toStringValue(HAPSerializationFormat.JSON));}

	@Override
	public String toStringValue(HAPSerializationFormat format) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		
		jsonMap.put("variables", HAPJsonUtility.buildJson(this.m_variables, format));
		jsonMap.put("childValues", HAPJsonUtility.buildJson(this.getChildConfigureValues(), format));
		jsonMap.put("childConfigurables", HAPJsonUtility.buildJson(this.getChildConfigurables(), format));
		return HAPJsonUtility.buildMapJson(jsonMap);
	}

	@Override
	String getType() {
		return HAPConfigureItem.CONFIGURE;
	}

	@Override
	public boolean buildObject(Object value, HAPSerializationFormat format) { return true;	}
}
