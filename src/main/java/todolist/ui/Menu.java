package todolist.ui;

import todolist.controller.TaskController;
import todolist.model.enums.TaskStatus;
import todolist.util.ColumnsEnum;

public class Menu {
    public static void showTaskMenu() {
        System.out.println("Selecione a opção desejada:");
        System.out.println("[1] Criar tarefa");
        System.out.println("[2] Listar todas as tarefas");
        System.out.println("[3] Listar as tarefas pela data mais próxima");
        System.out.println("[4] Listar as tarefas por...");
        System.out.println("[5] Atualizar o Status da tarefa");
        System.out.println("[6] Atualizar tarefa (descrição, data e categoria)");
        System.out.println("[7] Deletar tarefa");
        System.out.println("[8] Deletar todas as tarefas");
        System.out.println("[0] Sair do programa");
    }

    public static void processingTaskMenuOption(int option) {
        switch (option) {
            case 1 -> TaskController.saveTask();
            case 2 -> TaskController.findAll();
            case 3 -> TaskController.findByDataAsc();
            case 4 -> TaskController.findByCriteria();
            case 5 -> TaskController.updateStatus();
            case 6 -> TaskController.update();
            case 7 -> TaskController.delete();
            case 8 -> TaskController.deleteAll();
            default -> throw new IllegalArgumentException("Valor inválido");
        }
    }

    public static void showCriteriasMenu() {
        System.out.println("Selecione o critério:");
        for (ColumnsEnum columns : ColumnsEnum.values()){
            System.out.printf("[%d] %s%n", columns.getColumnClassification(), columns.getPortugueseColumnName());
        }
    }

    public static ColumnsEnum processingCriteriasMenuOption(int option) {
        return ColumnsEnum.selectByColumnClassification(option);
    }

    public static void taskStatusMenu() {
            for (TaskStatus status : TaskStatus.values()){
                System.out.printf("[%d] %s%n", status.getStatusClassification(), status.getPortugueseStatusName());
            }
    }

    public static TaskStatus processingTaskStatusMenu(int option) {
        return TaskStatus.selectByStatusId(option);
    }

}
