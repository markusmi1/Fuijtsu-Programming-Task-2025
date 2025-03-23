# Fuijtsu-Programming-Task-2025

Build the project in gradle and run spring boot application<br>
Command: gradlew build<br>
Run file \src\main\java\org\example\fooddeliveryapp\FoodDeliveryAppApplication.java<br>
<br>
Database can be found on http://localhost:8080/h2-console and is created on initial startup, after this database is saved in /data folder<br>
<br>
Endpoints:<br>
GET http://localhost:8080/api/fee<br>
Parameters - <br>
Parameter&nbsp;&nbsp;Type	Required	Description<br>
<strong>city</strong> String,	 Required,     City names (Tallinn, Tartu, Pärnu)<br>
<strong>vehicleType</strong> String,	Required,	    Vehicle types (Bike, Car, Scooter)<br>
<strong>dateTime</strong>  String,	Not required,	    Format yyyy-MM-dd'T'HH:mm:ss<br><br>
<br>
<strong>Example link</strong> http://localhost:8080/api/fee?city=Tartu&vehicleType=Bike:<br>
<img src="https://github.com/user-attachments/assets/9e96e41f-6edc-427c-8a51-5c33e6ac155a" width="600"><br><br>
<strong>Example when input is missing parameters:</strong><br>
<img src="https://github.com/user-attachments/assets/c25f9e83-a6a4-4c98-a7ea-f3bbb543b7c0" width="600"><br><br>
<strong>With datetime parameter applied</strong><br>
<img src="https://github.com/user-attachments/assets/2b2e3a8f-f047-48f6-8fc4-9365c78d9ba3" width="600"><br><br>



<strong>Bonus tasks:</strong>
GET http://localhost:8080/api/basefee<br>
Shows current base fees for each city<br>
<img src="https://github.com/user-attachments/assets/e5a368c1-8781-4c3c-8f1f-ca5f033ce15c" width="600"><br>


PUT http://localhost:8080/api/basefee<br>
Body must be in application/json format<br>
<strong>Example body:</strong><br>
{<br>
    "city": "Tallinn",<br>
    "vehicleType": "Scooter",<br>
    "newFee": 8<br>
}<br>

<img src="https://github.com/user-attachments/assets/890cd4bb-2a6c-4d0f-85c4-04c7f8aeed66" width="600">
