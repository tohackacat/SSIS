package org.example.person;
import java.util.Optional;

import org.example.core.EntityId;

public record Person (EntityId id,String fullName, Optional<String> email)

   
{}
