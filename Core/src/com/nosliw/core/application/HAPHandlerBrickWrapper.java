package com.nosliw.core.application;

import com.nosliw.common.path.HAPPath;

public abstract class HAPHandlerBrickWrapper extends HAPHandlerDownward{

	private HAPHandlerDownward m_processor;
	
	private boolean m_continueIfNotValidAttribute = false;
	
	public HAPHandlerBrickWrapper(HAPHandlerDownward processor) {
		this(processor, false);
	}

	public HAPHandlerBrickWrapper(HAPHandlerDownward processor, boolean continueIfNotValidAttribute) {
		this.m_processor = processor;
		this.m_continueIfNotValidAttribute = continueIfNotValidAttribute;
	}

	abstract protected boolean isValidAttribute(HAPAttributeInBrick attr);
	
	@Override
	public boolean processBrickNode(HAPWrapperBrickRoot rootBrickWrapper, HAPPath path, Object data) {
		if(this.isRoot(path)) {
			return this.m_processor.processBrickNode(rootBrickWrapper, path, data);
		}
		else {
			HAPAttributeInBrick attr = HAPUtilityBrick.getDescendantAttribute(rootBrickWrapper.getBrick(), path); 
			if(this.isValidAttribute(attr)) {
				return this.m_processor.processBrickNode(rootBrickWrapper, path, data);
			}
			return m_continueIfNotValidAttribute;
		}
	}

	@Override
	public void postProcessBrickNode(HAPWrapperBrickRoot rootBrickWrapper, HAPPath path, Object data) {
		if(this.isRoot(path)) {
			this.m_processor.postProcessBrickNode(rootBrickWrapper, path, data);
		}
		else {
			HAPAttributeInBrick attr = HAPUtilityBrick.getDescendantAttribute(rootBrickWrapper.getBrick(), path);
			if(this.isValidAttribute(attr)) {
				this.m_processor.postProcessBrickNode(rootBrickWrapper, path, data);
			}
		}
	}
}
