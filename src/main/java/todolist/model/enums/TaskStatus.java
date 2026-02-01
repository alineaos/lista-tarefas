package todolist.model.enums;

import lombok.Getter;

@Getter
public enum TaskStatus {
    NAO_INICIADA(1, "Não iniciada"),
    EM_PROGRESSO(2, "Em progresso"),
    CONCLUIDA(3, "Concluída");


    private final int STATUS_CLASSIFICATION;
    private final String PORTUGUESE_STATUS_NAME;

    TaskStatus(int STATUS_CLASSIFICATION, String PORTUGUESE_STATUS_NAME) {
        this.STATUS_CLASSIFICATION = STATUS_CLASSIFICATION;
        this.PORTUGUESE_STATUS_NAME = PORTUGUESE_STATUS_NAME;
    }

    public static TaskStatus selectByStatusId(int classification){
        for (TaskStatus taskStatus : TaskStatus.values()){
            if(classification == taskStatus.getSTATUS_CLASSIFICATION()){
                return taskStatus;
            }
        }
        return null;
    }

    public static TaskStatus selectByStatusPortugueseName(String name){
        for (TaskStatus taskStatus : TaskStatus.values()){
            if(name.equalsIgnoreCase(taskStatus.getPORTUGUESE_STATUS_NAME())){
                return taskStatus;
            }
        }
        return null;
    }
}
