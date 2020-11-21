import model._
import adt._

class ElevatorControlSystemImpl(state: Set[ElevatorStatus]) extends ElevatorControlSystem:
    override def status: Seq[ElevatorStatus] = state.toSeq
    override def request(pickUp: PickupRequest): Unit | Error = ???
    override def request(drop: DropRequest): Unit | Error = ???
    override def step: Unit = ???

