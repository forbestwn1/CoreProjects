package com.nosliw.data.core.component.command;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;

public interface HAPWithCommand {

	@HAPAttribute
	public static String COMMAND = "command";

	List<HAPDefinitionCommand> getCommands();
	
	void addCommand(HAPDefinitionCommand command);
}
