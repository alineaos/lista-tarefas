package todolist.ui;

import todolist.service.TaskService;
import todolist.util.ColumnsEnum;

public class Menu {
    public static void showTaskMenu() {
        System.out.println("Selecione a opção desejada:");
        System.out.println("[1] Criar tarefa");
        System.out.println("[2] Listar todas as tarefas");
        System.out.println("[3] Listar as tarefas por...");
        System.out.println("[0] Sair do programa");
    }

    public static void processingTaskOption(int option) {
        switch (option) {
            case 1 -> TaskService.saveTask();
            case 2 -> TaskService.findAll();
            case 3 -> TaskService.findByCriteria();
            default -> throw new IllegalArgumentException("Valor inválido");
        }
    }

    public static void showCriteriasMenu() {
        System.out.println("Selecione o critério:");
        System.out.println("[1] Id");
        System.out.println("[2] Descrição");
        System.out.println("[3] Status");
        System.out.println("[4] Data");
        System.out.println("[5] Categoria");
    }

    public static ColumnsEnum processingCriteriasOption(int option){
        return ColumnsEnum.selectByColumnClassification(option);
}

}
