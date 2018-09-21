{
	entry : {
		main_mobile : {
			module : [
				{
					role : "setting",
					name : "setting",
					module : "ModuleMySchoolSetting",
					configure : {
						settingData : {
							criteria : settingData
						},
						service : {
							name : "queryService",
							parm : {
								schoolScore : criteria.schoolScore,
								schoolType : criteria.schoolType
							},
							out : [
								{
									name : "",
									type : "moduleCommand",
									moduel : "application",
									command : "refresh",
									parm: {
										schools : out.output
									}
								}
							]
						},
						
					},
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
		page : {
			query : "Resource_MySchool_Query",
			schoolList : "Resource_MySchool_SchoolList",
			schoolInfo : "Resource_MySchool_SchoolData"
		},
	
		data : {
			settingData : {
				type : "setting",
				status : "",
				definition: {
					a : {
						aa : "test.string;1.0.0",
						bb : "test.array;1.0.0%||element:@||!(test.expression)!.outputCriteria(&(expression)&;;&(parms)&)||@||%",
						cc : {
							element : "test.string;1.0.0"
						}
					}
				},
			},
			
			result : {
				type : "variable"
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
