package de.thm.mow2gamecollection.sudoku.model

class Item {
    var name: String? = null
    var image: Int? = null

    constructor(name: String, image:Int){
        this.name = name
        this.image = image
    }
}