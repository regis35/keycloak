package bzh.redge.keycloak;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.OidcKeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.IDToken;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
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

        log.info("realm : " + getSecurityContext().getRealm());

        IDToken token = getSecurityContext().getIdToken();

        log.info("myEmail : " + token.getEmail());
        log.info("myId : " + token.getId());

        return "This is an USER endpoint payload";
    }

    private KeycloakSecurityContext getSecurityContext() {
        ServletRequestAttributes requestAttributes = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes());
        HttpServletRequest request = requestAttributes.getRequest();

        KeycloakAuthenticationToken authenticationToken = (KeycloakAuthenticationToken) request.getUserPrincipal();
        OidcKeycloakAccount oidcKeycloakAccount = authenticationToken.getAccount();
        return oidcKeycloakAccount.getKeycloakSecurityContext();
    }

    private AccessToken getAccessToken() {
        KeycloakSecurityContext securityContext = getSecurityContext();
        return securityContext.getToken();
    }
}
