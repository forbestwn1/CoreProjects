{
	"name": "test1",
	"description": "test1",
	"valueStructure": {
		"group" : {
			"public" : {
				"flat": {
					"testVar1" : {
						"definition": {
							"child" : {
								"var1" : {
									"child" : {
										"var11" : {"criteria":"test.string;1.0.0"}
									}
								}
							}
						},
						"defaultValue": {
							"var1" : {
								"var11" : {
									"dataTypeId": "test.string;1.0.0",
									"value": "9876543210"
								}
							}
						}
					},
					"testVar2" : {
						"definition": {
							"child" : {
								"var1" : {
									"child" : {
										"var11" : {"criteria":"test.string;1.0.0"}
									}
								}
							}
						},
						"defaultValue": {
							"var1" : {
								"var11" : {
									"dataTypeId": "test.string;1.0.0",
									"value": "9876543210"
								}
							}
						}
					},
					"testVar3": {
						"definition":{
							"criteria": "test.string"
						},
						"defaultValue": {
							"dataTypeId": "test.string",
							"value": "9876543210"
						}
					},
					"testVar4" : {
						"definition": {
							"criteria" : "test.integer"
						},
						"defaultValue": {
							"dataTypeId": "test.integer;1.0.0",
							"value": 5
						}
					},
					"testVar5" : {
						"definition": {
							"criteria": "test.integer"
						},
						"defaultValue": {
							"dataTypeId": "test.integer;1.0.0",
							"value": 7
						}
					},
					"constantFromContext1": {
						"definition" : {
							"value" : {
								"dataTypeId": "test.integer",
								"value": 1
							}
						}
					},
					"constantFromContext2": {
						"definition" : {
							"value" : {
								"dataTypeId": "test.integer",
								"value": 3
							}
						}
					},
					"constantFromContext3": {
						"definition" : {
							"value" : {
								"dataTypeId": "test.integer",
								"value": 2
							}
						}
					},
					"constantFromContext4": {
						"definition" : {
							"value" : {
								"dataTypeId": "test.integer",
								"value": 7
							}
						}
					}
				}
			}
		}
	},
	"element": [{
			"id": "test10",
			"name": "test10",
			"description": "standard",
			"element" : [
				{
					"expression" : "!(test.string)!.subString(?(testVar3___public)?,from:?(testVar4)?,to:?(testVar5)?)"
				}
			]
		}
	],
	"attachment": {
		"value" : [
			{
				"name": "constantFromAtt1",
				"entity": {
					"value" : {
						"dataTypeId": "test.integer;1.0.0",
						"value": 1
					}
				}
			},
			{
				"name": "constantFromAtt2",
				"entity": {
					"value" : {
						"dataTypeId": "test.integer;1.0.0",
						"value": 3
					}
				}
			},
			{
				"name": "constantFromAtt3",
				"entity": {
					"value" : {
						"dataTypeId": "test.integer;1.0.0",
						"value": 2
					}
				}
			},
			{
				"name": "constantFromAtt4",
				"entity": {
					"value" : {
						"dataTypeId": "test.integer;1.0.0",
						"value": 7
					}
				}
			}
			
		],
		"testData" : [
			{
				"name": "testData1",
				"entity": {
					"testVar1.var1.var11": {
						"dataTypeId": "test.string;1.0.0",
						"value": "0123456789"
					},
					"testVar2": {
						"var1" : {
							"var11":{
								"dataTypeId": "test.string;1.0.0",
								"value": "0123456789"
							}
						}
					},
					"testVar3": {
						"dataTypeId": "test.string;1.0.0",
						"value": "0123456789"
					},
					"testVar4": {
						"dataTypeId": "test.integer;1.0.0",
						"value": 1
					},
					"testVar5": {
						"dataTypeId": "test.integer;1.0.0",
						"value": 3
					}
				}
			}
		]
	}
}
