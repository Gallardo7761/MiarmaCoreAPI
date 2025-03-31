package net.miarma.core.sso.entities;

import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Row;
import net.miarma.core.common.Table;

@Table("users")
public class UserEntity {
    private Integer user_id;
    private String user_name;
    private String email;
    private String display_name;
    private String password;
    private String avatar;
    private int global_status;
    private int role;

    public UserEntity() {}

    public UserEntity(Row row) {
        this.user_id = row.getInteger("user_id");
        this.user_name = row.getString("user_name");
        this.email = row.getString("email");
        this.display_name = row.getString("display_name");
        this.password = row.getString("password");
        this.avatar = row.getString("avatar");
        this.global_status = row.getInteger("global_status");
        this.role = row.getInteger("role");
    }

    public Integer getUser_id() { return user_id; }
    public void setUser_id(Integer user_id) { this.user_id = user_id; }
    public String getUser_name() { return user_name; }
    public void setUser_name(String user_name) { this.user_name = user_name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDisplay_name() { return display_name; }
    public void setDisplay_name(String display_name) { this.display_name = display_name; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public int getGlobal_status() { return global_status; }
    public void setGlobal_status(int global_status) { this.global_status = global_status; }
    public int getRole() { return role; }
    public void setRole(int role) { this.role = role; }
    
    public String encode() {
		JsonObject json = new JsonObject();
		json.put("user_id", user_id);
		json.put("user_name", user_name);
		json.put("email", email);
		json.put("display_name", display_name);
		json.put("password", password);
		json.put("avatar", avatar);
		json.put("global_status", global_status);
		json.put("role", role);
		return json.toString();
    }
}