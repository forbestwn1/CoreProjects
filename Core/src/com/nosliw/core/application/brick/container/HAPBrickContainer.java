package com.nosliw.core.application.brick.container;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.utils.HAPUtilityNosliw;
import com.nosliw.core.application.HAPBrickImp;

public class HAPBrickContainer extends HAPBrickImp{

	@HAPAttribute
	static final public String ATTRINDEX = HAPUtilityNosliw.buildNosliwFullName("attrIndex");  

	@Override
	public void init() {
		this.setAttributeIndex(0);
	}
	
	private void setAttributeIndex(Integer index) {		this.setAttributeValueWithValue(ATTRINDEX, index);	}
	
	public void addElement(String attrName, HAPEntityOrReference brickOrRef) {   this.setAttributeValueWithBrick(attrName, brickOrRef);     }
	public String addElement(HAPEntityOrReference brickOrRef) {
		Integer index = (Integer)this.getAttributeValueOfValue(ATTRINDEX);
		index++;
		this.setAttributeValueWithValue(ATTRINDEX, index);
		String attrName = index+"";
		this.setAttributeValueWithBrick(attrName, brickOrRef);
		return attrName;
	}
	
    @Override
	protected boolean buildAttributeValueFormatJson(String attrName, Object obj) {
    	if(attrName.equals(ATTRINDEX)) {
			this.setAttributeIndex((Integer)obj);
		}
    	return true;
    }

}
