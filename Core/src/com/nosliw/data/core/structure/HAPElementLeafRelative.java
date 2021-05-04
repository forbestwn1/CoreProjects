package com.nosliw.data.core.structure;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.matcher.HAPMatcherUtility;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPElementLeafRelative extends HAPElementLeafVariable{

	@HAPAttribute
	public static final String PATH = "path";

	@HAPAttribute
	public static final String PARENT = "parent";

	@HAPAttribute
	public static final String PARENTCATEGARY = "parentCategary";

	@HAPAttribute
	public static final String DEFINITION = "definition";

	@HAPAttribute
	public static final String SOLIDNODEREF = "solidNodeRef";

	@HAPAttribute
	public static final String MATCHERS = "matchers";

	@HAPAttribute
	public static final String REVERSEMATCHERS = "reverseMatchers";
	
	//definition of the path (reference + path)
	private String m_referencePath;

	//path after resolve (root id + path)
	private HAPComplexPath m_resolvedPath; 
	private String m_unResolvedRemainPath;
	
	//parent name for referred context, for instance, self, external context
	private String m_parent;
	
	private HAPElement m_definition;
	
	private HAPInfoPathToSolidRoot m_solidNodeRef;
	
	//context node full name --- matchers
	//used to convert data from parent to data within uiTag
	private Map<String, HAPMatchers> m_matchers;

	private Map<String, HAPMatchers> m_reverseMatchers;
	
	public HAPElementLeafRelative() {
		this.m_matchers = new LinkedHashMap<String, HAPMatchers>();
		this.m_reverseMatchers = new LinkedHashMap<String, HAPMatchers>();
	}
	
	public HAPElementLeafRelative(String path) {
		this();
		this.setReferencePath(path);
	}
	
	@Override
	public String getType() {		return HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE;	}

	@Override
	public HAPElement getSolidStructureElement() {	return this.m_definition;	}

	public String getParent() {
		if(HAPBasicUtility.isStringNotEmpty(this.m_parent))   return this.m_parent;
		return HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_DEFAULT;  
	}
	
	public void setParent(String parent) {		this.m_parent = parent;	}
	
	public HAPElement getDefinition() {   return this.m_definition;   }
	public void setDefinition(HAPElement definition) {   this.m_definition = definition.getSolidStructureElement();   }
	
	public HAPInfoPathToSolidRoot getSolidNodeReference() {    return this.m_solidNodeRef;    }
	public void setSolidNodeReference(HAPInfoPathToSolidRoot solidNodeRef) {    this.m_solidNodeRef = solidNodeRef;    }
	
	public String getReferencePath() {   return this.m_referencePath;    }
	public void setReferencePath(String path) {  this.m_referencePath = path;	}

	public HAPComplexPath getResolvedPath() {   return this.m_resolvedPath; }
	public void setResolvedPath(HAPComplexPath resolvedPath) {   this.m_resolvedPath = resolvedPath;   }
	
	public void setMatchers(Map<String, HAPMatchers> matchers){
		this.m_matchers.clear();
		this.m_matchers.putAll(matchers);
		this.m_reverseMatchers.clear();
		for(String name : matchers.keySet()) {
			this.m_reverseMatchers.put(name, HAPMatcherUtility.reversMatchers(matchers.get(name)));
		}
	}

	public void updateReferredRootName(HAPUpdateName nameUpdate) {
		
	}
	
	@Override
	public HAPElement getChild(String childName) {
		if(this.m_definition!=null) 		return this.m_definition.getChild(childName);
		return null;   
	}
	
	@Override
	public void toStructureElement(HAPElement out) {
		super.toStructureElement(out);
		HAPElementLeafRelative that = (HAPElementLeafRelative)out;
		if(this.m_resolvedPath!=null)	that.m_resolvedPath = this.m_resolvedPath.clonePathStructure();
		that.m_referencePath = this.m_referencePath; 
		that.m_parent = this.m_parent; 
		if(this.m_definition!=null)  that.m_definition = this.m_definition.cloneStructureElement();
		
		for(String name : this.m_matchers.keySet()) 	that.m_matchers.put(name, this.m_matchers.get(name).cloneMatchers());
		for(String name : this.m_reverseMatchers.keySet())   that.m_reverseMatchers.put(name, this.m_reverseMatchers.get(name).cloneMatchers());
		
		if(this.m_solidNodeRef!=null)   that.m_solidNodeRef = this.m_solidNodeRef.cloneContextNodeReference();
	}
	
	@Override
	public HAPElement cloneStructureElement() {
		HAPElementLeafRelative out = new HAPElementLeafRelative();
		this.toStructureElement(out);
		return out;
	}

	@Override
	public HAPElement toSolidStructureElement(Map<String, Object> constants,
			HAPRuntimeEnvironment runtimeEnv) {
		HAPElementLeafRelative out = (HAPElementLeafRelative)this.cloneStructureElement();
		out.m_referencePath = HAPProcessorContextSolidate.getSolidName(this.getPathStr(), constants, runtimeEnv);
		out.m_path = null;
		out.m_parent = this.m_parent;
		if(this.m_definition!=null) 	out.m_definition = this.m_definition.toSolidStructureElement(constants, runtimeEnv);
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PATH, this.getPathFormat().toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(PARENT, this.getParent());
		jsonMap.put(DEFINITION, HAPJsonUtility.buildJson(this.m_definition, HAPSerializationFormat.JSON));
		if(this.m_matchers!=null && !this.m_matchers.isEmpty()){
			jsonMap.put(MATCHERS, HAPJsonUtility.buildJson(this.m_matchers, HAPSerializationFormat.JSON));
			jsonMap.put(REVERSEMATCHERS, HAPJsonUtility.buildJson(this.m_reverseMatchers, HAPSerializationFormat.JSON));
		}
		jsonMap.put(SOLIDNODEREF, HAPJsonUtility.buildJson(this.m_solidNodeRef, HAPSerializationFormat.JSON));
	}

	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj))  return false;

		boolean out = false;
		if(obj instanceof HAPElementLeafRelative) {
			HAPElementLeafRelative ele = (HAPElementLeafRelative)obj;
			if(!HAPBasicUtility.isEquals(this.getPathStr(), ele.getPathStr()))  return false;
			if(!HAPBasicUtility.isEqualMaps(ele.m_matchers, this.m_matchers)) 	return false;
			if(!HAPBasicUtility.isEqualMaps(ele.m_reverseMatchers, this.m_matchers))  return false;
			if(!ele.m_definition.equals(this.m_definition))  return false;
			out = true;
		}
		return out;
	}	
}
