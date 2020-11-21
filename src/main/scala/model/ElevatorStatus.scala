package model

import adt._

final case class ElevatorStatus(elevatorId: ElevatorID, currentFloor: Floor, goalFloor: Option[Floor])