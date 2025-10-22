package se.csn.applift.rest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import se.csn.applift.services.StringService;

@Path("/hello")
@RequestScoped
public class HelloResource {

    private static final Logger logger = Logger.getLogger(HelloResource.class.getName());

    @Context
    private HttpServletRequest request;

    @Inject
    private StringService stringService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHello() {
        logger.info("Processing GET /hello request");
        
        // Get application name using @WithSpan annotated method
        String applicationName = getApplicationName();
        
        // Get the current OpenTelemetry span that MicroProfile Telemetry automatically creates
        Span currentSpan = Span.current();
        String traceId = "";
        String spanId = "";
        
        if (currentSpan != null && currentSpan.getSpanContext().isValid()) {
            SpanContext spanContext = currentSpan.getSpanContext();
            traceId = spanContext.getTraceId();
            spanId = spanContext.getSpanId();
            
            // Add custom attributes to the current span
            currentSpan.setAttribute("application.name", applicationName);
            currentSpan.setAttribute("endpoint", "/hello");
            currentSpan.setAttribute("http.method", "GET");
        } else {
            // Fallback to manual generation if no active span (shouldn't happen with mpTelemetry-2.0)
            logger.warning("No active trace or span found. Shouldn't happen with mpTelemetry-2.0");
            traceId = "----- IGNORE ---";
            spanId = "----- IGNORE ---";
        }
        
        logger.info(String.format("Processing request with traceId: %s, spanId: %s", traceId, spanId));
        
        JsonObject jsonResponse = Json.createObjectBuilder()
            .add("application", applicationName)
            .add("message", "Hello")
            .add("traceId", traceId)
            .add("spanId", spanId)
            .build();
        
        logger.info("Successfully processed GET /hello request");
        return Response.ok(jsonResponse).build();
    }

    @WithSpan("Get Application Name")
    private String getApplicationName() {
        logger.info("Getting application name");
        String contextPath = request.getContextPath();
        String applicationName = contextPath.startsWith("/") ? contextPath.substring(1) : contextPath;
        return applicationName.isEmpty() ? "base-template-shift" : applicationName;
    }

    @POST
    @Path("/count")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @WithSpan("Count Vowels and Consonants")
    public Response countVowelsAndConsonants(String input) {
        logger.info("Processing POST /hello/count request");

        // Input validation
        if (input == null) {
            input = "";
        }

        // Get current span and add attributes
        Span currentSpan = Span.current();
        if (currentSpan.getSpanContext().isValid()) {
            currentSpan.setAttribute("input.length", input.length());
            currentSpan.setAttribute("input.empty", input.isEmpty());
        }

        // Use the service to count vowels and consonants
        int[] counts = stringService.countVowelsAndConsonants(input.toLowerCase());
        int vowels = counts[0];
        int consonants = counts[1];

        // Add results to span
        if (currentSpan.getSpanContext().isValid()) {
            currentSpan.setAttribute("result.vowels", vowels);
            currentSpan.setAttribute("result.consonants", consonants);
        }

        JsonObject jsonResponse = Json.createObjectBuilder()
            .add("input", input)
            .add("vowels", vowels)
            .add("consonants", consonants)
            .build();

        logger.info(String.format("Counted vowels: %d, consonants: %d for input length: %d", 
                                  vowels, consonants, input.length()));
        return Response.ok(jsonResponse).build();
    }

}