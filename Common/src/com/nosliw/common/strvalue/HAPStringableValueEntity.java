package com.nosliw.common.strvalue;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.strvalue.io.HAPStringableEntityImporterJSON;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfo;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoAtomic;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoEntity;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoEntityable;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;

/*
 * container for stringable value
 */
public class HAPStringableValueEntity extends HAPStringableValueComplex{

	@HAPAttribute
	public static String PROPERTIES = "properties";
	
	private Map<String, HAPStringableValue> m_childrens;

	private HAPValueInfoEntityable m_entityValueInfo;
	
	public HAPStringableValueEntity(){
		this.m_childrens = new LinkedHashMap<String, HAPStringableValue>();
	}

	public HAPStringableValueEntity(HAPValueInfoEntityable valueInfo){
		this();
		this.m_entityValueInfo = valueInfo;
	}
	
	public void setValueInfo(HAPValueInfoEntityable valueInfo){  this.m_entityValueInfo = valueInfo;  }
	
	@Override
	public Iterator<HAPStringableValue> iterate(){		return this.m_childrens.values().iterator();	}
	
	@Override
	public String getStringableStructure(){		return HAPConstant.STRINGABLE_VALUESTRUCTURE_ENTITY;	}
	
	public HAPStringableValueList getListChild(String name){
		HAPStringableValueList out = (HAPStringableValueList)this.getChild(name);
		if(out==null){
			out = new HAPStringableValueList();
			out = (HAPStringableValueList)this.updateChild(name, out);
		}
		return out;
	}
	
	public HAPStringableValueMap getMapChild(String name){
		HAPStringableValueMap out = (HAPStringableValueMap)this.getChild(name);
		if(out==null){
			out = new HAPStringableValueMap();
			out = (HAPStringableValueMap)this.updateChild(name, out);
		}
		return out;
	}
	
	@Override
	public HAPStringableValue getChild(String name){  return this.m_childrens.get(name);  }
	
	public HAPStringableValue updateChild(String name, HAPStringableValue childValue){
		if(childValue==null)    this.m_childrens.remove(name);
		else		this.m_childrens.put(name, childValue);
		return childValue;
	}

	public HAPStringableValueComplex updateComplexChild(String name, String type){
		HAPStringableValueComplex out = (HAPStringableValueComplex)this.getChild(name);
		if(out==null){
			out = HAPStringableValueUtility.newStringableValueComplex(type);
			if(out!=null){
				this.updateChild(name, out);
			}
		}
		return out;
	}
	
	public HAPStringableValueAtomic updateAtomicChildStrValue(String name, String strValue){
		String childDataType = null;
		String childSubDataType = null;
		if(this.m_entityValueInfo!=null){
			HAPValueInfo childValueInfo = this.m_entityValueInfo.getPropertyInfo(name);
			if(childValueInfo.getValueInfoType().equals(HAPConstant.STRINGABLE_VALUESTRUCTURE_ATOMIC)){
				HAPValueInfoAtomic childAtomicValueInfo = (HAPValueInfoAtomic)childValueInfo;
				childDataType = childAtomicValueInfo.getDataType();
				childSubDataType = childAtomicValueInfo.getSubDataType();
			}
		}
		return  this.updateAtomicChildStrValue(name, strValue, childDataType, childSubDataType);
	}
	
	public HAPStringableValueAtomic updateAtomicChildStrValue(String name, String strValue, String type, String subType){
		HAPStringableValueAtomic out = null; 
		HAPStringableValue child = this.getChild(name);
		if(child==null || child.getStringableStructure().equals(HAPConstant.STRINGABLE_VALUESTRUCTURE_ATOMIC)){
			out = new HAPStringableValueAtomic(strValue, type, subType);
			this.m_childrens.put(name, out);
		}
		return out;
	}
	
	public void updateAtomicChildStrValue(String name, String strValue, String type){
		this.updateAtomicChildStrValue(name, strValue, type, null);
	}
	
	public HAPStringableValueAtomic updateAtomicChildObjectValue(String name, Object value){
		HAPStringableValueAtomic out = null; 
		HAPStringableValue child = this.getChild(name);
		if(child==null || child.getStringableStructure().equals(HAPConstant.STRINGABLE_VALUESTRUCTURE_ATOMIC)){
			out = HAPStringableValueAtomic.buildFromObject(value); 
			this.m_childrens.put(name, out);
		}
		return out;
	}
	
