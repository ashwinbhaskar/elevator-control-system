package adt

import scala.annotation.infix

opaque type Floor = Int

object Floor:
    private def isValid(i: Int): Boolean = 
        i <= 10 && i >= 0

    def apply(f: Int): Option[Floor] = Option.when(isValid(f))(f)

    def unsafe(f: Int): Floor = 
        if(isValid(f))
            f
        else
            throw new IllegalArgumentException("floor should be between 0 and 10")

extension (f: Floor):
    def <(o: Floor): Boolean = f < o
    def ==(o: Floor): Boolean = f == o
    def >(o: Floor): Boolean = f > o
    def -(o: Floor): Floor = f - o
    def +(o: Floor): Floor = f + o

extension (fs: Seq[Floor]):
    def sortAsc: Seq[Floor] = fs.sorted
    def sortDesc: Seq[Floor] = sortAsc.reverse

