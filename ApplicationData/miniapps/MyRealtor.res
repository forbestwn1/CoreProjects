{
	uiEntries : {
		main : {
			setting : {
				module : "settingModule",
				entry : "main"
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
	
	uiModules : {
		"settingModule" : "MyRealtor_Setting",
		"applicationModule" : "MyRealtor_App"
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
	
	services : {
		query : {
			type : "dataSource",
			dataSourceId : "myrealtor"
		}
	
	}
}
