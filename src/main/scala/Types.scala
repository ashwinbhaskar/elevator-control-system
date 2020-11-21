package types

import adt._
import model._

type PickupAndDirection = (Floor, Direction)

type PickupRequest = PickupAndDirection

type ElevatorAndStatus = (Elevator, ElevatorStatus)

extension (p: PickupRequest):
    def floor: Floor = p._1
    def direction: Direction = p._2