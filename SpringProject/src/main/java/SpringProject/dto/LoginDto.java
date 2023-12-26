package SpringProject.dto;

import lombok.Data;

@Data
public class LoginDto {
    private Long id;
    private String full_name;
    private String email;
    private String password;
    private Integer phone;
    private String address;
    private String role;
}