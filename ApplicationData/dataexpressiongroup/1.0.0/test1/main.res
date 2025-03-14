{
	"info": {
		"name": "test.expression",
		"description": "test expression group entity"
	},
	"entity": {
		"valueContext" : {
			"entity": [
				{
					"groupType" : "public",
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
		"element": [
			{
				"name" : "normal_cleardatatype",
				"status": "disabled1",
				"expression" : "!(test.string)!.subString(base:?(baseVarNormal)?,from:?(parm1)?,to:?(parm2)?)"
			},
			{
				"name" : "normal_implieddatatype",
				"status": "disabled",
				"expression" : "?(baseVarNormal)?.subString(from:?(parm1)?,to:?(parm2)?)"
			},
			{
				"name" : "normal_impliedbase",
				"status": "disabled",
				"expression" : "!(test.string)!.subString(?(baseVarNormal)?,from:?(parm1)?,to:?(parm2)?)"
			},
			{
				"name" : "constant_embeded",
				"status": "disabled",
				"expression" : "&(#test##string___012345678901234567890)&.subString(from:?(parm1)?,to:?(parm2)?)"
			},
			{
				"name" : "constant_attachment",
				"status": "disabled",
				"expression" : "&(constantBase)&.subString(from:&(constantFrom)&,to:&(constantTo)&)"
			},
			{
				"name" : "matcher",
				"status": "disabled",
				"expression" : "!(test.string)!.subString(?(baseVarMatcher)?,from:?(parm1)?,to:?(parm2)?)"
			},
			{
				"name" : "reference",
				"status": "disabled1",
				"expression1" : "<(dataexpressionsingle|#local1)>.with(localBase:?(baseVarNormal)?,localParm1:?(parm1)?,localParm2:?(parm2)?)",
				"expression" : "<(dataexpressionsingle|#local1)>.with(localParm1:?(parm1)?,localParm2:?(parm2)?)"
			},
			{
				"name" : "reference_extensionVariable",
				"status": "disabled",
				"expression" : "<(dataexpressionsingle|#local_extension)>.with(newBase:?(baseVarNormal)?,newFrom:?(parm1)?,newTo:?(parm2)?)"
			}
		],
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
