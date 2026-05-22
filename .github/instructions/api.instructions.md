---
applyTo: 'src/main/java/pokedexapi/api/*.java'
description: 'Pokémon API Instructions'
---
Apply these instructions when modifying REST API classes in `src/main/java/pokedexapi/api/`.

## Class-level conventions

- Keep API classes in the `pokedexapi.api` package and name them with the `*Api` suffix.
- Annotate classes with `@RestController`, `@CrossOrigin(origins = "*")`, and a base `@RequestMapping` path (for example, `"/pokemon"`).
- Extend `BaseApiController` to reuse shared services and helper methods.
- Use constructor injection and pass dependencies to `super(...)`; `@Autowired` is commonly used on constructors in this codebase.
- Define a Log4j2 logger in each class:

```java
private static final Logger LOGGER = LogManager.getLogger(MyApi.class);
```

## Endpoint conventions

- Prefer `@GetMapping` for GET endpoints; `@RequestMapping(method = RequestMethod.GET)` also exists in current files and is acceptable when keeping consistency.
- Return `ResponseEntity<?>` for API responses.
- For list endpoints, use `limit` and `offset` query parameters with defaults:

```java
@RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
@RequestParam(value = "offset", required = false, defaultValue = "0") int offset
```

- Use `@PathVariable` for resource identifiers (`id` or `nameOrId`).
- Log the action at method start (`LOGGER.info(...)`) and log errors in catch blocks.

## Error handling and responses

- Wrap remote/service calls in `try/catch`.
- On success, return `ResponseEntity.ok(...)`.
- On failures, use existing patterns from neighboring APIs (`badRequest`, `notFound`, or `internalServerError`) and keep error messages clear.
- When proxying HTTP responses, map status codes via `switch` where appropriate.

## Optional documentation

- Add OpenAPI annotations (`@Operation`, `@ApiResponse`) for externally important endpoints, matching patterns already used in `PokemonApi`.

