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
					},
					"outputVar1" : {
						"definition": {
							criteria: "test.string"
						}
					},
					"outputVar2" : {
						"definition": {
							criteria: "test.string"
						}
					},
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
		},
		{
			"taskType": "activity",
			"id": "service",
			"name": "serviceActivity",
			"type": "Service_request",
			"serviceUse" : {
				"interface" : "service_simpleoutput",
				"provider" : "simpleServiceWithoutInterfaceProvider",
				"info" : {
					"enhanceContext" : "true"
				},
				"dataMapping" :{
					"inputMapping" : {
						"mapping" : {
							"parm1" : {
								"description" : "input from context node with default value",
								"definition" : {
									"path" : "business.a.aa"
								}
							},
							"parm2" : {
								"description" : "input from context node without default value",
								"definition" : {
									"path" : "mybusiness.a.aa"
								}
							},
						}
					},
					"outputMapping" : {
						"success" : {
							"mapping" : {
								"outputVar1" : {
									"definition" : {
										"path" : "simpleOutput1"	
									}
								},
								"outputVar2" : {
									"description" : "output to enhance variable",
									"definition" : {
										"path" : "simpleOutput2"	
									}
								}
							}
						}
					}
				}
			},
			"result": [
			]
		},			
		{
			"taskType": "activity",
			"id": "embededTask",
			"name": "embededTask",
			"type": "Service_request",
			"serviceUse" : {
				"interface" : "service_simpleoutput",
				"provider" : "simpleServiceWithoutInterfaceProvider",
				"info" : {
					"enhanceContext" : "true"
				},
				"dataMapping" :{
					"inputMapping" : {
						"mapping" : {
							"parm1" : {
								"description" : "input from context node with default value",
								"definition" : {
									"path" : "business.a.aa"
								}
							},
							"parm2" : {
								"description" : "input from context node without default value",
								"definition" : {
									"path" : "mybusiness.a.aa"
								}
							},
						}
					},
					"outputMapping" : {
						"success" : {
							"mapping" : {
								"outputVar1" : {
									"definition" : {
										"path" : "simpleOutput1"	
									}
								},
								"outputVar2" : {
									"description" : "output to enhance variable",
									"definition" : {
										"path" : "simpleOutput2"	
									}
								}
							}
						}
					}
				}
			},
			"result": [
			]
		},			
	],
	"attachment": {
		"service" : [
			{
				"name": "simpleServiceWithoutInterfaceProvider",
				"referenceId" : "simpleoutput_refinterface"
			},	
			{
				"name": "simpleServiceWithInterfaceProvider",
				"referenceId" : "simpleoutput_internalinterface"
			},	
		],
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
