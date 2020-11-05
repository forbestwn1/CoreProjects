package com.nosliw.uiresource.page.execute;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

@HAPEntityWithAttribute
public class HAPExecutableStyle extends HAPSerializableImp{

	private String m_id;
	private String m_definition;
	private List<HAPExecutableStyle> m_children;

	public HAPExecutableStyle(String id) {
		this.m_children = new ArrayList<HAPExecutableStyle>();
		this.m_id = id;
	}
	
	public String getId() {   return this.m_id;    }
	
	public String getDefinition() {   return this.m_definition;   }
	public void setDefinition(String def) {    this.m_definition = def;     }

	public void addChild(HAPExecutableStyle child) {   this.m_children.add(child);   }
	
}
