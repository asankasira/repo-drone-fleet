# Drone fleet Application

## Pre-requisites
   * Java 17
   * Maven 3.x
  
| Method | URI                                             | Description                                                                                                                           | Sample Payload             | Response                                                         |
|--------|-------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------|----------------------------|------------------------------------------------------------------|
| POST   | /drone-management/drones                        | Registers a new Drone                                                                                                                 | ![img.png](images/1.png)   |          ![img.png](img.png)                                                        |
| PUT    | /drone-management/drones/{id}/medical-item-line | Loads medication items for a given drone                                                                                              | ![img_1.png](images/2.png) | ![img_1.png](img_1.png) |
| PUT    | /drone-management/medical-item-line             | Automatically picks any available drone to load a medicine item                                                                       | ![img_1.png](images/2.png) |    ![img_3.png](img_3.png)                                                              |
| GET    | /drone-management/drones/3/medical-items        | Retrieves loaded medication items for a given drone                                                                                   | N/A                        |      ![img_2.png](img_2.png)                                                            |
| GET    | /drone-management/available-drones              | Gets all avaliable drones for items loading. Drones in IDLE/LOADING state and battery level is not below than 25% meets this criteria | N/A                        |                                                                                         |
| GET    | /drone-management/drone-info/{id}/battery-level | Checks drone battery level for a given drone                                                                                          | N/A                        |    ![img_4.png](img_4.png)                                                                                     |
| GET    | /drone-management/drones                        | Retrieves all resigtered drone details                                                                                                | N/A                        |                                                                                                                |
