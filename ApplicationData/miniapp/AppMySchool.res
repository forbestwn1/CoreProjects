{
	"name" : "AppMySchool",
	"id" : "AppMySchool",
	"entry": [{
		"name": "main",
		"module": [{
				"status": "disabled111",
				"role": "setting",
				"name": "setting",
				"module": "ModuleMySchoolSetting",
				"info": {},
				"inputMapping": [
					{
						"element": {
							"schoolTypeInModule": {
								"definition": {
									"path": "schoolTypeInData",
									"parent" : "applicationData.setting"
								}
							},
							"schoolRatingInModule": {
								"definition": {
									"path": "schoolRatingInData",
									"parent" : "applicationData.setting"
								}
							}
						}
					}
				],
				"outputMapping": [
					{
						"name" : "persistance",
						"element": {
							"applicationData.setting;schoolTypeInData": {
								"definition": {
									"path": "schoolTypeInModule",
								}
							},
							"applicationData.setting;schoolRatingInData": {
								"definition": {
									"path": "schoolRatingInModule",
								}
							}
						}
					},
					{
						"name" : "syncWithApp",
						"element": {
							"schoolTypeInApp": {
								"definition": {
									"path": "schoolTypeInModule",
								}
							},
							"schoolRatingInApp": {
								"definition": {
									"path": "schoolRatingInModule",
								}
							}
						}
					}
				]
			},
			{
				"status": "disabled111",
				"role": "application",
				"name": "MySchool",
				"module": "ModuleMySchoolResult",
				"inputMapping": [
					{
						"element": {
							"schoolTypeInModule": {
								"definition": {
									"path": "schoolTypeInApp"
								}
							},
							"schoolRatingInModule": {
								"definition": {
									"path": "schoolRatingInApp"
								}
							}
						}
					}
				]
			}
		],
		"process": {
			"submitSetting": {
				"activity": [{
						"id": "startActivityId",
						"name": "startActivity",
						"type": "start",
						"flow": {
							"target": "readSetting"
						}
					},
					{
						"id": "readSetting",
						"type": "UI_executeCommand",
						"partId": "module.setting.outputMapping.syncWithApp",
						"command": "execute",
						"result": [{
							"name": "success",
							"flow": {
								"target": "updateApplicationData"
							}
						}]
					},
					{
						"id": "updateApplicationData",
						"type": "UI_executeCommand",
						"partId": "module.application.inputMapping.syncWithApp",
						"command": "execute",
						"result": [{
							"name": "success",
							"flow": {
								"target": "updateApplication"
							}
						}]
					},
					{
						"id": "updateApplication",
						"name": "updateApplication",
						"type": "UI_executeCommand",
						"partId": "module.application",
						"command": "restart",
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
		}

	}],

	"pageInfo": [{
			"name": "schoolListPage",
			"id": "Page_MySchool_SchoolList"
		},
		{
			"name": "schoolInfoPage",
			"id": "Page_MySchool_SchoolData"
		},
		{
			"name": "schoolSettingPage",
			"id": "Page_MySchool_Query"
		}
	],

	"applicationData" : {
		"setting" : {
			"element": {
				"schoolTypeInData": {
					"definition": {
						"path": "schoolTypeInApp"
					}
				},
				"schoolRatingInData": {
					"definition": {
						"path": "schoolRatingInApp"
					}
				}
			}
		}
	},

	"context" : {
		"group" : {
			"public" : {
				"element" : {
					"schoolTypeInApp": {
						"definition": {
							"criteria": "test.options;1.0.0"
						},
						"defaultValue": {
							"dataTypeId": "test.options;1.0.0",
							"value": {
								"value": "Public",
								"optionsId": "schoolType"
							}
						}
					},
					"schoolRatingInApp": {
						"definition": {
							"criteria": "test.float;1.0.0"
						},
						"defaultValue": {
							"dataTypeId": "test.float;1.0.0",
							"value": 7.0
						}
					},
					"schoolListInApp": {
						"definition": {
							"criteria": "test.array;1.0.0%%||element:test.map;1.0.0%%||geo:test.geo;1.0.0,schoolName:test.string;1.0.0,schoolRating:test.float;1.0.0||%%||%%"
						},
						"defaultValue": {
							"dataTypeId": "test.array;1.0.0",
							"value": [{
									"dataTypeId": "test.map;1.0.0",
									"value": {
										"schoolName": {
											"dataTypeId": "test.string;1.0.0",
											"value": "School1"
										},
										"schoolRating": {
											"dataTypeId": "test.float;1.0.0",
											"value": 6.0
										},
										"geo": {
											"dataTypeId": "test.geo;1.0.0",
											"value": {
												"latitude": 43.651299,
												"longitude": -79.579473
											}
										}
									}
								},
								{
									"dataTypeId": "test.map;1.0.0",
									"value": {
										"schoolName": {
											"dataTypeId": "test.string;1.0.0",
											"value": "School2"
										},
										"schoolRating": {
											"dataTypeId": "test.float;1.0.0",
											"value": 8.5
										},
										"geo": {
											"dataTypeId": "test.geo;1.0.0",
											"value": {
												"latitude": 43.649016,
												"longitude": -79.485059
											}
										}
									}
								}
							]
						}
					}
				}
			}
		}			
	}
}
