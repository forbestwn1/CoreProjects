package com.nosliw.common.info;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

public class HAPInfoImpSimple extends HAPSerializableImp implements HAPInfo{

	private String m_seperator1 = ";";
	private String m_seperator2 = ":";
	
	private Map<String, Object> m_values;
	
	public HAPInfoImpSimple(String seperator1, String seperator2) {
		this();
		this.m_seperator1 = seperator1;
		this.m_seperator2 = seperator2;
	}

	public HAPInfoImpSimple() {	
		m_values = new LinkedHashMap<String, Object>();
	}
	
	@Override
	public Object getValue(String name) {		return this.m_values.get(name);	}

	@Override
	public Object getValue(String name, Object defaultValue) {
		Object out = this.getValue(name);
		return out==null?defaultValue:out;
	}

	@Override
	public Object setValue(String name, Object value) {
		Object out = this.m_values.get(name);
		if(value!=null)		this.m_values.put(name, value);
		else this.m_values.remove(name);
		return out;
	}

	@Override
	public Set<String> getNames() {   return this.m_values.keySet();  }

	@Override
	protected String buildFullJson(){	return HAPSerializeManager.getInstance().toStringValue(m_values, HAPSerializationFormat.JSON_FULL);	}

	@Override
	protected String buildJson(){	return HAPSerializeManager.getInstance().toStringValue(m_values, HAPSerializationFormat.JSON);	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		Iterator<String> it = jsonObj.keys();
		while(it.hasNext()){
			String key = it.next();
			Object value = jsonObj.opt(key);
			this.setValue(key, value);
		}
		return true;
	}
	
	@Override
	public HAPInfoImpSimple cloneInfo() {
		HAPInfoImpSimple out = new HAPInfoImpSimple();
		out.m_values.putAll(this.m_values);
		return out;
	}

	@Override
	public HAPInfoImpSimple cloneInfo(Set<String> excluded) {
		if(excluded==null||excluded.isEmpty())		 return this.cloneInfo();
		else {
			HAPInfoImpSimple out = new HAPInfoImpSimple();
			for(String name : this.getNames()) {
				if(!excluded.contains(name)) {
					out.setValue(name, this.getValue(name));
				}
			}
			return out;
		}
	}
	
	@Override
	protected String buildLiterate(){
		List<String> segs = new ArrayList<String>();
		for(String name : this.m_values.keySet()) {
			segs.add(HAPUtilityNamingConversion.cascadeComponents(new String[] {name, HAPSerializeManager.getInstance().toStringValue(this.m_values.get(name), HAPSerializationFormat.LITERATE)}, this.m_seperator2));
		}
		return HAPUtilityNamingConversion.cascadeComponents(segs.toArray(new String[0]), this.m_seperator1); 
	}

	@Override
	protected boolean buildObjectByLiterate(String literateValue){
		if(HAPBasicUtility.isStringNotEmpty(literateValue)) {
			String[] segs = HAPUtilityNamingConversion.splitTextByComponents(literateValue, m_seperator1);
			for(String seg : segs) {
				String[] eles = HAPUtilityNamingConversion.splitTextByComponents(seg, this.m_seperator2);
				this.m_values.put(eles[0], eles[1]);
			}
		}
		return true;  
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof HAPInfo) {
			HAPInfo info = (HAPInfo)obj;
			if(!HAPBasicUtility.isEqualSets(info.getNames(), this.getNames()))  return false;
			for(String name : info.getNames()) {
				if(!HAPBasicUtility.isEquals(info.getValue(name), this.getValue(name)))  return false;
			}
			return true;
		}
		return false;
	}

}
