package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.DTO.FilmDTO;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Rating;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFound;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.requests.film.FilmCreateRequest;
import ru.yandex.practicum.filmorate.requests.film.FilmUpdateRequest;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Component("db_film_storage")
public class DbFilmStorage implements FilmStorage{
    private final JdbcTemplate jdbcTemplate;

    public DbFilmStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public FilmDTO add(FilmCreateRequest filmCreateRequest) {
        KeyHolder keyHolder = new GeneratedKeyHolder();


        String sql = "insert into films (name, description, release, duration, rating_id) " +
                "values (?,?,?,?,?);";

        int status = jdbcTemplate.update(con -> {
                    PreparedStatement stmt = con.prepareStatement(sql, new String[]{"film_id"});
                    stmt.setString(1,filmCreateRequest.getName());
                    stmt.setString(2,filmCreateRequest.getDescription());
                    stmt.setDate(3,Date.valueOf(filmCreateRequest.getReleaseDate()));
                    stmt.setLong(4, filmCreateRequest.getDuration());
                    stmt.setLong(5, filmCreateRequest.getMpa().getId());
                    return stmt;
                },
                keyHolder
        );

        if (status == 1) {
            return FilmMapper.fromFilmToDTO(findById(keyHolder.getKey().longValue()));
        }

        return null;
    }

    @Override
    public FilmDTO update(FilmUpdateRequest filmUpdateRequest) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "update films set name = ?, description = ?, release=?, duration=?, rating_id = ? " +
                "where film_id = ?;";

        int status = jdbcTemplate.update(con -> {
                    PreparedStatement stmt = con.prepareStatement(sql, new String[]{"film_id"});
                    stmt.setString(1,filmUpdateRequest.getName());
                    stmt.setString(2,filmUpdateRequest.getDescription());
                    stmt.setDate(3,Date.valueOf(filmUpdateRequest.getReleaseDate()));
                    stmt.setLong(4, filmUpdateRequest.getDuration());
                    stmt.setLong(5, filmUpdateRequest.getMpa().getId());
                    stmt.setLong(6, filmUpdateRequest.getId());
                    return stmt;
                },
                keyHolder
        );

        if (status == 1) {
            return FilmMapper.fromFilmToDTO(Film.builder()
                    .id(Objects.requireNonNull(keyHolder.getKey()).longValue())
                    .name(filmUpdateRequest.getName())
                    .description(filmUpdateRequest.getDescription())
                    .release(filmUpdateRequest.getReleaseDate())
                    .duration(Duration.ofMinutes(filmUpdateRequest.getDuration()))
                    .mpa(filmUpdateRequest.getMpa())
                    .build());
        }

        throw  new FilmNotFound("Film with id "+filmUpdateRequest.getId()+" not found");
    }

    @Override
    public FilmDTO getFilm(Long id) {
        String sql = "select * from films where film_id = ?;";
        FilmDTO filmDTO;

        try {
            filmDTO = FilmMapper.fromFilmToDTO(jdbcTemplate.queryForObject(sql, this::mapRowToFilm, id));
        } catch (EmptyResultDataAccessException e) {
            throw  new FilmNotFound("Film with id " +id+ " is not found");
        }

        return filmDTO;
    }

    @Override
    public List<FilmDTO> getAll() {
        String sql = "select * from films;";

        return FilmMapper.fromFilmsToDTOs(jdbcTemplate.query(sql, this::mapRowToFilm));
    }

    @Override
    public Film findById(Long id) {
        String sql = "select * from films where film_id = ?;";
        Film film;

        try {
            film = jdbcTemplate.queryForObject(sql, this::mapRowToFilm, id);
        } catch (EmptyResultDataAccessException e) {
            throw  new FilmNotFound("Film with id "+id+" not found");
        }

        return film;
    }

    @Override
    public List<Film> get() {
        String sql = "select * from films;";

        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    private List<Long> getLikes (Long id) {
        String sql = "select l.user_id from likes as l " +
                     "left join films as f on f.film_id = l.film_id " +
                     "where l.film_id = ?";

        return jdbcTemplate.query(sql, this::mapRowToUserLike, id);
    }

    private Long mapRowToUserLike (ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getLong("user_id");
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        String sql = "select * from mpa as m " +
                     "left join films as f on m.mpa_id = f.rating_id " +
                     "where f.film_id = ?";

        return Film.builder()
                .id(resultSet.getLong("film_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .release(resultSet.getDate("release").toLocalDate())
                .duration(Duration.ofMinutes(resultSet.getLong("duration")))
                .mpa(jdbcTemplate.queryForObject(sql, this::mapRowToRating, resultSet.getLong("film_id")))
                .likes(new HashSet<>(getLikes(resultSet.getLong("film_id"))))
                .build();
    }

    private Rating mapRowToRating(ResultSet resultSet, int rowNum) throws SQLException {
        return Rating.builder()
                .id(resultSet.getLong("rating_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .build();
    }
}
