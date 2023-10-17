package kg.erudit.api.service;

import jakarta.annotation.PostConstruct;
import kg.erudit.api.util.FileUtil;
import kg.erudit.api.util.JwtUtil;
import kg.erudit.api.util.StringUtil;
import kg.erudit.common.exceptions.AuthenticateException;
import kg.erudit.common.inner.Class;
import kg.erudit.common.inner.*;
import kg.erudit.common.req.AuthRequest;
import kg.erudit.common.req.GradeRequest;
import kg.erudit.common.resp.*;
import kg.erudit.db.repository.MySQLJdbcRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Log4j2
public class ServiceWrapper {
    private final MySQLJdbcRepository mySQLJdbcRepository;
    private final FileUtil fileUtil;

//    @PostConstruct
//    void init() {
//        Map<String,ScheduleDay> scheduleDayMap = mySQLJdbcRepository.getScheduleDays(14);
//
//        List<ScheduleDay> scheduleDays = scheduleDayMap.values().stream().toList();
//        GetListResponse<ScheduleDay> response = new GetListResponse<>();
//        response.setItems(scheduleDays);
//        System.out.printf("");
//    }

//    @PostConstruct
//    void init() {
////        String pw_hash = BCrypt.hashpw("test", BCrypt.gensalt());
////        System.out.println(pw_hash);
//
//        List<Trimester> trimesterList = mySQLJdbcRepository.getTrimesters();
//        Trimester trimester = trimesterList.get(0);
//
//        Schedule schedule = new Schedule();
//        schedule.setClazz(3);
//        schedule.setTrimesterId(1);
//        schedule.setStartDate(trimester.getStartDate());
//        schedule.setEndDate(trimester.getEndDate());
//
//        mySQLJdbcRepository.addSchedule(schedule);
//
//        LocalDate startDate = DateUtil.convertToLocalDateViaInstant(new Date(trimester.getStartDate().getTime()));
//        LocalDate endDate = DateUtil.convertToLocalDateViaInstant(new Date(trimester.getEndDate().getTime()));
//        long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate);
//        System.out.println("Number of days between " + startDate + " and " + endDate + " is: " + numOfDaysBetween);
//        // iterate through the dates between start and end dates
//        LocalDate currentDate = startDate;
//        while (!currentDate.isAfter(endDate)) {
//            if (currentDate.getDayOfWeek().equals(DayOfWeek.SATURDAY) || currentDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
//                currentDate = currentDate.plusDays(1);
//                continue;
//            }
//            System.out.println(currentDate);
//            ScheduleDay scheduleDay = new ScheduleDay(DateUtil.convertToDateViaInstant(currentDate));
//            mySQLJdbcRepository.addScheduleDay(schedule.getId(), scheduleDay);
//            schedule.getDays().add(scheduleDay);
//
//            currentDate = currentDate.plusDays(1);
//        }
//        System.out.printf("");
//    }

    public ServiceWrapper(MySQLJdbcRepository mySQLJdbcRepository, FileUtil fileUtil) {
        this.mySQLJdbcRepository = mySQLJdbcRepository;
        this.fileUtil = fileUtil;
    }

    public HomePageResponse getHomePage() throws IOException {
        HomePageResponse response = new HomePageResponse();
        List<News> newsList = mySQLJdbcRepository.getNews(3, 0, false);
        for (News newsItem : newsList) {
            if (newsItem.getMainImage() == null)
                continue;
            String base64image = fileUtil.encodeFileToBase64Binary(newsItem.getMainImage().getFullFileName(), "news/" + newsItem.getUuid());
            newsItem.getMainImage().setBase64(base64image);
        }
        response.setNews(newsList);
        List<Review> reviewList = mySQLJdbcRepository.getMainReviews(3, 0, false);
        for (Review review : reviewList) {
            if (review.getImage() == null)
                continue;
            String base64image = fileUtil.encodeFileToBase64Binary(review.getImage().getFullFileName(), "reviews");
            review.getImage().setBase64(base64image);
        }
        response.setReviews(reviewList);
        return response;
    }

    public GetListResponse<Review> getReviews() throws IOException {
        GetListResponse<Review> response = new GetListResponse<>();
        List<Review> reviewList = mySQLJdbcRepository.getMainReviews(10, 0, true);
        for (Review review : reviewList) {
            if (review.getImage() == null)
                continue;
            String base64image = fileUtil.encodeFileToBase64Binary(review.getImage().getFullFileName(), "reviews");
            review.getImage().setBase64(base64image);
        }
        response.setItems(reviewList);
        return response;
    }


