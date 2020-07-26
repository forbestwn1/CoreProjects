package com.nosliw.data.core.story.element.node;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.service.interfacee.HAPServiceParm;
import com.nosliw.data.core.story.HAPStoryNodeImp;
import com.nosliw.data.core.story.design.HAPChangeItem;

@HAPEntityWithAttribute
public class HAPStoryNodeServiceInputParm extends HAPStoryNodeImp{

	public final static String STORYNODE_TYPE = HAPConstant.STORYNODE_TYPE_SERVICEINPUTPARM; 

	@HAPAttribute
	public static final String PARMDEFINITION = "parmDefinition";

	private HAPServiceParm m_parmDefinition;
	
	public HAPStoryNodeServiceInputParm(HAPServiceParm parmDefinition) {
		super(STORYNODE_TYPE);
		this.m_parmDefinition = parmDefinition;
	}

	@Override
	public List<HAPChangeItem> patch(String path, Object value) {
		return super.patch(path, value);
	}
}
