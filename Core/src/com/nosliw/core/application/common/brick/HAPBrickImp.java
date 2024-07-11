package com.nosliw.core.application.common.brick;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPAttributeInBrick;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPWrapperValue;
import com.nosliw.core.application.valuecontext.HAPValueContext;

public abstract class HAPBrickImp implements HAPBrick{

	//all attributes
	private List<HAPAttributeInBrick> m_attributes;
	
	private HAPValueContext m_valueContext;
	
	private HAPIdBrickType m_brickTypeId;

	public HAPBrickImp() {
		this.m_valueContext = new HAPValueContext(); 
		this.m_attributes = new ArrayList<HAPAttributeInBrick>();
	}
	
	@Override
	public HAPValueContext getValueContext() {    return this.m_valueContext;    }
	
	@Override
	public String getEntityOrReferenceType() {   return HAPConstantShared.BRICK;   }

	public void setBrickType(HAPIdBrickType brickTypeId) {   this.m_brickTypeId = brickTypeId;     }
	
	@Override
	public HAPIdBrickType getBrickType() {   return this.m_brickTypeId;     }
	
	@Override
	public List<HAPAttributeInBrick> getAttributes(){     return this.m_attributes;	}
	public HAPAttributeInBrick getAttribute(String attrName) {
		for(HAPAttributeInBrick attr : this.m_attributes) {
			if(attrName.equals(attr.getName())) {
				return attr;
			}
		}
		return null;
	}
	public void setAttribute(HAPAttributeInBrick attribute) {
		for(int i=0; i<this.m_attributes.size(); i++) {
			if(this.m_attributes.get(i).getName().equals(attribute.getName())) {
				this.m_attributes.remove(i);
				break;
			}
		}
		this.m_attributes.add(attribute);
	}
	
	private HAPWrapperValue getAttributeValueWrapper(String attributeName) {
		HAPWrapperValue out = null; 
		HAPAttributeInBrick attr = this.getAttribute(attributeName);
		if(attr!=null) {
			out = attr.getValueWrapper();
		}
		return out;
	}
}
