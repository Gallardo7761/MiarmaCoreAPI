package net.miarma.api.core.services;

import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Pool;
import net.miarma.api.common.Constants.CoreUserGlobalStatus;
import net.miarma.api.common.Constants.CoreUserRole;
import net.miarma.api.common.http.QueryParams;
import net.miarma.api.common.security.JWTManager;
import net.miarma.api.common.security.PasswordHasher;
import net.miarma.api.core.dao.UserDAO;
import net.miarma.api.core.entities.UserEntity;
import net.miarma.api.util.MessageUtil;

public class UserService {

    private final UserDAO userDAO;

    public UserService(Pool pool) {
        this.userDAO = new UserDAO(pool);
    }

    /* AUTHENTICATION */

    public Future<JsonObject> login(String emailOrUsername, String plainPassword, boolean keepLoggedIn) {
        return getByEmail(emailOrUsername).compose(user -> {
            if (user == null) {
                return getByUserName(emailOrUsername);
            }
            return Future.succeededFuture(user);
        }).compose(user -> {
            if (user == null || user.getGlobal_status() != CoreUserGlobalStatus.ACTIVE) {
                return Future.failedFuture("Invalid credentials");
            }

            if (!PasswordHasher.verify(plainPassword, user.getPassword())) {
                return Future.failedFuture("Invalid credentials");
            }

            JWTManager jwtManager = JWTManager.getInstance();
            String token = jwtManager.generateToken(user, keepLoggedIn);

            JsonObject response = new JsonObject()
                .put("token", token)
                .put("loggedUser", new JsonObject(user.encode()));

            return Future.succeededFuture(response);
        });
    }

    public Future<UserEntity> register(UserEntity user) {
        return getByEmail(user.getEmail()).compose(existing -> {
            if (existing != null) {
                return Future.failedFuture("User with this email already exists");
            }

            user.setPassword(PasswordHasher.hash(user.getPassword()));
            user.setRole(CoreUserRole.USER);
            user.setGlobal_status(CoreUserGlobalStatus.ACTIVE);

            return userDAO.insert(user);
        });
    }

    public Future<UserEntity> changePassword(int userId, String newPassword) {
        return getById(userId).compose(user -> {
            if (user == null) {
                return Future.failedFuture("User not found");
            }

            user.setPassword(PasswordHasher.hash(newPassword));
            return userDAO.update(user);
        });
    }

    public Future<Boolean> validateToken(String token) {
        JWTManager jwtManager = JWTManager.getInstance();
        return jwtManager.isValid(token) ?
            Future.succeededFuture(true) :
            Future.failedFuture("Invalid token");
    }

    /* USERS OPERATIONS */

    public Future<List<UserEntity>> getAll(QueryParams params) {
        return userDAO.getAll(params);
    }

    public Future<UserEntity> getById(Integer id) {
        return userDAO.getAll().compose(users -> {
            UserEntity found = users.stream()
                .filter(user -> user.getUser_id().equals(id))
                .findFirst()
                .orElse(null);
            return Future.succeededFuture(found);
        });
    }

    public Future<UserEntity> getByEmail(String email) {
        return userDAO.getAll().compose(users -> {
            UserEntity found = users.stream()
        		.filter(user -> user.getEmail() != null && user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
            return Future.succeededFuture(found);
        });
    }

    public Future<UserEntity> getByUserName(String userName) {
        return userDAO.getAll().compose(users -> {
            UserEntity found = users.stream()
                .filter(user -> user.getUser_name() != null && user.getUser_name().equals(userName))
                .findFirst()
                .orElse(null);
            return Future.succeededFuture(found);
        });
    }

    public Future<UserEntity> updateRole(Integer userId, CoreUserRole role) {
        return getById(userId).compose(user -> {
            if (user == null) {
                return Future.failedFuture("User not found in the database");
            }
            user.setRole(role);
            return userDAO.update(user);
        });
    }

    public Future<UserEntity> updateStatus(Integer userId, CoreUserGlobalStatus status) {
        return getById(userId).compose(user -> {
            if (user == null) {
                return Future.failedFuture("User not found in the database");
            }
            user.setGlobal_status(status);
            return userDAO.update(user);
        });
    }

    /* CRUD OPERATIONS */

    public Future<UserEntity> create(UserEntity user) {
        return register(user);
    }

    public Future<UserEntity> update(UserEntity user) {
        return userDAO.update(user);
    }

    public Future<UserEntity> delete(Integer id) {
        return getById(id).compose(user -> {
            if (user == null) {
                return Future.failedFuture(MessageUtil.notFound("User", "in the database"));
            }
            return userDAO.delete(id);
        });
    }
}