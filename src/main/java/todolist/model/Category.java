package todolist.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Category {
    Integer id;
    String name;
}
