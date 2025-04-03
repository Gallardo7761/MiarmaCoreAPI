package net.miarma.api.huertos.services;

import java.util.List;

import io.vertx.core.Future;
import io.vertx.sqlclient.Pool;
import net.miarma.api.huertos.dao.ExpenseDAO;
import net.miarma.api.huertos.entities.ExpenseEntity;

public class ExpenseService {

	private final ExpenseDAO expenseDAO;

	public ExpenseService(Pool pool) {
		this.expenseDAO = new ExpenseDAO(pool);
	}

	public Future<List<ExpenseEntity>> getAll() {
		return expenseDAO.getAll();
	}

	public Future<ExpenseEntity> getById(Integer id) {
		return getAll().compose(expenses -> {
			ExpenseEntity expense = expenses.stream()
				.filter(e -> e.getExpense_id().equals(id))
				.findFirst()
				.orElse(null);
			return Future.succeededFuture(expense);
		});
	}

	public Future<ExpenseEntity> create(ExpenseEntity expense) {
		return expenseDAO.insert(expense);
	}

	public Future<ExpenseEntity> update(ExpenseEntity expense) {
		return expenseDAO.update(expense);
	}

	public Future<ExpenseEntity> delete(Integer id) {
		return expenseDAO.delete(id);
	}
}
