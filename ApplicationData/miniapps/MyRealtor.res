{
	uiEntries : {
		main_mobile : {
			uiModuleEntries:{
				setting : {
					module : "settingModule",
					entry : "main",
					data : {
						"main" : "appSetting",
					},
					data1 : {
						"status" : "appSetting",
						"statusComplex" : {
							schoolType : {
								data : "appSetting",
								parm: "schoolType"
							}
						}
					}
				},
				application : {
					module : "applicationModule",
					entry : "main"
				}
			}
		},
		notification : {
			uiModuleEntries:{
				application : {
					module : "applicationModule",
					entry : "main"
				}
			}
		}
	},
	
	data : {
		appSetting : {
			type : "setting",
			status : "",
			name : ""
		},
		crunSetting : {
			type : "setting",
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
