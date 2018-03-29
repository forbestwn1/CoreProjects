package com.nosliw.common.info;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPBasicUtility;

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
	public void setValue(String name, Object value) {		this.m_values.put(name, value);	}

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
			String value = jsonObj.optString(key);
			this.setValue(key, value);
		}
		return true;
	}
	
	@Override
	protected String buildLiterate(){
		List<String> segs = new ArrayList<String>();
		for(String name : this.m_values.keySet()) {
			segs.add(HAPNamingConversionUtility.cascadeComponents(new String[] {name, HAPSerializeManager.getInstance().toStringValue(this.m_values.get(name), HAPSerializationFormat.LITERATE)}, this.m_seperator2));
		}
		return HAPNamingConversionUtility.cascadeComponents(segs.toArray(new String[0]), this.m_seperator1); 
	}

	@Override
	protected boolean buildObjectByLiterate(String literateValue){
		if(HAPBasicUtility.isStringNotEmpty(literateValue)) {
			String[] segs = HAPNamingConversionUtility.splitTextByComponents(literateValue, m_seperator1);
			for(String seg : segs) {
				String[] eles = HAPNamingConversionUtility.splitTextByComponents(seg, this.m_seperator2);
				this.m_values.put(eles[0], eles[1]);
			}
		}
		return true;  
	}
}
