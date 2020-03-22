{
	"name": "test1",
	"description": "test1",
	"context": {
		"group": {
			"public": {
				"element": {}
			}
		}
	},
	"attachment": {
	},	
	"element": [{
			"id": "main",
			"name": "main",
			"description": "test1",
			"expression" : "!(test.string)!.subString(?(inputA)?.b.c,from:?(fromVar)?,to:?(toVar)?)",
			"referenceMapping" : [
				{
					"name" : "ref1",
					"resourceId" : "aaa|bbb",
					"inputMapping" : {
						
					},
					
				}
			],

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
		},
	]
}
