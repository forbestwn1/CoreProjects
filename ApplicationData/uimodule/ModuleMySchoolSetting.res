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
	"process": {
		"active": {
			"activity": [{
					"id": "startActivityId",
					"name": "startActivity",
					"type": "start",
					"flow": {
						"target": "refreshSchoolSetting"
					}
				},
				{
					"id": "refreshSchoolSetting",
					"name": "refreshSchoolSetting",
					"type": "UI_executeCommand",
					"componentId": "ui.settingUI",
					"command": "refresh",
					"input": {
						"element": {
							"schoolType": {
								"definition": { 
									"path": "schoolTypeInModule"
								}
							},
							"schoolRating": {
								"definition": { 
									"path": "schoolRatingInModule"
								}
							}
						}
					},
					"result": [{
						"name": "success",
						"flow": {
							"target": "successEndId"
						}
					}]
				},
				{
					"id": "successEndId",
					"name": "successEnd",
					"type": "end"
				}
			]
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
			"outputMapping": {
				"element": {
					"schoolTypeInModule": {
						"definition": {
							"path": "schoolType"
						},
						"info": {
							"relativeConnection": "physical"
						}
					},
					"schoolRatingInModule": {
						"definition": {
							"path": "schoolRating"
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
