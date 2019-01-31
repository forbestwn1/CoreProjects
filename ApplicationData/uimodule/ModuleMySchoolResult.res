{
	"name": "ModuleMySchoolResult",
	"description": "",
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
	"context": {
		"group": {
			"public": {
				"element": {
					"schoolListInModule": {
						"definition": {
							"criteria": "test.array;1.0.0%%||element:test.map;1.0.0%%||geo:test.geo;1.0.0,schoolName:test.string;1.0.0,schoolRating:test.float;1.0.0||%%||%%"
						},
						"defaultValue": {
							"dataTypeId": "test.array;1.0.0",
							"value": [
								{
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
	"process1": {
		"init": {
			"activity": [
				{
					"id": "startActivityId",
					"name": "startActivity",
					"type": "start",
					"flow": {
						"target": "presentSchoolListUI"
					}
				},
				{
					"id": "presentSchoolListUI",
					"name": "presentSchoolListUI",
					"type": "UI_presentUI",
					"ui": "schoolListUI",
					"result": [
						{
							"name": "success",
							"flow": {
								"target": "successEndId"
							}
						}
					]
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
			"contextMapping": {
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
			"serviceMapping": {},
			"eventHandler": {
				"selectSchool": {
					"process": {
						"activity": [
							{
								"id": "startActivityId",
								"name": "startActivity",
								"type": "start",
								"flow": {
									"target": "refreshSchoolInfo"
								}
							},
							{
								"id": "presentSchoolDataUI",
								"name": "presentSchoolDataUI",
								"type": "UI_presentUI",
								"ui": "schoolInfoUI",
								"result": [
									{
										"name": "success",
										"flow": {
											"target": "refreshSchoolInfo"
										}
									}
								]
							},
							{
								"id": "refreshSchoolInfo",
								"name": "refreshSchoolInfo",
								"type": "UI_executeUICommand",
								"ui": "schoolInfoUI",
								"command": "refresh",
								"parm": {
									"inputData": {
										"schoolData": {
											"definition": {
												"path": "EVENT.data"
											}
										}
									}
								},
								"input": {
									"element": {
										"parm" : {
											"definition" : {
												"child" : {
													"schoolData": {
														"path" : "EVENT"
													}
												}
											}
										}
									},
									"info": {}
								},
								"result": [
									{
										"name": "success",
										"flow": {
											"target": "successEndId"
										}
									}
								]
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
			"contextMapping": {
				"element": {
					"schoolData": {
						"definition": {
							"path": "schoolListInModule.element"
						},
						"defaultValue1": {
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
