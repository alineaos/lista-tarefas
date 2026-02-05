package todolist.util;

import lombok.Getter;

@Getter
public enum CategoryColumn {
    ID(1, "Id", "Id"),
    NAME(2, "Name", "Name");

    private final int columnClassification;
    private final String englishColumnName;
    private final String portugueseColumnName;

    CategoryColumn(int columnClassification, String englishColumnName, String portugueseColumnName) {
        this.columnClassification = columnClassification;
        this.englishColumnName = englishColumnName;
        this.portugueseColumnName = portugueseColumnName;
    }

    public static CategoryColumn selectByColumnClassification(int classification){
        for (CategoryColumn column : CategoryColumn.values()){
            if (column.getColumnClassification() == classification){
                return column;
            }
        }
        return null;
    }
}
