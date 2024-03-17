package org.zad2.auth.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth.admin")
public record SecurityAdminProperties(String username, String password) {
}
