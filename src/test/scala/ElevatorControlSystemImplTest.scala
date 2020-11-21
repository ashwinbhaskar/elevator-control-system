import org.junit.Test
import org.junit.Assert._

import adt._
import model._
import adt.Direction._

class ElevatorControlSystemImplTest {
  @Test def pickupStationaryElevatorTest: Unit = {
    val f1 = Floor.unsafe(0)
    val f2 = Floor.unsafe(0)
    val elevator1 = Elevator.unsafe(1)
    val elevator2 = Elevator.unsafe(2)
    val id1GoalFloor = Floor.unsafe(2)
    
    val elevator1InitialStatus = ElevatorStatus.unsafe(f1, Seq(id1GoalFloor), None)
    val elevator2InitialStatus = ElevatorStatus.unsafe(f2, Seq.empty[Floor], None)

    val initialState = Map(elevator1 -> elevator1InitialStatus, elevator2 -> elevator2InitialStatus)
    val controlSystem = new ElevatorControlSystemImpl(initialState)

    val pickupRequest1 = Floor.unsafe(5) ->  DOWN
    val elevatorID = controlSystem.request(pickupRequest1)
    assertEquals(elevator2, elevatorID) 

    val expectedElevator2Status = elevator2 -> ElevatorStatus.unsafe(f2, Seq.empty[Floor], Some(Floor.unsafe(5) -> DOWN))
    assertEquals(controlSystem.status, Map(elevator1 -> elevator1InitialStatus, expectedElevator2Status))
  }

  
}