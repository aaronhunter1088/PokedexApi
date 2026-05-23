---
applyTo: 'src/test/java/**/*.java'
description: 'Java Testing Standards'
---
Apply these instructions when generating test classes.

## Core standards

- Use JUnit 5 (`@Test`, `@BeforeEach`, `@BeforeAll`, `@ParameterizedTest`).
- Use `assertThrows(...)` for exception testing.
- Use `@DisplayName` for readable test intent where it improves clarity.
- Do not use `Thread.sleep()`; use proper synchronization/testing utilities for async behavior.
- Keep tests independent and deterministic.
- Use the files in `src/test/resources/**/**.json` as needed for test data. Add new files accordingly and give each one an appropriate name.

## Project test patterns

- For Spring integration-style tests, prefer:
  - `@ExtendWith(SpringExtension.class)`
  - `@SpringBootTest`
  - `@AutoConfigureMockMvc` when endpoint/MockMvc behavior is tested
- Reuse shared test support by extending `BaseApiTest` when appropriate.
- For non-Spring unit tests, use Mockito with `@ExtendWith(MockitoExtension.class)` and `@InjectMocks`.
- Mock external dependencies (`PokeApiClient`, services, HTTP clients) instead of making real network calls.
- Load fixture JSON with `ClassPathResource` + `JsonMapper` in `@BeforeEach` setup.

## Parameterized tests

- Use parameterized tests for multi-scenario cases.
- `@CsvSource` is acceptable for simple scalar inputs already common in this codebase.
- `@MethodSource` is preferred for more complex scenario objects.
- Place scenario provider methods directly below their corresponding parameterized tests.

## Assertions and structure

- Use JUnit assertions and AssertJ consistently within a test class.
- Verify both HTTP status and key response body fields for controller tests.
- Keep test method order aligned with source method order only when it improves readability (for example, large API test classes using `@TestMethodOrder(OrderAnnotation.class)`).
