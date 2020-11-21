import model._
import adt._
import scala.util.chaining._
import types._

class ElevatorControlSystemImpl(initialState: Map[Elevator, ElevatorStatus]) extends ElevatorControlSystem:
    private var state: Map[Elevator, ElevatorStatus] = initialState

    override def status: Map[Elevator, ElevatorStatus] = state
    /*
    1. Find an elevator that is stationary
    2. If not found, find the elevator that is going in the direction that the user wants to go
       and which is not in a pick up mode. No optimisation for an elevator that is going to pickup someone
    3. If not found, then assign the elevator whose last stop is nearest to this one
    */
    override def request(pickUp: PickupRequest): Elevator = 
        val (elevator, elevatorStatus): ElevatorAndStatus = 
            pickUp
                .pipe(reqStationaryElevator)
                .pipe(result => ifNone(result, pickUp, reqNearestElevatorInDirection))
                .fold(reqNearestLastStopElevator(pickUp))(identity)
        state += (elevator -> elevatorStatus)
        elevator

    override def request(drop: DropRequest): Unit = ???

    override def step: Unit = ???

    private def ifNone(v: Option[ElevatorAndStatus], r: PickupRequest, f: PickupRequest => Option[ElevatorAndStatus]): Option[ElevatorAndStatus] = 
        v.fold(f(r))(Some.apply)

    private def reqStationaryElevator(r: PickupRequest): Option[ElevatorAndStatus] = 
        state
            .find((_, es) => es.isStationary)
            .map((e, es) => e -> es.copy(pickup = Some(r.floor -> r.direction)))

    private def reqNearestElevatorInDirection(r: PickupRequest): Option[ElevatorAndStatus] = ???
    
    private def reqNearestLastStopElevator(r: PickupRequest): ElevatorAndStatus = ???

    private def canPickup(pickup: PickupRequest, elevatorGoalFloor: Floor, elevatorDirection: Direction): Boolean = ???
        // pickup.direction == elevatorDirection &&  {
            // val pf = pickup.floor
            // if(elevatorDirection == Direction.UP)
                // elevatorGoalFloor > pf
            // else
                // elevatorGoalFloor > pf
        // }