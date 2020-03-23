package bzh.redge.keycloak;

import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApisController {

    @RequestMapping(path = {"/", "/unsecured"})
    public String noSecuredEndpoint(){
        return "This is an unsecured endpoint payload";
    }

    @RequestMapping(
            path = "/admin",
            method = RequestMethod.GET, // @RequestMapping default assignment
            produces = MediaType.APPLICATION_JSON_VALUE // TIP : use org.springframework.http.MediaType for MimeType instead of hard coded value
    )
    @Secured({"ROLE_ADMIN"})
    public String adminSecuredEndpoint(){
        return "This is an ADMIN endpoint payload";
    }

    @RequestMapping(
            path = "/user",
            method = RequestMethod.GET, // @RequestMapping default assignment
            produces = MediaType.APPLICATION_JSON_VALUE // TIP : use org.springframework.http.MediaType for MimeType instead of hard coded value
    )
    @Secured({"ROLE_VIEWER"})
    public String userSecuredEndpoint(){
        return "This is an USER endpoint payload";
    }

}
