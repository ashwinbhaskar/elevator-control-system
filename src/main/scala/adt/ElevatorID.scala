package adt

opaque type ElevatorID = Int

object ElevatorID:
    def apply(id: Int): Option[ElevatorID] = Option.when(id > 0 && id <= 16)(id)
