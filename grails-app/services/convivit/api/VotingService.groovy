package convivit.api

import grails.gorm.transactions.Transactional

@Transactional
class VotingService {

    Vote voteMeet(Long meetId, Long roleId, Boolean value) {
      def role = UserRole.get(roleId)
      def meet = Meet.get(meetId)
      Vote vote = role.vote(meet, value)
      return vote
    }
}
