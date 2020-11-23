package model

import adt._
import types._
import adt.Direction._

final case class ElevatorStatus(currentFloor: Floor, destinationDropFloors: Seq[Floor], pickup: Option[PickupAndDirection]):

    if(!ElevatorStatus.isValid(destinationDropFloors, pickup))
        throw new IllegalStateException("not possible")

    private def deriveDirection(from: Floor, to: Floor): Option[Direction] =
        if to - from > 0 then
            Some(UP)
        else if to - from < 0 then
            Some(DOWN)
        else
            None
        
    private def stepFloor(direction: Direction): Floor = 
        if direction == UP then
            currentFloor + 1
        else
            currentFloor - 1


    private def destinationDropFloorsDirection: Option[Direction] = 
        destinationDropFloors
            .headOption
            .flatMap(f => deriveDirection(currentFloor, f))
    
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

    private def directionBeforePickup: Option[Direction] = 
        pickup
            .flatMap((f, _) => deriveDirection(currentFloor, f))

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
            val d = directionBeforePickup.get                  //confident .get as pick up being defined means direction exists
            val updatedCurrentFloor = stepFloor(d) 
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