package convivit.api


import grails.rest.*
import grails.converters.*

class MeetController extends RestfulController {
    static responseFormats = ['json', 'xml']
    def VotingService

    MeetController() {
        super(Meet)
    }

    def index(Long consorcioId) {
      def consorcio = Consorcio.get(consorcioId)
      def meets = consorcio.meets
      respond([meetList: meets])
    }

    // def show(Long id) {
    //   println "Meet id: " + id
    //   def meet = Meet.get(id)
    //   println "Meet found: " + meet
    //   respond(meet)
    // }

    def vote(Long meetId, Long userRoleId) {
      def vote = VotingService.voteMeet(meetId, userRoleId)
      respond(vote)
    }
}
