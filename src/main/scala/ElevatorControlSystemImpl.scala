import model._
import adt._
import adt.Direction._
import adt.Error._
import scala.util.chaining._
import types._

class ElevatorControlSystemImpl(initialState: Map[Elevator, ElevatorStatus]) extends ElevatorControlSystem:
    private var state: Map[Elevator, ElevatorStatus] = initialState

    override def status: Map[Elevator, ElevatorStatus] = state
    /*
    1. Find an elevator that is stationary
    2. If not found, find elevator which is going for a pickup and whose direction after pick up 
       is in the same direction as the request
    3. If not found, find the elevator that is going in the direction that the user wants to go
       and which is not in a pick up mode. No optimisation for an elevator that is going to pickup someone
    */
    override def request(pickup: PickupRequest): Elevator | Error = 
        val result: ElevatorAndStatus | Error = 
            pickup
                .pipe(reqStationaryElevator)
                .pipe(result => ifNone(result, pickup, reqElevatorWithPickupDirection))
                .pipe(result => ifNone(result, pickup, reqElevatorInDirection))
                .fold(NoElevatorAvailable)(identity)
        result match
            case elevator -> elevatorStatus => 
                state += (elevator -> elevatorStatus)
                elevator
            case error: Error => error

    override def request(drop: DropRequest): Error | Unit = 
        val elevatorStatus = state(drop.elevator)
        val dropDirection = 
            if(elevatorStatus.currentFloor > drop.floor)
                DOWN
            else
                UP
        if(elevatorStatus.direction.get != dropDirection) //a confident .get as you cannot be inside a stationary elevator
            InvalidDropDirection
        else
            state += (drop.elevator -> elevatorStatus.addDestination(drop.floor))


    override def step: Unit = 
        state = state.map((e, es) => e -> es.step)

    private def ifNone(v: Option[ElevatorAndStatus], r: PickupRequest, f: PickupRequest => Option[ElevatorAndStatus]): Option[ElevatorAndStatus] = 
        v.fold(f(r))(Some.apply)

    private def reqStationaryElevator(r: PickupRequest): Option[ElevatorAndStatus] = 
        state
            .find((_, es) => es.isStationary)
            .map((e, es) => e -> es.copy(pickup = Some(r.floor -> r.direction)))

    private def reqElevatorWithPickupDirection(r: PickupRequest): Option[ElevatorAndStatus] = 
        def canPickup(elevatorStatus: ElevatorStatus): Boolean = 
            elevatorStatus match
                case ElevatorStatus(_, _, Some(pickupFloor -> dir)) if dir == r.direction => 
                        if dir == DOWN then
                            r.floor < pickupFloor
                        else
                            r.floor > pickupFloor
                case _ => false
        state
            .find((_, es) => canPickup(es))
            .map((e, es) => e -> es.addDestination(r.floor))

    private def reqElevatorInDirection(r: PickupRequest): Option[ElevatorAndStatus] = 
        def canPickup(es: ElevatorStatus): Boolean = 
            es match
                case e@ElevatorStatus(currentFloor, Seq(s, _*), None) if e.direction.get == r.direction =>
                    if r.direction == DOWN then
                        currentFloor > r.floor
                    else
                        currentFloor < r.floor
                case _ => false                    
        state
            .find((_, es) => canPickup(es))
            .map((e, es) => e -> es.addDestination(r.floor))