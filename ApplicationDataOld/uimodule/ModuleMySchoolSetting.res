{
	"name": "ModuleMySchoolSetting",
	"description": "",
	"info": {
		"setting": "setting"
	},
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
	"lifecycle" : [
		{
			"name" : "nosliw_INIT_ACTIVE",
			"process" : "nosliw_INIT_ACTIVE"
		}
	], 
	ui :[
		{
			"name": "settingUI",
			"type": "setting",
			"page": "schoolSettingPage",
			"info" : {
				"syncout" : "auto1"
			},
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
	],
	"attachment": {
		"uiResource" : [
			{
				"name": "schoolSettingPage",
				"id": "Page_MySchool_Query"
			}
		],
		"process": [
			{
				"name" : "nosliw_INIT_ACTIVE",
				"entity" : {
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
							"partId": "ui.settingUI",
							"command": "nosliw_refresh",
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
				},
			}
		],
	},
}
