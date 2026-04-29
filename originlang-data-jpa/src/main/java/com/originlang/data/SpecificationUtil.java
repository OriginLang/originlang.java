package com.originlang.data;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * 仿照 Lombok @Builder 模式的通用 Specification 构建器
 */
final class SpecificationUtil {

	private SpecificationUtil() {
		// 工具类，禁止实例化
	}

	/**
	 * 通用的 Specification 构建器 仿照 Lombok @Builder 模式，支持泛型
	 */
	public static class SpecificationBuilder<T> {

		private final List<Function<SpecificationContext<T>, Predicate>> predicates = new ArrayList<>();

		/**
		 * 添加等于条件
		 */
		public SpecificationBuilder<T> eq(String fieldName, Object value) {
			if (value != null) {
				predicates.add(ctx -> ctx.cb.equal(ctx.root.get(fieldName), value));
			}
			return this;
		}

		/**
		 * 添加不等于条件
		 */
		public SpecificationBuilder<T> ne(String fieldName, Object value) {
			if (value != null) {
				predicates.add(ctx -> ctx.cb.notEqual(ctx.root.get(fieldName), value));
			}
			return this;
		}

		/**
		 * 添加模糊查询条件
		 */
		public SpecificationBuilder<T> like(String fieldName, String value) {
			if (value != null && !value.trim().isEmpty()) {
				predicates.add(ctx -> ctx.cb.like(ctx.root.get(fieldName), "%" + value + "%"));
			}
			return this;
		}

		/**
		 * 添加大于条件
		 */
		public <V extends Comparable<? super V>> SpecificationBuilder<T> gt(String fieldName, V value) {
			if (value != null) {
				predicates.add(ctx -> ctx.cb.greaterThan(ctx.root.get(fieldName), value));
			}
			return this;
		}

		/**
		 * 添加小于条件
		 */
		public <V extends Comparable<? super V>> SpecificationBuilder<T> lt(String fieldName, V value) {
			if (value != null) {
				predicates.add(ctx -> ctx.cb.lessThan(ctx.root.get(fieldName), value));
			}
			return this;
		}

		/**
		 * 添加大于等于条件
		 */
		public <V extends Comparable<? super V>> SpecificationBuilder<T> ge(String fieldName, V value) {
			if (value != null) {
				predicates.add(ctx -> ctx.cb.greaterThanOrEqualTo(ctx.root.get(fieldName), value));
			}
			return this;
		}

		/**
		 * 添加小于等于条件
		 */
		public <V extends Comparable<? super V>> SpecificationBuilder<T> le(String fieldName, V value) {
			if (value != null) {
				predicates.add(ctx -> ctx.cb.lessThanOrEqualTo(ctx.root.get(fieldName), value));
			}
			return this;
		}

		/**
		 * 添加 IN 条件
		 */
		public SpecificationBuilder<T> in(String fieldName, Object... values) {
			if (values != null && values.length > 0) {
				predicates.add(ctx -> ctx.root.get(fieldName).in(values));
			}
			return this;
		}

		/**
		 * 添加自定义条件
		 */
		public SpecificationBuilder<T> custom(Function<SpecificationContext<T>, Predicate> predicateFunction) {
			if (predicateFunction != null) {
				predicates.add(predicateFunction);
			}
			return this;
		}

		/**
		 * 构建最终的 Specification
		 */
		public Specification<T> build() {
			return (root, query, cb) -> {
				List<Predicate> predicateList = new ArrayList<>();
				SpecificationContext<T> context = new SpecificationContext<>(root, query, cb);

				for (Function<SpecificationContext<T>, Predicate> predicateFunction : predicates) {
					try {
						Predicate predicate = predicateFunction.apply(context);
						if (predicate != null) {
							predicateList.add(predicate);
						}
					}
					catch (Exception e) {
						// 忽略单个条件的异常，继续处理其他条件
						System.err.println("Error applying predicate: " + e.getMessage());
					}
				}

				return predicateList.isEmpty() ? cb.conjunction() : cb.and(predicateList.toArray(new Predicate[0]));
			};
		}

	}

	/**
	 * Specification 上下文，提供 root, query, cb 等对象
	 */
	public static class SpecificationContext<T> {

		public final jakarta.persistence.criteria.Root<T> root;

		public final jakarta.persistence.criteria.CriteriaQuery<?> query;

		public final jakarta.persistence.criteria.CriteriaBuilder cb;

		public SpecificationContext(jakarta.persistence.criteria.Root<T> root,
				jakarta.persistence.criteria.CriteriaQuery<?> query, jakarta.persistence.criteria.CriteriaBuilder cb) {
			this.root = root;
			this.query = query;
			this.cb = cb;
		}

	}

	/**
	 * 创建通用 SpecificationBuilder 实例 仿照 Lombok @Builder 的静态方法
	 */
	public static <T> SpecificationBuilder<T> builder(Class<T> entityClass) {
		return new SpecificationBuilder<>();
	}

}
