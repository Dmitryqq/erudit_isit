package kg.erudit.common.inner.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import kg.erudit.common.inner.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatListItem {
    protected Integer userId;
    private String chatId;
    protected String name;
    protected String surname;
    protected String patronymic;
    protected Role role;
    protected ChatMessage message;
    private Integer unreadCount;

    public void setMessage(ChatMessage message) {
        message.setChatId(null);
        this.message = message;
    }

    public ChatListItem(Integer userId, String name, String surname, String patronymic, String roleCode) {
        this.userId = userId;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.role = new Role();
        this.role.setCode(roleCode);
    }

    @Override
    public String toString() {
        return "ChatListItem{" +
                "userId=" + userId +
                ", chatId='" + chatId + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", role=" + role +
                ", message=" + message +
                ", unreadCount=" + unreadCount +
                '}';
    }
}
