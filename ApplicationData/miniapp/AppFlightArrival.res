{
	"name" : "AppFlightArrival",
	"id" : "AppFlightArrival",
	"attachment": {
		"uiResource" : [
			{
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
		"uiModule" : [
			{	
				"name" : "MySchoolSetting",
				"id" : "ModuleMySchoolSetting"
			},		
			{	
				"name" : "MySchoolResult",
				"id" : "ModuleMySchoolResult"
			}		
		],
		"service" : [
			{	
				"name" : "getSchoolDataService",
				"id" : "schoolService"
			}		
		],
		"eventTask" : [
			{	
				"name" : "getSchoolDataService",
				"id" : "schoolService"
			}		
		],
	},
	"entry": [{
		"name": "main",
		"module": [{
				"status": "disabled111",
				"role": "setting",
				"name": "setting",
				"module": "MySchoolSetting",
				"info": {},
				"inputMapping": [
					{
						"element": {
							"schoolTypeInModule": {
								"definition": {
									"path": "schoolTypeInData",
									"parent" : "applicationData_setting"
								}
							},
							"schoolRatingInModule": {
								"definition": {
									"path": "schoolRatingInData",
									"parent" : "applicationData_setting"
								}
							}
						}
					}
				],
				"outputMapping": [
					{
						"name" : "persistance",
						"element": {
							"applicationData_setting;schoolTypeInData": {
								"definition": {
									"path": "schoolTypeInModule",
								}
							},
							"applicationData_setting;schoolRatingInData": {
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
				],
				"eventHandler" : {
				}
			},
			{
				"status": "disabled111",
				"role": "application",
				"name": "MySchool",
				"module": "MySchoolResult",
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

		}

	}],

	"applicationData" : {
		"setting" : {
			"element": {
				"schoolTypeInData": {
					"definition": {
						"path": "schoolTypeInApp"
					},
					"defaultValue": {
						"dataTypeId": "test.options;1.0.0",
						"value": {
							"value": "Public",
							"optionsId": "schoolType"
						}
					}
				},
				"schoolRatingInData": {
					"definition": {
						"path": "schoolRatingInApp"
					},
					"defaultValue": {
						"dataTypeId": "test.float;1.0.0",
						"value": 9.5
					}
				},
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
							"value": 8.0
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
