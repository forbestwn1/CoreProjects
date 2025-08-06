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
	"element": [{
			"id": "test1",
			"name": "test1",
			"description": "standard",
			"element" : [
				{
					"expression" : "!(test.string)!.subString(?(baseVar)?,from:?(fromVar)?,to:?(toVar)?)",
				}
			]
		},
		{
			"id": "test2",
			"name": "test2",
			"description": "discover base",
			"element" : [
				{
					"expression" : "?(baseVar)?.subString(from:?(fromVar)?,to:?(toVar)?)",
				}
			]
		},
		{
			"id": "test3",
			"name": "test3",
			"description": "constant",
			"element" : [
				{
					"expression" : "!(test.string)!.subString(?(baseVar)?,from:&(fromConstant)&,to:?(toVar)?)",
				}
			]
		},
		{
			"id": "test4",
			"name": "test4",
			"description": "attribute chain",
			"element" : [
				{
					"expression" : "!(test.string)!.subString(?(business)?.a.aa,from:?(fromVar)?,to:?(toVar)?)",
				}
			]
		},
		{
			"id": "test5",
			"name": "test5",
			"description": "reference with default mapping",
			"element" : [
				{
					"expression" : "!(test.string)!.subString(<(ref1)>,from:?(fromVar)?,to:?(toVar)?)",
					"referenceMapping" : [
						{
							"name" : "ref1",
							"resourceId" : "test1",
						}
					],
				}
			]
		},
		{
			"id": "test6",
			"name": "test6",
			"description": "reference with specified mapping",
			"element" : [
				{
					"expression" : "!(test.string)!.subString(<(ref1)>,from:?(fromVar)?,to:?(toVar)?)",
					"referenceMapping" : [
						{
							"name" : "ref1",
							"resourceId" : "test3",
							"inputMapping" : {
								"element" : {
									"baseVar" : {
										"definition": {
											"path": "mybusiness.a.aa"
										},
									}
								}
							}
						}
					],
				}
			]
		},
		{
			"id": "test7",
			"name": "test7",
			"description": "default reference",
			"element" : [
				{
					"expression" : "!(test.string)!.subString(<(test1)>,from:?(fromVar)?,to:?(toVar)?)",
				}
			]
		},
	],
	"attachment": {
		"dataexpression" : [
		],
		"value" : [
			{
				"name": "fromConstant",
				"entity": {
					"value" : {
						"dataTypeId": "test.integer;1.0.0",
						"value": 1
					}
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
	},	
	
}
