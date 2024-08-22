package task.usermanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @Null(groups = Create.class)
    @NotNull(groups = Update.class)
    private Long id;

    @NotBlank(groups = Create.class)
    @Size(min = 4, max = 50)
    private String login;

    @NotBlank(groups = Create.class)
    @Size(min = 7, max = 20)
    @Pattern(regexp = "^(?=.*[!@#$%^&*])(?=.*[0-9]{3,}).{7,}$",
            message = "Password must contain at least one special character, three digits, and be at least 7 characters long.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank(groups = Create.class)
    @Size(min = 1, max = 256)
    private String fullName;

    @NotNull(groups = Create.class)
    private GenderDTO gender;

    public interface Create {
    }

    public interface Update {
    }
}
