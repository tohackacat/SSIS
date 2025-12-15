package org.example.person.dto;

import java.util.Optional;

public record PersonDto(String id, String FullName, Optional<String> email)
{}
