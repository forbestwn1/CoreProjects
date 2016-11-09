package com.nosliw.common.configure;

/*
 * configurable implementation 
 * configure from property file as default configure
 * costomerConfigure is customer configure that override default configure
 */
public abstract class HAPConfigurableImp implements HAPConfigurable{

	private HAPConfigureImp m_configure;

	protected HAPConfigurableImp(){
	}

	protected void setConfiguration(HAPConfigureImp configure){
		this.m_configure = configure;
		this.resolveConfigure();
	}
	
	@Override
	public HAPConfigureValue getConfigureValue(String attr) {
		return this.m_configure.getConfigureValue(attr);
	}

	@Override
	public HAPConfigureImp getConfiguration() {
		return this.m_configure;
	}

	/*
	 * use configure to override current configure
	 */
	protected void applyConfiguration(HAPConfigureImp configure){
		HAPConfigureUtility.merge(this.m_configure, configure);
		this.resolveConfigure();
	}

	protected void resolveConfigure(){
		this.m_configure.resolve();
	}
}
