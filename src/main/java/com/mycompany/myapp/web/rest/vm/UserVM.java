package com.mycompany.myapp.web.rest.vm;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.time.Instant;
import java.util.Set;

/**
 * View Model to represent IdP User.
 */
@RegisterForReflection
public class UserVM {

    public String id;

    public String login;

    public String firstName;

    public String lastName;

    public String email;

    public String imageUrl;

    public boolean activated = false;

    public String langKey;

    public String createdBy;

    public Instant createdDate;

    public String lastModifiedBy;

    public Instant lastModifiedDate;

    public Set<String> authorities;
}
