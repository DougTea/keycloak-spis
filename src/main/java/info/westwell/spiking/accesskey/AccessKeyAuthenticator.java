package info.westwell.spiking.accesskey;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.CredentialValidator;
import org.keycloak.events.Errors;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.OAuth2ErrorRepresentation;
import org.keycloak.utils.MediaType;

import info.westwell.spiking.accesskey.credential.AccessKeyCredentialProvider;

public class AccessKeyAuthenticator implements Authenticator, CredentialValidator<AccessKeyCredentialProvider> {

    @Override
    public void close() {
    }

    @Override
    public AccessKeyCredentialProvider getCredentialProvider(KeycloakSession session) {
        return new AccessKeyCredentialProvider(session);
    }

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        String inputeAccessKeyStr = retrieveAccessKey(context);
        if (inputeAccessKeyStr != null) {
            UserCredentialModel input = new UserCredentialModel(
                    getCredentialProvider(context.getSession())
                            .getDefaultCredential(
                                    context.getSession(),
                                    context.getRealm(),
                                    context.getUser())
                            .getId(),
                    getType(context.getSession()), inputeAccessKeyStr);
            if (getCredentialProvider(context.getSession())
                    .isValid(context.getRealm(), context.getUser(), input)) {
                    context.success();
                    return;
            }
        }
        if (context.getUser() != null) {
            context.getEvent().user(context.getUser());
        }
        context.getEvent().error(Errors.INVALID_USER_CREDENTIALS);
        Response challengeResponse = errorResponse(Response.Status.UNAUTHORIZED.getStatusCode(), "invalid_grant",
                "Invalid user credentials");
        context.failure(AuthenticationFlowError.INVALID_USER, challengeResponse);
    }

    public Response errorResponse(int status, String error, String errorDescription) {
        OAuth2ErrorRepresentation errorRep = new OAuth2ErrorRepresentation(error, errorDescription);
        return Response.status(status).entity(errorRep).type(MediaType.APPLICATION_JSON_TYPE).build();
    }

    @Override
    public void action(AuthenticationFlowContext context) {
    }

    @Override
    public boolean requiresUser() {
        return true;
    }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        return getCredentialProvider(session).isConfiguredFor(realm, user);
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
    }

    protected String retrieveAccessKey(AuthenticationFlowContext context) {
        MultivaluedMap<String, String> inputData = context.getHttpRequest().getDecodedFormParameters();
        return inputData.getFirst(CredentialRepresentation.PASSWORD);
    }

}
