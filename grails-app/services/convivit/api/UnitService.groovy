package convivit.api

import grails.gorm.transactions.Transactional

@Transactional
class UnitService {

    UserRole addRole(Long unitId, Long userId, String role, Boolean authorization) {
        Unit unit = Unit.findById(unitId)
        User user = User.findById(userId)

        UserRole userRole = unit.addRole(user, role, authorization)
        return userRole
    }
}
