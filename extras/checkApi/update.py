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
            return "JSON Structure/Schema is already upto date for {}".format(UrlType[0])
        except:
            with open('{}.json'.format(UrlType[2]),'w') as f:
            
                f.write(latestSchema)
                return "Schema Updated for {}".format(UrlType[0])
def main(indianApiUrlList,globalUrlList,deepsetUrlList):
    for i in globalUrlList:
        resp = requests.get(i[1])
        data = resp.json()
        builder = SchemaBuilder()
        builder.add_object(data)
        latestSchema = builder.to_json()
        schema_update = schemaUpdate(data,latestSchema,i)
        print(schema_update)
        print("\n\n")
    for i in deepsetUrlList:
        resp = requests.get(i[1])
        data = resp.json()
        builder = SchemaBuilder()
        builder.add_object(data)
        latestSchema = builder.to_json()
        schema_update = schemaUpdate(data,latestSchema,i)
        print(schema_update)
        print("\n\n")
    for i in indianApiUrlList:
        resp = requests.get(i[1])
        data = resp.json()
        builder = SchemaBuilder()
        builder.add_object(data)
        latestSchema = builder.to_json()
        schema_update = schemaUpdate(data,latestSchema,i)
        print(schema_update)
        print("\n\n")

if __name__ == '__main__':
    main(indianApiUrlList,globalUrlList,deepsetUrlList)
