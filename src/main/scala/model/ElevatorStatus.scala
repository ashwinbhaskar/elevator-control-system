package model

import adt._
import types._
import adt.Direction._

final case class ElevatorStatus(currentFloor: Floor, destinationDropFloors: Seq[Floor], pickup: Option[PickupAndDirection]):

    if(!ElevatorStatus.isValid(destinationDropFloors, pickup))
        throw new IllegalStateException("not possible")

    private def intToDirection(i: Int): Direction = 
        if i > 0 then
            Direction.UP
        else
            Direction.DOWN
        
    private def stepFloor(direction: Direction): Floor = 
        if direction == UP then
            currentFloor + 1
        else
            currentFloor - 1


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
    
    def removeDestination(floor: Floor): ElevatorStatus = 
        if destinationDropFloors.isEmpty then
            this
        else
            copy(destinationDropFloors = destinationDropFloors.filterNot(_ == floor))
    
    def step: ElevatorStatus = 
        if pickup.isDefined then
            val (pickupFloor, _) = pickup.get
            val updatedCurrentFloor = stepFloor(direction.get) //confident .get as pick up being defined means direction exists
            if pickupFloor == updatedCurrentFloor then
                copy(currentFloor = updatedCurrentFloor, pickup = None)
            else
                copy(currentFloor = updatedCurrentFloor)
        else if destinationDropFloors.nonEmpty then
            val updatedCurrentFloor = stepFloor(direction.get) //confident .get as pick up being defined means direction exists
            removeDestination(updatedCurrentFloor).copy(currentFloor = updatedCurrentFloor)
        else
            this


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