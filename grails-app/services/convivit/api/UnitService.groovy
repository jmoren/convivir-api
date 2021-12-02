package convivit.api

import grails.gorm.transactions.Transactional

@Transactional
class UnitService {

    def findUnits(Long userId) {
      User user = User.get(userId)
      println "USER ID:" + userId
      def roles = user.roles.findAll { role ->
        role.role != 'admin'
      }

      def unitsId = roles.collect { it.unit.id }
      println "Units:" + unitsId
      return unitsId
    }
}
