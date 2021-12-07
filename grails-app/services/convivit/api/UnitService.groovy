package convivit.api

import grails.gorm.transactions.Transactional
import java.time.LocalDate
@Transactional
class UnitService {

    UserRole addRole(Long unitId, Long userId, String role, Boolean authorization) {
        Unit unit = Unit.findById(unitId)
        User user = User.findById(userId)
        println "User: ${user} - Unidad: ${unit} => params: ${role}, ${authorization}"
        UserRole userRole = unit.addRole(user, role, authorization)
        return userRole
    }
}