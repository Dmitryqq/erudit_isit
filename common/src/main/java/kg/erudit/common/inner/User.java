package kg.erudit.common.inner;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    protected Integer id;
    protected String username;
    protected String name;
    protected String surname;
    protected String patronymic;
    protected Role role;
    protected String password;
    @JsonIgnore
    private Boolean pwdChangeRequired;
    protected Boolean locked;

    //Авторизация
    public User(Integer id, String username, String name, String surname, String patronymic,
                String roleCode, String password, Boolean pwdChangeRequired, Boolean locked) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.role = new Role();
        this.role.setCode(roleCode);
        this.password = password;
        this.pwdChangeRequired = pwdChangeRequired;
        this.locked = locked;
    }

    public User(Integer id, String username, String name, String surname, String patronymic, Integer roleId, String roleCode, String roleName, Boolean locked) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.role = new Role(roleId, roleCode, roleName);
        this.locked = locked;
    }

    public User(Integer id, String name, String surname, String patronymic) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", role=" + role +
                ", password='" + (password != null ? "..." : null) + '\'' +
                ", locked=" + locked +
                '}';
    }
}
