package net.miarma.api.huertos.dao;

import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.sqlclient.Pool;
import net.miarma.api.common.db.DatabaseManager;
import net.miarma.api.common.db.DataAccessObject;
import net.miarma.api.common.db.QueryBuilder;
import net.miarma.api.huertos.entities.BalanceEntity;

public class BalanceDAO implements DataAccessObject<BalanceEntity> {

    private final DatabaseManager db;

    public BalanceDAO(Pool pool) {
        this.db = DatabaseManager.getInstance(pool);
    }

    @Override
    public Future<List<BalanceEntity>> getAll() {
        Promise<List<BalanceEntity>> promise = Promise.promise();
        String query = QueryBuilder.select(BalanceEntity.class).build();

        db.execute(query, BalanceEntity.class,
            list -> promise.complete(list.isEmpty() ? List.of() : list),
            promise::fail
        );

        return promise.future();
    }

    @Override
    public Future<BalanceEntity> insert(BalanceEntity balance) {
        Promise<BalanceEntity> promise = Promise.promise();
        String query = QueryBuilder.insert(balance).build();

        db.execute(query, BalanceEntity.class,
            list -> promise.complete(list.isEmpty() ? null : list.get(0)),
            promise::fail
        );

        return promise.future();
    }

    @Override
    public Future<BalanceEntity> update(BalanceEntity balance) {
        Promise<BalanceEntity> promise = Promise.promise();
        String query = QueryBuilder.update(balance).build();

        db.execute(query, BalanceEntity.class,
            list -> promise.complete(list.isEmpty() ? null : list.get(0)),
            promise::fail
        );

        return promise.future();
    }

    @Override
    public Future<BalanceEntity> delete(Integer id) {
        Promise<BalanceEntity> promise = Promise.promise();
        BalanceEntity balance = new BalanceEntity();
        balance.setId(id);

        String query = QueryBuilder.delete(balance).build();

        db.execute(query, BalanceEntity.class,
            list -> promise.complete(list.isEmpty() ? null : list.get(0)),
            promise::fail
        );

        return promise.future();
    }
}
