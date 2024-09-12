package com.nosliw.data.core.story.change;

import java.util.List;

public interface HAPHandlerChange {

	void onChanges(List<HAPChangeItem> changes);
	
}
