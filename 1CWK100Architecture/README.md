# 1CWK100 Cleann Architecture Starter Code


The main() method launches the Spring Boot application.

```Java
@SpringBootApplication
public class GameApplication {

	public static void main(String[] args) {

        SpringApplication.run(GameApplication.class, args);
	}

}
```

The Spring Boot application scans for components (any class annotated with `@Component`) and configuration classes (any class annotated with `@Configuration`)in the same package and sub-packages of the class with the main() method.

Classes annotated with `@Configuration` are used to define Beans that will be managed by the Spring Boot application. Beans are objects that are instantiated, assembled, and otherwise managed by a Spring DI container.
```Java
@Configuration
public class AppConfig
{

    @Bean
    Required required()
    {
        return new Technology();
    }


    @Bean
    uk.ac.mmu.game.applicationcode.usecase.play.Provided playUseCase(Required required)
    {
        return new uk.ac.mmu.game.applicationcode.usecase.play.UseCase(required);
    }

    @Bean
    uk.ac.mmu.game.applicationcode.usecase.replay.Provided replayUseCase(Required required)
    {
        return new uk.ac.mmu.game.applicationcode.usecase.replay.UseCase(required);
    }

}
```

Classes annotated with `@Component` and `implementing org.springframework.boot.CommandLineRunner` will be automatically run by the Spring Boot application SprintBoot will pass any dependencies required into the class constructor.

As we have more than one class implementing CommandLineRunner we also implement the Ordered interface to control the order in which they are run.

```Java
import uk.ac.mmu.game.applicationcode.usecase.play.Provided;
@Component
class Play implements org.springframework.boot.CommandLineRunner, Ordered {

    private final Provided usecase;

    Play(Provided usecase) {
        this.usecase = usecase;
    }


    @Override
    public void run(String... args)  {
        System.out.format("Played Game Id = %d%n", usecase.play());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}

```


