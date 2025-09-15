package uk.ac.mmu.diexample;

import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

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
