# from jsonschema import validate
from genson import SchemaBuilder
import json
import requests
from check_api import (
    indianApiUrlList,
    deepsetUrlList,
    globalUrlList,
)


def updateSchema(latestSchema,UrlType):
    with open('{}.txt'.format(UrlType[2]),'r+') as f:
        currentSchema = f.read()
        if currentSchema == latestSchema:
            return "JSON Structure/Schema is already upto date for {} U+2714".format(UrlType[0])
        else:
            f.write(latestSchema)
            return "Schema Updated for {} U+2714".format(UrlType[0])

def main(indianApiUrlList,globalUrlList,deepsetUrlList):
    for i in globalUrlList:
        resp = requests.get(i[1])
        data = resp.json()
        builder = SchemaBuilder()
        builder.add_object(data)
        latestSchema = json.dumps(builder.to_json())
        schema_update = updateSchema(latestSchema,i)
        print(schema_update)
        print("\n\n")
    for i in deepsetUrlList:
        resp = requests.get(i[1])
        data = resp.json()
        builder = SchemaBuilder()
        builder.add_object(data)
        latestSchema = json.dumps(builder.to_json())
        schema_update = updateSchema(latestSchema,i)
        print(schema_update)
        print("\n\n")
    for i in indianApiUrlList:
        resp = requests.get(i[1])
        data = resp.json()
        builder = SchemaBuilder()
        builder.add_object(data)
        latestSchema = json.dumps(builder.to_json())
        schema_update = updateSchema(latestSchema,i)
        print(schema_update)
        print("\n\n")

if __name__ == '__main__':
    main(indianApiUrlList,globalUrlList,deepsetUrlList)
