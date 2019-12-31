{
	"name": "ModuleMySchoolResult",
	"description": "",
	"attachment": {
		"uiResource" : [
			{
				"name": "schoolListPage",
				"id": "Page_MySchool_SchoolList"
			},
			{
				"name": "schoolInfoPage",
				"id": "Page_MySchool_SchoolData"
			}
		],
		"service" : [
			{	
				"name" : "getSchoolDataService",
				"id" : "schoolService"
			}		
		]
	},
	"service": 
	[
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
								"value": "Public",
								"optionsId": "schoolType"
							}
						}
					},
					"schoolRatingInModule": {
						"definition": {
							"criteria": "test.float;1.0.0"
						},
						"defaultValue": {
							"dataTypeId": "test.float;1.0.0",
							"value": 9.5
						}
					},
					"schoolListInModule": {
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
			},
			"protect": {
				"element": {}
			}
		}
	},
	"process": {
		"nosliw_INIT_ACTIVE": {
			"activity": [{
					"id": "startActivityId",
					"name": "startActivity",
					"type": "start",
					"flow": {
						"target": "retrieveSchoolData"
					}
				},
				{
					"id": "retrieveSchoolData",
					"name": "retrieveSchoolData",
					"type": "Service_request",
					"provider": "getSchoolDataService",
					"inputMapping" : {
						"element" : {
							"schoolTypeInService" : {
								"definition" : {
									"path" : "schoolTypeInModule"
								}
							},
							"schoolRatingInService" : {
								"definition" : {
									"path" : "schoolRatingInModule"
								}
							}
						}
					},
					"result": [{
						"name": "success",
						"flow": {
							"target": "presentSchoolListUI"
						},
						"output": {
							"element": {
								"schoolListInModule": {
									"definition": {
										"path": "outputInService"
									}
								}
							}
						}
					}]
				},
				{
					"id": "presentSchoolListUI",
					"name": "presentSchoolListUI",
					"type": "UI_presentUI",
					"setting" : {
						"updateData" : false
					},
					"ui": "schoolListUI",
					"result": [{
						"name": "success",
						"flow": {
							"target": "debug"
						}
					}]
				},
				{
					"id": "debug",
					"name": "debug",
					"type": "debug",
					"result": [{
						"name": "success",
						"flow": {
							"target": "refreshSchoolList"
						}
					}]
				},
				{
					"id": "refreshSchoolList",
					"name": "refreshSchoolList",
					"type": "UI_executeCommand",
					"partId": "ui.schoolListUI",
					"command": "nosliw_update_data",
					"inputMapping": {
						"element": {
							"schoolList": {
								"definition": { 
									"path": "schoolListInModule"
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
	"ui": [
		{
			"name": "schoolListUI",
			"type": "list",
			"page": "schoolListPage",
			"status" : "disabled111",
			"inputMapping": {
				"element": {
					"schoolList": {
						"definition": {
							"path": "schoolListInModule"
						},
						"info": {
							"relativeConnection": "physical"
						}
					}
				}
			},
			"outputMapping": {
				"element": {
					"schoolListInModule": {
						"definition": {
							"path": "schoolList"
						},
						"info": {
							"relativeConnection": "physical"
						}
					}
				}
			},
			"eventHandler": {
				"selectSchool": {
					"process": {
						"activity": [{
								"id": "startActivityId",
								"name": "startActivity",
								"type": "start",
								"flow": {
									"target": "debugId"
								}
							},
							{
								"id": "debugId",
								"name": "debug",
								"type": "debug",
								"result": [{
									"name": "success",
									"flow": {
										"target": "presentSchoolDataUI"
									}
								}]
							},
							{
								"id": "presentSchoolDataUI",
								"name": "presentSchoolDataUI",
								"type": "UI_presentUI",
								"ui": "schoolInfoUI",
								"result": [{
									"name": "success",
									"flow": {
										"target": "refreshSchoolInfo"
									}
								}]
							},
							{
								"id": "refreshSchoolInfo",
								"name": "refreshSchoolInfo",
								"type": "UI_executeCommand",
								"partId": "ui.schoolInfoUI",
								"ui11": "schoolInfoUI",
								"command": "nosliw_update_data",
								"inputMapping": {
									"element": {
										"schoolData": {
											"definition": { 
												"path": "nosliw_EVENT.data"
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
				}
			}
		},
		{
			"name": "schoolInfoUI",
			"type": "info",
			"page": "schoolInfoPage",
			"status" : "disabled111",
			"inputMapping": {
				"element": {
					"schoolData": {
						"definition": {
							"path": "schoolListInModule.element"
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
							"relativeConnection": "logical"
						}
					}
				}
			}
		}
	]
}
