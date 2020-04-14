package com.nosliw.data.core.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPDefinitionExpression extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String EXPRESSION = "expression";

	@HAPAttribute
	public static String REFERENCEMAPPING = "referenceMapping";

	private String m_expression;
	
	private Map<String, HAPDefinitionReference> m_references;

	public HAPDefinitionExpression() {
		this.m_references = new LinkedHashMap<String, HAPDefinitionReference>();
	}

	public HAPDefinitionExpression(String expression) {
		this();
		this.m_expression = expression;
	}

	public String getExpression() {   return this.m_expression;    }
	
	public void addReference(HAPDefinitionReference reference) {  this.m_references.put(reference.getName(), reference);   }
	public Map<String, HAPDefinitionReference> getReference(){   return this.m_references;    }
	public HAPDefinitionReference getReference(String name) {   return this.m_references.get(name);    }
	
	public HAPDefinitionExpression cloneDefinitionExpression() {
		HAPDefinitionExpression out = new HAPDefinitionExpression(this.m_expression);
		this.cloneToEntityInfo(out);
		for(String name : this.m_references.keySet()) {
			out.m_references.put(name, this.m_references.get(name).cloneReferenceDefinition());
		}
		return out;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		this.m_expression = jsonObj.getString(HAPDefinitionExpression.EXPRESSION);
		
		JSONArray refJsonArray = jsonObj.optJSONArray(HAPDefinitionExpression.REFERENCEMAPPING);
		if(refJsonArray!=null) {
			for(int i=0; i<refJsonArray.length(); i++) {
				JSONObject refJsonObj = refJsonArray.getJSONObject(i);
				HAPDefinitionReference ref = new HAPDefinitionReference();
				ref.buildObject(refJsonObj, HAPSerializationFormat.JSON);
				this.addReference(ref);
			}
		}
		return true;  
	}
}
