# Fuijtsu-Programming-Task-2025

Build the project in gradle and run spring boot application

Endpoints:
GET http://localhost:8080/api/fee
parameters - 
city - possible values (Tallinn, Tartu, Pärnu)
vehicleType - possible values (Bike, Car, Scooter)
dateTime - can be absent, format must be - yyyy-MM-dd'T'HH:mm:ss, if present then shows fee on this particular datetime if data about it is in the database.
Example:
![image](https://github.com/user-attachments/assets/f88b700c-bfb9-4d84-a647-cb49d471808c)
![image](https://github.com/user-attachments/assets/c25f9e83-a6a4-4c98-a7ea-f3bbb543b7c0)
with datetime param
![image](https://github.com/user-attachments/assets/2b2e3a8f-f047-48f6-8fc4-9365c78d9ba3)




GET http://localhost:8080/api/basefee
Shows current base fees for each city
![image](https://github.com/user-attachments/assets/e5a368c1-8781-4c3c-8f1f-ca5f033ce15c)

PUT http://localhost:8080/api/basefee
Body must be in application/json format
example body:
{
    "city": "Tallinn",
    "vehicleType": "Scooter",
    "newFee": 8
}
![image](https://github.com/user-attachments/assets/890cd4bb-2a6c-4d0f-85c4-04c7f8aeed66)
