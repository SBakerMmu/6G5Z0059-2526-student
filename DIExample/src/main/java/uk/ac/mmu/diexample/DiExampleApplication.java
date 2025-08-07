package uk.ac.mmu.diexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DiExampleApplication implements org.springframework.boot.CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DiExampleApplication.class, args);
    }

    private final Basket basket;

    DiExampleApplication(Basket basket) {
        this.basket = basket;
    }



    @Override
    public void run(String... args) {
        basket.chargeCreditCard("1234-5678-9012-3456", 2028, 6);
    }
}
