package adt

opaque type Floor = Int

object Floor:
    def apply(f: Int): Option[Floor] = Option.when(f <= 10 && f >= 0)(f)
