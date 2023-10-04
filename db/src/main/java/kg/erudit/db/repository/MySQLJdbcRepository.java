package kg.erudit.db.repository;

import kg.erudit.common.inner.Class;
import kg.erudit.common.inner.*;
import kg.erudit.db.props.MySQLDatabaseProperties;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Repository
@Log4j2
public class MySQLJdbcRepository {
    private final MySQLDatabaseProperties mySQLDatabaseProperties;
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public MySQLJdbcRepository(MySQLDatabaseProperties mySQLDatabaseProperties, @Qualifier("mySQLDataSource") DataSource dataSource) {
        this.mySQLDatabaseProperties = mySQLDatabaseProperties;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public User getUser(String username) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("username", username);
        return namedJdbcTemplate.query(mySQLDatabaseProperties.getQueries().get("GET_USER_BY_USERNAME_PASS"), parameters, rs -> {
            if (!rs.next()) return null;
            return new User(rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getString("patronymic"),
                    rs.getString("role_code"),
                    rs.getString("password"),
                    rs.getInt("locked") != 0);
        });
    }

    public List<Subject> getSubjects() {
        List<Subject> subjectList = new LinkedList<>();
        jdbcTemplate.query(mySQLDatabaseProperties.getQueries().get("GET_SUBJECTS"), rs -> {
            subjectList.add(new Subject(rs.getInt("id"), rs.getString("name")));
        });
        return subjectList;
    }

    public List<GradeType> getGradeTypes() {
        List<GradeType> gradeTypeList = new LinkedList<>();
        jdbcTemplate.query(mySQLDatabaseProperties.getQueries().get("GET_GRADE_TYPES"), rs -> {
            gradeTypeList.add(new GradeType(rs.getInt("id"), rs.getString("name"), rs.getFloat("weight")));
        });
        return gradeTypeList;
    }

    public List<Class> getClasses() {
        List<Class> classList = new LinkedList<>();
        jdbcTemplate.query(mySQLDatabaseProperties.getQueries().get("GET_CLASSES"), rs -> {
            classList.add(new Class(rs.getInt("id"), rs.getInt("number"), rs.getString("letter")));
        });
        return classList;
    }

    public List<Role> getRoles() {
        List<Role> roleList = new LinkedList<>();
        jdbcTemplate.query(mySQLDatabaseProperties.getQueries().get("GET_ROLES"), rs -> {
            roleList.add(new Role(rs.getInt("id"), rs.getString("code"), rs.getString("name")));
        });
        return roleList;
    }

    public List<User> getUsers() {
        List<User> usersList = new LinkedList<>();
        jdbcTemplate.query(mySQLDatabaseProperties.getQueries().get("GET_USERS"), rs -> {
            usersList.add(
                    new UserExtended(rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("name"),
                            rs.getString("surname"),
                            rs.getString("patronymic"),
                            rs.getInt("r_id"),
                            rs.getString("r_code"),
                            rs.getString("r_name"),
                            rs.getInt("locked") != 0,
                            rs.getTimestamp("create_date"))
            );
        });
        return usersList;
    }

