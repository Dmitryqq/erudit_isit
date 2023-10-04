package kg.erudit.api.service;

import kg.erudit.api.util.JwtUtil;
import kg.erudit.common.exceptions.AuthenticateException;
import kg.erudit.common.inner.Class;
import kg.erudit.common.inner.*;
import kg.erudit.common.req.AuthRequest;
import kg.erudit.common.resp.*;
import kg.erudit.db.repository.MySQLJdbcRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class ServiceWrapper {
    private final MySQLJdbcRepository mySQLJdbcRepository;

//    @PostConstruct
//    void init() {
//        String pw_hash = BCrypt.hashpw("test", BCrypt.gensalt());
//        System.out.println(pw_hash);
//    }

    public ServiceWrapper(MySQLJdbcRepository mySQLJdbcRepository) {
        this.mySQLJdbcRepository = mySQLJdbcRepository;
    }

    public HomePageResponse getHomePage() {
        HomePageResponse response = new HomePageResponse();
        response.setNews(mySQLJdbcRepository.getMainNews(3, 0));
        response.setReviews(mySQLJdbcRepository.getMainReviews());
        return response;
    }

    public GetListResponse<News> getNewsForHomePage() {
        GetListResponse<News> response = new GetListResponse<>();
        response.setItems(mySQLJdbcRepository.getMainNews(10, 0));
        return response;
    }

    public SingleItemResponse<News> getNewsSingleItem(String url) {
        News news = mySQLJdbcRepository.getSingleNews(url);
        return new SingleItemResponse<>(news);
    }

    public GetListResponse<Subject> getSubjects() {
        GetListResponse<Subject> response = new GetListResponse<>();
        response.setItems(mySQLJdbcRepository.getSubjects());
        return response;
    }

    public GetListResponse<GradeType> getGradeTypes() {
        GetListResponse<GradeType> response = new GetListResponse<>();
        response.setItems(mySQLJdbcRepository.getGradeTypes());
        return response;
    }

    public GetListResponse<Class> getClasses() {
        GetListResponse<Class> response = new GetListResponse<>();
        response.setItems(mySQLJdbcRepository.getClasses());
        return response;
    }
    public GetListResponse<Role> getRoles() {
        GetListResponse<Role> response = new GetListResponse<>();
        response.setItems(mySQLJdbcRepository.getRoles());
        return response;
    }

    public GetListResponse<User> getUsers() {
        GetListResponse<User> response = new GetListResponse<>();
        response.setItems(mySQLJdbcRepository.getUsers());
        return response;
    }

    public SingleItemResponse<UserExtended> getUserDetails(Integer userId) {
        return new SingleItemResponse<>(mySQLJdbcRepository.getUserDetails(userId));
    }

    public SingleItemResponse<Class> addClass(Class clazz) {
        mySQLJdbcRepository.addClass(clazz);
        return new SingleItemResponse<>(clazz, "Created");
    }

    public SingleItemResponse<GradeType> addGradeType(GradeType gradeType) {
        mySQLJdbcRepository.addGradeType(gradeType);
        return new SingleItemResponse<>(gradeType, "Created");
    }

    public SingleItemResponse<Subject> addSubject(Subject subject) {
        mySQLJdbcRepository.addSubject(subject);
        return new SingleItemResponse<>(subject, "Created");
    }

    public SingleItemResponse<UserExtended> addUser(UserExtended user) {
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        mySQLJdbcRepository.addUser(user);
        user.setPassword(null);
        switch (user.getRole().getCode()) {
            case "TEACHER" -> {
                mySQLJdbcRepository.addTeacherClasses(user);
                mySQLJdbcRepository.addTeacherSubjects(user);
            }
            case "STUDENT" -> mySQLJdbcRepository.addStudentClass(user);
            default -> {}
        }
        return new SingleItemResponse<>(user, "Created");
    }

    public DefaultServiceResponse linkSubjectAndClassWithTeacher(Integer userId, List<SubjectClass> subjectClasses) {
        mySQLJdbcRepository.linkSubjectAndClassWithTeacher(userId, subjectClasses);
        return new DefaultServiceResponse("Linked");
    }

    public DefaultServiceResponse updateClass(Class clazz) {
        mySQLJdbcRepository.updateClass(clazz);
        return new DefaultServiceResponse("Updated");
    }

    public DefaultServiceResponse updateGradeType(GradeType gradeType) {
        mySQLJdbcRepository.updateGradeType(gradeType);
        return new DefaultServiceResponse("Updated");
    }

    public DefaultServiceResponse updateSubject(Subject subject) {
        mySQLJdbcRepository.updateSubject(subject);
        return new DefaultServiceResponse("Updated");
    }

    public DefaultServiceResponse deleteClass(Integer classId) {
        mySQLJdbcRepository.deleteClass(classId);
        return new DefaultServiceResponse("Deleted");
    }

    public DefaultServiceResponse deleteGradeType(Integer gradeTypeId) {
        mySQLJdbcRepository.deleteGradeType(gradeTypeId);
        return new DefaultServiceResponse("Deleted");
    }

    public DefaultServiceResponse deleteSubject(Integer subjectId) {
        mySQLJdbcRepository.deleteSubject(subjectId);
        return new DefaultServiceResponse("Deleted");
    }

    public DefaultServiceResponse deleteUser(Integer userId) {
        mySQLJdbcRepository.deleteUser(userId);
        return new DefaultServiceResponse("Deleted");
    }

    public DefaultServiceResponse lockUser(Integer userId) {
        mySQLJdbcRepository.changeUserLock(userId, 1);
        return new DefaultServiceResponse("Locked");
    }

    public DefaultServiceResponse unlockUser(Integer userId) {
        mySQLJdbcRepository.changeUserLock(userId, 0);
        return new DefaultServiceResponse("Locked");
    }

    public AuthResponse authenticate(AuthRequest request) throws AuthenticateException {
        User user = mySQLJdbcRepository.getUser(request.getUsername());
        if (user == null)
            throw new AuthenticateException("Неверные авторизационные данные");
        if (user.getLocked())
            throw new AuthenticateException("Пользователь заблокирован");
        if (!BCrypt.checkpw(request.getPassword(), user.getPassword()))
            throw new AuthenticateException("Неверные авторизационные данные");

        String token = JwtUtil.getJwtToken(user);
        return new AuthResponse(token);
    }
}
