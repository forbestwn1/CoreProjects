package com.nosliw.core.application.common.structure;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.common.valueport.HAPReferenceElement;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.expression.data.HAPParserDataExpression;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.expression1.HAPUtilityScriptExpression1;
import com.nosliw.data.core.scriptexpression.HAPUtilityScriptExpression;

//a element refer to another element (on another tree or same tree)
//it can be use in define value structure by reference to another element
//or data association between two element
public abstract class HAPElementStructureLeafRelative extends HAPElementStructureLeafVariable{

	@HAPAttribute
	public static final String SOLIDNODEREF = "solidNodeRef";

	@HAPAttribute
	public static final String RESOLVEDINFO = "resolvedInfo";

	//reference definition
	private HAPReferenceElement m_reference;

	//resolved info (referred structure, element path, remain path, element)
	private HAPInfoRelativeResolve m_resolvedInfo;
	
	private HAPInfoPathToSolidRoot m_solidNodeRef;
	
	public HAPElementStructureLeafRelative() {}
	
	public HAPElementStructureLeafRelative(String path) {
		this();
		this.m_reference = new HAPReferenceElement(path);
	}
	
	public HAPReferenceElement getReference() {    return this.m_reference;    }
	public void setReference(HAPReferenceElement path) {   this.m_reference = path;     }

	public HAPInfoRelativeResolve getResolveInfo() {   return this.m_resolvedInfo;     }
	public void setResolvedInfo(HAPInfoRelativeResolve resolvedInfo) {    this.m_resolvedInfo = resolvedInfo;      }
	
	public HAPInfoPathToSolidRoot getSolidNodeReference() {    return this.m_solidNodeRef;    }
	public void setSolidNodeReference(HAPInfoPathToSolidRoot solidNodeRef) {    this.m_solidNodeRef = solidNodeRef;    }

	@Override
	public HAPElementStructure getChild(String childName) {  return this.getSolidStructureElement().getChild(childName);  } 

	@Override
	public void discoverConstantScript(HAPIdEntityInDomain complexEntityId, HAPContextParser parserContext, HAPParserDataExpression expressionParser) {
		String scriptExpressionId = HAPUtilityScriptExpression.discoverConstantScript(this.getReference().getElementPath(), complexEntityId, parserContext, expressionParser);
		if(scriptExpressionId!=null) {
			this.getReference().setElementPath(HAPUtilityScriptExpression.makeIdLiterate(scriptExpressionId));
		}
	}
	
	@Override
	public void solidateConstantScript(Map<String, String> values) {
		String id = HAPUtilityScriptExpression.isIdLterate(this.getReference().getElementPath());
		if(id!=null) {
			this.getReference().setElementPath(values.get(id));
		}
	}

	@Override
	public void toStructureElement(HAPElementStructure out) {
		super.toStructureElement(out);
		HAPElementStructureLeafRelative that = (HAPElementStructureLeafRelative)out;
		that.m_reference = this.m_reference.cloneElementReference(); 
		if(this.m_solidNodeRef!=null)   that.m_solidNodeRef = this.m_solidNodeRef.cloneContextNodeReference();
	}
	
	protected void solidateConstantScript(HAPElementStructureLeafRelative relativeElement, Map<String, Object> constants, HAPRuntimeEnvironment runtimeEnv) {
		HAPElementStructureLeafRelative out = (HAPElementStructureLeafRelative)this.cloneStructureElement();
		out.getReference().setPath(HAPUtilityScriptExpression1.solidateLiterate(this.getReference().getElementPath(), constants, runtimeEnv));
		out.getReference().setParentValueContextName(this.getReference().getParentValueContextName());
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(RESOLVEDINFO, HAPUtilityJson.buildJson(this.m_resolvedInfo, HAPSerializationFormat.JSON));
		jsonMap.put(SOLIDNODEREF, HAPUtilityJson.buildJson(this.m_solidNodeRef, HAPSerializationFormat.JSON));
	}

	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj))  return false;

		boolean out = false;
		if(obj instanceof HAPElementStructureLeafRelative) {
			HAPElementStructureLeafRelative ele = (HAPElementStructureLeafRelative)obj;
			if(!HAPUtilityBasic.isEquals(this.getReference(), ele.getReference()))  return false;
			out = true;
		}
		return out;
	}	
}