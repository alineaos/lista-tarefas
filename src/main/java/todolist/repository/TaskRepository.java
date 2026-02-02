package todolist.repository;

import lombok.extern.log4j.Log4j2;
import todolist.conn.ConnectionFactory;
import todolist.exceptions.DatabaseException;
import todolist.model.Task;
import todolist.model.enums.TaskStatus;
import todolist.util.ColumnsEnum;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
        ps.setString(2, task.getStatus().getPortugueseStatusName());
        ps.setDate(3, Date.valueOf(task.getDate()));
        ps.setString(4, task.getCategory());

        return ps;

    }

    public static List<Task> findAll() {
        log.info("Listando todas as tarefas...");
        List<Task> tasks = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementFindAll(conn);
             ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()){
                TaskStatus rsToEnum = TaskStatus.selectByStatusPortugueseName(rs.getString("status"));
                Task task = Task.builder()
                        .id(rs.getInt("id"))
                        .description(rs.getString("description"))
                        .status(rsToEnum)
                        .date(rs.getDate("date").toLocalDate())
                        .category(rs.getString("category"))
                        .build();
                tasks.add(task);
            }

            log.info("As tarefas foram listadas com sucesso.");
        } catch (SQLException e) {
            log.error("Não foi possível listar as tarefas", e);
            throw new DatabaseException("Não foi possível listar as tarefas. Erro interno no banco.", e);
        }
        return tasks;
    }

    private static PreparedStatement createPreparedStatementFindAll(Connection conn) throws SQLException {
        String sql = "SELECT * FROM todo_list.task;";
        return conn.prepareStatement(sql);
    }

    public static List<Task> findByCriteria(ColumnsEnum criteria, String param) {
        log.info("Listando todas as tarefas '{}' na coluna '{}'...", param, criteria);
        List<Task> tasks = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementFindByCriteria(conn, criteria.getEnglishColumnName(), param);
             ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()){
                TaskStatus rsToEnum = TaskStatus.selectByStatusPortugueseName(rs.getString("status"));
                Task task = Task.builder()
                        .id(rs.getInt("id"))
                        .description(rs.getString("description"))
                        .status(rsToEnum)
                        .date(rs.getDate("date").toLocalDate())
                        .category(rs.getString("category"))
                        .build();
                tasks.add(task);
            }

            log.info("As tarefas '{}' na coluna '{}' foram listadas com sucesso.", param, criteria);
        } catch (SQLException e) {
            log.error("Não foi possível listar as tarefas", e);
            throw new DatabaseException("Não foi possível listar as tarefas. Erro interno no banco.", e);
        }
        return tasks;
    }

    private static PreparedStatement createPreparedStatementFindByCriteria(Connection conn, String criteria, String param) throws SQLException {
        String sql = "SELECT * FROM todo_list.task WHERE "+ criteria +" LIKE ?;";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, String.format("%%%s%%", param));
        return ps;
    }

    public static void updateStatus(Task task, TaskStatus newStatus) {
        log.info("Atualizando o status da tarefa com ID '{}'...", task.getId());
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementUpdateStatus(conn, task, newStatus)
        ) {
            ps.execute();
            log.info("Status da tarefa com ID '{}' atualizada com sucesso", task.getId());
        } catch (SQLException e) {
            log.error("Não foi possível atualizar o status da tarefa", e);
            throw new DatabaseException("Não foi possível atualizar o status da tarefa. Erro interno no banco.", e);
        }
    }

    private static PreparedStatement createPreparedStatementUpdateStatus(Connection conn, Task task, TaskStatus newStatus) throws SQLException {
        String sql = "UPDATE todo_list.task SET status = ? WHERE id = ?;";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, newStatus.getPortugueseStatusName());
        ps.setInt(2, task.getId());
        return ps;
    }

    public static void update(Task task) {
        log.info("Atualizando a tarefa com ID '{}'...", task.getId());
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementUpdate(conn, task)
        ) {
            ps.execute();
            log.info("Tarefa com ID '{}' atualizada com sucesso", task.getId());
        } catch (SQLException e) {
            log.error("Não foi possível atualizar a tarefa", e);
            throw new DatabaseException("Não foi possível atualizar a tarefa. Erro interno no banco.", e);
        }
    }

    private static PreparedStatement createPreparedStatementUpdate(Connection conn, Task task) throws SQLException {
        String sql = "UPDATE todo_list.task SET description = ?, date = ?, category = ? WHERE id = ?;";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, task.getDescription());
        ps.setDate(2, Date.valueOf(task.getDate()));
        ps.setString(3, task.getCategory());
        ps.setInt(4, task.getId());
        return ps;
    }

    public static void delete(int id) {
        log.info("Deletando a tarefa com ID '{}'...", id);
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = createPreparedStatementDelete(conn, id)
        ) {
            ps.execute();
            log.info("Tarefa com ID '{}' deletada com sucesso", id);
        } catch (SQLException e) {
            log.error("Não foi possível deletar a tarefa", e);
            throw new DatabaseException("Não foi possível deletar a tarefa. Erro interno no banco.", e);
        }
    }

    private static PreparedStatement createPreparedStatementDelete(Connection conn, int id) throws SQLException {
        String sql = "DELETE FROM todo_list.task WHERE id = ?;";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        return ps;
    }

}
