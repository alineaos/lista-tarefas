package todolist;

import lombok.extern.log4j.Log4j2;
import todolist.exceptions.BusinessException;
import todolist.exceptions.DatabaseException;
import todolist.exceptions.Validator;
import todolist.ui.Menu;
import java.util.Scanner;

@Log4j2
public class Main {
    private final static Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        log.info("Sistema iniciado.");
        boolean isToContinue = true;
        while (isToContinue) {
            try {
                System.out.println("********************");
                System.out.println("* LISTA DE TAREFAS *");
                System.out.println("********************\n");
                Menu.showTaskMenu();


                int option = Validator.validateNumber(SCANNER.nextLine());
                if (option == 0) {
                    System.out.println("Sistema encerrado.");
                    log.info("Sistema encerrado com sucesso.");
                    return;
                }

                Menu.processingTaskMenuOption(option);
            } catch (BusinessException e) {
                log.warn("Aviso de negócio: {}", e.getMessage(), e);
                System.out.println("Aviso de negócio: " + e.getMessage());
            } catch (DatabaseException e) {
                log.error("Erro no banco de dados: {}", e.getMessage(), e);
                System.out.println("Erro interno no banco: " + e.getMessage());
                isToContinue = false;
            } catch (IllegalArgumentException e) {
                log.warn("Aviso: {}", e.getMessage(), e);
                System.out.println("Aviso: " + e.getMessage());
            }
        }
    }
}
