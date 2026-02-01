package todolist.util;

import lombok.Getter;

@Getter
public enum ColumnsEnum {
    ID(1, "Id", "Id"),
    DESCRIPTION(2, "Description", "Descrição"),
    STATUS(3, "Status", "Status"),
    DATE(4, "Date", "Data"),
    CATEGORY(5, "Category", "Categoria");

    private final int COLUMN_CLASSIFICATION;
    private final String ENGLISH_COLUMN_NAME;
    private final String PORTUGUESE_COLUMN_NAME;

    ColumnsEnum(int COLUMN_CLASSIFICATION, String ENGLISH_COLUMN_NAME, String PORTUGUESE_COLUMN_NAME) {
        this.COLUMN_CLASSIFICATION = COLUMN_CLASSIFICATION;
        this.ENGLISH_COLUMN_NAME = ENGLISH_COLUMN_NAME;
        this.PORTUGUESE_COLUMN_NAME = PORTUGUESE_COLUMN_NAME;
    }

    public static ColumnsEnum selectByColumnClassification(int classification){
        for (ColumnsEnum columnsEnum : ColumnsEnum.values()){
            if (columnsEnum.getCOLUMN_CLASSIFICATION() == classification){
                return columnsEnum;
            }
        }
        return null;
    }

}
