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
	},	
	"element": [{
			"id": "test1",
			"name": "test1",
			"description": "standard",
			"expression" : "!(test.string)!.subString(?(baseVar)?,from:?(fromVar)?,to:?(toVar)?)",
			"referenceMapping" : [
				{
					"name" : "ref1",
					"resourceId" : "aaa|bbb",
					"inputMapping" : {
						
					},
				}
			],
		},
		{
			"id": "test2",
			"name": "test2",
			"description": "discover base",
			"expression" : "?(baseVar)?.subString(from:?(fromVar)?,to:?(toVar)?)",
		},
		{
			"id": "test3",
			"name": "test3",
			"description": "constant",
			"expression" : "!(test.string)!.subString(?(baseVar)?,from:&(fromConstant)&,to:?(toVar)?)",
		},
		{
			"id": "test4",
			"name": "test4",
			"description": "attribute chain",
			"expression" : "!(test.string)!.subString(?(business)?.a.aa,from:?(fromVar)?,to:?(toVar)?)",
		},
		{
			"id": "test5",
			"name": "test5",
			"description": "reference",
			"expression" : "!(test.string)!.subString(<(ref1)>,from:?(fromVar)?,to:?(toVar)?)",
			"referenceMapping" : [
				{
					"name" : "ref1",
					"resourceId" : "test1",
				}
			],
		},
	]
}
