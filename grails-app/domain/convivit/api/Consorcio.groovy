package convivit.api

class Consorcio {
    String name
    String address
    // Building, Country
    String type

    Set units = []
    Set meets = []

    static belongsTo = [admin: UserRole]
    static hasMany = [
      units: Unit,
      meets: Meet
    ]
    static constraints = {
    }

    Unit addUnit(Unit unit) {
        this.addToUnits(unit)
        return unit
    }
}
