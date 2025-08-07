package uk.ac.mmu.diexample;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

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
