package com.nosliw.core.application.brick.task.script.task;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.parm.HAPParms;
import com.nosliw.common.parm.HAPWithParms;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.common.brick.HAPBrickImp;
import com.nosliw.core.resource.HAPResourceId;

public class HAPBlockTaskTaskScriptImp extends HAPBrickImp implements HAPBlockTaskTaskScript{

	public HAPBlockTaskTaskScriptImp() {
		super(HAPEnumBrickType.TASK_TASK_SCRIPT_100);
		this.setParms(new HAPParms());
	}
	
	@Override
	public HAPEntityOrReference getTaskInterface() {    return this.getAttributeValueOfBrick(TASKINTERFACE);  }
	@Override
	public void setTaskInterface(HAPEntityOrReference taskInterface) {   this.setAttributeValueWithBrick(TASKINTERFACE, taskInterface);     }

	@Override
	public HAPResourceId getScriptResourceId() {   return (HAPResourceId)this.getAttributeValueOfValue(SCRIPTRESOURCEID);  }
	public void setScriptResourceId(HAPResourceId resourceId) {    this.setAttributeValueWithValue(SCRIPTRESOURCEID, resourceId);  }

	@Override
	public HAPParms getParms() {   return (HAPParms)this.getAttributeValueOfValue(HAPWithParms.PARM);  }
	@Override
	public void setParms(HAPParms parms) {   this.setAttributeValueWithValue(HAPWithParms.PARM, parms);  }

}
 