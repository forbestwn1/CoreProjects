package com.nosliw.data.core.story.element.connectiongroup;

import java.util.List;
import java.util.Set;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.data.core.story.HAPConnectionGroup;

public class HAPConnectionGroupSwitch extends HAPEntityInfoImp implements HAPConnectionGroup{

	private Set<String> m_connections;
	
	private String m_endNode;
	
	String m_message;
	
	List<String> m_options;
	
	int m_current;
	
}
