package todolist.model;

import lombok.Builder;
import lombok.Value;
import todolist.model.enums.TaskStatus;

import java.time.LocalDate;

@Value
@Builder
public class Task {
    Integer id;
    String description;
    TaskStatus status;
    LocalDate date;
    Category category;
}
