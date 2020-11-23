import org.junit.Test
import org.junit.Assert._

import adt._
import model._
import adt.Direction._
import adt.Error._
import types._

class ElevatorStatusTest {
  private def f(floor: Int): Floor = Floor.unsafe(floor)
  private def es(f: Floor, fs: Seq[Floor], p: Option[PickupAndDirection]): ElevatorStatus = 
    ElevatorStatus.unsafe(f, fs, p)  
  
  @Test def stepTest: Unit = {
    val status1 = es(f(0), Seq(f(5),f(6)), None)
    val status1Step = status1.step
    assertEquals(es(f(1), Seq(f(5), f(6)), None), status1Step)

    val status2 = es(f(5), Seq.empty[Floor], Some(f(2) -> DOWN))
    val status2Step = status2.step
    assertEquals(es(f(4), Seq.empty[Floor], Some(f(2) -> DOWN)), status2Step)

    val status3 = es(f(1), Seq(), Some(f(4) -> DOWN))
    val status3Step = status3.step
    assertEquals(es(f(2), Seq(), Some(f(4) -> DOWN)), status3Step)

    val status4 = es(f(5), Seq(), None)
    val status4Step = status4.step
    assertEquals(es(f(5), Seq(), None), status4Step)

    val status5 = es(f(5), Seq(), Some(f(4) -> UP))
    val status5Step = status5.step
    assertEquals(es(f(4), Seq(), None), status5Step)

    val status6 = es(f(6),  Seq(f(5), f(4)), None)
    val status6Step = status6.step
    assertEquals(es(f(5), Seq(f(4)), None), status6Step)
  }
}
