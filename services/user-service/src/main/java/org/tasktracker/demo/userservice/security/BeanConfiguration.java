package org.tasktracker.demo.userservice.security;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Author: Artyom Aroyan
 * Date: 19.11.25
 * Time: 00:28:11
 */
@Configuration
@EnableConfigurationProperties(JwtProperties.class)
class BeanConfiguration {
}