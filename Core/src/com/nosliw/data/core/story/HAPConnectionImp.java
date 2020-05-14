package com.nosliw.data.core.story;

public class HAPConnectionImp extends HAPStoryElementImp implements HAPConnection{

	private HAPConnectionEnd m_end1;
	
	private HAPConnectionEnd m_end2;
	
	public HAPConnectionImp(String type) {
		super(type);
	}

	public HAPConnectionImp() {}

	@Override
	public HAPConnectionEnd getEnd1() {  return this.m_end1;  }

	@Override
	public HAPConnectionEnd getEnd2() {  return this.m_end2;  }

}
