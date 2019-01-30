{
	"entry" : [
		 {
			"name" : "main_mobile",
			"module" : [
				{
					"role" : "setting",
					"name" : "setting",
					"module" : "ModuleMySchoolSetting",
					"contextMapping" : {},
					"info" : {}
					configure : {
						settingData : {
							criteria : settingData
						},
						submitProcess :{
							activity : [
								{
									"name" : "moduleRefresh"
								}
							]
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
				{
					"role" : "application",
					"name" : "MySchool",
					"module" : "ModuleMySchoolResult",
					"parm" : {},
				}
			]
		}
	],
	
	"pageInfo": [
		{
			"name": "schoolListPage",
			"id": "Page_MySchool_SchoolList"
		},
		{
			"name": "schoolInfoPage",
			"id": "Page_MySchool_SchoolData"
		}
	],
	
	"dataDefinition" : {
		"settingData" : {
			"element": {
				"schoolTypeData": {
					"definition": {
						"criteria": "test.options;1.0.0"
					},
					"defaultValue": {
						"dataTypeId": "test.options;1.0.0",
						"value": {
							"value" : "Public",
							"optionsId" : "schoolType"
						}
					}
				},
				"schoolRatingData": {
					"definition": {
						"criteria": "test.float;1.0.0"
					},
					"defaultValue": {
						"dataTypeId": "test.float;1.0.0",
						"value": 9.0
					}
				}
			}
		}
	
	},

	"serviceMapping" : {
		
	
	}
}
