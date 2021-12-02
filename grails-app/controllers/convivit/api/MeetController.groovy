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

    def vote(Long id, Long roleId, Boolean v) {
      def myValue = request.JSON.vote
      def vote = VotingService.voteMeet(id, roleId, myValue)
      respond(vote)
    }
}
