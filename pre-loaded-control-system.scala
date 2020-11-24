import adt._
import model._
import adt.Direction._
import adt.Error._
import types._

def f(floor: Int): Floor = Floor.unsafe(floor)
def e(n: Int): Elevator = Elevator.unsafe(n)
def es(f: Floor, fs: Seq[Floor], p: Option[PickupAndDirection]): ElevatorStatus = 
    ElevatorStatus.unsafe(f, fs, p)
val e1AndStatus = e(1) -> es(f(4), Seq(f(5),f(6)), None)
val e2AndStatus = e(2) -> es(f(5), Seq.empty[Floor], Some(f(2) -> DOWN))
val e3AndStatus = e(3) -> es(f(1), Seq(), Some(f(4) -> DOWN))

val initialState = Map(e1AndStatus, e2AndStatus, e3AndStatus)
val controlSystem = new ElevatorControlSystemImpl(initialState)