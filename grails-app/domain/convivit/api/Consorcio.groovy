package convivit.api

class Consorcio {
    String name
    String address
    String type

    Set units = []
    static belongsTo = [admin: UserRole]
    static hasMany = [
      units: Unit
    ]
    static constraints = {
    }

    Unit addUnit(Unit unit) {
        this.addToUnits(unit)
        return unit
    }
}
