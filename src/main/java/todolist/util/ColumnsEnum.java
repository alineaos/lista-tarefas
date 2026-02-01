package todolist.util;

import lombok.Getter;

@Getter
public enum ColumnsEnum {
    ID(1, "Id", "Id"),
    DESCRIPTION(2, "Description", "Descrição"),
    STATUS(3, "Status", "Status"),
    DATE(4, "Date", "Data"),
    CATEGORY(5, "Category", "Categoria");

    private final int columnClassification;
    private final String englishColumnName;
    private final String portugueseColumnName;

    ColumnsEnum(int columnClassification, String englishColumnName, String portugueseColumnName) {
        this.columnClassification = columnClassification;
        this.englishColumnName = englishColumnName;
        this.portugueseColumnName = portugueseColumnName;
    }

    public static ColumnsEnum selectByColumnClassification(int classification){
        for (ColumnsEnum column : ColumnsEnum.values()){
            if (column.getColumnClassification() == classification){
                return column;
            }
        }
        return null;
    }

}
