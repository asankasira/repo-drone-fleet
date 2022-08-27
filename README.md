# Drone fleet Application

## Pre-requisites
    - Java 17
    - Maven 3.x
  

| Method | URI                      | Description                              | Sample Payload | Response                             |
|--------|--------------------------|------------------------------------------|----------------|--------------------------------------|
| POST   | /drone-management/drones | Registers a new Drone                    | ![img.png](images/img.png)               |                                      |
| PUT    | /drone-management/drones/{id}/medical-item-line                        | Loads medication items for a given drone | ![img_1.png](images/img_1.png)               | `{ "medicationID": 12, "quantity": 5}` |
|        |                          |                                          |                |                                      |
|        |                          |                                          |                |                                      |
