{
	"name": "all",
	"description": "process with different activity",
	"valueStructure": {
		"group": {
			"public": {
				"element": {
					business : {
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
				"element": {
					"expressionResultVar": {},
					"errorCodeVar": {},
					"errorDataVar": {}
				}
			}
		}
	},
	"element": [{
			"id": "expressionActivityId",
			"name": "expressionActivity",
			"type": "expression",
			"script": "#|!(test.string)!.subString(?(inputA.b.c)?,from:?(fromVar)?,to:?(toVar)?)|#",
			"inputMapping": {
				"element": {
					"inputA.b.c" : {
						"definition" : {
							"path" : "business.a.aa"
						}
					}, 
					"inputA.d.c" : {
						"definition" : {
							"path" : "business.a.aa"
						}
					} 
				},
				"info": {}
			},
			"result": [
				{
					"name" : "success",
					"output": {
						"element": {
							"expressionResultVar": {
								definition:{
									"path": "nosliw_output"
								}
							}
						}
					}
				},
				{
					"name" : fail",
					"output": {
						"element": {
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
	]
}
