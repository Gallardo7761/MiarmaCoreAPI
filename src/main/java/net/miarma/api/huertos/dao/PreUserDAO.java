package net.miarma.api.huertos.dao;

import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.sqlclient.Pool;
import net.miarma.api.common.db.DatabaseManager;
import net.miarma.api.common.db.DataAccessObject;
import net.miarma.api.common.db.QueryBuilder;
import net.miarma.api.huertos.entities.PreUserEntity;

public class PreUserDAO implements DataAccessObject<PreUserEntity> {

    private final DatabaseManager db;

    public PreUserDAO(Pool pool) {
        this.db = DatabaseManager.getInstance(pool);
    }

    @Override
    public Future<List<PreUserEntity>> getAll() {
        Promise<List<PreUserEntity>> promise = Promise.promise();
        String query = QueryBuilder.select(PreUserEntity.class).build();

        db.execute(query, PreUserEntity.class,
            list -> promise.complete(list.isEmpty() ? List.of() : list),
            promise::fail
        );

        return promise.future();
    }

    @Override
    public Future<PreUserEntity> insert(PreUserEntity preUser) {
        Promise<PreUserEntity> promise = Promise.promise();
        String query = QueryBuilder.insert(preUser).build();

        db.execute(query, PreUserEntity.class,
            list -> promise.complete(list.isEmpty() ? null : list.get(0)),
            promise::fail
        );

        return promise.future();
    }

    @Override
    public Future<PreUserEntity> update(PreUserEntity preUser) {
        Promise<PreUserEntity> promise = Promise.promise();
        String query = QueryBuilder.update(preUser).build();

        db.execute(query, PreUserEntity.class,
            list -> promise.complete(list.isEmpty() ? null : list.get(0)),
            promise::fail
        );

        return promise.future();
    }

    @Override
    public Future<PreUserEntity> delete(Integer id) {
        Promise<PreUserEntity> promise = Promise.promise();
        PreUserEntity preUser = new PreUserEntity();
        preUser.setPre_user_id(id);

        String query = QueryBuilder.delete(preUser).build();

        db.execute(query, PreUserEntity.class,
            list -> promise.complete(list.isEmpty() ? null : list.get(0)),
            promise::fail
        );

        return promise.future();
    }
}
