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
					service : {
						"main" : "query"
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
		}
	},
	
	uiModules : {
		"settingModule" : "School_Setting",
		"applicationModule" : "School_Result"
	},
	
	services : {
		query : {
			type : "dataSource",
			dataSourceId : "school"
		}
	
	}
}
