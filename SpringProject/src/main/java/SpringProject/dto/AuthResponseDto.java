package SpringProject.dto;

public class AuthResponseDto {
    private String accesToken;
    private String tokenType="Bearer";

    public AuthResponseDto(String accesToken){
        this.accesToken=accesToken;
    }
}