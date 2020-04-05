{
	"name": "test1",
	"description": "test1",
	"context": {
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
			mybusiness : {
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
	"attachment": {
		"data" : [
			{
				"name": "fromConstant",
				"entity": {
					"dataTypeId": "test.integer;1.0.0",
					"value": 1
				}
			},
		],
		"testData" : [
			{
				"name": "testData1",
				"entity": {
					"business.a.aa": {
						"dataTypeId": "test.string;1.0.0",
						"value": "This is my world!"
					},
					"mybusiness.a.aa": {
						"dataTypeId": "test.string;1.0.0",
						"value": "Hello This is my world!"
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
			},
		],
		"expression" : [
			{
				"name": "test1",
				"description": "standard",
				"entity" : {
					"expression" : "!(test.string)!.subString(?(baseVar)?,from:?(fromVar)?,to:?(toVar)?)",
				}
			},		
		]
	},	
	"element": [{
			"id": "test1",
			"name": "test1",
			"description": "standard",
			"element" : [
				{
					"script" : "<%=#|?(business)?.a.aa.subString(from:&(from)&,to:&(to)&)|#.value=='s isfff'?'red':'blue'%>",
				}
			]
		}
	]
}
