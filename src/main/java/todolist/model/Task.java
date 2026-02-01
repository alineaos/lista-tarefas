package todolist.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class Task {
    Integer id;
    String description;
    String status;
    LocalDate date;
    String category;
}
