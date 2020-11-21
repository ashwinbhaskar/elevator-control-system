package model

import adt._

final case class DropRequest(floor: Floor, elevatorId: ElevatorID)