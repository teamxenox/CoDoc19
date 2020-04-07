# from jsonschema import validate
from genson import SchemaBuilder
import json
import requests

# Globals
indianApiUrl = ['IndianApi','https://api.covid19india.org/data.json','IndianApiCurrentSchema']
globalApiUrl = ['globalApi','https://corona.lmao.ninja/all','globalApiCurrentSchema']
globalApiCountriesUrl = ['globalApi --> Countries','https://corona.lmao.ninja/countries','globalApiCountriesCurrentSchema']
globalApiUsaUrl = ['globalApi --> USA','https://corona.lmao.ninja/countries/USA','globalApiUSACurrentSchema']
deepsetApiFaqUrl = ['DeepsetApi --> FAQ','https://covid-backend.deepset.ai/models/1/faq-qa','deepSetApiFaqCurrentSchema']
deepsetApiFeedbackUrl = ['DeepSetApi --> Feedback','https://covid-backend.deepset.ai/models/1/feedback','deepSetApiFeedbackCurrentSchema']
indianApiUrlList = [indianApiUrl]
globalUrlList = [globalApiUrl,globalApiCountriesUrl,globalApiUsaUrl]
deepsetUrlList =[deepsetApiFaqUrl,deepsetApiFeedbackUrl]

# Function for checking individual api..
def apiCheck(latestSchema,UrlType):
    with open('{}.txt'.format(UrlType[2]),'r+') as f:
        currentSchema = f.read()
        if currentSchema == latestSchema:
            return "Json Structure/Schema is upto date for {} U+2714".format(UrlType[0])
        else:
            query = input("schema has changed do you want to update it[y/n]?\n")
            if query == 'y':
                newSchema = f.write(latestSchema)
                return "Schema updated... U+2714"
            else:
                return "You have chosen not to update schema.. Now you have to manually update it.. U+2757"

def statusCheck(response,urlType):
    if response.status_code == 200:
        return "Api is up and running for {} U+2714".format(urlType[0])
    if response.status_code == 301:
        return "The url for api has changed for {} U+26A0".format(urlType[0])
    if response.status_code == 400:
        return "bad request for {} U+26A0".format(urlType[0])
    if response.status_code == 401:
        return "Authentication problem for {} U+26A0".format(urlType[0])
    if response.status_code == 403:
        return "Forbidden request for {} U+26A0".format(urlType[0])
    if response.status_code == 404:
        return "Resource not found for {} U+26A0".format(urlType[0])
    if response.status_code == 503:
        return "Server not ready handle the request for {} U+26A0".format(urlType[0])
    else:
        return "{} response was returned U+26A0".format(response.status_code)

def main(indianApiUrlList,globalUrlList,deepsetUrlList):
    for i in globalUrlList:
        resp = requests.get(i[1])
        data = resp.json()
        builder = SchemaBuilder()
        builder.add_object(data)
        latestSchema = json.dumps(builder.to_json())
        apiStatus = statusCheck(resp,i)
        print(apiStatus)
        api_check = apiCheck(latestSchema,i)
        print(api_check)
        print("\n\n")
    for i in deepsetUrlList:
        resp = requests.get(i[1])
        data = resp.json()
        builder = SchemaBuilder()
        builder.add_object(data)
        latestSchema = json.dumps(builder.to_json())
        apiStatus = statusCheck(resp,i)
        print(apiStatus)
        api_check = apiCheck(latestSchema,i)
        print(api_check)
        print("\n\n")
    for i in indianApiUrlList:
        resp = requests.get(i[1])
        data = resp.json()
        builder = SchemaBuilder()
        builder.add_object(data)
        latestSchema = json.dumps(builder.to_json())
        apiStatus = statusCheck(resp,i)
        print(apiStatus)
        api_check = apiCheck(latestSchema,i)
        print(api_check)
        print("\n\n")

if __name__ == '__main__':
    main(indianApiUrlList,globalUrlList,deepsetUrlList)
        
# builder = SchemaBuilder()
# resp = requests.get('https://api.covid19india.org/data.json')
# data = resp.json()
# builder.add_object(data)
# textData = json.dumps(builder.to_json())
# # print(json.loads(textData))
# with open('./deepSetApiFeedbackCurrentSchema2.txt','w+') as firstFile:
#     content = firstFile.write(textData)
    # print(content)
# import requests
# import json

# resp = requests.get("https://api.covid19india.org/data.json")
# # resp = requests.get("https://corona.lmao.ninja/")
# d = statusCheck(resp)
# print(d)

# data = jsonresp.json()
# for i in data:
#     print(i)
# print(isinstance(data,dict))

# for i in resp.json().keys():
#     print(i)

# for i in d['statewise']:
#     print(i)

# currentCovidIndiaSchema = None

# resp = requests.get("https://covid-backend.deepset.ai/models/1/faq-qa")
# print(isinstance(resp.json(),dict))
# data = resp.json()
# builder.add_object(data)
# print(json.dumps(builder.to_json()))

# schema = {
#      "type" : "object",
#      "properties" : {
#          "price" : {"type" : "number"},
#          "name" : {"type" : "string"},
#      },
#  }
# try:
#     validate(instance={"name" : "Eggs", "price" : "Invalid"}, schema=schema,)  
# except:
#     print("invalid json")

