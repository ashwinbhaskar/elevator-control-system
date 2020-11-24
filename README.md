# Elevator Control System

The steps used to allocate an elevator for a pick up are:

1. Find an elevator that is stationary
2. If not found, find elevator which is going for a pickup and whose direction after pick up is in the same direction as the request
3. If not found, find the elevator that is going in the direction that the user wants to go and which is not in a pick up mode. No optimisation for an elevator that is going to pickup someone

If you see the contract for `request(req: PickupRequest)`, you can see that the return type is `Elevator | Error`. This was a conscious decision to avoid going into a rabit-hole. I could not find out a `proper` way of assigning an elevator when all the above 3 steps fail to assign an elevator.

# Run Simulation

```
sbt console
:load pre-loaded-elevator-control-system.scala

controlSystem.step
controlSystem.status
```

The file `pre-loaded-elevator-control-system.scala` is included in the zip. Feel free to change it's contents as per your needs. Once the file is loaded you will have all the imports and `controlSystem` in scope.

# Tests
The code includes test for `ElevatorStatus` and `ElevatorControlSystemImpl`. To run the tests, execute
```
sbt test
```

# Language
The system is implemented in Dotty (Scala 3)