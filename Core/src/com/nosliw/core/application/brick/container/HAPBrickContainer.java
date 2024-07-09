package com.nosliw.core.application.brick.container;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPUtilityNosliw;
import com.nosliw.core.application.HAPAttributeInBrick;
import com.nosliw.core.application.HAPBrick;

//attribute: id or name of item for attribute name, otherwise, create attribute name
@HAPEntityWithAttribute
public interface HAPBrickContainer extends HAPBrick{

	@HAPAttribute
	static final public String ATTRINDEX = HAPUtilityNosliw.buildNosliwFullName("attrIndex");  

	List<HAPAttributeInBrick> getElements();
	
	
/*	
	@Override
	public void init() {
		this.setAttributeIndex(0);
	}
	
	public List<HAPAttributeInBrick> getElements(){
		List<HAPAttributeInBrick> out = new ArrayList<HAPAttributeInBrick>();

		for(HAPAttributeInBrick attr : this.getAttributes()) {
			if(!ATTRINDEX.equals(attr.getName())) {
				out.add(attr);
			}
		}
		
		return out;
	}
	
	public String addElement(HAPEntityOrReference brickOrRef) {
		String attrName = null;
		String brickOrRefType = brickOrRef.getEntityOrReferenceType();
		if(brickOrRefType.equals(HAPConstantShared.BRICK)) {
			if(brickOrRef instanceof HAPEntityInfo) {
				attrName = ((HAPEntityInfo)brickOrRef).getId();
				this.setAttributeValueWithBrick(attrName, brickOrRef);
			}
			else {
				attrName = this.addElementAnom(brickOrRef);
			}
		}
		else if(brickOrRefType.equals(HAPConstantShared.RESOURCEID)) {
			attrName = this.addElementAnom(brickOrRef);
		}
		return attrName;
	}
	
	private String addElementAnom(HAPEntityOrReference brickOrRef) {
		Integer index = (Integer)this.getAttributeValueOfValue(ATTRINDEX);
		index++;
		this.setAttributeValueWithValue(ATTRINDEX, index);
		String attrName = index+"";
		this.setAttributeValueWithBrick(attrName, brickOrRef);
		return attrName;
	}
	
	private void setAttributeIndex(Integer index) {		this.setAttributeValueWithValue(ATTRINDEX, index);	}
*/

	
//	public void addElement(String attrName, HAPEntityOrReference brickOrRef) {   this.setAttributeValueWithBrick(attrName, brickOrRef);     }
	
	
	
	
//	@Override
//	protected boolean buildAttributeValueFormatJson(String attrName, Object obj) {
//    	if(attrName.equals(ATTRINDEX)) {
//			this.setAttributeIndex((Integer)obj);
//		}
//    	return true;
//    }

}
