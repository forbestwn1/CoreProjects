{
	"name": "ModuleMySchoolSetting",
	"description": "",
	"pageInfo": [
		{
			"name": "schoolSettingPage",
			"id": "Page_MySchool_Query"
		}
	],
	"context": {
		"group": {
			"public": {
				"element": {
					"schoolTypeInModule": {
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
					"schoolRatingInModule": {
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
		}
	},
	ui :[
		{
			"name": "settingUI",
			"type": "setting",
			"page": "schoolSettingPage",
			"inputMapping": {
				"element": {
					"schoolType": {
						"definition": {
							"path": "schoolTypeInModule"
						},
						"info": {
							"relativeConnection": "physical"
						}
					},
					"schoolRating": {
						"definition": {
							"path": "schoolRatingInModule"
						},
						"info": {
							"relativeConnection": "physical"
						}
					}
				}
			},
			"serviceMapping": {},
		}
	]
}
