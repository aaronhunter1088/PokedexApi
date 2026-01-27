# GitHub Copilot Instructions for PokedexApi

## Project Overview

PokedexApi is a RESTful Spring Boot API that provides comprehensive information about Pokémon. It serves as a backend service for PokedexApiUI, offering endpoints to retrieve details about Pokémon including their types, abilities, stats, evolutions, and more.

## Technology Stack

- **Language**: Java
- **Framework**: Spring Boot (version managed by parent POM)
- **Build Tool**: Maven
- **Testing**: JUnit 5 (Jupiter), Spring Boot Test, AssertJ
- **Logging**: Log4j2
- **API Documentation**: SpringDoc OpenAPI (Swagger)
- **Caching**: Spring Cache with Caffeine
- **External API Client**: pokeapi-reactor (for accessing PokeAPI)
- **Packaging**: WAR file

## Project Structure

```
src/
├── main/java/pokedexapi/
│   ├── PokedexApiApplication.java     # Main Spring Boot application
│   ├── api/                            # API resource interfaces
│   ├── config/                         # Spring configuration classes
│   ├── controllers/                    # REST controllers
│   ├── service/                        # Business logic services
│   └── utilities/                      # Utility classes and constants
└── test/java/pokedexapi/
    ├── api/                            # API tests
    ├── config/                         # Configuration tests
    ├── controllers/                    # Controller tests (BaseApiTest)
    ├── service/                        # Service tests
    └── entity/                         # Entity tests
```

## Coding Standards

### Java Style
- Use Java 17+ features
- Follow Spring Boot best practices
- Use `@NonNull` annotations from `org.jspecify.annotations` where appropriate (available via parent POM dependencies)
- Prefer dependency injection via constructor
- Use Log4j2 for logging with proper logger initialization:
  ```java
  private static final Logger LOGGER = LogManager.getLogger(ClassName.class);
  ```

### Controllers
- Extend `BaseApiController` for common functionality
- Use `@CrossOrigin(origins = "*")` for CORS support
- Use `@Controller` or `@RestController` annotations
- Inject services via constructor with `@Autowired` and `@Qualifier` when needed

### Services
- Implement service interfaces in the `api/` package
- Use Spring's caching annotations where appropriate
- Handle exceptions gracefully and log errors

### Testing
- Extend `BaseApiTest` for test classes
- Use JUnit 5 annotations: `@Test`, `@BeforeEach`, `@DisplayName`
- Use `@ExtendWith(SpringExtension.class)` and `@SpringBootTest` for integration tests
- Use AssertJ for assertions (`assertThat()`)
- Use Mockito for mocking (`@Mock`, `@MockBean`, `when()`, `verify()`)
- Test files should be named with `Test` suffix (e.g., `PokemonServiceTest`)

### Error Handling
- Log errors with appropriate levels (ERROR, WARN, INFO)
- Include relevant context in log messages
- Return null or throw exceptions from service methods when appropriate
- Controllers should handle service exceptions and return proper HTTP responses

## Build & Test Commands

### Build
```bash
# Clean and build
mvn clean package

# Skip tests
mvn clean package -Dmaven.test.skip=true

# Install to local repository
mvn clean install
```

### Test
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=ClassName

# Run tests with coverage
mvn clean verify
```

### Run
```bash
# Run Spring Boot application
mvn spring-boot:run
```

## Dependencies

### Key Libraries
- Spring Boot Starter Web, Cache, Actuator, HATEOAS
- SpringDoc OpenAPI for API documentation
- Caffeine Cache for caching
- pokeapi-reactor for PokeAPI integration
- ModelMapper for DTO conversions
- MySQL Connector (provided scope)

### Adding Dependencies
When adding new dependencies:
1. Check if a Spring Boot starter is available first
2. Add to `pom.xml` under appropriate section (runtime, test, etc.)
3. Use version properties when possible
4. Exclude conflicting transitive dependencies if needed

## API Documentation

The API is documented using SpringDoc OpenAPI:
- Swagger UI available at: `/swagger-ui/index.html`
- OpenAPI JSON generated during build: `dist/{version}/pokedexSwagger.json`

## Configuration

- Application properties in `src/main/resources/application.properties`
- Spring profiles for different environments
- External PokeAPI base URL configured via `skaro.pokeapi.baseUri`

## Important Notes

- The application uses deprecated methods marked with `@Deprecated(forRemoval = true)` - avoid using these
- GIF images are fetched from external URLs - handle 404 responses gracefully
- The API uses reactive programming with Mono for some operations
- Caching is enabled for performance - be mindful when modifying cached methods
- CORS is configured to allow all origins (`*`)

## When Making Changes

1. **Minimal Changes**: Make the smallest possible changes to achieve the goal
2. **Test First**: Run existing tests to ensure no regressions
3. **Follow Patterns**: Look at existing code for examples (especially in controllers and services)
4. **Log Appropriately**: Add logging for important operations and error cases
5. **Update Documentation**: Keep Swagger annotations up to date
6. **Handle Nulls**: Check for null responses from services
7. **Cache Awareness**: Consider cache invalidation when modifying data operations
