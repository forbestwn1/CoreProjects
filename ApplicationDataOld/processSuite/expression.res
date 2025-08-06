{
	"name": "all",
	"description": "process with different activity",
	"context": {
		"group": {
			"public": {
				"element": {}
			}
		}
	},
	"attachment": {
		"testData" : [
			{
				"name": "testData1",
				"entity": {
					"fromVar": {
						"dataTypeId": "test.integer;1.0.0",
						"value": 1
					},
					"toVar": {
						"dataTypeId": "test.integer;1.0.0",
						"value": 7
					}
				}
			},
			{
				"name": "testData2",
				"entity": {
					"fromVar": {
						"dataTypeId": "test.integer;1.0.0",
						"value": 3
					},
					"toVar": {
						"dataTypeId": "test.integer;1.0.0",
						"value": 5
					}
				}
			},
		],
	},	
	"element": [{
			"id": "main",
			"name": "expression",
			"description": "process including expression activity",
			"context": {
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
									criteria : "test.integer",
								},
								"defaultValue": {
									"dataTypeId": "test.integer;1.0.0",
									"value": 3
								}
							},
							"toVar" : {
								"definition": {
									criteria: "test.integer",
								},
								"defaultValue": {
									"dataTypeId": "test.integer;1.0.0",
									"value": 7
								}
							}
						}
					},
					"protected": {
						"element": {
							"expressionResultVar": {},
						}
					}
				}
			},
			"activity": [{
					"id": "startActivityId",
					"name": "startActivity",
					"type": "start",
					"flow": {
						"target": "expressionActivityId"
					}
				}, 
				{
					"id": "expressionActivityId",
					"name": "expressionActivity",
					"type": "expression",
					"script": "#|!(test.string)!.subString(?(inputA)?.b.c,from:?(fromVar)?,to:?(toVar)?)|#",
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
							}, 
							"inputB" : {
								"definition" : {
									"path" : "business.a.aa"
								}
							}, 
							"fromVar" : {
								"definition" : {
									"path" : "fromVar"
								}
							}, 
							"toVar" : {
								"definition" : {
									"path" : "toVar"
								}
							}
						},
						"info": {}
					},
					"result": [
						{
							"name" : "success",
							"flow": {
								"target": "successEndId"
							},
							"output": {
								"element": {
									"expressionResultVar": {
										definition:{
											"path": "nosliw_output"
										}
									}
								}
							}
						}
					]
				}, {
					"id": "successEndId",
					"name": "successEnd",
					"type": "end",
					"output": {
						"element": {
							"output": {
								definition: {
									"path": "expressionResultVar"
								}
							}
						}
					}
				}
			]
		},
	]
}
