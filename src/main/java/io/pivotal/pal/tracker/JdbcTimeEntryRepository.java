package io.pivotal.pal.tracker;


import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JdbcTimeEntryRepository implements TimeEntryRepository {
    private DataSource dataSource;
    private RowMapper timeEntryRowMapper = new TimeEntryMapper();

    private JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public TimeEntry create(TimeEntry timeEntry) {
        String sql = "INSERT INTO time_entries (project_id, user_id, date, hours)" +
                "VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, RETURN_GENERATED_KEYS);
            statement.setLong(1, timeEntry.getProjectId());
            statement.setLong(2, timeEntry.getUserId());
            statement.setDate(3, Date.valueOf(timeEntry.getDate()));
            statement.setInt(4, timeEntry.getHours());
            return statement;
        }, keyHolder);

        return find((keyHolder.getKey().longValue()));
    }

    @Override
    public TimeEntry find(long timeEntryId) {

        String sql = "SELECT * FROM time_entries WHERE id = ?";
        try {

            return jdbcTemplate.queryForObject(
                    sql,
                    new TimeEntryMapper(),
                    timeEntryId
            );
        } catch (DataAccessException e) {
            return null;
        }

    }

    @Override
    public List<TimeEntry> list() {
        String sql = "SELECT * FROM time_entries";
        return jdbcTemplate.query(sql, timeEntryRowMapper);
    }

    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        String sql = "UPDATE time_entries SET project_id= ?, user_id= ?, date= ?, hours= ?";
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, RETURN_GENERATED_KEYS);
            statement.setLong(1, timeEntry.getProjectId());
            statement.setLong(2, timeEntry.getUserId());
            statement.setDate(3, Date.valueOf(timeEntry.getDate()));
            statement.setInt(4, timeEntry.getHours());
            return statement;
        });
        return find(id);
    }

    @Override
    public void delete(long timeEntryId) {
        String sql = "DELETE FROM time_entries WHERE id=?";
        jdbcTemplate.update(sql, timeEntryId);
    }

    private static final class TimeEntryMapper implements RowMapper<TimeEntry> {
        public TimeEntry mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
            TimeEntry timeEntry = new TimeEntry(
                    resultSet.getLong("id"),
                    resultSet.getLong("project_Id"),
                    resultSet.getLong("user_id"),
                    resultSet.getDate("date").toLocalDate(),
                    resultSet.getInt("hours"));
            return timeEntry;
        }
    }
}
