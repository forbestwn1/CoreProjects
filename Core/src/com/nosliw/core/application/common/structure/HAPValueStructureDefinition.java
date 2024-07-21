package com.nosliw.core.application.common.structure;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.core.application.valuestructure.HAPRootInValueStructure;
import com.nosliw.core.application.valuestructure.HAPRootStructure;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.scriptexpression.HAPUtilityScriptExpression;

public class HAPValueStructureDefinition {

	public static final String ROOT = "root";

	public static final String INITVALUE = "initValue";
	
	private Map<String, HAPRootInValueStructure> m_roots;
	
	private Object m_initValue;
	
	public HAPValueStructureDefinition() {
		this.m_roots = new LinkedHashMap<String, HAPRootInValueStructure>();
	}

	public void setInitValue(Object initValue) {	this.m_initValue = initValue; 	}
	public Object getInitValue() {    return this.m_initValue;     } 
	
	public HAPRootInValueStructure addRoot(HAPRootInValueStructure root) {
		String name = root.getName();
		this.getRoots().put(name, root);
		return root;
	}

	public Map<String, HAPRootInValueStructure> getRoots(){	return this.m_roots;    }
	
	public Set<String> getRootNames(){   return this.getRoots().keySet();    }
	
	public HAPRootInValueStructure getRootByName(String rootName) {   return this.getRoots().get(rootName);  }
	
	public Set<HAPRootInValueStructure> getAllRoots(){   return new HashSet<HAPRootInValueStructure>(this.getRoots().values());      }


	
	
	
	
	
	public List<HAPRootStructure> resolveRoot(String rootName, boolean createIfNotExist) {
		HAPRootStructure root = this.getRootByName(rootName);
		if(createIfNotExist && root==null) {
			root = this.newRoot(rootName);
		}
		if(root!=null) {
			return Lists.newArrayList(root);
		} else {
			return Lists.newArrayList();
		}
	}

	public HAPRootStructure newRoot(String name) {  
		HAPRootStructure root = new HAPRootStructure();
		root.setName(name);
		return this.addRoot(root);  
	}

	@Override
	public void discoverConstantScript(HAPIdEntityInDomain complexEntityId, HAPContextParser parserContext, HAPParserDataExpression expressionParser) {
		Map<String, HAPRootStructure> roots = this.getRoots();
		
		Map<String, String> mapNameToExpId = new LinkedHashMap<String, String>();
		for(String name : roots.keySet()) {
			String scriptExpressionId = HAPUtilityScriptExpression.discoverConstantScript(name, complexEntityId, parserContext, expressionParser);
			if(scriptExpressionId!=null) {
				mapNameToExpId.put(name, scriptExpressionId);
			}
			roots.get(name).discoverConstantScript(complexEntityId, parserContext, expressionParser);
		}
		
		for(String name : mapNameToExpId.keySet()) {
			roots.put(HAPUtilityScriptExpression.makeIdLiterate(mapNameToExpId.get(name)), roots.remove(name));
		}
	}

	@Override
	public void solidateConstantScript(Map<String, String> values) {
		Map<String, HAPRootStructure> roots = this.getRoots();
		
		Map<String, String> mapNameToValue = new LinkedHashMap<String, String>();
		for(String name : roots.keySet()) {
			roots.get(name).solidateConstantScript(values);
			String id = HAPUtilityScriptExpression.isIdLterate(name);
			if(id!=null) {
				mapNameToValue.put(name, values.get(id));
			}
		}
		
		for(String name : mapNameToValue.keySet()) {
			roots.put(mapNameToValue.get(name), roots.remove(name));
		}
	}

	@Override
	public boolean equals(Object obj) {
		boolean out = false;
		if(obj instanceof HAPManualBrickValueStructure) {
			HAPManualBrickValueStructure context = (HAPManualBrickValueStructure)obj;
			if(context.getRootNames().equals(this.getRootNames())) {
				for(String eleName : this.getRootNames()) {
					out = this.getRootByName(eleName).equals(context.getRootByName(eleName));
					if(!out) {
						break;
					}
				}
				return true;
			}
		}
		return out;
	}
}
