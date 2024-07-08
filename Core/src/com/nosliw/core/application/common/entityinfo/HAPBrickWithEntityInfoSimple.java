package com.nosliw.core.application.common.entityinfo;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.division.manual.executable.HAPBrickBlockSimple;

public class HAPBrickWithEntityInfoSimple extends HAPBrickBlockSimple implements HAPEntityInfo{

	@Override
	public String getId() {   return (String)this.getAttributeValueOfValue(ID);    }

	@Override
	public void setId(String id) {   
		this.setAttributeValueWithValue(ID, id);
		if(this.getName()==null) {
			this.setName(id);
		}
	}

	@Override
	public String getName() {    return (String)this.getAttributeValueOfValue(NAME);    }

	@Override
	public void setName(String name) {
		this.setAttributeValueWithValue(NAME, name);
		if(this.getId()==null) {
			this.setId(name);
		}
	}

	@Override
	public String getStatus() {    return (String)this.getAttributeValueOfValue(STATUS);    }

	@Override
	public void setStatus(String status) {   this.setAttributeValueWithValue(STATUS, status);  }

	@Override
	public String getDisplayName() {    return (String)this.getAttributeValueOfValue(DISPLAYNAME);    }

	@Override
	public void setDisplayName(String name) {    this.setAttributeValueWithValue(DISPLAYNAME, name);  }

	@Override
	public String getDescription() {   return (String)this.getAttributeValueOfValue(DESCRIPTION);    }

	@Override
	public void setDescription(String description) {   this.setAttributeValueWithValue(DESCRIPTION, description);  }

	@Override
	public HAPInfo getInfo() {   return (HAPInfo)this.getAttributeValueOfValue(INFO);  }

	@Override
	public void setInfo(HAPInfo info) {   this.setAttributeValueWithValue(INFO, info);   }

	@Override
	public void cloneToEntityInfo(HAPEntityInfo entityInfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buildEntityInfoByJson(Object json) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HAPEntityInfo cloneEntityInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Object buildAttributeValueFormatJson(String attrName, Object obj) {
		Object out = null;
		if(attrName.equals(NAME)) {
			out = obj;
		}
		else if(attrName.equals(ID)) {
			out = obj;
		}
		else if(attrName.equals(DESCRIPTION)) {
			out = obj;
		}
		else if(attrName.equals(STATUS)) {
			out = obj;
		}
		else if(attrName.equals(INFO)) {
			HAPInfoImpSimple info = new HAPInfoImpSimple();
			info.buildObject(obj, HAPSerializationFormat.JSON);
			out = info;
		}
		
		return out;
	}
}
