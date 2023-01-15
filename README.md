# Clevershuttle - Code Challenge
#### Create a REST API and implement CREATE and READ operations for the “Car” model
| Property      | DataType                                           |
|---------------|----------------------------------------------------|
| Id            | Up to your decision                                |
| Brand         | Up to your decision                                |
| LicensePlate  | Up to your decision                                |
| Manufacturer  | Up to your decision                                |
| OperationCity | Up to your decision                                |
| Status        | available,<br/>in-maintenance,<br/> out-of-service |
| CreatedAt     | Up to your decision                                |
| LastUpdatedAt | Up to your decision                                |

## Author
Hamidreza Khayatzadeh [linkedin]

## Description
We have a need to create a backend API to store and retrieve the data about the cars of our fleet

## Examples
A sample JSON representation of such a model would look like (some columns are intentionally omitted):

```json
{
    “id”: 12345,
    “brand”: “Flexa”,
    “licensePlate”: “L-CS8877E”,
    “status”: “available”,
    “createdAt”: “2017-09-01T10:23:47.000Z",
    “lastUpdatedAt”: “2022-04-15T13:23:11.000Z"
}
```

## How to Build
It is a maven project then to build, please run 
```
mvn clean install 
```

## How to Test
To start the tests, please run 
```
mvn test 
```

## How to Run 
Run FleetManagementApplication.class to start the application on port **8080**.
The APIs are available via [ApiLink]


## Database
H2 File System for application
H2 Memory for test

## Postman Collection
It is also possible to import **FleetManagement.postman_collection.json** file in Postman application in order to call the APIs 

## License
**Free Software**

[linkedin]: <https://www.linkedin.com/in/hamidreza-khayatzadeh/>
[APILink]: http://localhost:8080/swagger-ui.html