import org.junit.Test
import org.junit.Assert._

import adt._
import model._
import adt.Direction._
import types._

class ElevatorControlSystemImplTest {

  private def f(floor: Int): Floor = Floor.unsafe(floor)
  private def e(n: Int): Elevator = Elevator.unsafe(n)
  private def es(f: Floor, fs: Seq[Floor], p: Option[PickupAndDirection]): ElevatorStatus = 
    ElevatorStatus.unsafe(f, fs, p)


  @Test def stationaryElevatorTest: Unit = {
    val f1 = f(0)
    val f2 = f(0)
    val elevator1 = e(1)
    val elevator2 = e(2)
    val id1GoalFloor = f(2)
    
    val elevator1InitialStatus = es(f1, Seq(id1GoalFloor), None)
    val elevator2InitialStatus = es(f2, Seq.empty[Floor], None)

    val initialState = Map(elevator1 -> elevator1InitialStatus, elevator2 -> elevator2InitialStatus)
    val controlSystem = new ElevatorControlSystemImpl(initialState)

    val pickupRequest1 = f(5) ->  DOWN
    val elevator = controlSystem.request(pickupRequest1)
    assertEquals(elevator2, elevator) 

    val expectedElevator2Status = elevator2 -> es(f2, Seq.empty[Floor], Some(Floor.unsafe(5) -> DOWN))
    assertEquals(controlSystem.status, Map(elevator1 -> elevator1InitialStatus, expectedElevator2Status))
  }

  @Test def elevatorWithPickupDirectionTest: Unit = {
    val e1AndStatus = e(1) -> es(f(0), Seq(f(5),f(6)), None)
    val e2AndStatus = e(2) -> es(f(5), Seq.empty[Floor], Some(f(2) -> DOWN))
    val e3AndStatus = e(3) -> es(f(1), Seq(), Some(f(4) -> DOWN))

    val initialState = Map(e1AndStatus, e2AndStatus, e3AndStatus)
    val controlSystem = new ElevatorControlSystemImpl(initialState)
    
    val pickupRequest1 = f(3) -> DOWN
    val elevator = controlSystem.request(pickupRequest1)
    assertEquals(elevator, e(3))

    val expectedElevator3Status = e(3) -> es(f(1), Seq(f(3)), Some(f(4) -> DOWN))
    assertEquals(controlSystem.status, initialState + expectedElevator3Status)
  }

  
}