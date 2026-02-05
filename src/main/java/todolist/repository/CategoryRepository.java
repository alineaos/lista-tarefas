package todolist.repository;

import lombok.extern.log4j.Log4j2;
import todolist.conn.ConnectionFactory;
import todolist.exceptions.DatabaseException;
import todolist.model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Log4j2
public class CategoryRepository {
    public static void save(Category category){
        log.info("Tentando salvar nova categoria '{}'...", category.getName());
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = prepareSaveStatement(conn, category)
        ) {
            ps.execute();
            log.info("Tarefa salva com sucesso! ID da tarefa: {}", category.getId());
        } catch (SQLException e) {
            log.error("Erro ao salvar a tarefa '{}'", category.getName(), e);
            throw new DatabaseException("Não foi possível salvar a tarefa. Erro interno no banco.", e);
        }
    }

    private static PreparedStatement prepareSaveStatement(Connection conn, Category category) throws SQLException {
        String sql = "INSERT INTO todo_list.category (name) values (?);";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, category.getName());

        return ps;
    }
}
