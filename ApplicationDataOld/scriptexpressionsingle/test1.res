{
	"info": {
		"name": "test.expression",
		"description": "test expression group entity"
	},
	"entity": {
		"valueContext" : {
			"entity": [
				{
					"scope" : "public",
					"valueStructure" : {
						"baseVarNormal": {
							"definition":{
								"criteria": "test.string;1.0.0"
							},
							"defaultValue": {
								"dataTypeId": "test.string;1.0.0",
								"value": "default value of baseVarNormal"
							}
						},
						"baseVarMatcher": {
							"definition":{
								"criteria": "test.url;1.0.0"
							},
							"defaultValue": {
								"dataTypeId": "test.url;1.0.0",
								"value": "default value of testvar3"
							}
						},
						"parm1" : {
							"definition": {
								"criteria" : "test.integer"
							},
							"defaultValue": {
								"dataTypeId": "test.integer;1.0.0",
								"value": 5
							}
						},
						"parm2" : {
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
			]
		},
		"expression": "HaHaHa  <%=(#|!(test.string)!.subString(?(baseVarNormal)?,from:?(parm1)?,to:?(parm2)?)|#.value+?(parm2)?.value)+?(parm1)?.value%>  End!!!!",
		"attachment": {
			"extra": {
				"status": "disabled1",
				"name": "parent attachment" 
			},
			"entity": {
				"data" : [
					{
						"name" : "constantBase",
						"entity": {
							"dataTypeId": "test.string;1.0.0",
							"value": "012345678901234567890"
						}
					},
					{
						"name" : "constantFrom",
						"entity": {
							"dataTypeId": "test.integer;1.0.0",
							"value": 5
						}
					},
					{
						"name" : "constantTo",
						"entity": {
							"dataTypeId": "test.integer;1.0.0",
							"value": 7
						}
					}
				]
			}
		}
	}
}
