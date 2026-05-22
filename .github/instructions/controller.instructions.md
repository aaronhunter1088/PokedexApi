---
applyTo: 'src/main/java/pokedexapi/controllers/*.java'
description: 'Controller Instructions'
---
Apply these instructions when adding or editing controller classes in `src/main/java/pokedexapi/controllers/`.

## Controller conventions

- Keep controller classes in the `pokedexapi.controllers` package.
- Use `@Controller` for shared/base controller classes and `@CrossOrigin(origins = "*")` when behavior should match existing controller CORS settings.
- Define a Log4j2 logger per class:

```java
private static final Logger LOGGER = LogManager.getLogger(ClassName.class);
```

- Use constructor injection for required dependencies; `@Autowired` on constructors is used in this codebase.
- Prefer `protected final` fields for injected services that are shared by subclasses.

## Base controller patterns

- Extend or mirror `BaseApiController` when adding shared behavior across API classes.
- Keep shared helper methods `protected` so API classes can reuse them.
- Use `@Value("${skaro.pokeapi.baseUri}")` for API base URL configuration when needed by subclasses.
- Use `@Qualifier("PokemonApiService")` for `PokemonService` injection where multiple implementations may exist.

## Error handling and logging

- Wrap external service/network calls in `try/catch`.
- Log contextual errors with the exception object where possible (for example: `LOGGER.error("... {}", value, e)`).
- For fallback helper methods, rethrow exceptions after logging when callers need to control the HTTP response.

## Deprecation guidance

- Do not add new dependencies on methods marked `@Deprecated(forRemoval = true)`.
- If editing deprecated methods, keep changes minimal and avoid expanding their usage.


