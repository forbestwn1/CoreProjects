{
	"name": "test1",
	"description": "test1",
	"context": {
		"element": {
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
	},
	"element": [{
			"id": "test10",
			"name": "test10",
			"description": "standard",
			"element" : [
				{
					"expression" : "!(test.string)!.subString(?(testVar3)?,from:?(testVar4)?,to:?(testVar5)?)"
				}
			]
		},
		{
			"id": "test11",
			"name": "test11",
			"description": "discover base, constant",
			"element" : [
				{
					"expression" : "&(#test##string___01234567899)&.subString(from:&(constantFromAtt1)&,to:&(constantFromContext2)&)"
				}
			]
		},
		{
			"id": "test12",
			"name": "test12",
			"description": "attribute chain",
			"element" : [
				{
					"expression" : "!(test.string)!.subString(?(testVar1)?.var1.var11,from:?(testVar4)?,to:?(testVar5)?)"
				}
			]
		},
		{
			"id": "test13",
			"name": "test13",
			"description": "attribute chain",
			"element" : [
				{
					"expression" : "!(test.string)!.subString(?(testVar1)?.var1.var11,from:?(testVar4)?,to:?(testVar5)?)"
				}
			]
		},
		{
			"id": "test20",
			"name": "test20",
			"description": "reference to real expression, default",
			"element" : [
				{
					"expression" : "!(test.string)!.subString(<(ref1)>,from:?(testVar4)?,to:?(testVar5)?)"
				}
			]
		},
		{
			"id": "test21",
			"name": "test21",
			"description": "reference to real expression, not default",
			"element" : [
				{
					"expression" : "!(test.string)!.subString(<(ref2ATtest2)>,from:?(testVar4)?,to:?(testVar5)?)"
				}
			]
		},
		{
			"id": "test22",
			"name": "test22",
			"description": "reference to global with default mapping",
			"element" : [
				{
					"expression" : "!(test.string)!.subString(<(ref3)>,from:?(testVar4)?,to:?(testVar5)?)"
				}
			]
		},
		{
			"id": "test23",
			"name": "test23",
			"description": "reference to global with mapping",
			"element" : [
				{
					"expression" : "!(test.string)!.subString(<(ref4)>,from:?(testVar4)?,to:?(testVar5)?)"
				}
			]
		},
		{
			"id": "test24",
			"name": "test24",
			"description": "reference to global with mapping",
			"element" : [
				{
					"expression" : "<(ref5)>"
				}
			]
		},
		{
			"id": "test25",
			"name": "test25",
			"description": "reference to local with default mapping",
			"element" : [
				{
					"expression" : "<(ref6)>"
				}
			]
		},
		{
			"id": "test26",
			"name": "test26",
			"description": "reference to global embeded resource with default mapping",
			"element" : [
				{
					"expression" : "<(ref7)>"
				}
			]
		}
	],
	"attachment": {
		"expression" : [
			{
				"name" : "ref1",
				"entity" : {
					"expression" : "!(test.string)!.subString(?(testVar3)?,from:&(constantFromContext3)&,to:&(constantFromAtt4)&)"
				}
			},
			{
				"name" : "ref2",
				"entity" : {
					"element" : [
						{
							"name" : "test1",
							"expression" : "?(testVar3)?"
						},					
						{
							"name" : "test2",
							"expression" : "!(test.string)!.subString(?(testVar3)?,from:&(constantFromContext3)&,to:&(constantFromAtt4)&)"
						}					
					]
				}
			},
			{
				"name" : "ref3",
				"referenceId": "test10;test2"
			},
			{
				"name" : "ref4",
				"referenceId": "test10;test3",
				"adaptor" : {
					"inputMapping" : {
						"element" : {
							"testVar33" : {
								"definition": {
									"path": "testVar3"
								}
							}
						}
					}
				}
			},
			{
				"name" : "ref5",
				"referenceId": "test11;test3",
				"adaptor" : {
					"inputMapping" : {
						"element" : {
							"testVar33" : {
								"definition": {
									"path": "testVar3"
								}
							}
						}
					}
				}
			},
			{
				"name" : "ref6",
				"referenceId": {
					"structure" : "local",
					"id" : "test10;local1"
				}
			},
			{
				"name" : "ref7",
				"referenceId": "@test10:expressionSuite|test2"
			}
		],
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
