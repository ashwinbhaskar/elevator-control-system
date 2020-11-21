import model._
import adt._

trait ElevatorControlSystem:
    def status: Seq[ElevatorStatus]
    def request(pickUp: PickupRequest): Unit | Error
    def request(drop: DropRequest): Unit | Error
    def step: Unit
