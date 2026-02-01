package todolist.ui;

import todolist.service.TaskService;

public class Menu {
    public static void showTaskMenu(){
        System.out.println("Selecione a opção desejada:");
        System.out.println("[1] Criar tarefa");
        System.out.println("[2] Listar todas as tarefas");
        System.out.println("[0] Sair do programa");
    }

    public static void processingTaskOption(int option){
        switch (option){
            case 1 -> TaskService.saveTask();
            case 2 -> TaskService.findAll();
            default -> throw new IllegalArgumentException("Valor inválido");
        }
    }
}
