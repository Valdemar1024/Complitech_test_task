package task.usermanager.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenderDTO {

    @NotNull(groups = UserDTO.Create.class)
    private Integer id;

    private String name;
}
