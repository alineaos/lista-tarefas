package todolist.repository;

import lombok.extern.log4j.Log4j2;
import todolist.conn.ConnectionFactory;
import todolist.exceptions.DatabaseException;
import todolist.model.Category;
import todolist.model.Task;
import todolist.model.enums.TaskStatus;
import todolist.util.TaskColumn;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class TaskRepository {
    public void save(Task task) {
        log.info("Tentando salvar nova tarefa '{}'...", task.getDescription());
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = prepareInsertStatement(conn, task)
        ) {
            ps.execute();
            log.info("Tarefa salva com sucesso! ID da tarefa: {}", task.getId());
        } catch (SQLException e) {
            log.error("Erro ao salvar a tarefa '{}'", task.getDescription(), e);
            throw new DatabaseException("Não foi possível salvar a tarefa. Erro interno no banco.", e);
        }
    }

    private PreparedStatement prepareInsertStatement(Connection conn, Task task) throws SQLException {
        String sql = "INSERT INTO todo_list.task (description, status, date, category_id) values (?, ?, ?, ?);";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, task.getDescription());
        ps.setString(2, task.getStatus().getPortugueseStatusName());
        ps.setDate(3, Date.valueOf(task.getDate()));
        ps.setInt(4, task.getCategory().getId());

        return ps;

    }

    public List<Task> findAll() {
        log.info("Iniciando a busca por todas as tarefas...");
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = prepareFindAllStatement(conn);
             ResultSet rs = ps.executeQuery()
        ) {
            return executeMap(rs);
        } catch (SQLException e) {
            log.error("Erro ao buscar todas as tarefas", e);
            throw new DatabaseException("Não foi possível listar as tarefas. Erro interno no banco.", e);
        }
    }


    private PreparedStatement prepareFindAllStatement(Connection conn) throws SQLException {
        String sql = "SELECT t.id, t.description, t.status, t.date, t.category_id, c.name AS category_name FROM todo_list.task t " +
                "INNER JOIN todo_list.category c " +
                "ON t.category_id = c.id;";

        return conn.prepareStatement(sql);
    }

    public List<Task> findByCriteria(TaskColumn criteria, String param) {
        log.info("Iniciando a busca por todas as tarefas '{}' na coluna '{}'...", param, criteria);

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = prepareFindByCriteriaStatement(conn, criteria.getEnglishColumnName(), param);
             ResultSet rs = ps.executeQuery()
        ) {
            return executeMap(rs);
        } catch (SQLException e) {
            log.error("Erro ao buscar as tarefas.", e);
            throw new DatabaseException("Não foi possível listar as tarefas. Erro interno no banco.", e);
        }
    }

    private PreparedStatement prepareFindByCriteriaStatement(Connection conn, String criteria, String param) throws SQLException {
        boolean isCriteriaNumber = criteria.contains("Id");
        String operator = isCriteriaNumber ? " = " : " LIKE ";
        String sql = "SELECT t.id, t.description, t.status, t.date, t.category_id, c.name AS category_name FROM todo_list.task t " +
                "INNER JOIN todo_list.category c " +
                "ON t.category_id = c.id " +
                "WHERE t.%s %s ?;".formatted(criteria, operator);

        PreparedStatement ps = conn.prepareStatement(sql);
        if (isCriteriaNumber) {
            ps.setInt(1, Integer.parseInt(param));
        } else {
            ps.setString(1, String.format("%%%s%%", param));

        }
        return ps;
    }

    private Task mapRowToTask(ResultSet rs) throws SQLException {
        TaskStatus rsToEnum = TaskStatus.selectByStatusPortugueseName(rs.getString("status"));
        Category category = Category.builder()
                .id(rs.getInt("category_id"))
                .name(rs.getString("category_name"))
                .build();

        return Task.builder()
                .id(rs.getInt("id"))
                .description(rs.getString("description"))
                .status(rsToEnum)
                .date(rs.getDate("date").toLocalDate())
                .category(category)
                .build();

    }

    private List<Task> executeMap(ResultSet rs) throws SQLException {
        List<Task> tasks = new ArrayList<>();

        while (rs.next()) {
            tasks.add(mapRowToTask(rs));
        }

        log.info("Busca finalizada com sucesso. Total de tarefas encontradas: {}", tasks.size());
        return tasks;
    }

    public void updateStatus(Task task, TaskStatus newStatus) {
        log.info("Atualizando o status da tarefa com ID {}...", task.getId());
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = prepareUpdateStatusStatement(conn, task, newStatus)
        ) {
            int rowsAffected = ps.executeUpdate();
            log.info("Tarefa com ID {} atualizada com sucesso. Linhas afetadas: {} ", task.getId(), rowsAffected);
        } catch (SQLException e) {
            log.error("Não foi possível atualizar o status da tarefa", e);
            throw new DatabaseException("Não foi possível atualizar o status da tarefa. Erro interno no banco.", e);
        }
    }

    private PreparedStatement prepareUpdateStatusStatement(Connection conn, Task task, TaskStatus newStatus) throws SQLException {
        String sql = "UPDATE todo_list.task SET status = ? WHERE id = ?;";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, newStatus.getPortugueseStatusName());
        ps.setInt(2, task.getId());
        return ps;
    }

    public void update(Task task) {
        log.info("Atualizando a tarefa com ID '{}'...", task.getId());
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = prepareUpdateStatement(conn, task)
        ) {
            int rowsAffected = ps.executeUpdate();
            log.info("Tarefa com ID '{}' atualizada com sucesso. Linhas afetadas: {} ", task.getId(), rowsAffected);
        } catch (SQLException e) {
            log.error("Não foi possível atualizar a tarefa", e);
            throw new DatabaseException("Não foi possível atualizar a tarefa. Erro interno no banco.", e);
        }
    }

    private PreparedStatement prepareUpdateStatement(Connection conn, Task task) throws SQLException {
        String sql = "UPDATE todo_list.task SET description = ?, date = ?, category_id = ? WHERE id = ?;";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, task.getDescription());
        ps.setDate(2, Date.valueOf(task.getDate()));
        ps.setInt(3, task.getCategory().getId());
        ps.setInt(4, task.getId());
        return ps;
    }

    public void delete(int id) {
        log.info("Deletando a tarefa com ID {}...", id);
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = prepareDeleteStatement(conn, id)
        ) {
            int rowsAffected = ps.executeUpdate();
            log.info("Tarefa com ID {} deletada com sucesso. Linhas afetadas: {}", id, rowsAffected);
        } catch (SQLException e) {
            log.error("Não foi possível deletar a tarefa", e);
            throw new DatabaseException("Não foi possível deletar a tarefa. Erro interno no banco.", e);
        }
    }

    private PreparedStatement prepareDeleteStatement(Connection conn, int id) throws SQLException {
        String sql = "DELETE FROM todo_list.task WHERE id = ?;";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        return ps;
    }

    public void deleteAll() {
        log.info("Deletando todos as tarefas da tabela...");
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = prepareDeleteAllStatement(conn)
        ) {
            int rowsAffected = ps.executeUpdate();
            log.info("Todas as tarefas da tabela foram deletadas do banco de dados. Linhas afetadas: {}", rowsAffected);
        } catch (SQLException e) {
            log.error("Não foi possível deletar todas as tarefas", e);
            throw new DatabaseException("Não foi possível deletar todas as tarefa. Erro interno no banco.", e);
        }
    }

    private PreparedStatement prepareDeleteAllStatement(Connection conn) throws SQLException {
        String sql = "TRUNCATE TABLE todo_list.task;";
        return conn.prepareStatement(sql);
    }

}
