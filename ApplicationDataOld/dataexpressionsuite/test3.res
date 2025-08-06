{
	"name": "test3",
	"description": "test3",
	"valueStructure": {
		"flat": {
			"testVar33": {
				"definition":{
					"criteria": "test.string"
				},
				"defaultValue": {
					"dataTypeId": "test.string",
					"value": "9876543210"
				}
			},
			"constantFromContext31": {
				"definition" : {
					"value" : {
						"dataTypeId": "test.integer",
						"value": 1
					}
				}
			},
			"constantFromContext32": {
				"definition" : {
					"value" : {
						"dataTypeId": "test.integer",
						"value": 3
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
					"expression" : "!(test.string)!.subString(?(testVar33)?,from:&(constantFromAtt31)&,to:&(constantFromContext32)&)"
				}
			]
		},
		{
			"id": "test11",
			"name": "test11",
			"description": "standard",
			"element" : [
				{
					"expression" : "!(test.string)!.subString(<(*test11;test2)>.with(testVar3:?(testVar33)?),from:&(constantFromAtt31)&,to:&(constantFromContext32)&)"
				}
			]
		}
	],
	"attachment": {
		"dataexpression" : [
			{
				"name" : "ref1",
				"referenceId": "test11;test2",
				"adaptor" : {
					"varMapping" : {
						"element" : {
							"testVar3" : {
								"definition": {
									"path": "testVar33"
								}
							}
						}
					}
				}
			}
		],
		"value" : [
			{
				"name": "constantFromAtt31",
				"entity": {
					"value" : {
						"dataTypeId": "test.integer;1.0.0",
						"value": 1
					}
				}
			},
			{
				"name": "constantFromAtt32",
				"entity": {
					"value" : {
						"dataTypeId": "test.integer;1.0.0",
						"value": 3
					}
				}
			}
		],
		"testData" : [
			{
				"name": "testData31",
				"entity": {
					"testVar1.var1.var11": {
						"dataTypeId": "test.string;1.0.0",
						"value": "0123456789"
					},
					"testVar32": {
						"var1" : {
							"var11":{
								"dataTypeId": "test.string;1.0.0",
								"value": "0123456789"
							}
						}
					},
					"testVar33": {
						"dataTypeId": "test.string;1.0.0",
						"value": "0123456789"
					},
					"testVar34": {
						"dataTypeId": "test.integer;1.0.0",
						"value": 1
					},
					"testVar35": {
						"dataTypeId": "test.integer;1.0.0",
						"value": 3
					}
				}
			}
		]
	}
}
