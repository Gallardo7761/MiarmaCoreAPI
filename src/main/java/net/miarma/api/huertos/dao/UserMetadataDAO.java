package net.miarma.api.huertos.dao;

import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.sqlclient.Pool;
import net.miarma.api.common.db.DatabaseManager;
import net.miarma.api.common.db.DataAccessObject;
import net.miarma.api.common.db.QueryBuilder;
import net.miarma.api.huertos.entities.UserMetadataEntity;

public class UserMetadataDAO implements DataAccessObject<UserMetadataEntity> {
	
	private final DatabaseManager db;
	
	public UserMetadataDAO(Pool pool) {
		this.db = DatabaseManager.getInstance(pool);
	}

	@Override
	public Future<List<UserMetadataEntity>> getAll() {
		Promise<List<UserMetadataEntity>> promise = Promise.promise();
		String query = QueryBuilder.select(UserMetadataEntity.class).build();

		db.execute(query, UserMetadataEntity.class,
			list -> promise.complete(list.isEmpty() ? List.of() : list),
			promise::fail
		);

		return promise.future();
	}

	@Override
	public Future<UserMetadataEntity> insert(UserMetadataEntity user) {
		Promise<UserMetadataEntity> promise = Promise.promise();
		String query = QueryBuilder.insert(user).build();

		db.execute(query, UserMetadataEntity.class,
			list -> promise.complete(list.isEmpty() ? null : list.get(0)),
			promise::fail
		);

		return promise.future();
	}

	@Override
	public Future<UserMetadataEntity> update(UserMetadataEntity user) {
		Promise<UserMetadataEntity> promise = Promise.promise();
		String query = QueryBuilder.update(user).build();

		db.execute(query, UserMetadataEntity.class,
			list -> promise.complete(list.isEmpty() ? null : list.get(0)),
			promise::fail
		);

		return promise.future();
	}

	@Override
	public Future<UserMetadataEntity> delete(Integer id) {
		Promise<UserMetadataEntity> promise = Promise.promise();
		UserMetadataEntity user = new UserMetadataEntity();
		user.setUser_id(id);

		String query = QueryBuilder.delete(user).build();

		db.execute(query, UserMetadataEntity.class,
			list -> promise.complete(list.isEmpty() ? null : list.get(0)),
			promise::fail
		);

		return promise.future();
	}
}
