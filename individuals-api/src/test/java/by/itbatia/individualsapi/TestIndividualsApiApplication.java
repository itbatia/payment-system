package by.itbatia.individualsapi;

import by.itbatia.psp.individualsapi.Application;
import org.springframework.boot.SpringApplication;

public class TestIndividualsApiApplication {

    public static void main(String[] args) {
        SpringApplication.from(Application::main).with(TestcontainersConfiguration.class).run(args);
    }

}
