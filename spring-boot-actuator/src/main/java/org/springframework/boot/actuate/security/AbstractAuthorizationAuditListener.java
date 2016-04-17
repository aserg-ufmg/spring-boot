/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.actuate.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.security.access.event.AbstractAuthorizationEvent;
import org.springframework.security.access.event.AuthenticationCredentialsNotFoundEvent;
import org.springframework.security.access.event.AuthorizationFailureEvent;

/**
 * Abstract {@link ApplicationListener} to expose Spring Security
 * {@link AbstractAuthorizationEvent authorization events} as {@link AuditEvent}s.
 *
 * @author Dave Syer
 * @author Vedran Pavic
 * @since 1.3.0
 */
public abstract class AbstractAuthorizationAuditListener implements
		ApplicationListener<AbstractAuthorizationEvent>, ApplicationEventPublisherAware {

	private ApplicationEventPublisher publisher;

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}

	protected ApplicationEventPublisher getPublisher() {
		return this.publisher;
	}

	protected void publish(AuditEvent event) {
		if (getPublisher() != null) {
			getPublisher().publishEvent(new AuditApplicationEvent(event));
		}
	}

	protected void onAuthenticationCredentialsNotFoundEvent(AuthenticationCredentialsNotFoundEvent event) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("type", event.getCredentialsNotFoundException().getClass().getName());
		data.put("message", event.getCredentialsNotFoundException().getMessage());
		publish(new AuditEvent("<unknown>", "AUTHENTICATION_FAILURE", data));
	}

	protected void onAuthorizationFailureEvent(AuthorizationFailureEvent event) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("type", event.getAccessDeniedException().getClass().getName());
		data.put("message", event.getAccessDeniedException().getMessage());
		publish(new AuditEvent(event.getAuthentication().getName(),
				"AUTHORIZATION_FAILURE", data));
	}

}
