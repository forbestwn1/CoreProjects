{
	"name" : "AppMySchool",
	"id" : "AppMySchool",
	"entry": [{
		"name": "main_mobile",
		"module": [{
				"role": "setting",
				"name": "setting",
				"module": "ModuleMySchoolSetting",
				"info": {},
				"inputMapping": {
					"element": {
						"schoolTypeInModule": {
							"definition": {
								"path": "schoolTypeInData",
								"parent" : "appdata.setting"
							}
						},
						"schoolRatingInModule": {
							"definition": {
								"path": "setting.schoolRatingInData",
								"parent" : "appdata.setting"
							}
						}
					}
				},
				"outputMapping": {
					"element": {
						"schoolTypeInApp": {
							"definition": {
								"path": "schoolTypeInModule"
							}
						},
						"schoolRatingInApp": {
							"definition": {
								"path": "schoolRatingInModule"
							}
						}
					}
				}
			},
			{
				"role": "application",
				"name": "MySchool",
				"module": "ModuleMySchoolResult",
				"inputMapping": {
					"element": {
						"schoolListInModule": {
							"definition": {
								"path": "schoolListInApp"
							}
						},
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
			}
		],
		"process": {
			"submitProcess": {
				"activity": [{
						"id": "startActivityId",
						"name": "startActivity",
						"type": "start",
						"flow": {
							"target": "refreshApplication"
						}
					},
					{
						"id": "refreshApplication",
						"type": "UI_executeModuleCommand",
						"module": "application",
						"command": "refresh",
						"parm": {
							"inputData": {
								"definition": {
									"path": "settingData"
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
							"value": 9.0
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
