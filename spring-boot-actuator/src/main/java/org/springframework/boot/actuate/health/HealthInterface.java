package org.springframework.boot.actuate.health;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

public interface HealthInterface {

	/**
	 * Return the status of the health.
	 * @return the status (never {@code null})
	 */
	Status getStatus();

	/**
	 * Return the details of the health.
	 * @return the details (or an empty map)
	 */
	Map<String, Object> getDetails();

	boolean equals(Object obj);

	int hashCode();

	String toString();

}