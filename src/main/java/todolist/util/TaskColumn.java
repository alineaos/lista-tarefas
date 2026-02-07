package todolist.util;

import lombok.Getter;

@Getter
public enum TaskColumn {
    ID(1, "Id", "Id"),
    DESCRIPTION(2, "Description", "Descrição"),
    STATUS(3, "Status", "Status"),
    DATE(4, "Date", "Data"),
    CATEGORY_ID(5, "Category_Id", "Id Categoria");

    private final int columnClassification;
    private final String englishColumnName;
    private final String portugueseColumnName;

    TaskColumn(int columnClassification, String englishColumnName, String portugueseColumnName) {
        this.columnClassification = columnClassification;
        this.englishColumnName = englishColumnName;
        this.portugueseColumnName = portugueseColumnName;
    }

    public static TaskColumn selectByColumnClassification(int classification){
        for (TaskColumn column : TaskColumn.values()){
            if (column.getColumnClassification() == classification){
                return column;
            }
        }
        return null;
    }

}
