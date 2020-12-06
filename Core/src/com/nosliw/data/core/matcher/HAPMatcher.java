package com.nosliw.data.core.matcher;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.data.core.data.HAPDataTypeId;
import com.nosliw.data.core.data.HAPRelationship;
import com.nosliw.data.core.data.HAPRelationshipImp;
import com.nosliw.data.core.runtime.HAPExecutableImp;

/**
 * Store matcher information (match from one data type to another data type)
 * 		source data type id
 * 		relationship
 * 		sub matchers by name
 */
@HAPEntityWithAttribute
public class HAPMatcher extends HAPExecutableImp{

	@HAPAttribute
	public static String REVERSE = "reverse";
	
	@HAPAttribute
	public static String DATATYPEID = "dataTypeId";

	@HAPAttribute
	public static String RELATIONSHIP = "relationship";
	
	@HAPAttribute
	public static String SUBMATCHERS = "subMatchers";
	
	private boolean m_reverse = false;
	
	private HAPDataTypeId m_dataTypeId;
	
	private HAPRelationship m_relationship;
	
	private Map<String, HAPMatchers> m_subMatchers = new LinkedHashMap<String, HAPMatchers>();
	
	public HAPMatcher() {}
	
	public HAPMatcher(HAPDataTypeId dataTypeId, HAPRelationship relationship){
		this.m_dataTypeId = dataTypeId;
		this.m_relationship = relationship;
	}

	public HAPMatcher(HAPDataTypeId dataTypeId, HAPRelationship relationship, boolean reverse){
		this(dataTypeId, relationship);
		this.m_reverse = reverse;
	}
	
	public double getScore() {
		if(this.isVoid())   return 1;
		else {
			int maxSize = 10;
			int pathSize = this.m_relationship.getPath().getSegments().size();
			if(pathSize>=maxSize)  pathSize = maxSize-1;
			return (maxSize-pathSize)/maxSize;
		}
	}
	
	public boolean isVoid(){
		return this.m_relationship.getSource().equals(this.m_relationship.getTarget());
	}
	
	public HAPDataTypeId getDataTypeId(){   return this.m_dataTypeId;  }
	
	public HAPRelationship getRelationship(){  return this.m_relationship;  }
	
	public Map<String, HAPMatchers> getSubMatchers(){  return this.m_subMatchers;    }
	
	public void removeAllSubMatcher(){  this.m_subMatchers.clear();  }
	
	public Set<HAPRelationship> discoverRelationships(){
		Set<HAPRelationship> out = new HashSet<HAPRelationship>();
		out.add(this.m_relationship);
		for(String name : this.m_subMatchers.keySet()){
			out.addAll(this.m_subMatchers.get(name).discoverRelationships());
		}
		return out;
	}
	
	public void addSubMatchers(String name, HAPMatchers matcher){
		this.m_subMatchers.put(name, matcher);
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		
		this.m_dataTypeId = new HAPDataTypeId();
		this.m_dataTypeId.buildObject(jsonObj.getString(DATATYPEID), HAPSerializationFormat.LITERATE);
		
		this.m_relationship = new HAPRelationshipImp();
		this.m_relationship.buildObject(jsonObj.getJSONObject(RELATIONSHIP), HAPSerializationFormat.JSON);
		
		this.m_reverse = jsonObj.getBoolean(REVERSE);
		
		JSONObject subMatchersJsonObj = jsonObj.getJSONObject(SUBMATCHERS);
		for(Object key : subMatchersJsonObj.keySet()) {
			HAPMatchers matchers = new HAPMatchers();
			matchers.buildObject(subMatchersJsonObj.getJSONObject((String)key), HAPSerializationFormat.JSON);
			this.m_subMatchers.put((String)key, matchers);
		}
		
		return true;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(DATATYPEID, HAPSerializeManager.getInstance().toStringValue(this.m_dataTypeId, HAPSerializationFormat.LITERATE));
		jsonMap.put(RELATIONSHIP, HAPSerializeManager.getInstance().toStringValue(this.m_relationship, HAPSerializationFormat.JSON));
		jsonMap.put(SUBMATCHERS, HAPSerializeManager.getInstance().toStringValue(this.m_subMatchers, HAPSerializationFormat.JSON));
		jsonMap.put(REVERSE, this.m_reverse+"");
		typeJsonMap.put(REVERSE, Boolean.class);
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean out = false;
		if(obj instanceof HAPMatcher) {
			HAPMatcher matcher  = (HAPMatcher)obj;
			if(!this.m_reverse!=matcher.m_reverse)  return false;
			if(!HAPBasicUtility.isEquals(this.m_dataTypeId, matcher.m_dataTypeId))  return false;
			if(!HAPBasicUtility.isEquals(this.m_relationship, matcher.m_relationship))  return false;
			if(!HAPBasicUtility.isEqualMaps(this.m_subMatchers, matcher.m_subMatchers))    return false;
		}
		return out;
	}
}
