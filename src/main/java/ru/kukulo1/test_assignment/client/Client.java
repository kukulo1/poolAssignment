package ru.kukulo1.test_assignment.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.kukulo1.test_assignment.client.dto.AddClientDTO;

import java.util.ArrayList;
import java.util.List;
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

    public Client(AddClientDTO client) {
        this.name = client.getName();
        this.phone = client.getPhone();
        this.email = client.getEmail();
    }

    @JsonIgnore
    public boolean isValid() {
        return name != null && !name.trim().isEmpty() &&
                email != null && isValidEmail(email) &&
                phone != null && isValidPhone(phone);
    }

    @JsonIgnore
    public List<String> getInvalidFields() {
        List<String> invalidFields = new ArrayList<>();

        if (name == null || name.trim().isEmpty()) {
            invalidFields.add("Поле 'name' обязательно и не должно быть пустым.");
        }

        if (email == null || !isValidEmail(email)) {
            invalidFields.add("Поле 'email' должно быть корректным адресом электронной почты (например: user@example.com).");
        }

        if (phone == null || !isValidPhone(phone)) {
            invalidFields.add("Поле 'phone' должно содержать минимум 10 цифр и не должно содержать других символов.");
        }

        return invalidFields;
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
