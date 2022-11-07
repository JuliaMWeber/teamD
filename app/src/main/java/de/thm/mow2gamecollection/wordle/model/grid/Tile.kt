package de.thm.mow2gamecollection.wordle.model.grid

class Tile(val position: Position) {
    constructor(row: Int, index: Int) : this(Position(row, index))
}