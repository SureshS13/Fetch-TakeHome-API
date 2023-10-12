# Fetch Takehome API

This is the README for the Fetch Takehome API, a service designed to process receipts and calculate points based on specific rules.

## Running the Application

### Prerequisites

- Docker: Make sure you have Docker installed on your system.

### How to Run

1. **Clone the Repository**: Clone the project from my GitHub repository (attached at the bottom of this README) to your machine, and open the folder containing the project files in your preferred IDE


2. **Pull the Docker Image**: To run the Fetch Takehome API using a Docker container, pull the Docker image from Docker Hub by running the following command in your terminal:

   ```bash
   docker pull sureshs13/fetch_takehome_api
    ```

3. **Run the Docker Container**: Once the image is successfully pulled, you can run the application in a Docker container. Use the following command:

   ```bash
   docker run -p 8000:8080 sureshs13/fetch_takehome_api
    ```
    This command will start the Fetch Takehome API in a Docker container, exposing it on port 8000 of your local machine. You can access the API locally by visiting http://localhost:8000



### Docker Repository
You can also find the Fetch Takehome API Docker image on Docker Hub: https://hub.docker.com/r/sureshs13/fetch_takehome_api

### API Endpoints
The Fetch Takehome API provides the following endpoints:

1. **POST** /receipts/process: Accepts a JSON receipt and returns a JSON object with a new receipt ID


2. **GET** /receipts/{id}/points: Retrieves the points awarded for a specific receipt with the provided ID

#### Example Endpoints
- To add a receipt, send a POST request to http://localhost:8000/receipts/process with a JSON receipt.


- To retrieve points for a receipt with ID, send a GET request to http://localhost:8000/receipts/{id}/points.

## Repository
The source code for the Fetch Takehome API can be found on GitHub: https://github.com/SureshS13/Fetch-TakeHome-API

If you have any questions or need further assistance, please don't hesitate to let me know!

Sachin
