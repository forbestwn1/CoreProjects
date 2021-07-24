{
	"name": "MySchoolResult",
	"description": "",
	"info": {
		"setting": "application"
	},
	"context": {
		"group": {
			"public": {
				"element": {
					"schoolTypeInModule": {
						"definition": {
							"criteria": "test.string;1.0.0"
						},
						"defaultValue": {
							"dataTypeId": "test.string;1.0.0",
							"value": "Public"
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
				"element": {
					"selectSchoolInfoInModule" : {
						"definition": {
							"path": "schoolListInModule.element"
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
	"ui": [
		{
			"name": "schoolListUI",
			"type": "list",
			"page": "schoolListPage",
			"status" : "disabled111",
			"eventHandler": [
				{
					"eventName" : "selectItem",
					"eventDataVariable" : "selectSchoolInfoInModule",
					"steps" : [
						{
							"stepType" : "process",
							"process" : "selectSchool"
						}
					]
				}
			]
		},
		{
			"name": "schoolInfoUI",
			"type": "info",
			"page": "schoolInfoPage",
			"status" : "disabled111"
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
	"attachment": {
		"uiResource" : [
			{
				"name": "schoolListPage",
				"referenceId": {
					"structure" : "local",
					"id" : "MySchool_SchoolList"
				},
				"adapter" : {
					"inputMapping": {
						"element": {
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
					},
					"event" : {
						"fromName" : "selectSchool",
						"toName" : "selectItem",
						"dataMapping" : {
							"selectSchoolInfoInModule" : "EVENT.data"
						}
					}
				}
			},
			{
				"name": "schoolInfoPage",
				"referenceId": "Page_MySchool_SchoolData",
				"inputMapping": {
					"element": {
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
