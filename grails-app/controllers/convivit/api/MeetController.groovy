package convivit.api


import grails.rest.*
import grails.converters.*

class MeetController extends RestfulController {
    static responseFormats = ['json', 'xml']
    VotingService votingService

    MeetController() {
        super(Meet)
    }

    def index(Long consorcioId) {
      def consorcio = Consorcio.get(consorcioId)
      def meets = consorcio.meets
      respond([meetList: meets])
    }

    def vote(Long id, Long roleId) {
      try {
        def myValue = request.JSON.vote
        def vote = votingService.voteMeet(id, roleId, myValue)
        respond(vote)
      } catch (Exception e) {
        render( status: 403, contentType: "text/json"){
            error e.message
        }
      }
    }
}
