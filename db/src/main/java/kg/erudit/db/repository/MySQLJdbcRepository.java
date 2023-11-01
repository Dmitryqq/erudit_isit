package kg.erudit.db.repository;

import kg.erudit.common.inner.Class;
import kg.erudit.common.inner.*;
import kg.erudit.common.req.GradeRequest;
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
import java.sql.Date;
import java.sql.*;
import java.util.*;

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
            return new User(rs.getInt("id"), rs.getString("username"), rs.getString("name"), rs.getString("surname"), rs.getString("patronymic"), rs.getString("role_code"), rs.getString("password"), rs.getInt("pwd_change_required") != 0, rs.getInt("locked") != 0);
        });
    }

    public List<Subject> getSubjects(Boolean teacherRequired) {
        List<Subject> subjectList = new LinkedList<>();
        String query = mySQLDatabaseProperties.getQueries().get("GET_SUBJECTS");
        if (Boolean.TRUE.equals(teacherRequired))
            query += " WHERE teacher_required = 1";
        namedJdbcTemplate.query(query, rs -> {
            subjectList.add(new Subject(rs.getInt("id"), rs.getString("name"), rs.getInt("teacher_required") != 0));
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

    public List<NewsType> getNewsType() {
        List<NewsType> newsTypes = new LinkedList<>();
        jdbcTemplate.query(mySQLDatabaseProperties.getQueries().get("GET_NEWS_TYPES"), rs -> {
            newsTypes.add(new NewsType(rs.getInt("id"), rs.getString("name")));
        });
        return newsTypes;
    }

    public List<User> getUsers(Integer roleId) {
        List<User> usersList = new LinkedList<>();
        String request = mySQLDatabaseProperties.getQueries().get("GET_USERS");
        if (roleId != null)
            request += " WHERE u.role_id = " + roleId;
        jdbcTemplate.query(request, rs -> {
            usersList.add(new UserExtended(rs.getInt("id"), rs.getString("username"), rs.getString("name"), rs.getString("surname"), rs.getString("patronymic"), rs.getInt("r_id"), rs.getString("r_code"), rs.getString("r_name"), rs.getInt("locked") != 0, rs.getTimestamp("create_date")));
        });
        return usersList;
    }

    public UserExtended getUserDetails(Integer userId) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", userId);
        return namedJdbcTemplate.query(mySQLDatabaseProperties.getQueries().get("GET_USER_DETAILS"), parameters, rs -> {
            if (!rs.next()) return null;
            return new UserExtended(rs.getInt("id"), rs.getString("username"), rs.getString("name"), rs.getString("surname"), rs.getString("patronymic"), rs.getInt("r_id"), rs.getString("r_code"), rs.getString("r_name"), rs.getObject("student_class_id") != null ? rs.getInt("student_class_id") : null, rs.getString("teacher_classes_ids"), rs.getString("teacher_subjects_ids"), rs.getString("teacher_subject_classes_ids"), rs.getInt("locked") != 0, rs.getDate("birth_date"), rs.getTimestamp("create_date"));
        });
    }

    public List<Trimester> getTrimesters() {
        List<Trimester> trimesterList = new LinkedList<>();
        jdbcTemplate.query(mySQLDatabaseProperties.getQueries().get("GET_TRIMESTERS"), rs -> {
            trimesterList.add(new Trimester(rs.getInt("id"), rs.getString("name"), rs.getDate("start_date"), rs.getDate("end_date")));
        });
        return trimesterList;
    }

    public List<ScheduleItemType> getScheduleItemTypes() {
        List<ScheduleItemType> scheduleItemTypeList = new LinkedList<>();
        jdbcTemplate.query(mySQLDatabaseProperties.getQueries().get("GET_SCHEDULE_ITEM_TYPES"), rs -> {
            scheduleItemTypeList.add(new ScheduleItemType(rs.getInt("id"), rs.getString("name"), rs.getInt("duration_min")));
        });
        return scheduleItemTypeList;
    }

    public List<SubjectTeacher> getSubjectTeachersForClass(Integer classId) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("classId", classId);
        List<SubjectTeacher> subjectTeachers = new LinkedList<>();
        namedJdbcTemplate.query(mySQLDatabaseProperties.getQueries().get("GET_SUBJECT_TEACHERS_FOR_CLASS"), parameters, rs -> {
            subjectTeachers.add(new SubjectTeacher(rs.getInt("s_id"), rs.getString("s_name"),
                    rs.getInt("u_id"), rs.getString("u_name"), rs.getString("u_surname"), rs.getString("u_patronymic")));
        });
        return subjectTeachers;
    }

    public List<ScheduleItem> getScheduleItemTemplate() {
        List<ScheduleItem> scheduleItemList = new LinkedList<>();
        jdbcTemplate.query(mySQLDatabaseProperties.getQueries().get("GET_SCHEDULE_ITEM_TEMPLATE"), rs -> {
            scheduleItemList.add(new ScheduleItem(rs.getInt("type_id"), rs.getTime("start_time").toLocalTime(), rs.getTime("end_time").toLocalTime()));
        });
        return scheduleItemList;
    }

    public Schedule getSchedule(Integer scheduleId) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("scheduleId", scheduleId);
        return namedJdbcTemplate.query(mySQLDatabaseProperties.getQueries().get("GET_SCHEDULE"), parameters, rs -> {
            if (!rs.next()) return null;
            return new Schedule(rs.getInt("id"), rs.getInt("class_id"), rs.getInt("trimester_id"),
                    rs.getString("status"), rs.getDate("start_date"), rs.getDate("end_date"));
        });
    }

    public void fillUpSchedule(ScheduleCompleted schedule) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("classId", schedule.getClassId()).addValue("trimesterId", schedule.getTrimesterId());
        namedJdbcTemplate.query(mySQLDatabaseProperties.getQueries().get("GET_SCHEDULE_BY_CLASS_TRIMESTER"), parameters, rs -> {
//            if (!rs.next()) return;
            schedule.setId(rs.getInt("id"));
            schedule.setStatus(rs.getString("status"));
            schedule.setStartDate(rs.getDate("start_date"));
            schedule.setEndDate(rs.getDate("end_date"));
        });

    }

    public Map<String, ScheduleDay> getScheduleDays(Integer scheduleId, java.util.Date fromDate, java.util.Date toDate) {
        Map<String, ScheduleDay> scheduleDayMap = new LinkedHashMap<>();
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", scheduleId).addValue("fromDate", new java.sql.Date(fromDate.getTime()), Types.TIMESTAMP).addValue("toDate", new java.sql.Date(toDate.getTime()), Types.TIMESTAMP);
        namedJdbcTemplate.query(mySQLDatabaseProperties.getQueries().get("GET_SCHEDULE_BY_DATE_RANGE"), parameters, rs -> {
            scheduleDayMap.put(String.valueOf(rs.getInt("id")), new ScheduleDay(rs.getInt("id"), rs.getDate("date")));
        });
        this.getScheduleDaysItems(scheduleDayMap);
        return scheduleDayMap;
    }

    public Map<String, ScheduleDay> getScheduleDays(Integer scheduleId) {
        Map<String, ScheduleDay> scheduleDayMap = new LinkedHashMap<>();
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", scheduleId);
        namedJdbcTemplate.query(mySQLDatabaseProperties.getQueries().get("GET_CURRENT_WEEK_SCHEDULE_BY_ID"), parameters, rs -> {
            scheduleDayMap.put(String.valueOf(rs.getInt("id")), new ScheduleDay(rs.getInt("id"), rs.getDate("date")));
        });
        if (scheduleDayMap.isEmpty()) {
            namedJdbcTemplate.query(mySQLDatabaseProperties.getQueries().get("GET_FIRST_WEEK_SCHEDULE_BY_ID"), parameters, rs -> {
                scheduleDayMap.put(String.valueOf(rs.getInt("id")), new ScheduleDay(rs.getInt("id"), rs.getDate("date")));
            });
        }
        if (scheduleDayMap.isEmpty())
            return null;
        this.getScheduleDaysItems(scheduleDayMap);
        return scheduleDayMap;
    }

    public void getScheduleDaysItems(Map<String, ScheduleDay> scheduleDayMap) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("dayIds", scheduleDayMap.keySet().stream().toList());
        namedJdbcTemplate.query(mySQLDatabaseProperties.getQueries().get("GET_SCHEDULE_ITEMS_BY_DAY_IDS"), parameters, rs -> {
            scheduleDayMap.get(String.valueOf(rs.getInt("day_id"))).getItems().add(new ScheduleItem(rs.getInt("id"), rs.getInt("type_id"), rs.getObject("subject_id", Integer.class),
                    rs.getObject("teacher_id", Integer.class), rs.getString("status"), rs.getTime("start_time").toLocalTime(), rs.getTime("end_time").toLocalTime()));
        });
    }

    public Map<Integer, Diary> getDiary(String username, java.util.Date fromDate, java.util.Date toDate) {
        Map<Integer, Diary> diaryMap = new LinkedHashMap<>();
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("username", username)
                .addValue("fromDate", new java.sql.Date(fromDate.getTime()), Types.TIMESTAMP)
                .addValue("toDate", new java.sql.Date(toDate.getTime()), Types.TIMESTAMP);
        namedJdbcTemplate.query(mySQLDatabaseProperties.getQueries().get("GET_AUTH_STUDENT_DIARY"), parameters, rs -> {
            Diary diary = diaryMap.get(rs.getInt("day_id"));
            if (diary == null) {
                diary = new Diary(rs.getDate("date"));
                diaryMap.put(rs.getInt("day_id"), diary);
            }
            DiaryItem diaryItem = new DiaryItem(rs.getString("subject_name"), rs.getString("homework"), rs.getString("grades"),
                    rs.getInt("visited") != 0, rs.getTime("start_time").toLocalTime(), rs.getTime("end_time").toLocalTime());
            diary.getDiaryItems().add(diaryItem);
        });
        return diaryMap;
    }

    public Map<Integer, StudentItem> getDiaryByScheduleItemId(Integer scheduleItemId) {
        Map<Integer, StudentItem> diaryItems = new LinkedHashMap<>();
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("scheduleItemId", scheduleItemId);
        namedJdbcTemplate.query(mySQLDatabaseProperties.getQueries().get("GET_DIARY_BY_SCHEDULE_ITEM_ID"), parameters, rs -> {
            StudentItem studentItem = diaryItems.get(rs.getInt("u_id"));
            if (studentItem == null) {
                studentItem = new StudentItem(rs.getInt("u_id"), rs.getString("u_name"), rs.getString("u_surname"), rs.getString("u_patronymic"));
                diaryItems.put(studentItem.getId(), studentItem);
            }
            studentItem.addScheduleDay(rs.getInt("d_id"), rs.getDate("date"), rs.getInt("si_id"),
                    rs.getString("grades"), rs.getInt("visited") != 0, rs.getTime("start_time").toLocalTime(), rs.getTime("end_time").toLocalTime());
        });
        return diaryItems;
    }

    public Map<Integer, StudentItem> getDiaryByClassAndSubjectIds(Integer classId, Integer subjectId, Integer teacherId) {
        Map<Integer, StudentItem> diaryItems = new LinkedHashMap<>();
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("classId", classId)
                .addValue("subjectId", subjectId)
                .addValue("teacherId", teacherId);
        namedJdbcTemplate.query(mySQLDatabaseProperties.getQueries().get("GET_DIARY_BY_CLASS_AND_SUBJECT_IDS"), parameters, rs -> {
            StudentItem studentItem = diaryItems.get(rs.getInt("u_id"));
            if (studentItem == null) {
                studentItem = new StudentItem(rs.getInt("u_id"), rs.getString("u_name"), rs.getString("u_surname"), rs.getString("u_patronymic"));
                diaryItems.put(studentItem.getId(), studentItem);
            }
            studentItem.addScheduleDay(rs.getInt("d_id"), rs.getDate("date"), rs.getInt("si_id"),
                    rs.getString("grades"), rs.getInt("visited") != 0, rs.getTime("start_time").toLocalTime(), rs.getTime("end_time").toLocalTime());
        });
        return diaryItems;
    }


    public List<ScheduleItemDashboard> getStudentDashboardSchedule(Integer userId, java.util.Date date) {
        List<ScheduleItemDashboard> scheduleItems = new LinkedList<>();
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("userId", userId)
                .addValue("date", new java.sql.Date(date.getTime()), Types.TIMESTAMP);
        namedJdbcTemplate.query(mySQLDatabaseProperties.getQueries().get("GET_SCHEDULE_ITEMS_STUDENT_DASHBOARD"), parameters, rs -> {
            scheduleItems.add(new ScheduleItemDashboard(rs.getInt("id"), rs.getInt("sit_id"), rs.getString("sit_name"),
                    rs.getInt("sj_id"), rs.getString("sj_name"), rs.getTime("start_time").toLocalTime(), rs.getTime("end_time").toLocalTime()));
        });
        return scheduleItems;
    }

    public List<ScheduleItemDashboard> getTeacherDashboardSchedule(Integer userId, java.util.Date date) {
        List<ScheduleItemDashboard> scheduleItems = new LinkedList<>();
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("userId", userId)
                .addValue("date", new java.sql.Date(date.getTime()), Types.TIMESTAMP);
        namedJdbcTemplate.query(mySQLDatabaseProperties.getQueries().get("GET_SCHEDULE_ITEMS_TEACHER_DASHBOARD"), parameters, rs -> {
            scheduleItems.add(new ScheduleItemDashboard(rs.getInt("id"), rs.getInt("sit_id"), rs.getString("sit_name"),
                    rs.getInt("sj_id"), rs.getString("sj_name"), rs.getTime("start_time").toLocalTime(), rs.getTime("end_time").toLocalTime()));
        });
        return scheduleItems;
    }

    public List<HomeworkDate> getNextLessonDates(Integer scheduleItemId) {
        List<HomeworkDate> homeworkDates = new LinkedList<>();
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("scheduleItemId", scheduleItemId);
        namedJdbcTemplate.query(mySQLDatabaseProperties.getQueries().get("GET_HOMEWORK_NEXT_DATES"), parameters, rs -> {
            homeworkDates.add(new HomeworkDate(rs.getInt("id"), rs.getDate("date"), rs.getTime("start_time").toLocalTime(), rs.getTime("end_time").toLocalTime()));
        });
        return homeworkDates;
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

    public void addNews(NewsSingle news) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(mySQLDatabaseProperties.getQueries().get("INSERT_NEWS"), Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, news.getUuid());
            preparedStatement.setString(2, news.getUrl());
            preparedStatement.setString(3, news.getTitle());
            preparedStatement.setString(4, news.getText());
            preparedStatement.setInt(5, news.getTypeId());
            return preparedStatement;
        }, keyHolder);
        news.setId(keyHolder.getKey().intValue());
    }

    public void addNewsImage(Integer newsId, List<Image> imageList) {
        jdbcTemplate.batchUpdate(mySQLDatabaseProperties.getQueries().get("INSERT_NEWS_IMAGE"), new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, newsId);
                ps.setString(2, imageList.get(i).getFullFileName());
                ps.setInt(3, Boolean.TRUE.equals(imageList.get(i).getMain()) ? 1 : 0);
            }

            public int getBatchSize() {
                return imageList.size();
            }
        });
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
            preparedStatement.setInt(2, Boolean.TRUE.equals(subject.getTeacherRequired()) ? 1 : 0);
            return preparedStatement;
        }, keyHolder);
        subject.setId(keyHolder.getKey().intValue());
    }

    public void addReview(Review review) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("name", review.getName()).addValue("role", review.getRole()).addValue("rating", review.getRating()).addValue("text", review.getText()).addValue("image", review.getImage().getFullFileName());
        namedJdbcTemplate.update(mySQLDatabaseProperties.getQueries().get("INSERT_REVIEW"), parameters);
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
            preparedStatement.setString(6, user.getRoleCode());
            preparedStatement.setDate(7, new java.sql.Date(user.getBirthDate().getTime()));
            return preparedStatement;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
    }

    public void addStudentClass(UserExtended user) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("user_id", user.getId()).addValue("class_id", user.getStudentClassId());
        namedJdbcTemplate.update(mySQLDatabaseProperties.getQueries().get("INSERT_USER_STUDENT"), parameters);
    }

    public void addTeacherClasses(UserExtended user) {
        jdbcTemplate.batchUpdate(mySQLDatabaseProperties.getQueries().get("INSERT_USER_TEACHER_CLASSES"), new BatchPreparedStatementSetter() {
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
        jdbcTemplate.batchUpdate(mySQLDatabaseProperties.getQueries().get("INSERT_USER_TEACHER_SUBJECTS"), new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, user.getId());
                ps.setInt(2, user.getTeacherSubjectIds().get(i));
            }

            public int getBatchSize() {
                return user.getTeacherSubjectIds().size();
            }
        });
    }

    public void linkSubjectAndClassWithTeacher(UserExtended user) {
        jdbcTemplate.batchUpdate(mySQLDatabaseProperties.getQueries().get("INSERT_USER_TEACHER_SUBJECTS_CLASSES"), new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, user.getId());
                ps.setInt(2, user.getTeacherSubjectClassesIds().get(i).getSubjectId());
                ps.setInt(3, user.getTeacherSubjectClassesIds().get(i).getClassId());
            }

            public int getBatchSize() {
                return user.getTeacherSubjectClassesIds().size();
            }
        });
    }

    public void addSchedule(Schedule schedule) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(mySQLDatabaseProperties.getQueries().get("INSERT_SCHEDULE"), Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, schedule.getClassId());
            preparedStatement.setInt(2, schedule.getTrimesterId());
            preparedStatement.setDate(3, new java.sql.Date(schedule.getStartDate().getTime()));
            preparedStatement.setDate(4, new java.sql.Date(schedule.getEndDate().getTime()));
            return preparedStatement;
        }, keyHolder);
        schedule.setId(keyHolder.getKey().intValue());
    }

    public void updateScheduleStatus(Integer scheduleId, String status) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("scheduleId", scheduleId)
                .addValue("status", status);
        namedJdbcTemplate.update(mySQLDatabaseProperties.getQueries().get("UPDATE_SCHEDULE_STATUS"), parameters);
    }

    public void insertTempTableAndFillSchedule(Integer scheduleId, List<ScheduleDayBase> scheduleDayList) {
        for (ScheduleDayBase scheduleDay : scheduleDayList) {
            jdbcTemplate.batchUpdate(mySQLDatabaseProperties.getQueries().get("INSERT_TEMP_SCHEDULE_ITEM"), new BatchPreparedStatementSetter() {
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    ps.setInt(1, scheduleId);
                    ps.setInt(2, scheduleDay.getDayNumber());
                    ps.setInt(3, scheduleDay.getItems().get(i).getTypeId());
                    if (scheduleDay.getItems().get(i).getTypeId() == 1 && scheduleDay.getItems().get(i).getSubjectId() != null && scheduleDay.getItems().get(i).getUserId() != null) {
                        ps.setInt(4, scheduleDay.getItems().get(i).getSubjectId());
                        if (scheduleDay.getItems().get(i).getUserId() != null)
                            ps.setInt(5, scheduleDay.getItems().get(i).getUserId());
                        else
                            ps.setNull(5, Types.INTEGER);
                    } else {
                        ps.setNull(4, Types.INTEGER);
                        ps.setNull(5, Types.INTEGER);
                    }
                    ps.setTime(6, Time.valueOf(scheduleDay.getItems().get(i).getStartTime()));
                    ps.setTime(7, Time.valueOf(scheduleDay.getItems().get(i).getEndTime()));
                }

                public int getBatchSize() {
                    return scheduleDay.getItems().size();
                }
            });
        }
        jdbcTemplate.update(mySQLDatabaseProperties.getProcedures().get("FILL_SCHEDULE_FROM_TEMP"), scheduleId);
    }

    public void newGrade(GradeRequest gradeRequest, Integer userId) {
        jdbcTemplate.update(mySQLDatabaseProperties.getProcedures().get("NEW_GRADE"),
                gradeRequest.getScheduleItemId(), gradeRequest.getStudentId(), gradeRequest.getGradeTypeId(), gradeRequest.getValue(), userId);
    }

    public void addScheduleDay(Integer scheduleId, ScheduleDay scheduleDay) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(mySQLDatabaseProperties.getQueries().get("INSERT_SCHEDULE_DAY"), Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, scheduleId);
            preparedStatement.setDate(2, new Date(scheduleDay.getDate().getTime()));
            return preparedStatement;
        }, keyHolder);
        scheduleDay.setId(keyHolder.getKey().intValue());
    }

    public void addHomework(Homework homework, String username) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(mySQLDatabaseProperties.getQueries().get("INSERT_HOMEWORK"), Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, homework.getScheduleItemId());
            preparedStatement.setString(2, homework.getText());
            preparedStatement.setString(3, username);
            return preparedStatement;
        }, keyHolder);
        homework.setId(keyHolder.getKey().intValue());
    }

    public int updateStudentVisit(Integer scheduleItemId, Integer studentId, Integer visited) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("scheduleItemId", scheduleItemId)
                .addValue("studentId", studentId).addValue("visited", visited);
        return namedJdbcTemplate.update(mySQLDatabaseProperties.getQueries().get("UPDATE_STUDENT_VISIT"), parameters);
    }

    public int addStudentVisit(Integer scheduleItemId, Integer studentId, Integer visited) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("scheduleItemId", scheduleItemId)
                .addValue("studentId", studentId).addValue("visited", visited);
        return namedJdbcTemplate.update(mySQLDatabaseProperties.getQueries().get("INSERT_STUDENT_VISIT"), parameters);
    }

    public void updateScheduleDay(List<ScheduleItem> scheduleItems) {
        //TODO добавить новые итемы без id, удалить те id что не пришли
        jdbcTemplate.batchUpdate(mySQLDatabaseProperties.getQueries().get("UPDATE_SCHEDULE_DAY"), new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, scheduleItems.get(i).getTypeId());
                if (scheduleItems.get(i).getTypeId() == 1 && scheduleItems.get(i).getSubjectId() != null) {
                    ps.setInt(2, scheduleItems.get(i).getSubjectId());
                    if (scheduleItems.get(i).getUserId() != null)
                        ps.setInt(3, scheduleItems.get(i).getUserId());
                    else
                        ps.setNull(3, Types.INTEGER);
                } else {
                    ps.setNull(2, Types.INTEGER);
                    ps.setNull(3, Types.INTEGER);
                }
                ps.setTime(4, Time.valueOf(scheduleItems.get(i).getStartTime()));
                ps.setTime(5, Time.valueOf(scheduleItems.get(i).getEndTime()));
                ps.setInt(6, scheduleItems.get(i).getId());
            }

            public int getBatchSize() {
                return scheduleItems.size();
            }
        });
    }

    public void updateClass(Class clazz) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", clazz.getId()).addValue("number", clazz.getNumber()).addValue("letter", clazz.getLetter());
        namedJdbcTemplate.update(mySQLDatabaseProperties.getQueries().get("UPDATE_CLASS"), parameters);
    }

    public void updateUser(UserExtended user) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", user.getId()).addValue("username", user.getUsername()).addValue("name", user.getName()).addValue("surname", user.getSurname()).addValue("patronymic", user.getPatronymic()).addValue("password", user.getPassword()).addValue("birth_date", user.getBirthDate());
        namedJdbcTemplate.update(mySQLDatabaseProperties.getQueries().get("UPDATE_USER"), parameters);
    }

    public void updateUserPassword(String username, String newPassword) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("username", username).addValue("password", newPassword);
        namedJdbcTemplate.update(mySQLDatabaseProperties.getQueries().get("UPDATE_USER_PASSWORD"), parameters);
    }

    public void updateUserNoPass(UserExtended user) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", user.getId()).addValue("username", user.getUsername()).addValue("name", user.getName()).addValue("surname", user.getSurname()).addValue("patronymic", user.getPatronymic()).addValue("birth_date", user.getBirthDate());
        namedJdbcTemplate.update(mySQLDatabaseProperties.getQueries().get("UPDATE_USER_NO_PASS"), parameters);
    }

    public void updateUserStudentClass(UserExtended user) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("userId", user.getId()).addValue("classId", user.getStudentClassId());
        namedJdbcTemplate.update(mySQLDatabaseProperties.getQueries().get("UPDATE_STUDENT_CLASS"), parameters);
    }

    public void updateNews(NewsSingle news) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", news.getId()).addValue("title", news.getTitle()).addValue("text", news.getText()).addValue("type_id", news.getTypeId());
        namedJdbcTemplate.update(mySQLDatabaseProperties.getQueries().get("UPDATE_NEWS"), parameters);
    }

    public void updateNewsImage(Image image) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("fileName", image.getFullFileName()).addValue("isMain", Boolean.TRUE.equals(image.getMain()) ? 1 : 0);
        namedJdbcTemplate.update(mySQLDatabaseProperties.getQueries().get("UPDATE_NEWS_IMAGE"), parameters);
    }

    public void updateReview(Review review) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", review.getId()).addValue("name", review.getName()).addValue("role", review.getRole()).addValue("rating", review.getRating()).addValue("text", review.getText()).addValue("image", review.getImage().getFullFileName());
        namedJdbcTemplate.update(mySQLDatabaseProperties.getQueries().get("UPDATE_REVIEW"), parameters);
    }

    public void updateGradeType(GradeType gradeType) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", gradeType.getId()).addValue("name", gradeType.getName()).addValue("weight", gradeType.getWeight());
        namedJdbcTemplate.update(mySQLDatabaseProperties.getQueries().get("UPDATE_GRADE_TYPE"), parameters);
    }

    public void updateSubject(Subject subject) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", subject.getId())
                .addValue("name", subject.getName())
                .addValue("teacher_required", Boolean.TRUE.equals(subject.getTeacherRequired()) ? 1 : 0);
        namedJdbcTemplate.update(mySQLDatabaseProperties.getQueries().get("UPDATE_SUBJECT"), parameters);
    }

    public void deleteClass(Integer classId) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", classId);
        namedJdbcTemplate.update(mySQLDatabaseProperties.getQueries().get("DELETE_CLASS"), parameters);
    }

    public void deleteNews(Integer newsId) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", newsId);
        namedJdbcTemplate.update(mySQLDatabaseProperties.getQueries().get("DELETE_NEWS"), parameters);
    }

    public void deleteGradeType(Integer gradeTypeId) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", gradeTypeId);
        namedJdbcTemplate.update(mySQLDatabaseProperties.getQueries().get("DELETE_GRADE_TYPE"), parameters);
    }

    public void deleteSubject(Integer subjectId) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", subjectId);
        namedJdbcTemplate.update(mySQLDatabaseProperties.getQueries().get("DELETE_SUBJECT"), parameters);
    }

    public void deleteReview(Integer reviewId) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", reviewId);
        namedJdbcTemplate.update(mySQLDatabaseProperties.getQueries().get("DELETE_REVIEW"), parameters);
    }

    public void deleteUser(Integer userId) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", userId);
        namedJdbcTemplate.update(mySQLDatabaseProperties.getQueries().get("DELETE_USER"), parameters);
    }

    public void deleteTeacherClasses(Integer userId) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("userId", userId);
        namedJdbcTemplate.update(mySQLDatabaseProperties.getQueries().get("DELETE_TEACHER_CLASSES"), parameters);
    }

    public void deleteTeacherSubjects(Integer userId) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("userId", userId);
        namedJdbcTemplate.update(mySQLDatabaseProperties.getQueries().get("DELETE_TEACHER_SUBJECTS"), parameters);
    }

    public void deleteTeacherSubjectAndClass(Integer userId) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("userId", userId);
        namedJdbcTemplate.update(mySQLDatabaseProperties.getQueries().get("DELETE_TEACHER_SUBJECT_CLASSES"), parameters);
    }

    public void deleteOldNewsImages(Integer newsId, List<String> fileNames) {
//        String fileNamesStr = fileNames.stream().collect(Collectors.joining("','", "'", "'"));
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("newsId", newsId).addValue("fileNames", fileNames);
        namedJdbcTemplate.update(mySQLDatabaseProperties.getQueries().get("DELETE_OLD_NEWS_IMAGES"), parameters);
    }

    public void changeUserLock(Integer userId, Integer locked) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", userId).addValue("locked", locked);
        namedJdbcTemplate.update(mySQLDatabaseProperties.getQueries().get("CHANGE_USER_LOCK"), parameters);
    }

    public List<Review> getMainReviews(Integer limit, Integer offset, Boolean getId) {
        List<Review> reviewList = new ArrayList<>(limit);
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("limit", limit).addValue("offset", offset);
        namedJdbcTemplate.query(mySQLDatabaseProperties.getQueries().get("GET_REVIEWS"), parameters, rs -> {
            Review review = new Review(rs.getString("name"), rs.getString("role"), rs.getFloat("rating"), rs.getString("text"), rs.getString("image"));
            if (getId) review.setId(rs.getInt("id"));
            reviewList.add(review);
        });
        return reviewList;
    }


    public Review getReview(Integer reviewId) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", reviewId);
        return namedJdbcTemplate.query(mySQLDatabaseProperties.getQueries().get("GET_SINGLE_REVIEW"), parameters, rs -> {
            if (!rs.next()) return null;
            return new Review(rs.getInt("id"), rs.getString("name"), rs.getString("role"), rs.getFloat("rating"), rs.getString("text"), rs.getString("image"));
        });
    }

    public List<News> getNews(Integer limit, Integer offset, Boolean getId) {
        List<News> newsList = new ArrayList<>(3);
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("limit", limit).addValue("offset", offset);
        namedJdbcTemplate.query(mySQLDatabaseProperties.getQueries().get("GET_NEWS"), parameters, rs -> {
            News news = new News(rs.getString("uuid"), rs.getString("url"), rs.getString("title"), rs.getString("text"), rs.getDate("create_date"), rs.getString("type_name"), rs.getString("image_name"));
            if (getId) news.setId(rs.getInt("id"));
            newsList.add(news);
        });
        return newsList;
    }

    public NewsSingle getSingleNews(String url) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("url", url);
        return namedJdbcTemplate.query(mySQLDatabaseProperties.getQueries().get("GET_SINGLE_NEWS"), parameters, rs -> {
            if (!rs.next()) return null;
            NewsSingle news = new NewsSingle(rs.getString("uuid"), rs.getString("url"), rs.getString("title"), rs.getString("text"), rs.getDate("create_date"), rs.getString("type_name"));
            return getNewsSingle(rs, news);
        });
    }


    public NewsSingle getSingleNews(Integer newsId) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", newsId);
        return namedJdbcTemplate.query(mySQLDatabaseProperties.getQueries().get("GET_SINGLE_NEWS_BY_ID"), parameters, rs -> {
            if (!rs.next()) return null;
            NewsSingle news = new NewsSingle(rs.getInt("id"), rs.getInt("type_id"), rs.getString("uuid"), rs.getString("url"), rs.getString("title"), rs.getString("text"), rs.getDate("create_date"));
            return getNewsSingle(rs, news);
        });
    }

    private NewsSingle getNewsSingle(ResultSet rs, NewsSingle news) throws SQLException {
        if (rs.getString("images") == null) return news;
        List<String> imageNames = Arrays.stream(rs.getString("images").split(";")).toList();
        if (imageNames.isEmpty()) return news;
        List<Image> imageList = new LinkedList<>();
        for (String imageName : imageNames) {
            String[] imageParts = imageName.split("_");
            String[] imageParts1 = imageParts[0].split("\\.");
            imageList.add(new Image(imageParts1[0], imageParts1[1], imageParts[1]));
        }
        news.setImages(imageList);
        return news;
    }

    public String getSingleNewsUuidById(Integer newsId) {
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("newsId", newsId);
        return namedJdbcTemplate.query(mySQLDatabaseProperties.getQueries().get("GET_SINGLE_NEWS_UUID_BY_ID"), parameters, rs -> {
            if (!rs.next()) return null;
            return rs.getString("uuid");
        });
    }

//    public List<Image> imageList(Integer newsId) {
//        List<Image> imageList = new ArrayList<>();
//        SqlParameterSource parameters = new MapSqlParameterSource().addValue("newsId", newsId);
//        namedJdbcTemplate.query(mySQLDatabaseProperties.getQueries().get("GET_NEWS_IMAGES"), parameters, rs -> {
//            imageList.add(new ArrayList<>(rs.getString("uuid"), rs.getString("url"), rs.getString("title"), rs.getString("text"),
//                    rs.getDate("create_date"), rs.getString("type_name"), rs.getString("image_name")));
//        });
//        return imageList;
//    }
}
