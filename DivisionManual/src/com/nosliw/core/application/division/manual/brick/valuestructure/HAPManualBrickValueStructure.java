package com.nosliw.core.application.division.manual.brick.valuestructure;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.core.application.division.manual.HAPManualBrickBlockSimple;
import com.nosliw.core.application.division.manual.HAPManualEnumBrickType;
import com.nosliw.core.application.valuestructure.HAPRootStructure;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.scriptexpression.HAPUtilityScriptExpression;

public class HAPManualBrickValueStructure extends HAPManualBrickBlockSimple{

	public static final String ROOT = "root";

	public static final String INITVALUE = "initValue";

	public HAPManualBrickValueStructure() {
		super(HAPManualEnumBrickType.VALUESTRUCTURE_100);
		this.setAttributeWithValueValue(ROOT, new LinkedHashMap<String, HAPManualRootInValueStructure>());
	}

	public void setInitValue(Object defaultValue) {	this.setAttributeWithValueValue(INITVALUE, defaultValue); 	}
	public Object getInitValue() {    return this.getAttributeValueWithValue(INITVALUE);     } 
	
	public HAPManualRootInValueStructure addRoot(HAPManualRootInValueStructure root) {
		String name = root.getName();
		this.getRoots().put(name, root);
		return root;
	}

	public Map<String, HAPManualRootInValueStructure> getRoots(){	return (Map<String, HAPManualRootInValueStructure>)this.getAttributeValueWithValue(ROOT);    }
	
	public Set<String> getRootNames(){   return this.getRoots().keySet();    }
	
	public HAPManualRootInValueStructure getRootByName(String rootName) {   return this.getRoots().get(rootName);  }
	
	public Set<HAPManualRootInValueStructure> getAllRoots(){   return new HashSet<HAPManualRootInValueStructure>(this.getRoots().values());      }


	
	
	
	
	
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