	public void updateAtomicChildrens(Map<String, String> values){
		for(String name : values.keySet()){
			this.updateAtomicChildStrValue(name, values.get(name));
		}
	}
	
	public Set<String> getProperties(){		return this.m_childrens.keySet();	}

	@Override
	public HAPStringableValueEntity cloneStringableValue(){
		return this.clone(this.getClass());
	}
	
	public <T extends HAPStringableValueEntity> T clone(Class<T> cs){
		T out = null;
		try{
			out = cs.newInstance();
			out.cloneFrom(this);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}
	
	protected void cloneFrom(HAPStringableValueEntity entity){
		for(String name : entity.m_childrens.keySet()){
			this.m_childrens.put(name, entity.m_childrens.get(name).clone());
		}
	}
	
	public HAPStringableValueEntity hardMerge(HAPStringableValueEntity entity){
		HAPStringableValueEntity out = (HAPStringableValueEntity)this.clone();
		for(String attr : entity.m_childrens.keySet()){
			out.m_childrens.put(attr, entity.m_childrens.get(attr).clone());
		}
		return out;
	}
	
	public HAPStringableValueEntity hardMergeWith(HAPStringableValueEntity entity, Set<String> attrs){
		HAPStringableValueEntity out = (HAPStringableValueEntity)this.clone();
		for(String attr : attrs){
			HAPStringableValue value = entity.m_childrens.get(attr);
			if(!HAPStringableValueUtility.isStringableValueEmpty(value)){
				out.m_childrens.put(attr, value.clone());
			}
		}
		return out;
	}

	public HAPStringableValueEntity hardMergeExcept(HAPStringableValueEntity entity, Set<String> attrs){
		HAPStringableValueEntity out = (HAPStringableValueEntity)this.clone();
		for(String attr : entity.m_childrens.keySet()){
			if(!attrs.contains(attr)){
				out.m_childrens.put(attr, entity.m_childrens.get(attr).clone());
			}
		}
		return out;
	}
	
	@Override
	protected boolean buildObjectByFullJson(Object json){	return this.buildByJson((JSONObject)json);	}

	@Override
	protected boolean buildObjectByJson(Object json){		return this.buildByJson((JSONObject)json);	}

	@Override
	protected boolean buildObjectByLiterate(String literateValue){
		boolean out = false;
		try {
			out = buildByJson(new JSONObject(literateValue));
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		return out;
	}

	
	private boolean buildByJson(JSONObject json){
		HAPValueInfoEntity entityValueInfo = HAPValueInfoManager.getInstance().getEntityValueInfoByClass(this.getClass());
		HAPStringableEntityImporterJSON.buildStringableValueEntity(json, this, this.getClass(), HAPValueInfoManager.getInstance());
		return true;
	}
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildFullJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PROPERTIES, HAPJsonUtility.buildJson(this.m_childrens, HAPSerializationFormat.JSON_FULL));
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		for(String child : this.m_childrens.keySet()){
			HAPStringableValue propertyValue = this.m_childrens.get(child);
			switch(propertyValue.getStringableStructure()){
			case HAPConstant.STRINGABLE_VALUESTRUCTURE_ATOMIC:
				jsonMap.put(child, this.m_childrens.get(child).toStringValue(HAPSerializationFormat.JSON));
				typeJsonMap.put(child, String.class);
				break;
			default:
				jsonMap.put(child, this.m_childrens.get(child).toStringValue(HAPSerializationFormat.JSON));
				break;
			}
		}
	}
	
	@Override
	public boolean equals(Object obj){
		boolean out = false;
		if(obj instanceof HAPStringableValueEntity){
			HAPStringableValueEntity value = (HAPStringableValueEntity)obj;
			out = HAPBasicUtility.isEqualMaps(value.m_childrens, this.m_childrens);
		}
		return out;
	}

	public static <T> T buildDefault(Class<T> c) {
		HAPValueInfoEntity valueInfoEntity = HAPValueInfoManager.getInstance().getEntityValueInfoByClass(c);
		T out = (T)valueInfoEntity.newValue();
		return out;
	}
}
