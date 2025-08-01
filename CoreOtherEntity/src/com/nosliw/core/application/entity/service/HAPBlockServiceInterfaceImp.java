package com.nosliw.core.application.entity.service;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.displayresource.HAPDisplayResourceNode;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.brick.interactive.interfacee.task.HAPBlockInteractiveInterfaceTask;
import com.nosliw.core.application.brick.service.interfacee.HAPBlockServiceInterface;
import com.nosliw.core.application.common.brick.HAPBrickImpWithEntityInfo;
import com.nosliw.core.application.valueport.HAPContainerValuePorts;

//static information for a service. readable, query for service
//information needed during configuration time
@HAPEntityWithAttribute
public class HAPBlockServiceInterfaceImp extends HAPBrickImpWithEntityInfo implements HAPBlockServiceInterface{

	public static final String CHILD_INTERFACE = "interface";

	@HAPAttribute
	public static String INTERFACE = "interface";

	@HAPAttribute
	public static String TAG = "tag";

	@HAPAttribute
	public static String DISPLAY = "display";

	public HAPBlockServiceInterfaceImp() {
		this.setBrickType(HAPEnumBrickType.SERVICEINTERFACE_100);
	}
	
	@Override
	public HAPBlockInteractiveInterfaceTask getTaskInteractiveInterface() {  return (HAPBlockInteractiveInterfaceTask)this.getAttributeValueOfBrickLocal(INTERFACE);  }
	public void setTaskInteractiveInterface(HAPBlockInteractiveInterfaceTask taskInterface) {    this.setAttributeValueWithBrick(INTERFACE, taskInterface);      }
	
	@Override
	public List<String> getTags(){   return (List<String>)this.getAttributeValueOfValue(TAG);    }
	
	@Override
	public HAPDisplayResourceNode getDisplayResource() {   return (HAPDisplayResourceNode)this.getAttributeValueOfValue(DISPLAY);     }

	@Override
	public HAPContainerValuePorts getInternalValuePorts() {   return null;  }

	@Override
	public HAPContainerValuePorts getExternalValuePorts() {   return null;  }
	
}