    public UserExtended getUserDetails(Integer userId) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", userId);
        return namedJdbcTemplate.query(mySQLDatabaseProperties.getQueries().get("GET_USER_DETAILS"), parameters, rs -> {
            if (!rs.next())
                return null;
            return new UserExtended(rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getString("patronymic"),
                    rs.getInt("r_id"),
                    rs.getString("r_code"),
                    rs.getString("r_name"),
                    rs.getObject("student_class_id") != null ? rs.getInt("student_class_id") : null,
                    rs.getString("teacher_classes_ids"),
                    rs.getString("teacher_subjects_ids"),
                    rs.getString("teacher_subject_classes_ids"),
                    rs.getInt("locked") != 0,
                    rs.getDate("birth_date"),
                    rs.getTimestamp("create_date"));
        });
    }

    public void addClass(Class clazz) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(mySQLDatabaseProperties.getQueries().get("INSERT_CLASS"), Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, clazz.getNumber());
            preparedStatement.setString(2, clazz.getLetter());
            return preparedStatement;
        }, keyHolder);
        clazz.setId(keyHolder.getKey().intValue());
    }

    public void addGradeType(GradeType gradeType) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(mySQLDatabaseProperties.getQueries().get("INSERT_GRADE_TYPE"), Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, gradeType.getName());
            preparedStatement.setFloat(2, gradeType.getWeight());
            return preparedStatement;
        }, keyHolder);
        gradeType.setId(keyHolder.getKey().intValue());
    }

    public void addSubject(Subject subject) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(mySQLDatabaseProperties.getQueries().get("INSERT_SUBJECT"), Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, subject.getName());
            return preparedStatement;
        }, keyHolder);
        subject.setId(keyHolder.getKey().intValue());
    }

    public void addUser(UserExtended user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(mySQLDatabaseProperties.getQueries().get("INSERT_USER"), Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getSurname());
            preparedStatement.setString(4, user.getPatronymic());
            preparedStatement.setString(5, user.getPassword());
            preparedStatement.setInt(6, user.getRole().getId());
            preparedStatement.setDate(7, new java.sql.Date(user.getBirthDate().getTime()));
            return preparedStatement;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
    }

    public void addStudentClass(UserExtended user) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("user_id", user.getId())
                .addValue("class_id", user.getStudentClassId());
        namedJdbcTemplate.update(mySQLDatabaseProperties.getQueries().get("INSERT_USER_STUDENT"), parameters);
    }

    public void addTeacherClasses(UserExtended user) {
        jdbcTemplate.batchUpdate(mySQLDatabaseProperties.getQueries().get("INSERT_USER_TEACHER_CLASSES"),
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, user.getId());
                        ps.setInt(2, user.getTeacherClassIds().get(i));
                    }

                    public int getBatchSize() {
                        return user.getTeacherClassIds().size();
                    }
                });
    }

    public void addTeacherSubjects(UserExtended user) {
        jdbcTemplate.batchUpdate(mySQLDatabaseProperties.getQueries().get("INSERT_USER_TEACHER_SUBJECTS"),
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, user.getId());
                        ps.setInt(2, user.getTeacherSubjectIds().get(i));
                    }

                    public int getBatchSize() {
                        return user.getTeacherSubjectIds().size();
                    }
                });
    }

    public void linkSubjectAndClassWithTeacher(Integer userId, List<SubjectClass> subjectClasses) {
        jdbcTemplate.batchUpdate(mySQLDatabaseProperties.getQueries().get("INSERT_USER_TEACHER_SUBJECTS_CLASSES"),
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, userId);
                        ps.setInt(2, subjectClasses.get(i).getSubjectId());
                        ps.setInt(3, subjectClasses.get(i).getClassId());
                    }

                    public int getBatchSize() {
                        return subjectClasses.size();
                    }
                });
    }

    public void updateClass(Class clazz) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", clazz.getId()).addValue("number", clazz.getNumber()).addValue("letter", clazz.getLetter());
        namedJdbcTemplate.update(mySQLDatabaseProperties.getQueries().get("UPDATE_CLASS"), parameters);
    }

    public void updateGradeType(GradeType gradeType) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", gradeType.getId()).addValue("name", gradeType.getName()).addValue("weight", gradeType.getWeight());
        namedJdbcTemplate.update(mySQLDatabaseProperties.getQueries().get("UPDATE_GRADE_TYPES"), parameters);
    }

    public void updateSubject(Subject subject) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", subject.getId()).addValue("name", subject.getName());
        namedJdbcTemplate.update(mySQLDatabaseProperties.getQueries().get("UPDATE_SUBJECT"), parameters);
    }

    public void deleteClass(Integer classId) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", classId);
        namedJdbcTemplate.update(mySQLDatabaseProperties.getQueries().get("DELETE_CLASS"), parameters);
    }

    public void deleteGradeType(Integer gradeTypeId) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", gradeTypeId);
        namedJdbcTemplate.update(mySQLDatabaseProperties.getQueries().get("DELETE_GRADE_TYPE"), parameters);
    }


    public void deleteSubject(Integer subjectId) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", subjectId);
        namedJdbcTemplate.update(mySQLDatabaseProperties.getQueries().get("DELETE_SUBJECT"), parameters);
    }

    public void deleteUser(Integer userId) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", userId);
        namedJdbcTemplate.update(mySQLDatabaseProperties.getQueries().get("DELETE_USER"), parameters);
    }

    public void changeUserLock(Integer userId, Integer locked) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", userId)
                .addValue("locked", locked);
        namedJdbcTemplate.update(mySQLDatabaseProperties.getQueries().get("CHANGE_USER_LOCK"), parameters);
    }

    public List<Review> getMainReviews() {
        List<Review> reviewList = new ArrayList<>(3);
        jdbcTemplate.query(mySQLDatabaseProperties.getQueries().get("GET_MAIN_REVIEWS"), rs -> {
            reviewList.add(new Review(rs.getString("name"), rs.getString("role"), rs.getFloat("rating"), rs.getString("text")
//                    rs.getString("image"))
            ));
        });
        return reviewList;
    }

    public List<News> getMainNews(Integer limit, Integer offset) {
        List<News> newsList = new ArrayList<>(3);
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("limit", limit).addValue("offset", offset);
        namedJdbcTemplate.query(mySQLDatabaseProperties.getQueries().get("GET_NEWS"), parameters, rs -> {
            newsList.add(new News(rs.getString("url"), rs.getString("title"), rs.getString("text"), rs.getDate("create_date"), rs.getString("type_name"), rs.getString("image_name")));
        });
        return newsList;
    }

    public News getSingleNews(String url) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("url", url);
        return namedJdbcTemplate.query(mySQLDatabaseProperties.getQueries().get("GET_SINGLE_NEWS"), parameters, rs -> {
            if (!rs.next()) return null;
            News news = new News(rs.getString("url"), rs.getString("title"), rs.getString("text"), rs.getDate("create_date"), rs.getString("type_name"));
            news.setImages(Arrays.stream(rs.getString("images").split(";")).toList());
            return news;
        });
    }
}
