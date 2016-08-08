package com.nosliw.common.configure;

/*
 * configurable implementation 
 * configure from property file as default configure
 * costomerConfigure is customer configure that override default configure
 */
public abstract class HAPConfigurableImp implements HAPConfigurable{

	private HAPConfigureImp m_configure;

	protected HAPConfigurableImp(String propertyFile, HAPConfigureImp customerConfigure){
		this.m_configure = HAPConfigureManager.getInstance().createConfigure();
		this.m_configure.importFromProperty(propertyFile, this.getClass(), false);
		if(customerConfigure!=null) 	this.m_configure.merge(customerConfigure, false, true);	
		this.resolveConfigure();
	}

	protected HAPConfigurableImp(String propertyFile){
		this(propertyFile, null);
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
		this.m_configure.merge(configure, false, true);
		this.resolveConfigure();
	}

	protected void resolveConfigure(){
		this.m_configure.resolve();
	}
}
