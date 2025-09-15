# DI Example


The main() method launches the Spring Boot application.

```Java
@SpringBootApplication
public class DiExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiExampleApplication.class, args);
    }

}
```

The Spring Boot application scans for components (classess annotated with `@Component`) and configuration classes (classes annotated with `@Configuration`)in the same package and sub-packages of the class with the main() method.

Classes annotated with `@Configuration` are used to define Beans that will be managed by the Spring Boot application. Beans are objects that are instantiated, assembled, and otherwise managed by a Spring DI container.

```Java
@Configuration
class AppConfig {

    @Bean
    @Scope("singleton")
    AbstractCreditCardService creditCardService() {
        return new ConcreteCreditCardService();
    }

    @Bean
    @Scope("prototype")
    Basket basket(AbstractCreditCardService creditCardService) {
        return new Basket(creditCardService);
    }
}
```
Classes annotated with `@Component` and `implementing org.springframework.boot.CommandLineRunner` will be automatically run by the Spring Boot application.

SprintBoot will pass any dependencies required into the class constructor.

If we have more than one class implementing CommandLineRunner we also implement the Ordered interface to control the order in which they are run.

```Java
@Component
class Runner  implements org.springframework.boot.CommandLineRunner, Ordered {
    private final Basket basket;

    Runner(Basket basket) {
        this.basket = basket;
    }

    @Override
    public void run(String... args) {
        basket.chargeCreditCard("1234-5678-9012-3456", 2028, 6);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
```


