package task.usermanager.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteRangeDTO {
    @Min(1)
    @NotNull
    private Long startId;
    @Min(1)
    @NotNull
    private Long endId;
}
