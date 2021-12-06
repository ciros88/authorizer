package com.ciros.authorizer.interceptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.ciros.authorizer.annotation.Authorize;
import com.ciros.authorizer.model.AuthorizationHeaderDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Ciro Scognamiglio
 */

@Slf4j
@Aspect
@Component
public class AuthorizeInterceptor {

	private static final String INVALID_EXPIRED_AUTH_ERROR = "Access denied: invalid or expired authorization";

	private HttpServletRequest request;

	public AuthorizeInterceptor(HttpServletRequest request) {
		this.request = request;
	}

	@Around("@annotation(com.ciros.authorizer.annotation.Authorize)")
	public Object authorize(final ProceedingJoinPoint joinPoint) throws Throwable {

		final String authorizationHeaderJson = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (authorizationHeaderJson == null || authorizationHeaderJson.isBlank()) {
			log.warn("Missing or blank authorization header for request: {}", request.getRequestURI());
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, INVALID_EXPIRED_AUTH_ERROR);
		}

		log.info("Authorization header provided: {}", authorizationHeaderJson);

		final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		final Method method = methodSignature.getMethod();
		String requiredPrincipal = method.getAnnotation(Authorize.class).requiredPrincipal();

		final boolean hasRequiredPrincipal = !requiredPrincipal.isEmpty();

