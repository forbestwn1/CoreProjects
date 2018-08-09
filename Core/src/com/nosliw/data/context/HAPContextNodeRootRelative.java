package com.nosliw.data.context;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.expression.HAPMatcherUtility;
import com.nosliw.data.core.expression.HAPMatchers;

/**
 * Context element that based on context element on parent
 * When tag has its own variable definition which is different from definition from parent, 
 * we should treat those two variables as different variables 
 * And matcher is needed to do convert between these two variables  
 */
public class HAPContextNodeRootRelative extends HAPContextNodeRoot{

	@HAPAttribute
	public static final String PATH = "path";

	@HAPAttribute
	public static final String PARENTCATEGARY = "parentCategary";
	
	@HAPAttribute
	public static final String MATCHERS = "matchers";

	@HAPAttribute
	public static final String REVERSEMATCHERS = "reverseMatchers";
	
	//relative path from parent context
	private HAPContextPath m_path;
	private String m_pathStr;
	
	private String m_parentCategary;
	
	//context node full name --- matchers
	//used to convert data from parent to data within uiTag
	private Map<String, HAPMatchers> m_matchers;

	private Map<String, HAPMatchers> m_reverseMatchers;
	
	public HAPContextNodeRootRelative() {
		this.m_matchers = new LinkedHashMap<String, HAPMatchers>();
		this.m_reverseMatchers = new LinkedHashMap<String, HAPMatchers>();
	}
	
	@Override
	public String getType() {		return HAPConstant.UIRESOURCE_ROOTTYPE_RELATIVE;	}

	public void setPath(HAPContextPath path){	this.m_path = path;	}
	public void setPath(String path) {  this.m_pathStr = path;   }

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
	
	public String getParentCategary() {  return this.m_parentCategary;   }
	public void setParentCategary(String parentCategary) {  this.m_parentCategary = parentCategary;   }
	
	public void setMatchers(Map<String, HAPMatchers> matchers){
		this.m_matchers.clear();
		this.m_matchers.putAll(matchers);
		this.m_reverseMatchers.clear();
		for(String name : matchers.keySet()) {
			this.m_reverseMatchers.put(name, HAPMatcherUtility.reversMatchers(matchers.get(name)));
		}
	}

	@Override
	public HAPContextNodeRoot toSolidContextNode(Map<String, Object> constants, HAPEnvContextProcessor contextProcessorEnv) {
		HAPContextNodeRootRelative out = new HAPContextNodeRootRelative();
		this.toSolidContextNode(out, constants, contextProcessorEnv);
		out.m_pathStr = HAPContextUtility.getSolidName(this.m_pathStr, constants, contextProcessorEnv);
		out.m_parentCategary = this.m_parentCategary;
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PATH, this.getPath().toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(PARENTCATEGARY, this.m_parentCategary);
		if(this.m_matchers!=null && !this.m_matchers.isEmpty()){
			jsonMap.put(MATCHERS, HAPJsonUtility.buildJson(this.m_matchers, HAPSerializationFormat.JSON));
			jsonMap.put(REVERSEMATCHERS, HAPJsonUtility.buildJson(this.m_reverseMatchers, HAPSerializationFormat.JSON));
		}
	}
}
