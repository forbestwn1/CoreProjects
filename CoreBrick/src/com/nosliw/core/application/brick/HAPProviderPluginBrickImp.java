package com.nosliw.core.application.brick;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPInfoBrickType;
import com.nosliw.core.application.HAPPluginBrick;
import com.nosliw.core.application.HAPProviderPluginBrick;
import com.nosliw.core.application.brick.dataexpression.library.HAPPluginBrickDataExpressionLibrary;
import com.nosliw.core.application.brick.scriptexpression.library.HAPPluginBrickScriptExpressionLibrary;
import com.nosliw.core.application.brick.service.interfacee.HAPPluginBrickServiceInterface;
import com.nosliw.core.application.brick.service.profile.HAPPluginBrickServiceProfile;
import com.nosliw.core.application.brick.wrappertask.HAPPluginBrickTaskWrapper;

@Component
public class HAPProviderPluginBrickImp implements HAPProviderPluginBrick{

	@Override
	public List<HAPPluginBrick> getBrickPlugins() {
		
		List<HAPPluginBrick> out = new ArrayList<HAPPluginBrick>();
		
		out.add(new HAPPluginBrick(HAPEnumBrickType.MODULE_100));

		out.add(new HAPPluginBrick(HAPEnumBrickType.TEST_COMPLEX_1_100));
		out.add(new HAPPluginBrick(HAPEnumBrickType.TEST_COMPLEX_SCRIPT_100));
		out.add(new HAPPluginBrick(HAPEnumBrickType.TEST_COMPLEX_TASK_100, new HAPInfoBrickType(HAPConstantShared.TASK_TYPE_TASK)));

		out.add(new HAPPluginBrickTaskWrapper());
		out.add(new HAPPluginBrick(HAPEnumBrickType.WRAPPERBRICK_100));

		out.add(new HAPPluginBrick(HAPEnumBrickType.TASK_TASK_FLOW_100));
		out.add(new HAPPluginBrick(HAPEnumBrickType.TASK_TASK_ACTIVITYTASK_100));

		out.add(new HAPPluginBrick(HAPEnumBrickType.TASK_TASK_SCRIPT_100, new HAPInfoBrickType(HAPConstantShared.TASK_TYPE_TASK)));
		out.add(new HAPPluginBrick(HAPEnumBrickType.TASK_EXPRESSION_SCRIPT_100, new HAPInfoBrickType(HAPConstantShared.TASK_TYPE_EXPRESSION)));

		out.add(new HAPPluginBrick(HAPEnumBrickType.SERVICEPROVIDER_100));
		out.add(new HAPPluginBrickServiceProfile());
		out.add(new HAPPluginBrickServiceInterface());

		out.add(new HAPPluginBrick(HAPEnumBrickType.INTERACTIVETASKINTERFACE_100));
		out.add(new HAPPluginBrick(HAPEnumBrickType.INTERACTIVEEXPRESSIONINTERFACE_100));

		out.add(new HAPPluginBrick(HAPEnumBrickType.DATAEXPRESSIONSTANDALONE_100, new HAPInfoBrickType(HAPConstantShared.TASK_TYPE_EXPRESSION)));
		out.add(new HAPPluginBrickDataExpressionLibrary());
		out.add(new HAPPluginBrick(HAPEnumBrickType.DATAEXPRESSIONLIBELEMENT_100, new HAPInfoBrickType(HAPConstantShared.TASK_TYPE_EXPRESSION)));

		out.add(new HAPPluginBrick(HAPEnumBrickType.DATAEXPRESSIONGROUP_100));

		out.add(new HAPPluginBrickScriptExpressionLibrary());
		out.add(new HAPPluginBrick(HAPEnumBrickType.SCRIPTEXPRESSIONLIBELEMENT_100, new HAPInfoBrickType(HAPConstantShared.TASK_TYPE_EXPRESSION)));

		out.add(new HAPPluginBrick(HAPEnumBrickType.SCRIPTEXPRESSIONGROUP_100));

		
		out.add(new HAPPluginBrick(HAPEnumBrickType.CONTAINER_100));
		out.add(new HAPPluginBrick(HAPEnumBrickType.CONTAINERLIST_100));
		
		
		out.add(new HAPPluginBrick(HAPEnumBrickType.DATAASSOCIATION_100));
		out.add(new HAPPluginBrick(HAPEnumBrickType.DATAASSOCIATIONFORTASK_100));
		out.add(new HAPPluginBrick(HAPEnumBrickType.DATAASSOCIATIONFOREXPRESSION_100));
		
		out.add(new HAPPluginBrick(HAPEnumBrickType.DECORATIONSCRIPT_100));
		
		out.add(new HAPPluginBrick(HAPEnumBrickType.UICONTENT_100));
		out.add(new HAPPluginBrick(HAPEnumBrickType.UIPAGE_100));
		out.add(new HAPPluginBrick(HAPEnumBrickType.UICUSTOMERTAGDEBUGGER_100));
		
		return out;
	}

}
