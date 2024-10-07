package dk.lyngby.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.lyngby.controller.ExceptionController;
import dk.lyngby.exception.ApiException;
import dk.lyngby.routes.Routes;
import dk.lyngby.security.controllers.SecurityController;
import dk.lyngby.security.routes.SecurityRoutes;
import dk.lyngby.security.utils.Utils;
import dk.lyngby.util.ApiProps;
import dk.bugelhartmann.UserDTO;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.security.RouteRole;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class AppConfig
{

    private static final Routes routes = new Routes();
    private static final ExceptionController exceptionController = new ExceptionController();
    private static ObjectMapper jsonMapper = new Utils().getObjectMapper();
    private static SecurityController securityController = SecurityController.getInstance();
    private static Logger logger = LoggerFactory.getLogger(AppConfig.class);

    // == Configuration ==
    private static void configuration(JavalinConfig config)
    {
        config.showJavalinBanner = false;

        // == Server ==
        config.router.contextPath = ApiProps.API_CONTEXT; // Base path for all endpoints

        // == Plugins ==
        config.bundledPlugins.enableRouteOverview("/routes"); // Enable route overview
        config.bundledPlugins.enableDevLogging(); // Enable development logging

        // == Routes ==
        config.router.apiBuilder(routes.getApiRoutes());
        config.router.apiBuilder(SecurityRoutes.getSecuredRoutes());
        config.router.apiBuilder(SecurityRoutes.getSecurityRoutes());
    }

    // == Start Server ==
    // Metoden er tilføjet fra ApplicationConfig for at starte serveren med en bestemt port
    // Den inkluderer også beforeMatched for at kalde accessHandler og kontrollere adgangsrettigheder
    public static Javalin startServer(int port)
    {
        Javalin app = Javalin.create(AppConfig::configuration);
        app.beforeMatched(AppConfig::accessHandler); // Tilføjet for adgangskontrol på alle ruter før de matches
        exceptionHandler(app); // Tilføj undtagelseshåndtering
        app.error(404, ctx -> ctx.json("Not found"));
        app.start(port);
        return app;
    }

    // == Stop Server ==
    // Metoden er allerede i AppConfig og stopper serveren korrekt
    public static void stopServer(Javalin app)
    {
        app.stop();
    }

    // == Exception Handling ==
    // Denne metode håndterer generelle undtagelser samt ApiException, logget og returnerer en JSON-fejlmeddelelse
    private static void exceptionHandler(Javalin app)
    {
        app.exception(ApiException.class, exceptionController::apiExceptionHandler);
        app.exception(Exception.class, (e, ctx) ->
        {
            logger.error("An unhandled exception occurred", e.getMessage());
            ctx.json(Utils.convertErrorToJson(e.getMessage())); // Konverter fejlmeddelelsen til JSON
        });
    }

    // == Access Handler ==
    // Metoden er tilføjet fra ApplicationConfig for at tjekke brugerens rettigheder før ruten håndteres
    // Hvis brugeren ikke er autoriseret, kaster den en ApiException med status 403 (FORBIDDEN)
    private static void accessHandler(Context ctx)
    {
        UserDTO user = ctx.attribute("user"); // Hent brugeren fra konteksten
        Set<RouteRole> allowedRoles = ctx.routeRoles(); // Hent tilladte roller for den aktuelle rute

        // Tjek om brugeren har de nødvendige roller for ruten
        if (!securityController.authorize(user, allowedRoles))
        {
            if (user != null)
            {
                throw new ApiException(HttpStatus.FORBIDDEN.getCode(),
                        "Unauthorized with roles: " + user.getRoles() + ". Needed roles are: " + allowedRoles);
            } else
            {
                throw new ApiException(HttpStatus.FORBIDDEN.getCode(), "You need to log in, dude!"); // Hvis ingen bruger er logget ind
            }
        }
    }
}