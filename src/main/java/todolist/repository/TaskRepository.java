package todolist.repository;

import lombok.extern.log4j.Log4j2;
import todolist.conn.ConnectionFactory;
import todolist.exceptions.DatabaseException;
import todolist.model.Task;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Log4j2
public class TaskRepository {
    public static void saveTask(Task task) {
        log.info("Salvando tarefa...");
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementSaveTask(conn, task)
        ) {
            ps.execute();
            log.info("Tarefa salva com sucesso.");
            System.out.println("Tarefa salva com sucesso.");
        } catch (SQLException e) {
            log.error("Não foi possível salvar a tarefa", e);
            throw new DatabaseException("Não foi possível salvar a tarefa. Erro interno no banco.", e);
        }
    }

    private static PreparedStatement createPreparedStatementSaveTask(Connection conn, Task task) throws SQLException {
        String sql = "INSERT INTO todo_list.task (description, status, date, category) values (?, ?, ?, ?);";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, task.getDescription());
        ps.setString(2, task.getStatus());
        ps.setDate(3, Date.valueOf(task.getDate()));
        ps.setString(4, task.getCategory());

        return ps;

    }


}
