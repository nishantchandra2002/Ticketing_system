package dto;

import lombok.Data;
import java.util.Set;

@Data
public class CreateUserRequest {
    private String username;
    private String password;
    private Set<String> roles;
}