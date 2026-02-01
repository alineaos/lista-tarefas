package todolist.model.enums;

import lombok.Getter;

@Getter
public enum TaskStatus {
    NAO_INICIADA(1, "Não iniciada"),
    EM_PROGRESSO(2, "Em progresso"),
    CONCLUIDA(3, "Concluída");


    private final int statusClassification;
    private final String portugueseStatusName;

    TaskStatus(int statusClassification, String portugueseStatusName) {
        this.statusClassification = statusClassification;
        this.portugueseStatusName = portugueseStatusName;
    }

    public static TaskStatus selectByStatusId(int classification){
        for (TaskStatus status : TaskStatus.values()){
            if(classification == status.getStatusClassification()){
                return status;
            }
        }
        return null;
    }

    public static TaskStatus selectByStatusPortugueseName(String name){
        for (TaskStatus status : TaskStatus.values()){
            if(name.equalsIgnoreCase(status.getPortugueseStatusName())){
                return status;
            }
        }
        return null;
    }
}
