package com.nosliw.core.application.division.manual.executable;

import com.nosliw.core.application.HAPBundle;

public class HAPReferenceBrickGlobal {

	//which bundle this brick belong
	private HAPBundle m_bundle;
	//export name from this bundle
	private String m_exportName;

	public HAPReferenceBrickGlobal(HAPBundle bundle, String exportName) {
		this.m_bundle = bundle;
		this.m_exportName = exportName;
	}

	public HAPBundle getBundle() {    return this.m_bundle;    }
	public String getExportName() {   return this.m_exportName;   }
}
