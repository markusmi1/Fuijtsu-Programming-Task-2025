# Fuijtsu-Programming-Task-2025

Build the project in gradle and run spring boot application<br>
<br>
Endpoints:<br>
GET http://localhost:8080/api/fee<br>
parameters - <br>
city - possible values (Tallinn, Tartu, Pärnu)<br>
vehicleType - possible values (Bike, Car, Scooter)<br>
dateTime - can be absent, format must be - yyyy-MM-dd'T'HH:mm:ss, if present then shows fee on this particular datetime if data about it is in the database.<br>
Example:<br>
![image](https://github.com/user-attachments/assets/f88b700c-bfb9-4d84-a647-cb49d471808c)<br>
![image](https://github.com/user-attachments/assets/c25f9e83-a6a4-4c98-a7ea-f3bbb543b7c0)<br>
with datetime param<br>
![image](https://github.com/user-attachments/assets/2b2e3a8f-f047-48f6-8fc4-9365c78d9ba3)<br>




GET http://localhost:8080/api/basefee<br>
Shows current base fees for each city<br>
![image](https://github.com/user-attachments/assets/e5a368c1-8781-4c3c-8f1f-ca5f033ce15c)<br>

PUT http://localhost:8080/api/basefee<br>
Body must be in application/json format<br>
example body:<br>
{<br>
    "city": "Tallinn",<br>
    "vehicleType": "Scooter",<br>
    "newFee": 8<br>
}<br>
![image](https://github.com/user-attachments/assets/890cd4bb-2a6c-4d0f-85c4-04c7f8aeed66)<br>
