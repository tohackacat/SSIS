package org.example.person;

import org.example.core.EntityId;

import java.util.Optional;

public record Person(EntityId id, String fullName, Optional<String> email) {
}
