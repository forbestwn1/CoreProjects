package com.nosliw.data.core.script.context;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.expression.HAPMatcherUtility;
import com.nosliw.data.core.expression.HAPMatchers;

public class HAPContextDefinitionLeafRelative extends HAPContextDefinitionLeafVariable{

	@HAPAttribute
	public static final String PATH = "path";

	//whether related to parent context or just to sibling root
	@HAPAttribute
	public static final String ISTOPARENT = "isToParent";

	@HAPAttribute
	public static final String PARENTCATEGARY = "parentCategary";

	@HAPAttribute
	public static final String DEFINITION = "definition";
	
	@HAPAttribute
	public static final String MATCHERS = "matchers";

	@HAPAttribute
	public static final String REVERSEMATCHERS = "reverseMatchers";
	
	//relative path from parent context
	private HAPContextPath m_path;
	private String m_pathStr;
	
	private HAPContextDefinitionElement m_definition;
	
	//context node full name --- matchers
	//used to convert data from parent to data within uiTag
	private Map<String, HAPMatchers> m_matchers;

	private Map<String, HAPMatchers> m_reverseMatchers;
	
	public HAPContextDefinitionLeafRelative() {
		this.m_matchers = new LinkedHashMap<String, HAPMatchers>();
		this.m_reverseMatchers = new LinkedHashMap<String, HAPMatchers>();
	}
	
	@Override
	public String getType() {		return HAPConstant.CONTEXT_ELEMENTTYPE_RELATIVE;	}

	@Override
	public HAPContextDefinitionElement getSolidContextDefinitionElement() {	return this.m_definition;	}
	
	public HAPContextDefinitionElement getDefinition() {   return this.m_definition;   }
	public void setDefinition(HAPContextDefinitionElement definition) {   this.m_definition = definition.getSolidContextDefinitionElement();   }
	
	public void setPath(HAPContextPath path){	
		this.m_path = path;
		this.m_pathStr = null;
	}
	public void setPath(String path) {  
		this.m_pathStr = path;
		this.m_path = null;
	}
	public void setPath(String categary, String path) {  
		this.m_path = new HAPContextPath(categary, path);
		this.m_pathStr = null;
	}
	public void setPath(String categary, String rootNodeName, String path) {  
		this.m_path = new HAPContextPath(categary, rootNodeName, path);    
		this.m_pathStr = null;
	}

	public HAPContextPath getPath() {
		if(this.m_path==null && HAPBasicUtility.isStringNotEmpty(m_pathStr)) {
			this.m_path = new HAPContextPath(this.m_pathStr);
		}
		return this.m_path;
	}
	public String getPathStr() {
		if(this.m_path!=null && HAPBasicUtility.isStringEmpty(m_pathStr)) {
			this.m_pathStr = this.m_path.getFullPath();
		}
		return this.m_pathStr;
	}
	
	public String getParentCategary() {		return this.getPath().getRootElementId().getCategary();	}
	
	public boolean isRelativeToParent() {	return !HAPBasicUtility.isStringEmpty(this.getPath().getRootElementId().getCategary());	}
	
	public void setMatchers(Map<String, HAPMatchers> matchers){
		this.m_matchers.clear();
		this.m_matchers.putAll(matchers);
		this.m_reverseMatchers.clear();
		for(String name : matchers.keySet()) {
			this.m_reverseMatchers.put(name, HAPMatcherUtility.reversMatchers(matchers.get(name)));
		}
	}

	@Override
	public HAPContextDefinitionElement getChild(String childName) {
		if(this.m_definition!=null) 		return this.m_definition.getChild(childName);
		return null;   
	}
	
	@Override
	public void toContextDefinitionElement(HAPContextDefinitionElement out) {
		super.toContextDefinitionElement(out);
		HAPContextDefinitionLeafRelative that = (HAPContextDefinitionLeafRelative)out;
		if(this.m_path!=null)	that.m_path = this.m_path.clone();
		that.m_pathStr = this.m_pathStr; 
		if(this.m_definition!=null)  that.m_definition = this.m_definition.cloneContextDefinitionElement();
		
		for(String name : this.m_matchers.keySet()) 	that.m_matchers.put(name, this.m_matchers.get(name).cloneMatchers());
		for(String name : this.m_reverseMatchers.keySet())   that.m_reverseMatchers.put(name, this.m_reverseMatchers.get(name).cloneMatchers());
	}
	
	@Override
	public HAPContextDefinitionElement cloneContextDefinitionElement() {
		HAPContextDefinitionLeafRelative out = new HAPContextDefinitionLeafRelative();
		this.toContextDefinitionElement(out);
		return out;
	}

	@Override
	public HAPContextDefinitionElement toSolidContextDefinitionElement(Map<String, Object> constants,
			HAPEnvContextProcessor contextProcessorEnv) {
		HAPContextDefinitionLeafRelative out = (HAPContextDefinitionLeafRelative)this.cloneContextDefinitionElement();
		out.m_pathStr = HAPProcessorContextSolidate.getSolidName(this.getPathStr(), constants, contextProcessorEnv);
		out.m_path = null;
		if(this.m_definition!=null) 	out.m_definition = this.m_definition.toSolidContextDefinitionElement(constants, contextProcessorEnv);
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PATH, this.getPath().toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(ISTOPARENT, this.isRelativeToParent()+"");
		typeJsonMap.put(ISTOPARENT, Boolean.class);
		jsonMap.put(DEFINITION, HAPJsonUtility.buildJson(this.m_definition, HAPSerializationFormat.JSON));
		if(this.m_matchers!=null && !this.m_matchers.isEmpty()){
			jsonMap.put(MATCHERS, HAPJsonUtility.buildJson(this.m_matchers, HAPSerializationFormat.JSON));
			jsonMap.put(REVERSEMATCHERS, HAPJsonUtility.buildJson(this.m_reverseMatchers, HAPSerializationFormat.JSON));
		}
	}

	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj))  return false;

		boolean out = false;
		if(obj instanceof HAPContextDefinitionLeafRelative) {
			HAPContextDefinitionLeafRelative ele = (HAPContextDefinitionLeafRelative)obj;
			if(!HAPBasicUtility.isEquals(this.getPathStr(), ele.getPathStr()))  return false;
			if(!HAPBasicUtility.isEqualMaps(ele.m_matchers, this.m_matchers)) 	return false;
			if(!HAPBasicUtility.isEqualMaps(ele.m_reverseMatchers, this.m_matchers))  return false;
			if(!ele.m_definition.equals(this.m_definition))  return false;
			out = true;
		}
		return out;
	}
	
}
