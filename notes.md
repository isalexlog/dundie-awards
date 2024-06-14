- no API documentations
- no tests suit - just context loads
- `application.properties` and `application.yml` - we need either `properties` or `yml`
- wrong H2 DB configuration in `application.yml`

## Controller

[EmployeeController.java](src/main/java/com/ninjaone/dundie_awards/controller/EmployeeController.java)

- unused imports
- `@RestController` can be used instead of `@Controller` - better separation of concerns visibility,
no need to mark with `@ResponseBody` every controller's method
- `@RequestMapping` can include `/employees` - controller is responsible for `/employees` path, simplifies annotations above methods
- field injection of beans, controller injection is much safer
- unused beans are injected
- `createEmployee` method is better to return '201 Created' response status
- [EmployeeController.java:76](src/main/java/com/ninjaone/dundie_awards/controller/EmployeeController.java)
  `isEmpty` can be used instead of `!optionalEmployee.isPresent()` 
- `deleteEmployee` method is better to return empty response body with '204 No Content' in this case. 
Should we do soft-delete?

[IndexController.java](src/main/java/com/ninjaone/dundie_awards/controller/IndexController.java)
- constructor injection is better than field injection

General
- no service (business logic) layer in the app, repositories are called from controllers 
- entities are used as DTOs 
- no validation of the incoming data
- no proper exception handling

## Model

This package is called `model` but contains entities

[Employee.java](src/main/java/com/ninjaone/dundie_awards/model/Employee.java)
- `private long id` - it's better to use boxed type `Long` instead of primitive `long` 
(I would also suggest to use UUID as id, if `id` value is customer-faced)
- 
```java
@ManyToOne
private Organization organization;
```
We can also add `fetch = FetchType.LAZY` and explicitly set join column

- no need to call `super()` in constructor (no explicit inheritance)

[Activity.java](src/main/java/com/ninjaone/dundie_awards/model/Activity.java)

- `private long id` - it's better to use boxed type `Long` instead of primitive `long`
    (I would also suggest to use UUID as id, if `id` value is customer-faced)
- no need to call `super()` in constructor (no explicit inheritance)

[Organization.java](src/main/java/com/ninjaone/dundie_awards/model/Organization.java)

- `private long id` - it's better to use boxed type `Long` instead of primitive `long`
  (I would also suggest to use UUID as id, if `id` value is customer-faced)
- no need to call `super()` in constructor (no explicit inheritance)
