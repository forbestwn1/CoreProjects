package com.nosliw.core.application.division.manual.brick.valuestructure;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualEntitySimple;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.expression.data.HAPParserDataExpression;
import com.nosliw.data.core.domain.entity.valuestructure.HAPRootStructure;
import com.nosliw.data.core.scriptexpression.HAPUtilityScriptExpression;
import com.nosliw.data.core.structure.HAPStructure;

@HAPEntityWithAttribute
public class HAPDefinitionEntityValueStructure extends HAPManualEntitySimple implements HAPStructure{

	@HAPAttribute
	public static final String VALUE = "value";

	private Map<String, HAPRootStructure> m_roots;
	
	public HAPDefinitionEntityValueStructure() {
		super(HAPEnumBrickType.VALUESTRUCTURE_100);
		this.setAttributeValue(VALUE, new LinkedHashMap<String, HAPRootStructure>());
	}

	public HAPRootStructure addRoot(HAPRootStructure root) {
		root = root.cloneRoot();
		String name = root.getName();
		this.getRoots().put(name, root);
		return root;
	}

	public Map<String, HAPRootStructure> getRoots(){	return (Map<String, HAPRootStructure>)this.getAttributeValue(VALUE);    }
	
	@Override
	public HAPRootStructure getRoot(String rootName, boolean createIfNotExist) {
		HAPRootStructure out = null;
		Map<String, HAPRootStructure> roots = this.getRoots();
		out = roots.get(rootName);
		if(createIfNotExist==true&&out==null) {
			out = new HAPRootStructure();
			roots.put(rootName, out);
		}
		return out;
	}

	public Set<String> getRootNames(){   return this.getRoots().keySet();    }
	
	public HAPRootStructure getRootByName(String rootName) {   return this.getRoots().get(rootName);  }
	
	@Override
	public Set<HAPRootStructure> getAllRoots(){   return new HashSet<HAPRootStructure>(this.getRoots().values());      }

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
		if(obj instanceof HAPDefinitionEntityValueStructure) {
			HAPDefinitionEntityValueStructure context = (HAPDefinitionEntityValueStructure)obj;
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