    public SingleItemResponse<Review> getReview(Integer reviewId) throws IOException {
        SingleItemResponse<Review> response = new SingleItemResponse<>();
        Review review = mySQLJdbcRepository.getReview(reviewId);
        response.setItem(review);
        if (review.getImage() == null)
            return response;
        String base64image = fileUtil.encodeFileToBase64Binary(review.getImage().getFullFileName(), "reviews");
        review.getImage().setBase64(base64image);
        return response;
    }

    public GetListResponse<News> getNews(Integer page) throws IOException {
        GetListResponse<News> response = new GetListResponse<>();
        List<News> newsList = mySQLJdbcRepository.getNews(10, 0, true);
        for (News newsItem : newsList) {
            if (newsItem.getMainImage() == null)
                continue;
            String base64image = fileUtil.encodeFileToBase64Binary(newsItem.getMainImage().getFullFileName(), "news/" + newsItem.getUuid());
            newsItem.getMainImage().setBase64(base64image);
        }
        response.setItems(newsList);
        return response;
    }

    public SingleItemResponse<NewsSingle> getNewsSingleItem(String url) throws IOException {
        return getNewsSingleSingleItemResponse(mySQLJdbcRepository.getSingleNews(url));
    }


    public SingleItemResponse<NewsSingle> getNewsSingleItem(Integer newsId) throws IOException {
        return getNewsSingleSingleItemResponse(mySQLJdbcRepository.getSingleNews(newsId));
    }

