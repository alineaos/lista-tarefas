package todolist.repository;

import lombok.extern.log4j.Log4j2;
import todolist.conn.ConnectionFactory;
import todolist.exceptions.DatabaseException;
import todolist.model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
public class CategoryRepository {
    public static void save(Category category) {
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

    public static List<Category> findAll() {
        log.info("Iniciando a busca por todas as categorias...");
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = prepareFindAllStatement(conn);
             ResultSet rs = ps.executeQuery()
        ) {
            return executeMap(rs);
        } catch (SQLException e) {
            log.error("Erro ao buscar todas as categorias", e);
            throw new DatabaseException("Não foi possível listar categorias. Erro interno no banco.", e);
        }
    }

    private static PreparedStatement prepareFindAllStatement(Connection conn) throws SQLException {
        String sql = "SELECT * FROM todo_list.category;";

        return conn.prepareStatement(sql);
    }

    public static Optional<Category> findById(int id) {
        log.info("Iniciando a busca de categorias por ID...");
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = prepareFindByIdStatement(conn, id);
             ResultSet rs = ps.executeQuery()
        ) {
            return executeSingleMap(rs);
        } catch (SQLException e) {
            log.error("Erro ao buscar categorias por ID", e);
            throw new DatabaseException("Não foi possível listar categorias por ID. Erro interno no banco.", e);
        }
    }

    private static PreparedStatement prepareFindByIdStatement(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM todo_list.category WHERE id = ?;";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        return ps;
    }

    private static Category mapRowToTask(ResultSet rs) throws SQLException {
        return Category.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .build();
    }

    private static List<Category> executeMap(ResultSet rs) throws SQLException {
        List<Category> categories = new ArrayList<>();

        while (rs.next()) {
            categories.add(mapRowToTask(rs));
        }

        log.info("Busca finalizada com sucesso. Total de categorias encontradas: {}", categories.size());
        return categories;
    }

    private static Optional<Category> executeSingleMap(ResultSet rs) throws SQLException {
        if (!rs.next()) return Optional.empty();

        log.info("Busca finalizada com sucesso");
        return Optional.of(mapRowToTask(rs));
    }
}
