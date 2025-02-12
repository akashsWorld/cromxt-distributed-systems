from fastapi import FastAPI
from typing import Union
import subprocess

app = FastAPI()

@app.get("/")
async def root():
    return {"message": "Hello Akash"}


@app.post("/api/v1/buckets/{bucketId}")
async def create_server(bucketId: str):
    if bucketId == None:
        return {"message": "Bucket ID is required"}
    
    print(f"Bucket ID: {bucketId}")
    process = subprocess.Popen(["mvn","--version"],stdout=subprocess.PIPE,stderr=subprocess.PIPE,text=True)
    _ ,stderr = process.communicate()
    
    if stderr:
        print("stderr")
        print(stderr)    
    return {"message": "Bucket created"}
