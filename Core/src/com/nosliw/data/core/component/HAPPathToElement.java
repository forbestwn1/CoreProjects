package com.nosliw.data.core.component;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

@HAPEntityWithAttribute
public class HAPPathToElement extends HAPSerializableImp{

	@HAPAttribute
	public static String SEGMENT = "segment";

	private List<HAPPathToChildElement> m_pathSegs;
	
	public HAPPathToElement() {
		this.m_pathSegs = new ArrayList<HAPPathToChildElement>();
	}

	public HAPPathToChildElement getLastSegment() {     return this.m_pathSegs.get(this.m_pathSegs.size()-1);    }
	
	public void addPathSegment(HAPPathToChildElement pathSeg) {   this.m_pathSegs.add(pathSeg);    }
	
	public HAPPathToElement clonePathToElement() {
		HAPPathToElement out = new HAPPathToElement();
		for(HAPPathToChildElement pathSeg : this.m_pathSegs) {
			out.addPathSegment(pathSeg.clonePathToChildElement());
		}
		return out;
	}
	
	@Override
	public boolean buildObject(Object value, HAPSerializationFormat format){
		if(value instanceof String) {
			String[] segStrs = HAPUtilityNamingConversion.parsePaths((String)value);
			for(String segStr : segStrs) {
				HAPPathToChildElement seg = new HAPPathToChildElement();
				seg.buildObject(segStr, HAPSerializationFormat.JSON);
				this.m_pathSegs.add(seg);
			}
		}
		else if(value instanceof JSONArray) {
			JSONArray pathArray = (JSONArray)value;
			for(int i=0; i<pathArray.length(); i++) {
				HAPPathToChildElement seg = new HAPPathToChildElement();
				seg.buildObject(pathArray.get(i), HAPSerializationFormat.JSON);
				this.m_pathSegs.add(seg);
			}
		}
		else if(value instanceof JSONObject) {
			Object segsObj = ((JSONObject)value).get(SEGMENT);
			this.buildObject(segsObj, format);
		}
		return true;
	}

}
