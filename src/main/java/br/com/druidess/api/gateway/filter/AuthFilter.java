package br.com.druidess.api.gateway.filter;

import javax.ws.rs.core.HttpHeaders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import br.com.druidess.api.gateway.ValidateTokenDtoResponse;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

	private static final Logger LOG = LoggerFactory.getLogger(AuthFilter.class);

	private final WebClient.Builder webClientBuilder;

	public AuthFilter(WebClient.Builder webClientBuilder) {
		super(Config.class);
		this.webClientBuilder = webClientBuilder;
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
				throw new RuntimeException("Missing auth information");
			}

			String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
			String[] parts = authHeader.split(" ");
			if (parts.length != 2 || !"Bearer".equals(parts[0])) {
				throw new RuntimeException("Incorrect auth structure");
			}

			return webClientBuilder.build().post().uri("http://jwt-server/auth/validateToken?token=" + parts[1])
					.retrieve().bodyToMono(ValidateTokenDtoResponse.class).map(validateTokenDtoResponse -> {

						System.err.println(validateTokenDtoResponse.getUserDto());

						if (validateTokenDtoResponse.getUserDto() != null) {
							exchange.getRequest().mutate().header("x-auth-user-id",
									String.valueOf(validateTokenDtoResponse.getUserDto().getId()));
						}

						return exchange;
					}).flatMap(chain::filter);
		};
	}

	@ExceptionHandler(WebClientResponseException.class)
	public ResponseEntity<String> handleWebClientResponseException(WebClientResponseException ex) {
		LOG.error("Error from WebClient - Status {}, Body {}", ex.getRawStatusCode(), ex.getResponseBodyAsString(), ex);
		return ResponseEntity.status(ex.getRawStatusCode()).body(ex.getResponseBodyAsString());
	}

	public static class Config {

	}

}
