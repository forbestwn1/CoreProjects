{
	entry : {
		main_mobile : {
			module : [
				{
					role : "setting",
					name : "setting",
					module : "ModuleMySchoolSetting",
					parm : {}
				},
				application : {
					role : "application",
					name : "MySchool",
					module : "ModuleMySchoolResult",
					parm : {},
				}
			],
			parm : {
			
			}
		}
	},
	
	require : {
		data : {
			settingData : {
				type : "setting",
				status : "",
				name : "MySchool_Data_Setting",
				context : {
					definition: {
						a : {
							aa : "test.string;1.0.0",
							bb : "test.array;1.0.0%||element:@||!(test.expression)!.outputCriteria(&(expression)&;;&(parms)&)||@||%",
							cc : {
								element : "test.string;1.0.0"
							}
						}
					},
					default: {
						a : {
							aa : {
								dataTypeId: "test.string;1.0.0",
								value: "This is my world!"
							},
							dd : "HELLO!!!!",
							cc : [
								{
									dataTypeId: "test.string;1.0.0",
									value: "This is my world 1111!"
								},
								{
									dataTypeId: "test.string;1.0.0",
									value: "This is my world 2222!"
								},
							]
						}
					}
				}
			}
		},
		
		service : {
			queryService : {
				type : "dataSource",
				dataSourceId : "school"
			}
		}
	}
}
