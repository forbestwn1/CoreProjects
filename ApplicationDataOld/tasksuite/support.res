{
	"name": "all",
	"description": "process with different activity",
	"valueStructure": {
		"group": {
			"public": {
				"flat": {
					"support_business" : {
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
					"support_baseVar": {
						"definition":{
							criteria: "test.string",
						},
						"defaultValue": {
							"dataTypeId": "test.string",
							"value": "This is my world!"
						}
					},
					"support_fromVar" : {
						"definition": {
							criteria : "test.integer"
						},
						"defaultValue": {
							"dataTypeId": "test.integer",
							"value": 3
						}
					},
					"support_toVar" : {
						"definition": {
							criteria: "test.integer"
						} ,
						"defaultValue": {
							"dataTypeId": "test.integer",
							"value": 7
						}
					},
				}
			},
			"protected": {
				"flat": {
					"support_expressionResultVar": {},
					"support_errorCodeVar": {},
					"support_errorDataVar": {}
				}
			}
		}
	},
	"element": [{
			"taskType": "activity",
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
				}
			]
		},
	],
	"attachment": {
	},
}
