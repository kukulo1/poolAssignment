package ru.kukulo1.test_assignment.client;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.kukulo1.test_assignment.client.records.AddClientRecord;

import java.util.regex.Pattern;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String phone;

    public Client(String name, String phone, String email) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
    public Client(AddClientRecord client) {
        this.name = client.name();
        this.phone = client.phone();
        this.email = client.email();
    }
    public boolean isValid() {
        return name != null && !name.trim().isEmpty() &&
                email != null && isValidEmail(email) &&
                phone != null && isValidPhone(phone);
    }
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private boolean isValidPhone(String phone) {
        String phoneRegex = "^\\d{10,}$";
        Pattern pattern = Pattern.compile(phoneRegex);
        return pattern.matcher(phone).matches();
    }
}
