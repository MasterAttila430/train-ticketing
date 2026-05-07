package com.siemens.train.repo;

import com.siemens.train.model.Station;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class StationRepository {

    private final JdbcTemplate jdbc;

    public StationRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // Maps a ResultSet row to a Station object
    private final RowMapper<Station> rowMapper = (rs, rowNum) -> new Station(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("city")
    );

    public List<Station> findAll() {
        return jdbc.query("SELECT * FROM stations", rowMapper);
    }

    public Optional<Station> findById(Long id) {
        return jdbc.query("SELECT * FROM stations WHERE id = ?", rowMapper, id)
                .stream().findFirst();
    }

    public Station save(Station station) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO stations (name, city) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, station.getName());
            ps.setString(2, station.getCity());
            return ps;
        }, keyHolder);
        // Write the auto-generated id back to the object
        station.setId(keyHolder.getKey().longValue());
        return station;
    }

    public void update(Station station) {
        jdbc.update("UPDATE stations SET name = ?, city = ? WHERE id = ?",
                station.getName(), station.getCity(), station.getId());
    }

    public void deleteById(Long id) {
        jdbc.update("DELETE FROM stations WHERE id = ?", id);
    }
}