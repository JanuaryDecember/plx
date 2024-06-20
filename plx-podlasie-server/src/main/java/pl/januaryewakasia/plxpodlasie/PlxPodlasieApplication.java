package pl.januaryewakasia.plxpodlasie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Random;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class PlxPodlasieApplication {
    public static void main(String[] args) {
        SpringApplication.run(PlxPodlasieApplication.class, args);
    }

}

class TestDataGenerator {
    public static void main(String[] args) throws IOException {
        FileWriter writer = new FileWriter("src/main/resources/import.sql");

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Inserting Categories
        for (int i = 1; i <= 100; i++) {
            writer.write(String.format("INSERT INTO category (name, image) VALUES ('Category %d', 'category%d.png');%n",
                    i, i));
        }

        // Inserting Users
        for (int i = 1; i <= 100; i++) {
            String rawPassword = "password" + i;
            String encodedPassword = passwordEncoder.encode(rawPassword);
            writer.write(String.format(
                    "INSERT INTO _user (username, email, password, role, enabled, account_non_expired, credentials_non_expired, account_non_locked) VALUES ('user%d', 'user%d@gmail.com', '%s', '%s', true, true, true, true);%n",
                    i, i, encodedPassword, i == 54 ? Role.USER_PREMIUM.name() : Role.USER.name()));
        }

        // Inserting Listings
        Random rand = new Random();
        for (int i = 1; i <= 100; i++) {
            long userId = rand.nextInt(100) + 1;
            long categoryId = rand.nextInt(100) + 1;
            BigDecimal price = BigDecimal.valueOf(rand.nextDouble() * 1000).setScale(2, RoundingMode.HALF_UP);
            LocalDateTime ldt = LocalDateTime.now().plusHours(2);
            writer.write(String.format(
                    "INSERT INTO listing (user_id, name, description, image, price, approved, expiry_date, category_id) VALUES ( %d, 'Listing %d', 'Description for listing %d', 'image%d.png', %.2f, false, '%s', %d);%n",
                    userId, i, i, i, price, ldt, categoryId));
        }

        writer.write(String.format(
                "INSERT INTO _user (username, email, password, role, enabled, account_non_expired, credentials_non_expired, account_non_locked) VALUES ('january', 'janek2121ff@gmail.com', '%s', 'ADMIN', true, true, true, true);%n",
                passwordEncoder.encode("Janek111")));

        writer.write("CREATE UNIQUE INDEX SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);\n");
        writer.write("CREATE INDEX SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);\n");
        writer.write("CREATE INDEX SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);\n");

        writer.close();
    }
}
