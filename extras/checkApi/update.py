from jsonschema import validate
from genson import SchemaBuilder
import json
import requests
from check_api import (
    indianApiUrlList,
    deepsetUrlList,
    globalUrlList,
)


def schemaUpdate(data,latestSchema,UrlType):
    with open('{}.json'.format(UrlType[2]),'r') as f:
        currentSchema = json.loads(f.read())
        try:
            validate(instance=data,schema=currentSchema)
            return "✅ JSON Structure/Schema is already upto date for {}".format(UrlType[0])
        except:
            with open('{}.json'.format(UrlType[2]),'w') as f:
            
                f.write(latestSchema)
                return "Schema Updated for {}".format(UrlType[0])
def main(indianApiUrlList,globalUrlList,deepsetUrlList):
    questionFormat = data = '{"questions":["string"],"filters":{"additionalProp1":"string","additionalProp2":"string","additionalProp3":"string"},"top_k_reader":0,"top_k_retriever":0}'
    headers = {
        'accept': 'application/json',
        'Content-Type': 'application/json',
    }
    for i in globalUrlList:
        resp = requests.get(i[1])
        data = resp.json()
        builder = SchemaBuilder()
        builder.add_object(data)
        latestSchema = builder.to_json()
        schema_update = schemaUpdate(data,latestSchema,i)
        print(schema_update)
        print("----------------------------------------------------------------")
    for i in deepsetUrlList:
        resp = requests.post(i[1],headers=headers,data=questionFormat)
        data = resp.json()
        builder = SchemaBuilder()
        builder.add_object(data)
        latestSchema = builder.to_json()
        schema_update = schemaUpdate(data,latestSchema,i)
        print(schema_update)
        print("----------------------------------------------------------------")
    for i in indianApiUrlList:
        resp = requests.get(i[1])
        data = resp.json()
        builder = SchemaBuilder()
        builder.add_object(data)
        latestSchema = builder.to_json()
        schema_update = schemaUpdate(data,latestSchema,i)
        print(schema_update)
        print("----------------------------------------------------------------")

if __name__ == '__main__':
    main(indianApiUrlList,globalUrlList,deepsetUrlList)
