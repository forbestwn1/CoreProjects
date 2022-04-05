package com.nosliw.data.core.resource;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.data.core.component.HAPPathLocationBase;

public class HAPResourceIdLocal  extends HAPResourceId{

	private static final String SEPERATOR = HAPConstantShared.SEPERATOR_LEVEL3;
	
	@HAPAttribute
	public static String BASEPATH = "basePath";

	@HAPAttribute
	public static String NAME = "name";

	private HAPPathLocationBase m_basePath;
	
	private String m_name;

	public HAPResourceIdLocal(String type) {
		super(type);
	}
	
	@Override
	public String getStructure() {  return HAPConstantShared.RESOURCEID_TYPE_LOCAL;  }

	public HAPPathLocationBase getBasePath() {   return this.m_basePath;   }
	public void setBasePath(HAPPathLocationBase base) {   this.m_basePath = base;   }
	
	public String getName() {   return this.m_name;   }
	public void setName(String name) {    this.m_name = name;    }
	
	@Override
	public String getCoreIdLiterate() {
		return HAPUtilityNamingConversion.cascadeElements(this.m_name, HAPSerializeManager.getInstance().toStringValue(this.m_basePath, HAPSerializationFormat.LITERATE), SEPERATOR);
	}

	@Override
	protected void buildCoreIdByLiterate(String idLiterate) {
		String[] segs = HAPUtilityNamingConversion.splitTextByElements(idLiterate, SEPERATOR);
		this.m_name = segs[0];
		if(segs.length>=2) {
			this.m_basePath = new HAPPathLocationBase(segs[1]);
		}
	}

	@Override
	protected void buildCoreIdJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		if(this.m_basePath!=null)	jsonMap.put(BASEPATH, this.m_basePath.toStringValue(HAPSerializationFormat.LITERATE));
		jsonMap.put(NAME, this.m_name);
	}

	@Override
	protected void buildCoreIdByJSON(JSONObject jsonObj) {
		Object basePathObj = jsonObj.opt(BASEPATH);
		if(basePathObj!=null) {
			this.m_basePath = new HAPPathLocationBase((String)basePathObj);
		}
		this.m_name = jsonObj.optString(NAME);
	}

	@Override
	public int hashCode() {
		return this.toStringValue(HAPSerializationFormat.LITERATE).hashCode();
	}

	@Override
	public HAPResourceId clone() {
		HAPResourceIdLocal out = new HAPResourceIdLocal(this.getResourceType());
		out.cloneFrom(this);
		return out;
	}
	
	@Override
	public boolean equals(Object o){
		boolean out = false;
		if(super.equals(o)) {
			if(o instanceof HAPResourceIdLocal){
				HAPResourceIdLocal resourceId = (HAPResourceIdLocal)o;
				return HAPBasicUtility.isEquals(this.getBasePath(), resourceId.getBasePath())||HAPBasicUtility.isEquals(this.getName(), resourceId.getName());
			}
		}
		return out;
	}

	protected void cloneFrom(HAPResourceIdLocal resourceId){
		super.cloneFrom(resourceId);
		this.m_basePath = resourceId.m_basePath;
		this.m_name = resourceId.m_name;
	}

}
