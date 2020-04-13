from jsonschema import validate
from genson import SchemaBuilder
import json
import requests

# Globals
indianApiUrl = ['Indian API','https://api.covid19india.org/data.json','IndianApiCurrentSchema']
globalApiUrl = ['Global API','https://corona.lmao.ninja/all','globalApiCurrentSchema']
globalApiCountriesUrl = ['Global API / Countries','https://corona.lmao.ninja/countries','globalApiCountriesCurrentSchema']
globalApiUsaUrl = ['Global API / USA','https://corona.lmao.ninja/countries/USA','globalApiUSACurrentSchema']
deepsetApiFaqUrl = ['DeepSet API / Questions','https://covid-backend.deepset.ai/question/ask','deepSetApiFaqCurrentSchema']
indianApiUrlList = [indianApiUrl]
globalUrlList = [globalApiUrl,globalApiCountriesUrl,globalApiUsaUrl]
deepsetUrlList =[deepsetApiFaqUrl]

# Function for checking individual api..
def apiCheck(data,UrlType):
    with open('{}.json'.format(UrlType[2]),'r') as f:
        currentSchema = json.loads(f.read())
        try:
            validate(instance=data,schema=currentSchema)
            return "✅ JSON Structure/Schema is upto date for {}".format(UrlType[0])
        except:
            return "❌ JSON Structure/Schema has been changed for {}".format(UrlType[0])

def statusCheck(response,urlType):
    if response.status_code == 200:
        return "✅ {}".format(urlType[0])
    if response.status_code == 301:
        return "❌ The url for api has changed for {}".format(urlType[0])
    if response.status_code == 400:
        return "❌ Bad request for {}".format(urlType[0])
    if response.status_code == 401:
        return "❌ Authentication problem for {}".format(urlType[0])
    if response.status_code == 403:
        return "❌ Forbidden request for {}".format(urlType[0])
    if response.status_code == 404:
        return "❌ Resource not found for {}".format(urlType[0])
    if response.status_code == 503:
        return "❌ Server not ready handle the request for {}".format(urlType[0])
    else:
        return "❌ {} response was returned".format(response.status_code)

def main(indianApiUrlList,globalUrlList,deepsetUrlList):
    questionFormat = data = '{"questions":["string"],"filters":{"additionalProp1":"string","additionalProp2":"string","additionalProp3":"string"},"top_k_reader":0,"top_k_retriever":0}'
    headers = {
        'accept': 'application/json',
        'Content-Type': 'application/json',
    }
    for i in globalUrlList:
        resp = requests.get(i[1])
        data = resp.json()
        apiStatus = statusCheck(resp,i)
        print(apiStatus)
        api_check = apiCheck(data,i)
        print(api_check)
        print("--------------------------------")
    for i in deepsetUrlList:
        resp = requests.post(i[1],headers=headers,data=questionFormat)
        data = resp.json()
        apiStatus = statusCheck(resp,i)
        print(apiStatus)
        api_check = apiCheck(data,i)
        print(api_check)
        print("--------------------------------")
    for i in indianApiUrlList:
        resp = requests.get(i[1])
        data = resp.json()
        apiStatus = statusCheck(resp,i)
        print(apiStatus)
        api_check = apiCheck(data,i)
        print(api_check)
        print("--------------------------------")

if __name__ == '__main__':
    main(indianApiUrlList,globalUrlList,deepsetUrlList)