		if (hasRequiredPrincipal && requiredPrincipal.isBlank()) {
			log.warn("Required principal is blank");
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, INVALID_EXPIRED_AUTH_ERROR);
		}

		if (hasRequiredPrincipal) {
			if (requiredPrincipal.startsWith("#")) {
				log.info("Required principal expression: {}", requiredPrincipal);
				Object requiredPrincipalArg;
				try {
					requiredPrincipalArg = getRequiredPrincipalFromExpression(requiredPrincipal, joinPoint,
							methodSignature);
				} catch (IllegalArgumentException e) {
					log.warn("Unable to process provided expression: {}", e.getMessage());
					throw new ResponseStatusException(HttpStatus.FORBIDDEN, INVALID_EXPIRED_AUTH_ERROR);
				}
				if (requiredPrincipalArg.toString().isBlank()) {
					log.warn("Required principal is blank");
					throw new ResponseStatusException(HttpStatus.FORBIDDEN, INVALID_EXPIRED_AUTH_ERROR);
				}
				requiredPrincipal = requiredPrincipalArg.toString();
			}
			log.info("Required principal: {}", requiredPrincipal);
		} else
			log.info("Required principal not provided");

		final String[] requiredAuthorities = method.getAnnotation(Authorize.class).requiredAuthorities();

		if (requiredAuthorities.length == 0) {
			log.warn("Required authorities are missing");
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, INVALID_EXPIRED_AUTH_ERROR);
		}

		for (String requiredAuthority : requiredAuthorities) {
			if (requiredAuthority.isBlank()) {
				log.warn("One or more required authorities are blank");
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, INVALID_EXPIRED_AUTH_ERROR);
			}
		}

		log.info("Required authorities: {}", (Object) requiredAuthorities);

		AuthorizationHeaderDto authorizationHeaderDto;
		final ObjectMapper mapper = new ObjectMapper();

		try {
			authorizationHeaderDto = mapper.readValue(authorizationHeaderJson, AuthorizationHeaderDto.class);
		} catch (JsonProcessingException e) {
			log.warn("Unable to parse/decode/map authorization header: {}", e.getMessage());
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, INVALID_EXPIRED_AUTH_ERROR);
		}

		if (hasRequiredPrincipal) {

			final String principalClaimed = authorizationHeaderDto.getClaimedPrincipal();

			if (principalClaimed.isBlank()) {
				log.warn("Invalid authorization header: claimed principal is missing or blank");
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, INVALID_EXPIRED_AUTH_ERROR);
			}

			log.info("Claimed principal: {}", principalClaimed);

			if (!principalClaimed.equals(requiredPrincipal)) {
				log.warn("Required principal <-> Claimed principal mismatch");
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, INVALID_EXPIRED_AUTH_ERROR);
			}
		}

		final Set<String> authoritiesClaimed = authorizationHeaderDto.getClaimedAuthorities();
		authoritiesClaimed.remove("");

		if (authoritiesClaimed.isEmpty()) {
			log.warn("Invalid authorization header: missing claimed authorities");
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, INVALID_EXPIRED_AUTH_ERROR);
		}

		log.info("Claimed authorities: {}", authoritiesClaimed);

		final boolean matchingAllAuthorities = method.getAnnotation(Authorize.class).matchingAllRequiredAuthorities();

		log.info("Authorities matching policy: {}", matchingAllAuthorities ? "all" : "any");

		if (matchingAllAuthorities) {
			for (String requiredAuthority : requiredAuthorities) {
				if (!authoritiesClaimed.contains(requiredAuthority)) {
					log.warn("One or more required authorities not found among those claimed");
					throw new ResponseStatusException(HttpStatus.FORBIDDEN, INVALID_EXPIRED_AUTH_ERROR);
				}
			}
		} else {
			boolean hasMatched = false;
			for (String requiredAuthority : requiredAuthorities) {
				if (authoritiesClaimed.contains(requiredAuthority)) {
					hasMatched = true;
					break;
				}
			}
			if (!hasMatched) {
				log.warn("None of the required authorities found among those claimed");
				throw new ResponseStatusException(HttpStatus.FORBIDDEN, INVALID_EXPIRED_AUTH_ERROR);
			}
		}

		log.info("Successful authorization");

		return joinPoint.proceed();
	}

	private Object getRequiredPrincipalFromExpression(final String expression, final ProceedingJoinPoint joinPoint,
			MethodSignature methodSignature) throws IllegalArgumentException {

		final String[] expressionAttributes = parseExpressionGetAttributes(expression);

		if (expressionAttributes.length == 0)
			throw new IllegalArgumentException("Invalid expression");

		final String[] parameterNames = methodSignature.getParameterNames();

		int parameterPosition = -1;

		for (int i = 0; i < parameterNames.length; i++) {
			if (parameterNames[i].equals(expressionAttributes[0]))
				parameterPosition = i;
		}

		if (parameterPosition == -1)
			throw new IllegalArgumentException(String.format("Param '%s' not found", expressionAttributes[0]));

		Object requiredPrincipalArg = joinPoint.getArgs()[parameterPosition];

		if (requiredPrincipalArg == null || requiredPrincipalArg.toString().isBlank())
			throw new IllegalArgumentException("Required principal is null or blank");

		if (expressionAttributes.length < 2)
			return requiredPrincipalArg;

		for (int i = 1; i < expressionAttributes.length; i++) {
			requiredPrincipalArg = getValueByGetterInvocation(requiredPrincipalArg, expressionAttributes[i]);
			if (requiredPrincipalArg == null)
				throw new IllegalArgumentException("Required principal is null");
		}
		return requiredPrincipalArg;
	}

	private Object getValueByGetterInvocation(final Object obj, final String argName) throws IllegalArgumentException {

		try {

			final Method method = obj.getClass()
					.getMethod("get" + argName.substring(0, 1).toUpperCase() + argName.substring(1));

			return method.invoke(obj);

		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(
					String.format("Method '%s' does not exists or is not public", e.getMessage()));
		} catch (SecurityException | IllegalAccessException | InvocationTargetException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	private String[] parseExpressionGetAttributes(String expression) {

		expression = expression.substring(1);

		if (expression.endsWith("."))
			return new String[0];

		final String[] splittedExpression = expression.split("\\.");

		if (splittedExpression.length == 0)
			return new String[0];

		for (String element : splittedExpression)
			if (element.isBlank())
				return new String[0];

		return splittedExpression;
	}
}
