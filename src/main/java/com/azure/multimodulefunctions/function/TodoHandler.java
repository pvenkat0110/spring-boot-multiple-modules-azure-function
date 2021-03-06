package com.azure.multimodulefunctions.function;

import java.util.Optional;

import org.springframework.cloud.function.adapter.azure.AzureSpringBootRequestHandler;

import com.azure.multimodulefunctions.model.Greeting;
import com.azure.multimodulefunctions.model.User;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

public class TodoHandler  extends AzureSpringBootRequestHandler<User, Greeting> {

	    @FunctionName("hello")
	    public HttpResponseMessage execute(
	            @HttpTrigger(name = "request", methods = {HttpMethod.GET, HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<User>> request,
	            ExecutionContext context) {
	        User user = request.getBody()
	                .filter((u -> u.getName() != null))
	                .orElseGet(() -> new User(
	                        null, request.getQueryParameters()
	                                .getOrDefault("name", "world")));
	        context.getLogger().info("Greeting user name: " + user.getName());
	        return request
	                .createResponseBuilder(HttpStatus.OK)
	                .body(handleRequest(user, context))
	                .header("Content-Type", "application/json")
	                .build();
	    }
}
