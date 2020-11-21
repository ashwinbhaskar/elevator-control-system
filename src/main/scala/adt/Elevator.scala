package adt

opaque type Elevator = Int

object Elevator:
    private def isValid(id: Int): Boolean = 
        id > 0 && id <= 16
    def apply(id: Int): Option[Elevator] = Option.when(isValid(id))(id)
    def unsafe(id: Int): Elevator = 
        if(isValid(id))
            id
        else
            throw new IllegalArgumentException("id should be between 1 and 16")
