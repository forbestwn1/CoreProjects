{
	"name": "all",
	"description": "process with different activity",
	"valueStructure": {
		"group": {
			"public": {
				"flat": {
					"business" : {
						definition: {
							child : {
								a : {
									child : {
										aa : {criteria:"test.string;1.0.0"},
									}
								}
							}
						},
						defaultValue: {
							a : {
								aa : {
									dataTypeId: "test.string;1.0.0",
									value: "This is my world!"
								}
							}
						}
					},
					"baseVar": {
						"definition":{
							criteria: "test.string",
						},
						"defaultValue": {
							"dataTypeId": "test.string",
							"value": "This is my world!"
						}
					},
					"fromVar" : {
						"definition": {
							criteria : "test.integer"
						},
						"defaultValue": {
							"dataTypeId": "test.integer",
							"value": 3
						}
					},
					"toVar" : {
						"definition": {
							criteria: "test.integer"
						} ,
						"defaultValue": {
							"dataTypeId": "test.integer",
							"value": 7
						}
					}
				}
			},
			"protected": {
				"flat": {
					"expressionResultVar": {},
					"errorCodeVar": {},
					"errorDataVar": {}
				}
			}
		}
	},
	"element": [{
			"id": "expression",
			"name": "expressionActivity",
			"type": "expression",
			"script": "#|!(test.string)!.subString(?(business.a.aa)?,from:?(fromVar)?,to:?(toVar)?)|#",
			"input": {
				"info": {}
			},
			"result": [
				{
					"name" : "success",
					"output": {
						"mapping": {
							"expressionResultVar": {
								definition:{
									"path": "nosliw_output"
								}
							}
						}
					}
				},
				{
					"name" : "fail",
					"output": {
						"mapping": {
							"errorCodeVar": {
								definition: {
									"path": "error.code"
								}
							},
							"errorDataVar": {
								definition: {
									"path": "error.data"
								}
							}
						}
					}
				}
			]
		}	
	],
	"attachment": {
		"testData" : [
			{
				"name": "testData",
				"entity": {
					"business": {
						"a" : {
							"aa" : {
								"dataTypeId": "test.string;1.0.0",
								"value": "This is my world!"
							}
						}
					},
					"mybusiness": {
						"a" : {
							"aa" : {
								"dataTypeId": "test.string;1.0.0",
								"value": "Hello This is my world!"
							}
						}
					},
					"baseVar": {
						"dataTypeId": "test.string;1.0.0",
						"value": "This is my world!"
					},
					"fromVar": {
						"dataTypeId": "test.integer;1.0.0",
						"value": 1
					},
					"toVar": {
						"dataTypeId": "test.integer;1.0.0",
						"value": 7
					}
				}
			}
		],
	},
}
