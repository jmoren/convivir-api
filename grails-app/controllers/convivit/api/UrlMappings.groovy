package convivit.api

class UrlMappings {

    static mappings = {
        post "/user/login"(controller: "user", action: "login")
        post "/user/$roleId/vote/$meetId"(controller: "userRole", action: "vote")
        post "/user/$roleId/invite"(controller: "userRole", action: "invite")

        post "/invitation/$invitationId/move"(controller: "invitation", action: "move")
        post "/invitation/$invitationId/extend"(controller: "invitation", action: "extend")

        delete "/$controller/$id(.$format)?"(action:"delete")
        get "/$controller(.$format)?"(action:"index")
        get "/$controller/$id(.$format)?"(action:"show")
        post "/$controller(.$format)?"(action:"save")
        put "/$controller/$id(.$format)?"(action:"update")
        patch "/$controller/$id(.$format)?"(action:"patch")

        "/"(controller: 'application', action:'index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
