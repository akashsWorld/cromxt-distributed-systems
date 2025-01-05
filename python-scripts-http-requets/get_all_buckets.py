import requests
import json

protocol = 'http://'
domain_name= 'localhost'
port= '8902'
# URL for creating a bucket
url = f'{protocol}{domain_name}:{port}/api/v1/admin'
bucket_object ={
    'hostname': 'localhost',
    'port': '9090',
}

response = requests.get(url,
              headers={'Content-Type': 'application/json'}
              )
print(response.status_code)
if(response.status_code in range(200, 299)):
    buckets = response.json()
    print("All buckets:")
    print(json.dumps(buckets, indent=2))