    private SingleItemResponse<NewsSingle> getNewsSingleSingleItemResponse(NewsSingle singleNews) throws IOException {
        SingleItemResponse<NewsSingle> response = new SingleItemResponse<>(singleNews);
        if (singleNews.getImages() == null || singleNews.getImages().isEmpty())
            return response;
        for (Image image : singleNews.getImages()) {
            String base64image = fileUtil.encodeFileToBase64Binary(image.getFullFileName(), "news/" + singleNews.getUuid());
            image.setBase64(base64image);
        }
        return response;
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

    public GetListResponse<NewsType> getNewsType() {
        GetListResponse<NewsType> response = new GetListResponse<>();
        response.setItems(mySQLJdbcRepository.getNewsType());
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

    public GetListResponse<Trimester> getTrimesters() {
        GetListResponse<Trimester> response = new GetListResponse<>();
        response.setItems(mySQLJdbcRepository.getTrimesters());
        return response;
    }

    public GetListResponse<ScheduleItemType> getScheduleItemTypes() {
        GetListResponse<ScheduleItemType> response = new GetListResponse<>();
        response.setItems(mySQLJdbcRepository.getScheduleItemTypes());
        return response;
    }

    public GetListResponse<ScheduleItem> getScheduleItemTemplate() {
        GetListResponse<ScheduleItem> response = new GetListResponse<>();
        response.setItems(mySQLJdbcRepository.getScheduleItemTemplate());
        return response;
    }

    public SingleItemResponse<Class> addClass(Class clazz) {
        mySQLJdbcRepository.addClass(clazz);
        return new SingleItemResponse<>(clazz, "Created");
    }

    public SingleItemResponse<NewsSingle> addNews(NewsSingle news) {
        String newsUuid = UUID.randomUUID().toString().replace("-", "");
        news.setUuid(newsUuid);
        news.setUrl(StringUtil.transliterate(news.getTitle()));
        mySQLJdbcRepository.addNews(news);
        for (Image image : news.getImages()) {
            String imageUuid = UUID.randomUUID().toString().replace("-", "");
            image.setFileName(imageUuid);
            fileUtil.saveFile(image, "news/" + newsUuid);
        }
        mySQLJdbcRepository.addNewsImage(news.getId(), news.getImages());
        return new SingleItemResponse<>(news, "Created");
    }

    public SingleItemResponse<GradeType> addGradeType(GradeType gradeType) {
        mySQLJdbcRepository.addGradeType(gradeType);
        return new SingleItemResponse<>(gradeType, "Created");
    }

    public SingleItemResponse<GradeType> newGrade(GradeRequest gradeRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        mySQLJdbcRepository.newGrade(gradeRequest, username);
        return new SingleItemResponse<>(null, "Created");
    }

    public SingleItemResponse<Subject> addSubject(Subject subject) {
        mySQLJdbcRepository.addSubject(subject);
        return new SingleItemResponse<>(subject, "Created");
    }

    public SingleItemResponse<Review> addReview(Review review) {
        if (review.getImage() != null) {
            String imageUuid = UUID.randomUUID().toString().replace("-", "");
            review.getImage().setFileName(imageUuid);
            fileUtil.saveFile(review.getImage(), "reviews");
        }
        mySQLJdbcRepository.addReview(review);
        return new SingleItemResponse<>(review, "Created");
    }

    public SingleItemResponse<UserExtended> addUser(UserExtended user) {
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        mySQLJdbcRepository.addUser(user);
        user.setPassword(null);
        switch (user.getRoleCode()) {
            case "TEACHER" -> {
                mySQLJdbcRepository.addTeacherClasses(user);
                mySQLJdbcRepository.addTeacherSubjects(user);
            }
            case "STUDENT" -> mySQLJdbcRepository.addStudentClass(user);
            default -> {
            }
        }
        return new SingleItemResponse<>(user, "Created");
    }

    public DefaultServiceResponse linkSubjectAndClassWithTeacher(Integer userId, List<SubjectClass> subjectClasses) {
        mySQLJdbcRepository.linkSubjectAndClassWithTeacher(userId, subjectClasses);
        return new DefaultServiceResponse("Linked");
    }

    public SingleItemResponse<Schedule> addSchedule(Schedule schedule) {
        mySQLJdbcRepository.addSchedule(schedule);
        return new SingleItemResponse<>(schedule, "Created");
    }

    public GetListResponse<ScheduleDay> fillSchedule(Integer scheduleId, List<ScheduleDayBase> scheduleDayList) {
        mySQLJdbcRepository.createScheduleTempTable(scheduleId, scheduleDayList);
        Map<String,ScheduleDay> scheduleDayMap = mySQLJdbcRepository.getScheduleDays(scheduleId);

        List<ScheduleDay> scheduleDays = scheduleDayMap.values().stream().toList();
        GetListResponse<ScheduleDay> response = new GetListResponse<>();
        response.setItems(scheduleDays);
        return response;
    }

    public DefaultServiceResponse updateClass(Class clazz) {
        mySQLJdbcRepository.updateClass(clazz);
        return new DefaultServiceResponse("Updated");
    }

    public DefaultServiceResponse updateUser(UserExtended user) {
        if (user.getPassword() != null) {
            user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            mySQLJdbcRepository.updateUser(user);
            user.setPassword(null);
        } else {
            mySQLJdbcRepository.updateUserNoPass(user);
        }

        switch (user.getRoleCode()) {
            case "TEACHER" -> {
                mySQLJdbcRepository.deleteTeacherClasses(user.getId());
                mySQLJdbcRepository.addTeacherClasses(user);
                mySQLJdbcRepository.deleteTeacherSubjects(user.getId());
                mySQLJdbcRepository.addTeacherSubjects(user);
            }
            case "STUDENT" -> mySQLJdbcRepository.updateUserStudentClass(user);
            default -> {
            }
        }
        return new DefaultServiceResponse("Updated");
    }

    public DefaultServiceResponse updateNews(NewsSingle news) throws IOException {
        mySQLJdbcRepository.updateNews(news);
        String filePath = "news/" + news.getUuid();
        List<String> fileNames = news.getImages().stream().map(Image::getFullFileName).toList();
        fileUtil.deleteOldFiles(filePath, fileNames);
        mySQLJdbcRepository.deleteOldNewsImages(news.getId(), fileNames);
        for (Image image : news.getImages()) {
            if (image.getFileName() != null) {
                mySQLJdbcRepository.updateNewsImage(image);
                news.getImages().remove(image);
                image = null;
                continue;
            }
            String imageUuid = UUID.randomUUID().toString().replace("-", "");
            image.setFileName(imageUuid);
            fileUtil.saveFile(image, "news/" + news.getUuid());
        }
        mySQLJdbcRepository.addNewsImage(news.getId(), news.getImages());
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

    public DefaultServiceResponse updateReview(Review review) {
        if (review.getImage().getFileName() == null) {
            Review oldReview = mySQLJdbcRepository.getReview(review.getId());
//            fileUtil.deleteFile("reviews/" + oldReview.getImage().getFullFileName());
            String imageUuid = oldReview.getImage() != null ? oldReview.getImage().getFileName() : UUID.randomUUID().toString().replace("-", "");
            review.getImage().setFileName(imageUuid);
            fileUtil.saveFile(review.getImage(), "reviews");
        }
        mySQLJdbcRepository.updateReview(review);
        return new DefaultServiceResponse("Updated");
    }

    public DefaultServiceResponse deleteClass(Integer classId) {
        mySQLJdbcRepository.deleteClass(classId);
        return new DefaultServiceResponse("Deleted");
    }

    public DefaultServiceResponse deleteNews(Integer newsId) throws IOException {
        String newsUuid = mySQLJdbcRepository.getSingleNewsUuidById(newsId);
        mySQLJdbcRepository.deleteNews(newsId);
        fileUtil.deleteDirectory("news/" + newsUuid);
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

    public DefaultServiceResponse deleteReview(Integer reviewId) throws IOException {
        Review review = mySQLJdbcRepository.getReview(reviewId);
        mySQLJdbcRepository.deleteReview(reviewId);
        if (review.getImage() != null) {
            fileUtil.deleteFile("reviews/" + review.getImage().getFullFileName());
        }
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
