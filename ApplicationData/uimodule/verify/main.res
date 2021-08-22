{
	"name": "verify",
	"description": "",
	"info": {
		"setting": "application"
	},
	"valuestructure": 
	 {
		"group": {
			"public": {
				"flat": {
					"aaa_module":{
						"definition" : {
							"criteria": "test.string;1.0.0",
						},
						"defaultValue": {
							"dataTypeId": "test.string;1.0.0",
							"value": "This is my world!"
						}
					},

					"bbb_module":{
						"definition" : {
							"criteria": "test.string;1.0.0",
						},
						"defaultValue": {
							"dataTypeId": "test.string;1.0.0",
							"value": "This is my world!"
						}
					},
				}
			},
			"protect": {
				"flat": {
					"eventDataInModule" : {
						"definition": {
							"criteria": "test.string;1.0.0",
						}
					}
				}
			}
		}
	},
	"ui": [
		{
			"name": "schoolListUI",
			"type": "list",
			"page": "schoolListPage",
			"status" : "disabled111",
			"inputMapping": [
				{
					"name" : "default",
					"mapping": {
						"schoolList": {
							"definition": {
								"path": "schoolListInModule"
							},
							"info": {
								"relativeConnection": "physical"
							}
						},
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
				}
			],
			"outputMapping": [
				{
					"name" : "default",
					"mapping" : {
						"schoolListInModule": {
							"definition": {
								"path": "schoolList"
							},
							"info": {
								"relativeConnection": "physical"
							}
						},
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
				}
			],
			"eventHandler": [
				{
					"eventName" : "selectSchool",
					"dataMapping" : {
						"selectSchoolInfoInModule" : "EVENT.data"
					},
					"handler" : [
						{
							"task" : "showInfo",
						},
					]
				}
			],
		},
		{
			"name": "schoolInfoUI",
			"type": "info",
			"page": "schoolInfoPage",
			"status" : "disabled111",
			"inputMapping": [
				{
					"name" : "default",
					"mapping": {
						"schoolData": {
							"definition": {
								"path": "selectSchoolInfoInModule"
							},
							"defaultValue": {
								"dataTypeId": "test.map;1.0.0",
								"value": {
									"schoolName": {
										"dataTypeId": "test.string;1.0.0",
										"value": "School5"
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
							"info": {
								"relativeConnection": "physical"
							}
						}
					}
				}
			
			]				
		}
	],
	
	"task" : [
			{
				"stepType" : "activity",
				"activityType" : "transitPage",
				"configuration" : {
					"page" : "infoPage"
				}
			},
			{
				"stepType" : "activity",
				"activityType" : "exeDA",
				"configuration" : {
					"path" : ""
				}
			},
		
	
	],
	
	"lifecycle" : [
		{
			"name" : "nosliw_INIT_ACTIVE",
			"task" : "nosliw_INIT_ACTIVE"
		}
	], 
	
	"command" : [
		{
			"name" : "command1",
			"task" : "commandTask1",
			"request" : {
				"eventData1" : {
					"definition" : {
						"path": "aaa"
					}
				}
			},
			"result" : {
				"success" : {
					"output" : {
						"output1" : {
							"definition" : {
								"path": "aaa"
							}
						}
					}
				}
			}
		}
	],
	
	"services":
	[
		{
			"name" : "getSchoolData",
			"interface" : {
				"parm" : [
					{
						"name" : "schoolTypeInService",
						"displayName" : "School Type",
						"description" : "The type of school, public, private, ...",
						"dataInfo" : {
							"criteria": "test.string;1.0.0"
						}
					},
					{
						"name" : "schoolRatingInService",
						"displayName" : "School Rating",
						"description" : "The rating of school, 0--10",
						"dataInfo" : {
							"criteria": "test.float;1.0.0"
						}
					}
				],
				"result" : {
					"success" : {
						"output" : [
							{
								"name" : "outputInService",
								"displayName": "All Schools",
								"dataInfo" : {
									"criteria" : "test.array;1.0.0%%||element:test.map;1.0.0%%||schoolName:test.string;1.0.0,schoolRating:test.float;1.0.0,geo:test.geo;1.0.0||%%||%%"
								}
							}
						]
					}
				}
			},
			"provider" : "getSchoolDataService",
			"dataMapping" :{
				"inputMapping" : {
					"element" : {
						"schoolTypeInService" : {
							"definition" : {
								"path" : "schoolType"
							}
						},
						"schoolRatingInService" : {
							"definition" : {
								"path" : "schoolRating"
							}
						}
					}
				},
				"outputMapping" : {
					"success" : {
						"element" : {
							"schoolList" : {
								"definition" : {
									"path" : "outputInService"
								}
							}
						}
					}
				}
			}
		}
	],
	"lifecycle" : [
		{
			"name" : "nosliw_INIT_ACTIVE",
			"process" : "nosliw_INIT_ACTIVE"
		}
	], 
	"attachment": {
		"uiResource" : [
			{
				"name": "schoolListPage",
				"referenceId": {
					"structure" : "local",
					"id" : "MySchool_SchoolList"
				},
			},
			{
				"name": "schoolInfoPage",
				"referenceId": "Page_MySchool_SchoolData",
			}
		],
		"service" : [
			{
				"name" : "getSchoolDataService",
				"referenceId" : "schoolService"
			}
			
		],
		"process": [
			{
				"name" : "selectSchool",
				"referenceId": {
					"id" : "navigation"
				},
				"dataMapping" : {
					"inputMapping" : {
						"element": {
							"toPageName": {
								"definition": {
									"value" : "schoolInfoUI"
								},
								"info": {
									"relativeConnection": "physical"
								}
							}
						}
					}
				}
			},
			{
				"name" : "nosliw_INIT_ACTIVE",
				"referenceId": {
					"structure" : "local",
					"id" : "init"
				}
			}
		]
	}
}
