package pl.selfcloud.security.api.util;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SecurityConstants {

  HEADER("HEADER"), ISSUER("ISSUER"), KEY("KEY"), PREFIX("Bearer"), AUTHORIZATION("AUTHORIZATION");

  final String value;

}
