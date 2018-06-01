{
	uiEntries : {
		main_mobile : {
			setting : {
				module : "settingModule",
				entry : "main",
				data : [
					"status" : "appSetting",
					"statusComplex" : {
						schoolType : {
							data : "appSetting",
							parm: "schoolType"
						}
					}
				]
			},
			application : {
				module : "applicationModule",
				entry : "main"
			}
		},
		notification : {
			application : {
				module : "applicationModule",
				entry : "main"
			}
		}
	},
	
	uiModuleEntries : {
		setting : {
			module : "settingModule",
			entry : "main"
		},
		application : {
			module : "applicationModule",
			entry : "main"
		}
	},
	
	datas : {
		appSetting : {
			type : "setting",
			status : "",
			name : ""
		},
		crunSetting : {
		
		}
	},
	
	uiModules : {
		"settingModule" : "MyRealtor_Setting",
		"applicationModule" : "MyRealtor_App"
	},
	
	services : {
		query : {
			type : "dataSource",
			dataSourceId : "myrealtor"
		}
	
	}
}
