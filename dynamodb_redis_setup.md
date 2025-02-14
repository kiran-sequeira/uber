# DynamoDB and Redis Local Setup

 **Start DynamoDB and Redis Local**
  ```sh
  docker-compose up -d
  ```

## DynamoDB Local Commands

1. **Access DynamoDB Local**
  ```sh
  aws dynamodb list-tables --endpoint-url http://localhost:8000
  ```

2. **DynamoDB Admin GUI**
  ```sh
  dynamodb-admin --dynamo-endpoint=http://localhost:8000
  ```
  > This will open the DynamoDB Admin GUI at [http://localhost:8081](http://localhost:8081)

## Redis Local Commands

1. **Access Redis CLI**
  ```sh
  redis-cli
  ```

2. **Basic Redis Commands**
  - **Set a key-value pair**
    ```sh
    SET key value
    ```
  - **Get the value of a key**
    ```sh
    GET key
    ```
  - **Delete a key**
    ```sh
    DEL key
    ```