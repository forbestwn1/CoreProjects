package com.nosliw.core.application.division.story.brick;

import java.util.List;
import java.util.Map;

public class HAPStoryElement {

	private Map<String, HAPStoryContainerElement> m_children;
	
	private List<HAPStoryContainerVariable> m_variables;
	
	private HAPStoryContainerCommand m_commands;
	
	private HAPStoryContainerEvent m_events;
	
}
