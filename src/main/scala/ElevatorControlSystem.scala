import model._
import adt._
import types._

trait ElevatorControlSystem:
    def status: Map[Elevator, ElevatorStatus]
    def request(pickUp: PickupRequest): Elevator
    def request(drop: DropRequest): Unit
    def step: Unit
