package kg.erudit.common.inner;

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
    protected Boolean locked;

    //Авторизация
    public User(Integer id, String username, String name, String surname, String patronymic, String roleCode, String password, Boolean locked) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.role = new Role();
        this.role.setCode(roleCode);
        this.password = password;
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
}
