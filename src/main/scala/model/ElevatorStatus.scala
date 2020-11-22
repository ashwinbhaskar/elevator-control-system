package model

import adt._
import types._

final case class ElevatorStatus(currentFloor: Floor, destinationDropFloors: Seq[Floor], pickup: Option[PickupAndDirection]):

    if(!ElevatorStatus.isValid(destinationDropFloors, pickup))
        throw new IllegalStateException("not possible")

    private def intToDirection(i: Int): Direction = 
        if(i > 0)
            Direction.UP
        else
            Direction.DOWN

    private def destinationDropFloorsDirection: Option[Direction] = 
        destinationDropFloors
            .headOption
            .map(f => intToDirection(f - currentFloor))
    
    private def pickupAndThenGoToDirection: Option[Direction] = 
        pickup
            .map((_, direction) => direction)

    def direction: Option[Direction] = destinationDropFloorsDirection.orElse(pickupAndThenGoToDirection)
    
    def isStationary: Boolean = direction.isEmpty

    private def sort(fs: Seq[Floor], direction: Direction): Seq[Floor] =
        if(direction == Direction.UP)
            fs.sortAsc
        else
            fs.sortDesc

    def addDestination(floor: Floor): ElevatorStatus = 
        direction
            .map(d => sort(destinationDropFloors :+ floor, d))
            .map(ddf => copy(destinationDropFloors = ddf))
            .getOrElse(copy(destinationDropFloors = Seq(floor)))

object ElevatorStatus:
    def isValid(destinationDropFloors: Seq[Floor], pickup: Option[PickupAndDirection]): Boolean = 
        true//!(destinationDropFloors.nonEmpty && pickup.isDefined)
    def apply(currentFloor: Floor, 
             destinationDropFloors: Seq[Floor],
             pickup: Option[PickupAndDirection]): Option[ElevatorStatus] = 
        Option.when(isValid(destinationDropFloors, pickup))(new ElevatorStatus(currentFloor, destinationDropFloors, pickup))
    
    def unsafe(currentFloor: Floor, 
             destinationDropFloors: Seq[Floor],
             pickup: Option[PickupAndDirection]): ElevatorStatus = 
        new ElevatorStatus(currentFloor, destinationDropFloors, pickup)