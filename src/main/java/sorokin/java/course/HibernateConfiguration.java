package sorokin.java.course;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sorokin.java.course.account.Account;
import sorokin.java.course.user.User;

@Configuration
public class HibernateConfiguration {

    private final String driverClass;
    private final String url;
    private final String username;
    private final String password;
    private final String dialect;
    private final String hbm2ddlAuto;
    private final String showSql;
    private final String formatSql;
    private final String currentSessionContextClass;

    public HibernateConfiguration(
            @Value("${db.driver}") String driverClass,
            @Value("${db.url}") String url,
            @Value("${db.username}") String username,
            @Value("${db.password}") String password,
            @Value("${db.dialect}") String dialect,
            @Value("${hibernate.hbm2ddl.auto}") String hbm2ddlAuto,
            @Value("${hibernate.show_sql}") String showSql,
            @Value("${hibernate.format_sql}") String formatSql,
            @Value("${hibernate.current_session_context_class:thread}") String currentSessionContextClass
    ) {
        this.driverClass = driverClass;
        this.url = url;
        this.username = username;
        this.password = password;
        this.dialect = dialect;
        this.hbm2ddlAuto = hbm2ddlAuto;
        this.showSql = showSql;
        this.formatSql = formatSql;
        this.currentSessionContextClass = currentSessionContextClass;
    }

    @Bean
    public SessionFactory sessionFactory() {
        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();

        configuration
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Account.class)
                .setProperty("hibernate.connection.driver_class", driverClass)
                .setProperty("hibernate.connection.url", url)
                .setProperty("hibernate.connection.username", username)
                .setProperty("hibernate.connection.password", password)
                .setProperty("hibernate.dialect", dialect)
                .setProperty("hibernate.hbm2ddl.auto", hbm2ddlAuto)
                .setProperty("hibernate.show_sql", showSql)
                .setProperty("hibernate.format_sql", formatSql)
                .setProperty("hibernate.current_session_context_class", currentSessionContextClass);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        return configuration.buildSessionFactory(serviceRegistry);
    }
}
