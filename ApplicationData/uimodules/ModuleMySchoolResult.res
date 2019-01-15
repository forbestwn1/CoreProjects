{
	"name": "",
	"description": "",
	"pageInfo": [
		{
			"name": "schoolListPage",
			"id": "Resource_MySchool_SchoolList"
		},
		{
			"name": "schoolInfoPage",
			"id": "Resource_MySchool_SchoolData"
		}
	],
	"context": {
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
	},
	"process": {
		"init": {
			"activity": [
				{
					"id": "presentSchoolListUI",
					"name": "presentSchoolListUI",
					"type": "UI_presentUI",
					"ui": "schoolListUI"
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
							"mappingType": "physical"
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
								"id": "presentSchoolDataUI",
								"name": "presentSchoolDataUI",
								"type": "UI_presentUI",
								"ui": "schoolInfoUI"
							},
							{
								"id": "refreshSchoolInfo",
								"name": "refreshSchoolInfo",
								"type": "UI_executeUICommand",
								"ui": "schoolInfoUI",
								"command": "refresh",
								"parms": {
									"inputData": {
										"schoolData": {
											"definition": {
												"path": "event.parm.schoolData"
											}
										}
									}
								}
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
						"info": {
							"mappingType": "logical"
						}
					}
				}
			}
		}
	]
}