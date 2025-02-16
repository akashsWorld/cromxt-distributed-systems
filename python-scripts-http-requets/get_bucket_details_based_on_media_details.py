import requests
import time
import json
import sys
URL = 'http://localhost:8904/routing/api/v1/routes/get-bucket-id'
# URL for creating a bucket
media_details = {
    "fileSize":1000,
    "fileExtension":'jpg'
}

def get_bucket_details():
    start_time = time.time()
    response = requests.post(URL,
                json=media_details,
                headers={'Content-Type': 'application/json'}
                )
    end_time = time.time()
    print(f"Time taken to get bucket details: {end_time - start_time}")
    
    status_code = response.status_code

    if(status_code not in range(200, 299)):
        print(f"Some Error Occurred: {status_code}")
        sys.exit(1)

    
    response_data = response.json()
    if(response_data['bucketId']==None):
        print(f"Bucket not found for media details: {media_details}")
        sys.exit(1)

    print(response_data)  

wait_time = 0.05

while True:
    get_bucket_details()
    print(f"Waiting for {wait_time} seconds")
    time.sleep(wait_time)