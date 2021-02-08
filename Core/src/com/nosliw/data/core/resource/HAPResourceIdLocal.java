package com.nosliw.data.core.resource;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPNamingConversionUtility;
import com.nosliw.data.core.component.HAPLocalReferenceBase;

public class HAPResourceIdLocal  extends HAPResourceId{

	@HAPAttribute
	public static String BASEPATH = "basePath";

	@HAPAttribute
	public static String NAME = "name";

	private HAPLocalReferenceBase m_basePath;
	
	private String m_name;

	public HAPResourceIdLocal(String type) {
		super(type);
	}
	
	@Override
	public String getStructure() {  return HAPConstantShared.RESOURCEID_TYPE_LOCAL;  }

	public HAPLocalReferenceBase getBasePath() {   return this.m_basePath;   }
	public void setBasePath(HAPLocalReferenceBase base) {   this.m_basePath = base;   }
	
	public String getName() {   return this.m_name;   }
	
	@Override
	public String getIdLiterate() {
		return HAPNamingConversionUtility.cascadeElements(this.m_name, HAPSerializeManager.getInstance().toStringValue(this.m_basePath, HAPSerializationFormat.LITERATE), HAPConstantShared.SEPERATOR_LEVEL1);
	}

	@Override
	protected void buildCoreIdByLiterate(String idLiterate) {
		String[] segs = HAPNamingConversionUtility.splitTextByElements(idLiterate, HAPConstantShared.SEPERATOR_LEVEL1);
		this.m_name = segs[0];
		if(segs.length>=2) {
			this.m_basePath = new HAPLocalReferenceBase(segs[1]);
		}
	}

	@Override
	protected void buildCoreJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		if(this.m_basePath!=null)	jsonMap.put(BASEPATH, this.m_basePath.toStringValue(HAPSerializationFormat.LITERATE));
		jsonMap.put(NAME, this.m_name);
	}

	@Override
	protected void buildCoreIdByJSON(JSONObject jsonObj) {
		Object basePathObj = jsonObj.opt(BASEPATH);
		if(basePathObj!=null) {
			this.m_basePath = new HAPLocalReferenceBase((String)basePathObj);
		}
		this.m_name = jsonObj.optString(NAME);
	}

	@Override
	public HAPResourceId clone() {
		HAPResourceIdLocal out = new HAPResourceIdLocal(this.getType());
		out.cloneFrom(this);
		return out;
	}
	
	protected void cloneFrom(HAPResourceIdLocal resourceId){
		super.cloneFrom(resourceId);
		this.m_basePath = resourceId.m_basePath;
		this.m_name = resourceId.m_name;
	}

}
