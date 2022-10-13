package com.nosliw.data.core.domain.container;

import java.util.ArrayList;
import java.util.List;

public abstract class HAPContainerEntityList<T extends HAPInfoDefinitionContainerElementList> extends HAPContainerEntityImp<T>{

	private List<T> m_eleArray;
	
	public HAPContainerEntityList() {
		this.m_eleArray = new ArrayList<T>();
	}
	
	@Override
	public void addEntityElement(T eleInfo) {
		super.addEntityElement(eleInfo);
		int index = eleInfo.getIndex();
		if(index==-1) {
			eleInfo.setIndex(this.m_eleArray.size());
			this.m_eleArray.add(eleInfo);
		}
		else {
			this.m_eleArray.add(index, eleInfo);
			for(int i=index+1; i<this.m_eleArray.size(); i++) {
				this.m_eleArray.get(i).setIndex(i);
			}
		}
	}

	@Override
	public List<T> getAllElementsInfo() {  return this.m_eleArray;  }

}
