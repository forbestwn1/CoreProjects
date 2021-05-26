{
	"name": "test1",
	"description": "test1",
	"valueStructure": {
		"group" : {
			"public" : {
				"flat": {
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
					}
				}
			}
		}
	},
	"element": [
		{
			"id": "test22",
			"name": "test22",
			"description": "reference to global with default mapping",
			"element" : [
				{
					"expression" : "!(test.string)!.subString(<(*test10;test2)>,from:?(testVar4)?,to:?(testVar5)?)"
				}
			]
		}
	],
	"attachment": {
		"dataexpression" : [
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
					"testVar1": {
						"var1" : {
							"var11":{
								"dataTypeId": "test.string;1.0.0",
								"value": "0123456789"
							}
						}
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
